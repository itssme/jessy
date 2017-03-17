package chessfigure;

import board.Position;
import java.io.File;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Bishop chessfigure
 */
public class Bishop extends  ChessFigure {

    public Bishop(Position pos, File img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

}
