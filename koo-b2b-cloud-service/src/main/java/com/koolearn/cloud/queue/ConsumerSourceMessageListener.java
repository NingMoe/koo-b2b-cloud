package com.koolearn.cloud.queue;

import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * queue消息消费者,业务实现，业务方需要自己实现这个类 queue消息消费者在spring配置加载后即运行
 */
public class ConsumerSourceMessageListener implements MessageListener {

    private Logger log = Logger.getLogger(ConsumerSourceMessageListener.class);

    @Autowired
    private ResourceInfoService resourceInfoService;

    public void setResourceInfoService(ResourceInfoService resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    public void onMessage(Message msg) {
        long startTime = System.currentTimeMillis();
        if (msg instanceof ObjectMessage) {
            ObjectMessage message = (ObjectMessage) msg;
            try {
                ResourceInfo resourceInfo = (ResourceInfo) message.getObject();
                resourceInfoService.updateResourcesInfoIndex(resourceInfo);

                log.info("--消费者一ConsumerSourceMessageListener收到信息--" + resourceInfo.getId() + "--time--"
                        + System.currentTimeMillis());
            } catch (JMSException e) {
                log.error("消息队列处理失败" + e.getMessage());
            }
        }
    }

}
