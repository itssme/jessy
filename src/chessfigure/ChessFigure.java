package chessfigure;

import board.Position;
import java.io.File;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Chessfigure is an abstract class which all Chessfigures inherit from
 */
public abstract class ChessFigure {

    protected Position pos;
    protected File img;
    protected boolean canJump = false;
    protected boolean isWhite = false;

    public ChessFigure(Position pos, File img, boolean isWhite, boolean canJump) {
        this.pos     = pos;
        this.img     = img;
        this.isWhite = isWhite;
        this.canJump = canJump;
    }
}
