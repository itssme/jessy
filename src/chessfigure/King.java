package chessfigure;

import board.Position;
import java.io.File;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The King chessfigure
 */
public class King extends ChessFigure {

    public King(Position pos, File img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

}
