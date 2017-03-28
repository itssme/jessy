package chessfigure;

import board.Position;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Knight Chessfigure (this is the only chessfigure which can jump over other figures)
 */
public class Knight extends ChessFigure {

    public Knight(Position pos, String img, boolean white) {
        super(pos, img, white, true);
    }

}
