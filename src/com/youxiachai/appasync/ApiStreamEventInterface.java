package com.youxiachai.appasync;


/**
 * @author youxiachai
 * @date   2014年7月3日
 */
public interface ApiStreamEventInterface {
	
	 public void onData(int downloaded, int total);
	 
	 public void onEnd(Object result);
	 
	 public void onError(Exception e, Object obj);
	 
     

}
