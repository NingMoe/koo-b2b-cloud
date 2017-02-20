package com.koolearn.cloud.event;

/**
 * Created by fn on 2016/4/27.
 */
import com.google.common.base.MoreObjects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.DeadEvent;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.log4j.Logger;


import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;


public class EventBus {


    private Logger log = Logger.getLogger(this.getClass());

    /**
     * A thread-safe cache for flattenHierarchy(). The Class class is immutable. This cache is shared
     * across all EventBus instances, which greatly improves performance if multiple such instances
     * are created and objects of the same class are posted on all of them.
     */
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
            .weakKeys().build(
                    new ClassSetCacheLoader());

    /**
     * All registered event subscribers, indexed by event type.
     * <p>
     * <p>This SetMultimap is NOT safe for concurrent use; all access should be
     * made after acquiring a read or write lock via {@link #subscribersByTypeLock}.
     */
    private final SetMultimap<Class<?>, EventSubscriber> subscribersByType = HashMultimap.create();
    private final ReadWriteLock subscribersByTypeLock = new ReentrantReadWriteLock();

    private final AnnotatedSubscriberFinder finder = new AnnotatedSubscriberFinder();

    private  ExecutorService executor = null;

    private SubscriberExceptionHandler subscriberExceptionHandler = new LoggingSubscriberExceptionHandler();
    private Executor timeoutMonitor = Executors.newSingleThreadExecutor(new VaporThreadFactory(
            getClass().getName() + "-TimeoutMonitor", true));


    /**
     * Creates a new EventBus named "default".
     */
    public EventBus(ExecutorService executor) {
        this.executor = executor;
    }

    public void register(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = finder.findAllSubscribers(object);
        subscribersByTypeLock.writeLock().lock();
        try {
            subscribersByType.putAll(methodsInListener);
        } finally {
            subscribersByTypeLock.writeLock().unlock();
        }
    }

    public void setSubscriberExceptionHandler(SubscriberExceptionHandler subscriberExceptionHandler) {
        this.subscriberExceptionHandler = subscriberExceptionHandler;
    }

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     */
    public void unregister(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = finder.findAllSubscribers(object);
        for (Map.Entry<Class<?>, Collection<EventSubscriber>> entry : methodsInListener.asMap().entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<EventSubscriber> eventMethodsInListener = entry.getValue();

            subscribersByTypeLock.writeLock().lock();
            try {
                Set<EventSubscriber> currentSubscribers = subscribersByType.get(eventType);
                if (!currentSubscribers.containsAll(eventMethodsInListener)) {
                    throw new IllegalArgumentException(
                            "missing event subscriber for an annotated method. Is " + object + " registered?");
                }
                currentSubscribers.removeAll(eventMethodsInListener);
            } finally {
                subscribersByTypeLock.writeLock().unlock();
            }
        }
    }

    /**
     * Posts an event to all registered subscribers.  This method will return
     * successfully after the event has been posted to all subscribers, and
     * regardless of any exceptions thrown by subscribers.
     * <p>
     * <p>If no subscribers have been subscribed for {@code event}'s class, and
     * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a
     * DeadEvent and reposted.
     *
     * @param event event to post.
     */
    public void post(Object event) {
        Set<Class<?>> dispatchTypes = flattenHierarchy(event.getClass());

        boolean dispatched = false;
        for (Class<?> eventType : dispatchTypes) {
            subscribersByTypeLock.readLock().lock();
            try {
                Set<EventSubscriber> wrappers = subscribersByType.get(eventType);

                if (!wrappers.isEmpty()) {
                    dispatched = true;
                    for (EventSubscriber wrapper : wrappers) {
                        EventWithSubscriber task = new EventWithSubscriber(event, wrapper);
                        if (log.isDebugEnabled()) {
                            System.out.print("Dispatching {}" + task);
                        }
                        Future<Object> future = executor.submit(task);
                        timeoutMonitor.execute(new TimeoutMonitor(future, task));
                    }
                }
            } finally {
                subscribersByTypeLock.readLock().unlock();
            }
        }

        if (!dispatched && !(event instanceof DeadEvent)) {
            post(new DeadEvent(this, event));
        }

    }

    /**
     * Flattens a class's type hierarchy into a set of Class objects.  The set
     * will include all superclasses (transitively), and all interfaces
     * implemented by these superclasses.
     *
     * @param concreteClass class whose type hierarchy will be retrieved.
     * @return {@code clazz}'s complete type hierarchy, flattened and uniqued.
     */
    Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        try {
            return flattenHierarchyCache.getUnchecked(concreteClass);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private static final Object VOID = new Object();

    /**
     * simple struct representing an event and it's subscriber
     */
    class EventWithSubscriber implements Callable<Object> {
        final Object event;
        final EventSubscriber subscriber;

        public EventWithSubscriber(Object event, EventSubscriber subscriber) {
            this.event = checkNotNull(event);
            this.subscriber = checkNotNull(subscriber);
        }

        @Override
        public Object call() throws Exception {
            try {
                subscriber.handleEvent(event);
            } catch (InvocationTargetException e) {
                subscriberExceptionHandler.handleException(e, new SubscriberExceptionContext(EventBus.this, event,
                        subscriber.getSubscriber(),
                        subscriber.getMethod()));
            }
            return VOID;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("event", event).add("subscriber", subscriber).toString();
        }
    }

    private static class ClassSetCacheLoader extends CacheLoader<Class<?>, Set<Class<?>>> {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        // safe cast
        @Override
        public Set<Class<?>> load(Class<?> concreteClass) {
            return (Set) TypeToken.of(concreteClass).getTypes().rawTypes();
        }
    }

    private class TimeoutMonitor implements Runnable {
        private final Future<Object> future;
        private final EventWithSubscriber task;

        public TimeoutMonitor(Future<Object> future, EventWithSubscriber task) {
            this.future = future;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                Uninterruptibles.getUninterruptibly(future, 1, TimeUnit.MINUTES);
            } catch ( Exception e) {
                log.error("Timeout executing task {}");
            }
        }
    }
}

