package com.youxiachai.appasync;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.parser.AsyncParser;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.koushikdutta.ion.future.ResponseFuture;
import com.youxiachai.appasync.DataHanlder.DataEnd;
import com.youxiachai.appasync.DataHanlder.DataError;
import com.youxiachai.appasync.DataHanlder.DataRequest;
import com.youxiachai.appasync.DataHanlder.DataResHeaders;

/**
 * @author youxiachai
 * @date   2014年7月3日
 */
public class WrapIon<T> { 
	
    public String mUrl = "";
    
    private Context mContext;
	
    private T mType;

	public WrapIon (String url,Context ctx,T type) {
		this.mUrl = url;
		this.mContext = ctx;
		this.mType = type;
	}

	
	private DataResHeaders mDataResHeaders = null;
	
	private DataRequest mDataRequest = null;
	
	private DataError mDataError = null;
	
    private DataEnd<T> mDataEnd = null;
	
	public WrapIon<T> onHeaders(DataResHeaders callback){
		this.mDataResHeaders = callback;
		return this;
	}
	
	public WrapIon<T> onRequest(DataRequest callback){
		this.mDataRequest = callback;
		return this;
	}
	
	
	public WrapIon<T> onEnd(DataEnd<T> apiend){
		this.mDataEnd = apiend;
		return this;
	}
	
	public WrapIon<T> onError(DataError apiError) {
		this.mDataError = apiError;
		return this;
	}
	
	/**default use fastjson hanlde json object
	 * @param builder
	 * @return
	 */
	private WrapIon<T> handleObject(B builder) {
		final WrapIon<T> that = this;
		
		ResponseFuture<String> responseFuture = builder
				.asString();
				
				responseFuture
					.withResponse()
					.setCallback(new FutureCallback<Response<String>>() {
						
						@SuppressWarnings("unchecked")
						@Override
						public void onCompleted(Exception e, Response<String> response) {
							
							if(e != null && that.mDataError != null){
								that.mDataError.onError(e);
								return;
							}
							
							if(that.mDataResHeaders != null) {
								that.mDataResHeaders.onHeaders(response.getHeaders());
							}
							
							if(that.mDataRequest != null) {
								that.mDataRequest.onRequest(response.getRequest());
							}
							
							if(that.mDataEnd != null) {
								that.mDataEnd.onEnd((T) JSON.parseObject(response.getResult(), mType.getClass()));
							}
							
						
						}
					});
				
				return this;
		
	}
	
	private WrapIon<T> handleImageView(B builder) {
				 builder
				.withBitmap()
				.intoImageView((ImageView) mType);
		return this;
	}
	
	private WrapIon<T> handleString(B builder){
		
		final WrapIon<T> that = this;
				
				ResponseFuture<String> responseFuture = builder.asString();
				
				responseFuture
					.withResponse()
					.setCallback(new FutureCallback<Response<String>>() {
						
						@SuppressWarnings("unchecked")
						@Override
						public void onCompleted(Exception e, Response<String> response) {
							
							if(e != null){
								that.mDataError.onError(e);
								return;
							}
							
							if(that.mDataResHeaders != null) {
								that.mDataResHeaders.onHeaders(response.getHeaders());
							}
							
							if(that.mDataRequest != null) {
								that.mDataRequest.onRequest(response.getRequest());
							}
							
							if(that.mDataEnd != null){
								that.mDataEnd.onEnd((T) response.getResult());
							}
							
						}
					});
		
		return this;
	}
	
	
	
	/**run request
	 * @return
	 */
	public WrapIon<T> pipe() {
		B builder = Ion.with(mContext)
				.load(mUrl);
	
		if(mType instanceof String){
			
			return handleString(builder);
		
		}else if(mType instanceof ImageView){
			
			return handleImageView(builder);
			
		} else {
			return handleObject(builder);
		}
	
	}
	
	/**
	 * 预留一个自定义parse
	 * @param dataEnd
	 * @param parse
	 * @return
	 */
	public WrapIon<T> pipe(final DataEnd<T> dataEnd, AsyncParser<T> parse) {
		ResponseFuture<T> responseFuture = Ion.with(mContext)
				.load(mUrl)
				.as(parse);
				
		responseFuture.setCallback(new FutureCallback<T>() {

			@Override
			public void onCompleted(Exception arg0, T arg1) {
				
				if(dataEnd != null) {
					dataEnd.onEnd(arg1);
				}
			}
		});
		
		return this;
	}
	

	/**进行流处理
	 * @param ctx
	 */
	public WrapIon<T> pipe(DataEnd<T> dataEnd) {
		this.mDataEnd = dataEnd;
		pipe();
		return this;
	}
	
	/**open debug
	 * @param logTag
	 */
	public void setLogging(String logTag){
		Ion.getDefault(mContext).configure().setLogging(logTag, Log.DEBUG);
	}
}
