package com.erasmus.appli.exception;

public class ErasmusAppException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ErasmusAppException(String errorMessage) {  
    	super(errorMessage);  
    } 
	
}
