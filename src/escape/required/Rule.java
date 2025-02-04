/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved.
 * All rights reserved.
 *******************************************************************************/

package escape.required;

/**
 * Interface for the rule implementations. It also contains a static
 * enumeration for the rule names. How you implement the actual  implementations
 * is up to you and the design decisions you make.
 * 
 * MODIFIABLE: NO
 * MOVEABLE: YES
 * REQUIRED: YES
 * 
 * You may extend this interface for your internal use, but this is the public interface
 * that all clients will use.
 */
public interface Rule
{
	public static enum RuleID {POINT_CONFLICT, SCORE, TURN_LIMIT};
	
	/**
	 * @return the RuleID
	 */
	RuleID getId();

	/**
	 * @return If this is a rule with a value (e.g. TURN_LIMIT), then this returns that value. 
	 * If it is has no value, return 0
	 */
	int getIntValue();
}
