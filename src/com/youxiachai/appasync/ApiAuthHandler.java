package com.youxiachai.appasync;

import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.builder.Builders.Any.B;

/**
 * @author youxiachai
 * @date   2014年7月9日
 */
public interface ApiAuthHandler {
	
	
	/**进行授权
	 * @param builder
	 * @return
	 */
	public boolean authenticated(B builder);
	
	
	/**
	 * 实现验证的逻辑 
	 */
	public void auth();
	
	
	/**
	 * 验证过期处理 
	 */
	public boolean expired(RawHeaders headers);
	
	
	
}
