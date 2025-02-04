/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/
package escape.builder;

import escape.required.EscapePiece.PieceAttributeID;

/**
 * A JavaBean that represents an attribute for piece. This file
 * is provided as an example that can be used to initialize instances of a GameManager
 * via the EscapeBuilder. You do not have to use this, but are encouraged to do so.
 *
 * However, you do need to be able to load the appropriate named data from the 
 * configuration file in order to create a game correctly.
 * 
 * MODIFIABLE: YES
 * MOVEABLE: YES
 * REQUIRED: NO
 */
public class PieceAttribute {
	
    PieceAttributeID id;
    int value;
    
    public PieceAttribute() {}
    
    public PieceAttributeID getId() { return id; }
    public void setId(PieceAttributeID id) { this.id = id; }
    public int getValue() { return value; }
    public void setValue(int intValue) { this.value = intValue; }

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PieceAttribute [id=" + id + ", value=" + value + "]";
	}
}
