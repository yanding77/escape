## Escape Game Engine:
This project is a Java-based Escape Game Engine designed to model customizable board games with flexible rules, piece types, and win conditions. It was originally developed for coursework in Object-Oriented Design (CS4233, WPI) but is structured as a general-purpose framework for turn-based board games.
Features
## Configurable Boards:
Supports SQUARE, HEX, and ORTHOSQUARE coordinate systems.
Boards can contain blocked spaces, exit locations, and scoring zones.
## Game Builder:
EscapeGameBuilder loads .egc configuration files.
Configurations define board dimensions, coordinate type, pieces, rules, and players.
## Pieces and Movement:
Pieces (EscapePiece) are tied to a player.
Each piece type has attributes such as movement rules and allowed directions.
Movement validation differs based on the board type (e.g., linear vs. diagonal moves).
## Gameplay Rules:
Players take turns moving pieces on the board.
Some locations may be blocked.
Pieces may exit the board through special exit squares, earning points.
The game ends when win conditions are met (e.g., a score threshold, no moves left, etc.).
## Observer Pattern:
GameObserver interface lets external classes subscribe to game updates.
MasterTestObserver is provided as a concrete implementation for debugging.
## Project Structure:
escape/ – Core source files (game manager, pieces, builder, rules, observers).
configurations/ – Sample .egc configuration files.
test/ – JUnit test suite (sample tests provided for each milestone).
## Key classes:
EscapeGameBuilder – Parses .egc configs and builds the game manager.
EscapeGameManager – Runs gameplay logic (place, move, check win).
EscapePiece – Represents an individual game piece.
GameObserver – Observer interface for notifications.
MasterTestObserver – Debug observer used in sample tests.
## Getting Started
Prerequisites
Java 17+
JUnit 5 for testing
## Running the Game
Use the EscapeGameBuilder to load a .egc file:
EscapeGameBuilder builder = new EscapeGameBuilder("configurations/milestone2Sample.egc");
EscapeGameManager manager = builder.makeGameManager();
Place pieces and execute moves through the EscapeGameManager API.
Subscribe to game updates with a custom GameObserver if needed.
