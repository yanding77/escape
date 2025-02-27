package escape.builder;

import econfig.*;
import escape.*;
import escape.required.*;
import escape.required.EscapePiece.PieceName;
import escape.builder.PieceTypeDescriptor;
import escape.builder.LocationInitializer;
import escape.builder.RuleDescriptor;
import org.antlr.v4.runtime.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;

public class EscapeGameBuilder
{
	private final EscapeGameInitializer gameInitializer;

	public EscapeGameBuilder(String fileName) throws Exception
	{
		String xmlConfiguration = getXmlConfiguration(fileName);
		gameInitializer = unmarshalXml(xmlConfiguration);
	}

	private String getXmlConfiguration(String fileName) throws IOException
	{
		EscapeConfigurator configurator = new EscapeConfigurator();
		return configurator.makeConfiguration(CharStreams.fromFileName(fileName));
	}

	private EscapeGameInitializer unmarshalXml(String xmlConfiguration) throws JAXBException
	{
		JAXBContext contextObj = JAXBContext.newInstance(EscapeGameInitializer.class);
		Unmarshaller mub = contextObj.createUnmarshaller();
		return (EscapeGameInitializer) mub.unmarshal(new StreamSource(new StringReader(xmlConfiguration)));
	}

	public EscapeGameInitializer getGameInitializer()
	{
		return gameInitializer;
	}

	/**
	 * Create the EscapeGameManager from the parsed configuration.
	 */
	public EscapeGameManager makeGameManager()
	{
		if (gameInitializer == null) {
			throw new EscapeException("Game initializer is not properly loaded.");
		}

		int xMax = gameInitializer.getxMax();
		int yMax = gameInitializer.getyMax();
		Coordinate.CoordinateType coordinateType = gameInitializer.getCoordinateType();
		List<String> players = gameInitializer.getPlayers();

		// Build the board.
		Board board = new Board(xMax, yMax);

		// Process location initializers.
		if (gameInitializer.getLocationInitializers() != null) {
			for (LocationInitializer loc : gameInitializer.getLocationInitializers()) {
				// For milestone3, if a location initializer has a pieceName, place the piece.
				// Otherwise, if it indicates a special location (BLOCK/EXIT), then (once Board supports it)
				// you could call board.setLocationType(...).
				if (loc.pieceName != null && loc.player != null) {
					PieceTypeDescriptor desc = null;
					if (gameInitializer.getPieceTypes() != null) {
						for (PieceTypeDescriptor ptd : gameInitializer.getPieceTypes()) {
							if (ptd.getPieceName().equals(loc.pieceName)) {
								desc = ptd;
								break;
							}
						}
					}
					if (desc == null) {
						throw new EscapeException("Unknown piece type: " + loc.pieceName);
					}
					EscapePieceImpl piece = new EscapePieceImpl(loc.player, desc);
					board.putPieceAt(piece, new CoordinateImpl(loc.x, loc.y));
				}
				else {
					// If location initializer indicates a special location (e.g., BLOCK or EXIT),
					// and if your configuration provides such info via a getter (e.g., loc.getLocationType()),
					// then set it on the board:
					// board.setLocationType(new CoordinateImpl(loc.x, loc.y), LocationType.valueOf(loc.getLocationType()));
				}
			}
		}

		RuleDescriptor[] ruleDescriptors = gameInitializer.getRules();
		return new EscapeGameManagerImpl<>(xMax, yMax, coordinateType, players, board, buildPieceTypes(), ruleDescriptors);
	}

	private Map<PieceName, PieceTypeDescriptor> buildPieceTypes() {
		Map<PieceName, PieceTypeDescriptor> pieceTypes = new HashMap<>();
		if (gameInitializer.getPieceTypes() != null) {
			for (PieceTypeDescriptor ptd : gameInitializer.getPieceTypes()) {
				pieceTypes.put(ptd.getPieceName(), ptd);
			}
		}
		return pieceTypes;
	}
}
