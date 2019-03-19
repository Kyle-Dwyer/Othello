package Core;

import java.util.ArrayList;

class ChessBoard {
    private int dimension;
    private ChessMan[][] chessMen;

    ChessBoard(int dimension) {
        this.dimension = dimension;
        chessMen = new ChessMan[dimension][dimension];
        addChessMan(Color.WHITE, dimension / 2, dimension / 2);
        addChessMan(Color.WHITE, dimension / 2 - 1, dimension / 2 - 1);
        addChessMan(Color.BLACK, dimension / 2 - 1, dimension / 2);
        addChessMan(Color.BLACK, dimension / 2, dimension / 2 - 1);
    }

    int getDimension() {
        return dimension;
    }

    void setDimension(int dimension) {
        this.dimension = dimension;
    }

    private void addChessMan(Color color, int x, int y) {
        chessMen[x][y] = new ChessMan(color, x, y);
    }

    private void addChessMan(ChessMan chessMan) {
        int x = chessMan.getPosition().getX();
        int y = chessMan.getPosition().getY();
        chessMen[x][y] = chessMan;
    }

    ChessMan[][] getChessMen() {
        return chessMen;
    }

    ArrayList<Position> getLegalPositions(Color color) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (NumOfFlippedChessMen(i, j, color) > 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    private ArrayList<Position> positionsToBeFliped(int x, int y, Color color) {
        if (chessMen[x][y] != null) return null;
        ArrayList<Position> positions = new ArrayList<>();
        int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};//8个方向
        int num;
        for (int i = 0; i < 8; i++) {
            num = 1;
            int nextX = x + direction[i][0];
            int nextY = y + direction[i][1];
            while (nextX + direction[i][0] >= 0 && nextX + direction[i][0] < dimension
                    && nextY + direction[i][1] >= 0 && nextY + direction[i][1] < dimension
                    && chessMen[nextX][nextY] != null && chessMen[nextX + direction[i][0]][nextY + direction[i][1]] != null
                    && chessMen[nextX][nextY].getColor() != color) {
                //找到另一头
                if (chessMen[nextX + direction[i][0]][nextY + direction[i][1]].getColor() == color) {
                    for (int j = 1; j <= num; j++) {
                        positions.add(chessMen[x + j * direction[i][0]][y + j * direction[i][1]].getPosition());
                    }
                    break;
                }
                num++;
                nextX += direction[i][0];
                nextY += direction[i][1];
            }
        }
        return positions;
    }

    int NumOfFlippedChessMen(int x, int y, Color color) {
        ArrayList<Position> positionsTemp = positionsToBeFliped(x, y, color);
        if (positionsTemp == null) {
            return -1;
        }
        return positionsTemp.size();
    }

    void place(Position position, Color color) {
        int x = position.getX();
        int y = position.getY();
        ArrayList<Position> positionsTemp = positionsToBeFliped(x, y, color);
        if (positionsTemp == null) {
            return;
        }
        addChessMan(new ChessMan(color, x, y));
        for (Position eachPosition : positionsTemp) {
            chessMen[eachPosition.getX()][eachPosition.getY()].changeColor();
        }
    }

    boolean isFull() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (chessMen[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isNoValid(Color color) {
        return getLegalPositions(color) == null || getLegalPositions(color).size() == 0;
    }
}
