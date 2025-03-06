/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved.
 *******************************************************************************/

import escape.required.GameObserver;

import java.util.Deque;
import java.util.LinkedList;

public class MasterTestObserver implements GameObserver
{
    final Deque<Notification> messages;
    
    public MasterTestObserver()
    {
        messages = new LinkedList<Notification>();
    }
    
    /*
     * @see escape.GameObserver#notify(java.lang.String)
     */
    @Override
    public void notify(String message)
    {
        messages.addLast(new Notification(message, null));
//        System.err.println(message);
    }

    /*
     * @see escape.GameObserver#notify(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void notify(String message, Throwable cause)
    {
        messages.addLast(new Notification(message, cause));
//        System.err.println(message);
    }    
    
    public int messageCount()
    {
        return messages.size();
    }
    
    public Notification nextMessage()
    {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.removeFirst();
    }
    
    public void clearMessages()
    {
        messages.clear();
    }
}

class Notification
{
    final String message;
    final Throwable cause;
    
    public Notification(String message, Throwable cause)
    {
        this.message = message;
        this.cause = cause;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @return the cause
     */
    public Throwable getCause()
    {
        return cause;
    }

}