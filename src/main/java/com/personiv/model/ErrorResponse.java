package com.personiv.model;

public class ErrorResponse {
	private String message;
	private Object source;
	
	public ErrorResponse(String message,Object source) {
		this.message = message;
		this.source = source;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	
}
