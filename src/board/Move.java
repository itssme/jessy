package board;

import chessfigure.ChessFigure;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Move of a Chessfigure
 */
public class Move {

    private ChessFigure chessFigure;
    private Position to;

    public Move(ChessFigure chessFigure, Position to) {
        this.chessFigure = chessFigure;
        this.to = to;
    }
}
