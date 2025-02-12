Name: 

TDD TODO/Task list

**Build Tests**

These are for implementing the EscapeGameBuilder's `makeGameManager()` method.

| **#** | Test                                                            | Comments                        |
|:-----:|:----------------------------------------------------------------|:--------------------------------|
|   1   | Build game manager 2 X 2, 2 players, and coordinate type SQUARE | create game object              |
|   2   | Build game with one CLEAR [default]  location                   | create game object              |
|   3   | Build game manager 3 X 3 with 2 players and coordinate type HEX     | test alternative board size and coordinate type                  |
|   4   | Build game manager with missing players list                      | verify that missing players default to an empty list             |
|   5   | Build game manager with multiple location initializers            | confirm multiple locations are processed correctly               |
|   6   | Build game manager with an extra BLOCKED location initializer       | test alternate location type handling                             |
|   7   | Build game manager with defined piece type descriptors              | ensure piece types are configured correctly                       |
|   8   | Build game manager with defined rule descriptors                    | ensure game rules are parsed correctly                            |
|   9   | Build game manager with an empty rule descriptor array              | check that absence of rules is handled without errors              |
|  10   | Build game manager with maximum board dimensions                    | verify extreme board sizes are handled properly                   |
|  11   | Build game manager with non-square board dimensions                 | confirm rectangular board initialization                          |
|  12   | Build game manager with coordinate type OCTAGON (if supported)        | test alternate coordinate type usage                              |
|  13   | Build game manager and simulate a dummy move                        | ensure move method linkage without full implementation              |
|  14   | Build game manager with null location initializers                  | verify that null location data is safely handled                    |

|       |                                                                 |                                 |