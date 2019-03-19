package Core;

import java.util.ArrayList;

class Player {
    private int chessMenNum;
    private Color color;
    private ChessBoard chessBoard;
    private Position movePosition;

    Player() {
        chessMenNum = 2;
    }

    Player(Color color) {
        this.color = color;
        chessMenNum = 2;
    }

    void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    int getChessMenNum() {
        return chessMenNum;
    }

    void setChessMenNum(int chessMenNum) {
        this.chessMenNum = chessMenNum;
    }

    Color getColor() {
        return color;
    }

    void setColor(Color color) {
        this.color = color;
    }

    Position getMovePosition() {
        return movePosition;
    }

    boolean canMove() {
        return !chessBoard.isNoValid(color);
    }

    int move(Position position) {
        int num = chessBoard.NumOfFlippedChessMen(position.getX(), position.getY(), color);
        if (num > 0) {
            movePosition = position;
            chessBoard.place(position, color);
        }
        return num;
    }

    int autoMove() {
        ArrayList<Position> positions = chessBoard.getLegalPositions(color);
        if (positions.size() == 0) {
            return -1;
        }
        int maxNum = 0;
        int maxNumIndex = -1;
        int eachNum;
        for (int i = 0; i < positions.size(); i++) {
            eachNum = chessBoard.NumOfFlippedChessMen(positions.get(i).getX(), positions.get(i).getY(), color);
            if (eachNum > maxNum) {
                maxNum = eachNum;
                maxNumIndex = i;
            }
        }
        movePosition = positions.get(maxNumIndex);
        chessBoard.place(movePosition, color);
        return maxNum;
    }
}
