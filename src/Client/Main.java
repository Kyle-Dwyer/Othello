package Client;

import Service.Game;
import constant.InfoConstant;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws WriteException, IOException {
        int size = getSize();
        boolean color = getColor();
        Game game = new Game(size, color);
        game.start();
        game.log();
    }

    private static int getSize() {
        Scanner scanner = new Scanner(System.in);
        int size;
        do {
            System.out.print(InfoConstant.DIMENSION);
            size = scanner.nextInt();
        } while (!(size % 2 == 0 && size >= 4 && size <= 26));
        return size;
    }

    private static boolean getColor() {
        Scanner scanner = new Scanner(System.in);
        char color;
        do {
            System.out.print(InfoConstant.COLOR);
            color = scanner.next().toUpperCase().charAt(0);
        } while (color != 'X' && color != 'O');
        return color == 'O';
    }
}
