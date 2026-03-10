package chess101;

import static chess101.Index.GRID;

public enum Step {
    xxy(-2, -1),
    xxY(-2, 1),
    xyy(-1, -2),
    xy(-1, -1),
    x_(-1, 0),
    xY(-1, 1),
    xYY(-1, 2),
    _y(0, -1),
    _Y(0, 1),
    Xyy(1, -2),
    Xy(1, -1),
    X_(1, 0),
    XY(1, 1),
    XYY(1, 2),
    XXy(2, -1),
    XXY(2, 1);
    
    final public int x;
    final public int y;
    
    Step(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean hasNext(Index index) {
        int x = index.file + this.x;
        int y = index.rank + this.y;
        
        return x >= 0 && x < GRID && y >= 0 && y < GRID;
    }
    
    public Index getNext(Index index) {
        return Index.BOARD[index.rank + y][index.file + x];
    }
    
}
