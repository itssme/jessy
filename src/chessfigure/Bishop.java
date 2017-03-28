package chessfigure;

import board.Position;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Bishop chessfigure
 */
public class Bishop extends  ChessFigure {

    public Bishop(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

}
