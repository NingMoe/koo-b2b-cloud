package com.koolearn.cloud.queue;

import java.io.Serializable;

public interface ProducerService {
	//public <T> void send(T value) throws Exception;
	public  void send(final String value) throws Exception;
	public  void send(final Serializable value) throws Exception;
	public  void send(final Object value, final String label) throws Exception;
}
