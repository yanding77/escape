/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/

package escape.required;

/**
 * Interface for the piece implementations. It also contains static
 * enumerations for the piece names, movement patterns, and attributes since 
 * these are properties of pieces. How you implement the actual piece implementations
 * is up to you and the design decisions you make.
 * 
 * MODIFIABLE: NO
 * MOVEABLE: YES
 * REQUIRED: YES
 * 
 * You may extend this interface for your internal use, but this is the public interface
 * that all clients will use.
 */
public interface EscapePiece
{
	public static enum PieceName {BIRD, DOG, FROG, HORSE, SNAIL};
	
	public static enum MovementPattern {DIAGONAL, LINEAR, ORTHOGONAL};
	
	public static enum PieceAttributeID {FLY, DISTANCE, JUMP, UNBLOCK, VALUE};
	
	/**
	 * @return the name
	 */
	default PieceName getName()
	{
		throw new EscapeException("Not implemented");
	}
	
	/**
	 * @return the owning player
	 */
	default String getPlayer()
	{
		throw new EscapeException("Not implemented");
	}
}
