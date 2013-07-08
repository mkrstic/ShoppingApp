package com.comtrade.praksa.shopping.model;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidationException.
 */
public class ValidationException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4218635038545624069L;
	
	/** The error code. */
	private ValidationErrorCode errorCode;

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 */
	public ValidationException(String message) {
		super(message);
		errorCode = null;
	}
	
	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 * @param errorCode the error code
	 */
	public ValidationException(String message, ValidationErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public ValidationErrorCode getErrorCode() {
		return errorCode;
	}
	
	
	
}
