package escape.builder;

import escape.required.Coordinate.*;
import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
public class EscapeGameInitializer {
	private CoordinateType coordinateType;
	private int xMax, yMax;
	private LocationInitializer[] locationInitializers;
	private PieceTypeDescriptor[] pieceTypes;
	private RuleDescriptor[] rules;
	private List<String> players;

	public EscapeGameInitializer() {
		this.players = new ArrayList<>();
	}

	public CoordinateType getCoordinateType() {
		return coordinateType;
	}
	public void setCoordinateType(CoordinateType coordinateType) {
		this.coordinateType = coordinateType;
	}
	public int getxMax() {
		return xMax;
	}
	public void setxMax(int xMax) {
		this.xMax = xMax;
	}
	public int getyMax() {
		return yMax;
	}
	public void setyMax(int yMax) {
		this.yMax = yMax;
	}
	public LocationInitializer[] getLocationInitializers() {
		return locationInitializers;
	}
	public void setLocationInitializers(LocationInitializer... locationInitializers) {
		this.locationInitializers = locationInitializers;
	}
	public PieceTypeDescriptor[] getPieceTypes() {
		return pieceTypes;
	}
	public void setPieceTypes(PieceTypeDescriptor... types) {
		this.pieceTypes = types;
	}
	public RuleDescriptor[] getRules() {
		return rules;
	}
	public void setRules(RuleDescriptor[] rules) {
		this.rules = rules;
	}
	public List<String> getPlayers() {
		return players;
	}
	public void setPlayers(List<String> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "EscapeGameInitializer [xMax=" + xMax + ", yMax=" + yMax +
				", coordinateType=" + coordinateType +
				", locationInitializers=" + Arrays.toString(locationInitializers) +
				", pieceTypes=" + Arrays.toString(pieceTypes) + "]";
	}
}
