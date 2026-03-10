# Chess101
Chess Framework (FEN)

All classes needed in a FEN string orientated chess game.

The State class constructor (private) contains all 6 elements of a FEN string. Those are used to get all possible destinations (index), not moves, per piece (index) and if it is check.

With this all moves can be made or mate.

toString returns the FEN of this state.
