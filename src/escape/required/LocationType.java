/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/
package escape.required;

/**
 * Every location (e.g., square) on an Escape board has a type. This enumeration
 * identifies the valid types.
 * 
 * MODIFIABLE: NO
 * MOVEABLE: YES
 * REQUIRED: YES
 */
public enum LocationType
{
	BLOCK, CLEAR, EXIT;
}
