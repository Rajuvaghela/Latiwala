package com.lujayn.latiwala.network;


import com.lujayn.latiwala.common.AppConstant.HttpRequestType;

public interface RequestTaskDelegate {

	void backgroundActivityComp(String response,
								HttpRequestType completedRequestType);

	void timeOut();

	void codeError(int code);

	void percentageDownloadCompleted(int percentage, Object record);
}
