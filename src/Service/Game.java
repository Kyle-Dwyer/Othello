package Service;

import Core.Othello;
import Util.FileUtil;
import constant.FileConstant;
import constant.InfoConstant;
import jxl.write.WriteException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Game {
    private Othello othello;
    private boolean initialized = false;
    private boolean playerColor;//true:Black;false:White
    private int dimension;
    private boolean wrongStep = false;
    private long gameTime;

    public Game() {
        othello = new Othello();
    }

    public Game(int dimension, boolean color) {
        othello = new Othello();
        initialize(dimension, color);
    }

    /*初始化棋盘大小，玩家颜色*/
    public void initialize(int dimension, boolean color) {
        this.dimension = dimension;
        playerColor = color;
        initialized = othello.initialize(dimension, color);
    }

    public void start() {
        if (!initialized) {
            throw new RuntimeException(InfoConstant.UNINITIALIZED_WARNING);
        }
        long startTime = System.currentTimeMillis();
        //游戏开始，打印初始棋盘
        System.out.println(playerColor ? InfoConstant.PLAYER_BLACK : InfoConstant.PLAYER_WHITE);
        printChessBoard();
        //检查电脑是否先手
        if (!playerColor) {
            othello.autoMove();
            printComputerMove();
            printChessBoard();
        }
        //对局
        while (!checkGameOver()) {
            //检查是否非法落子
            if (!playerMove()) {
                wrongStep = true;
                break;
            }
            //玩家落子结束，检查是否结束
            if (checkGameOver()) break;
            //检查电脑能否落子
            if (!othello.computerMove()) {
                printNoValidStep(!playerColor);
            } else {
                printComputerMove();
                printChessBoard();
            }
        }
        //游戏结束，打印结果
        printResult();
        long endTime = System.currentTimeMillis();
        gameTime = endTime - startTime;
    }

    private boolean playerMove() {
        if (othello.canPlayerMove()) {
            //读取用户合法输入
            Scanner scanner = new Scanner(System.in);
            String position;
            char tail = (char) ('a' + dimension - 1);
            do {
                System.out.print(playerColor ? InfoConstant.BLACK_REMIND : InfoConstant.WHITE_REMIND);
                position = scanner.nextLine().trim().toLowerCase();
            }
            while (!(position.length() >= 2 && position.charAt(0) >= 'a' && position.charAt(0) <= tail && position.charAt(1) >= 'a' && position.charAt(1) <= tail));
            //判断是否合法落子
            if (othello.isLegalStep(position.charAt(0) - 'a', position.charAt(1) - 'a')) {
                othello.playerMove(position.charAt(0) - 'a', position.charAt(1) - 'a');
            } else {
                return false;
            }
            printChessBoard();
        } else {
            printNoValidStep(playerColor);
        }
        return true;
    }

    private boolean checkGameOver() {
        return othello.checkFull() || othello.checkValid();
    }

    private int checkWinner() {
        if (wrongStep)
            return playerColor ? 1 : 0;
        int[] score = othello.getScores();
        if (score[0] > score[1])
            return playerColor ? 0 : 1;
        if (score[0] < score[1])
            return playerColor ? 1 : 0;
        else return 2;
    }

    public String checkScore() {
        int[] score = othello.getScores();
        return playerColor ? (score[0] + ":" + score[1]) : (score[1] + ":" + score[0]);
    }


    private void printComputerMove() {
        int[] position = othello.getComputerStep();
        System.out.print(playerColor ? InfoConstant.COMP_WHITE_PLACE : InfoConstant.COMP_BLACK_PLACE);
        System.out.println("" + (char) (position[0] + 'a') + (char) (position[1] + 'a'));
    }

    private void printChessBoard() {
        String icons = InfoConstant.ICONS;
        System.out.print("\t");
        for (int i = 0; i < dimension; i++) {
            System.out.print(icons.charAt(i) + "\t");
        }
        System.out.println();
        int[][] chessBoard = othello.getChessBoard();
        for (int i = 0; i < dimension; i++) {
            System.out.print(icons.charAt(i) + "\t");
            for (int j = 0; j < dimension; j++) {
                if (chessBoard[i][j] == 0) {
                    System.out.print(InfoConstant.EMPTY_PIECE + "\t");
                } else if (chessBoard[i][j] == 1) {
                    System.out.print(InfoConstant.BLACK_PIECE + "\t");
                } else {
                    System.out.print(InfoConstant.WHITE_PIECE + "\t");
                }
            }
            System.out.println();
        }
    }

    private void printResult() {
        //判断是否双方无子可落
        if (othello.checkValid() && !othello.checkFull()) {
            System.out.println(InfoConstant.BOTH_NO_VALID);
        }
        //判断是否非法落子
        if (wrongStep) {
            System.out.println(InfoConstant.INVALID_MOVE);
        }
        System.out.println(InfoConstant.GAME_OVER);
        printScore();
        String[] winner = {InfoConstant.XWIN, InfoConstant.OWIN, InfoConstant.DRAW};
        System.out.println(winner[checkWinner()]);
    }

    private void printScore() {
        if (wrongStep) {
            System.out.println(InfoConstant.HUMAN_GAVE_UP);
            return;
        }
        String printScore = checkScore();
        System.out.println(InfoConstant.B_VS_W + printScore);
    }

    private void printNoValidStep(boolean color) {
        System.out.print(color ? InfoConstant.BLACK_NO_VALID : InfoConstant.WHITE_NO_VALID);
    }

    /*记录日志*/
    public void log() throws WriteException, IOException {
        String[] data = new String[6];
        data[0] = getTime();
        data[1] = "" + (gameTime / 1000);
        data[2] = "" + dimension + "*" + dimension;
        data[3] = playerColor ? "human" : "computer";
        data[4] = !playerColor ? "human" : "computer";
        data[5] = wrongStep ? InfoConstant.HUMAN_GAVE_UP : checkScore();
        FileUtil.write(data, FileConstant.DATA_PATH);
//        FileUtil.createXl();
//        FileUtil.editXl(this);
    }

    private static String getTime() {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return dateFormat.format(date);
    }
}
