package com.erasmus.appli.payload.request;

import lombok.Data;

@Data
public class ApplicationApproveRequest {
	private long appId;
	private long userId;
}
