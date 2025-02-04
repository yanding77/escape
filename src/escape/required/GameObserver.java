/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/

package escape.required;

/**
 * The interface for a client to use to get messages from the
 * game manager by registering an observer.
 * 
 * MODIFIABLE: NO
 * MOVEABLE: YES
 * REQUIRED: YES
 * 
 * You may extend this interface for your internal use, but this is the public interface
 * that all clients will use.
 */
public interface GameObserver
{
    /**
     * Receive a message from the game
     * @param message
     */
    default void notify(String message)
    {
    	throw new EscapeException("Not implemented");
    }
    
    
    /**
     * Receive a message with the cause
     * @param message
     * @param cause usually the exception that caused the state indicated
     * 	by the message
     */
    default void notify(String message, Throwable cause)
    {
    	throw new EscapeException("Not implemented");
    }
}
