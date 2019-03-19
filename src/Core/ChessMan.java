package Core;

class ChessMan {
    private Color color;
    private Position position;

    ChessMan(Color color) {
        this.color = color;
    }

    ChessMan(Color color, int x, int y) {
        this.color = color;
        this.position = new Position(x, y);
    }

    Position getPosition() {
        return position;
    }

    Color getColor() {
        return color;
    }

    void changeColor() {
        this.color = this.color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }
}

enum Color {
    BLACK, WHITE
}
