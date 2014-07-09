package com.youxiachai.appasync;

import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.libcore.RawHeaders;

/**
 * request callback 
 * @author youxiachai
 * @date   2014年7月9日
 */
public class DataHanlder {
	
	public interface DataEnd<T> {
		public void onEnd(T result);
	}
	
	public interface DataError {
		public void onError(Exception e);
	}
	
	public interface DataResHeaders {
		public void onHeaders(RawHeaders headers);
	}
	
	public interface DataRequest {
		public void onRequest(AsyncHttpRequest request);
	}
}
