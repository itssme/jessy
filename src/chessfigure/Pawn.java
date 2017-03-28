package chessfigure;

import board.Position;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Pawn chessfigure
 */
public class Pawn extends ChessFigure {

    public Pawn(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

}