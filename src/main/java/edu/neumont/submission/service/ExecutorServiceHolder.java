package edu.neumont.submission.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceHolder {
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	public void destroy() {
		executorService.shutdown();
	}
}
