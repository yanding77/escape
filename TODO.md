Name: 

TDD TODO/Task list

**Build Tests**

These are for implementing the EscapeGameBuilder's `makeGameManager()` method.

| **#** | Test                                                                                            | Comments                                                                                                   |
|:-----:|:------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------|
|   1   | Build game manager 2 X 2, 2 players, and coordinate type SQUARE                                 | create game object                                                                                         |
|   2   | Build game with one CLEAR [default]  location                                                   | create game object                                                                                         |
|   3   | Build game manager 3 X 3 with 2 players and coordinate type HEX                                 | test alternative board size and coordinate type                                                            |
|   4   | Build game manager with missing players list                                                    | verify that missing players default to an empty list                                                       |
|   5   | Build game manager with multiple location initializers                                          | confirm multiple locations are processed correctly                                                         |
|   6   | Build game manager with an extra BLOCKED location initializer                                   | test alternate location type handling                                                                      |
|   7   | Build game manager with defined piece type descriptors                                          | ensure piece types are configured correctly                                                                |
|   8   | Build game manager with defined rule descriptors                                                | ensure game rules are parsed correctly                                                                     |
|   9   | Build game manager with an empty rule descriptor array                                          | check that absence of rules is handled without errors                                                      |
|  10   | Build game manager with maximum board dimensions                                                | verify extreme board sizes are handled properly                                                            |
|  11   | Build game manager with non-square board dimensions                                             | confirm rectangular board initialization                                                                   |
|  12   | Build game manager with coordinate type OCTAGON (if supported)                                  | test alternate coordinate type usage                                                                       |
|  13   | Build game manager and simulate a dummy move                                                    | ensure move method linkage without full implementation                                                     |
|  14   | Build game manager with null location initializers                                              | verify that null location data is safely handled                                                           |
|  15   | Build game manager with infinite board (xMax and yMax = 0)                                      | Verify that an infinite board is correctly handled when xMax and yMax are 0 (accepts all positive indices) |
|  16   | Verify that coordinates outside typical bounds are valid on an infinite board                   | Verify that coordinates outside typical bounds are valid on an infinite board                              |
|  17   | Verify that coordinates outside typical bounds are valid on an infinite board                   | Ensure that only positive indices are allowed in Milestone2                                                |
|  18   | Ensure that only positive indices are allowed in Milestone2                                     | Test a straight-line (horizontal or vertical) move within distance (e.g., SNAIL moving 1 step).            |
|  19   | Test a straight-line (horizontal or vertical) move within distance (e.g., SNAIL moving 1 step). | Test a straight-line (horizontal or vertical) move within distance (e.g., SNAIL moving 1 step).            |
|  20   | Simulate a valid move for a flying piece despite blocked intermediate squares                   | Confirm that a piece with the FLY attribute ignores obstructions in its straight-line path.                |
|  21   | Simulate an invalid move for a flying piece due to distance violation                           | Verify that even flying pieces cannot exceed their defined maximum movement distance.                      |
|  22   | Verify turn order changes on a valid move                                                       | Ensure that the player turn advances after a valid move is made.                                           |
|  23   | Verify turn order does not change on an invalid move                                            | Ensure that if a move is invalid, the same player gets another chance.                                     |
|  24   | Simulate multiple sequential moves and verify game state updates                                | Check overall consistency of the game state after several moves                                            |
|  25   | Verify that after a valid move, the piece is no longer at its original location                 | Confirm that the board correctly updates piece positions.                                                  |
|  26   | Verify that a move to an already occupied square is rejected                                    | Confirm that destination occupancy causes an invalid move (since POINT_CONFLICT is not yet supported).     |
|  27   | Verify that the piece's distance attribute is correctly enforced                                | Test that moves exceeding the allowed distance are rejected.                                               |
|  28   | Verify that the piece's fly attribute is recognized correctly                                   | Test that the FLY attribute permits bypassing intermediate obstructions.                                   |
|  29   | Verify that attempting to move from an empty cell returns an invalid move                       | Check that if no piece exists at the starting coordinate, the move is invalid.                             |

