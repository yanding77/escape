package escape.builder;

import escape.required.EscapePiece.PieceAttributeID;

public class PieceAttribute {
	private PieceAttributeID id;
	private int value;

	public PieceAttribute() { }

	public PieceAttributeID getId() {
		return id;
	}
	public void setId(PieceAttributeID id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PieceAttribute [id=" + id + ", value=" + value + "]";
	}
}
