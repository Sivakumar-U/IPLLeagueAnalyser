package com.blz.iplanalyser;

public class IPLAnalyserException extends Exception {

	enum ExceptionType {
		NO_SUCH_FILE, NO_SUCH_FIELD
	}

	ExceptionType exceptionType;

	public IPLAnalyserException(String message, ExceptionType exceptionType) {
		super(message);
		this.exceptionType = exceptionType;
	}

	public IPLAnalyserException(String message, ExceptionType type, Throwable cause) {
		super(message, cause);
		this.exceptionType = type;
	}

}
