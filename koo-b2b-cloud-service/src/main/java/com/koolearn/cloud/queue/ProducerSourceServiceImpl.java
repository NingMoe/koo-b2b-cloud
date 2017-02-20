package com.koolearn.cloud.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.io.Serializable;

/**
 * 集成spring消息生产者
 * queue消息生成者
 */
public class ProducerSourceServiceImpl implements ProducerService{
	private static final transient Logger log = LoggerFactory.getLogger(ProducerSourceServiceImpl.class);
	private JmsTemplate jmsTemplate;  //注入属性
	private Destination destination;  //注入属性
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	
	public  void send(final String value) throws Exception{
		MessageCreator messageCreator = new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {	
				TextMessage message = session.createTextMessage();
				message.setText((String)value);
				log.info("text--发送消息：" + value);
				return message;				
			}
		};
		
		jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTemplate.send(this.destination,messageCreator);
	}
	public  void send(final Serializable value) throws Exception{
		MessageCreator messageCreator = new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {					
				ObjectMessage message = session.createObjectMessage();
				message.setObject((Serializable)value);
				log.info("Serializable--发送消息：" + value);
				return message;				
			}
		};
		
		jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTemplate.send(this.destination,messageCreator);
	}
	
	public  void send(final Object value, final String label) throws Exception{
		MessageCreator messageCreator = new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {					
				MapMessage message = session.createMapMessage();
				message.setObject(label, value);
				log.info("Serializable--发送消息：" + value);
				return message;				
			}
		};
		
		jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTemplate.send(this.destination,messageCreator);
	}

}
