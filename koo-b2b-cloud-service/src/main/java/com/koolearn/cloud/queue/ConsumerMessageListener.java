package com.koolearn.cloud.queue;

import org.apache.log4j.Logger;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by fn on 2016/7/26.
 */
public abstract class ConsumerMessageListener implements MessageListener {

    private Logger logger = Logger.getLogger(this.getClass());



   // public abstract < T extends Message  > T  onMessage( T t1 , Class< ? extends MessageContent > classw  );



}
