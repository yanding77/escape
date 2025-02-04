/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/
package escape.required;

/**
 * This is the base exception class for the Escape game. It is a subclass of
 * RuntimeException. Making this runtime is a convenience since these are unchecked. 
 * We expect exceptions to be meaningful, so any new exception types should be a
 * subclass of this class.
 */
public class EscapeException extends RuntimeException
{
	/**
	 * There must at least be a message associated with any exception.
	 * @param message a meaningful message describing the problem
	 */
	public EscapeException(String message)
	{
		super(message);
	}
	
	/**
	 * Use this constructor if the exception were caused by something like
	 * a NullPointerException and you want to provide that for debugging
	 * purposes.
	 * @param message a meaningful message describing the problem
	 * @param cause the exception that caused the problem
	 */
	public EscapeException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
