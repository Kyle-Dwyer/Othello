package Core;

import java.util.ArrayList;

public class Othello {
    private Player player;
    private Player computerPlayer;
    private ChessBoard chessBoard;

    public Othello() {
        computerPlayer = new Player();
        player = new Player();
    }

    public boolean initialize(int dimension, boolean color) {
        chessBoard = new ChessBoard(dimension);
        player.setChessBoard(chessBoard);
        computerPlayer.setChessBoard(chessBoard);
        player.setColor(color ? Color.BLACK : Color.WHITE);
        computerPlayer.setColor(color ? Color.WHITE : Color.BLACK);
        return true;
    }

    public void autoMove() {
        int num = computerPlayer.autoMove();
        player.setChessMenNum(player.getChessMenNum() - num);
        computerPlayer.setChessMenNum(computerPlayer.getChessMenNum() + num + 1);
    }

    public boolean computerMove() {
        if (computerPlayer.canMove()) {
            autoMove();
        } else {
            return false;
        }
        return true;
    }

    public boolean isLegalStep(int x, int y) {
        ArrayList<Position> positionsTemp = chessBoard.getLegalPositions(player.getColor());
        for (Position position : positionsTemp) {
            if (position.getX() == x && position.getY() == y)
                return true;
        }
        return false;
    }

    public void playerMove(int x, int y) {
        int num = player.move(new Position(x, y));
        player.setChessMenNum(player.getChessMenNum() + num + 1);
        computerPlayer.setChessMenNum(computerPlayer.getChessMenNum() - num);
    }

    public boolean checkValid() {
        return chessBoard.isNoValid(Color.BLACK) && chessBoard.isNoValid(Color.WHITE);
    }

    public boolean checkFull() {
        return chessBoard.isFull();
    }

    public boolean canPlayerMove() {
        return player.canMove();
    }

    public int[] getComputerStep() {
        Position position = computerPlayer.getMovePosition();
        return new int[]{position.getX(), position.getY()};
    }

    public int[] getScores() {
        return new int[]{player.getChessMenNum(), computerPlayer.getChessMenNum()};
    }

    public int[][] getChessBoard() {
        int dimension = chessBoard.getDimension();
        int[][] board = new int[dimension][dimension];
        ChessMan[][] chessMen = chessBoard.getChessMen();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (chessMen[i][j] == null) {
                    board[i][j] = 0;
                } else
                    board[i][j] = chessMen[i][j].getColor() == Color.BLACK ? 1 : 2;
            }
        }
        return board;
    }
}
