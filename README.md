# Chess101
Chess Framework (FEN)

All classes needed in a FEN string orientated chess game.

enums:

  -Index -> a1-h8
  
  -Piece -> K-p
  
  -Side -> w, b
  
  -Step -> all directions

moves:

  -Move -> from, to

  -Promotion -> Move + piece

fen:
  
  -State -> fen, moves, check

The State class constructor (private) contains all 6 elements of a FEN string. Those are used to get per piece (index) all possible destinations (index) (no moves), and if it is check.

A move results in a new State.

toString() returns the FEN string.
