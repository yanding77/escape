package escape.builder;

import econfig.*;
import escape.*;
import escape.required.*;
import escape.required.EscapePiece.PieceName;
import escape.builder.PieceTypeDescriptor;
import escape.builder.LocationInitializer;
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
		return (EscapeGameInitializer) mub.unmarshal(
				new StreamSource(new StringReader(xmlConfiguration)));
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

		Board board = new Board(xMax, yMax);

		Map<PieceName, PieceTypeDescriptor> pieceTypes = new HashMap<>();
		if (gameInitializer.getPieceTypes() != null) {
			for (PieceTypeDescriptor ptd : gameInitializer.getPieceTypes()) {
				pieceTypes.put(ptd.getPieceName(), ptd);
			}
		}

		if (gameInitializer.getLocationInitializers() != null) {
			for (LocationInitializer loc : gameInitializer.getLocationInitializers()) {
				if (loc.pieceName != null && loc.player != null) {
					PieceTypeDescriptor desc = pieceTypes.get(loc.pieceName);
					if (desc == null) {
						throw new EscapeException("Unknown piece type: " + loc.pieceName);
					}
					EscapePieceImpl piece = new EscapePieceImpl(loc.player, desc);
					board.putPieceAt(piece, new CoordinateImpl(loc.x, loc.y));
				}
			}
		}

		// 4) Build the manager
		return new EscapeGameManagerImpl<>(xMax, yMax, coordinateType, players, board, pieceTypes);
	}
}
