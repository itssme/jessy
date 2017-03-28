package chessfigure;

import board.Position;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Chessfigure is an abstract class which all Chessfigures inherit from
 */
public abstract class ChessFigure {

    protected Position pos;
    protected String img;
    protected boolean canJump = false;
    protected boolean isWhite = false;

    public ChessFigure(Position pos, String img, boolean isWhite, boolean canJump) {
        this.pos     = pos;
        this.img     = img;
        this.isWhite = isWhite;
        this.canJump = canJump;
    }

    public Position getPos() {
        return pos;
    }

    public String getImg() {
        return img;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Image getImage() {
        URL url = ClassLoader.getSystemClassLoader().getResource(this.img);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return new ImageIcon().getImage();
    }

    @Override
    public String toString() {
        return "Position: " + pos.toString() + " | " + "Image: " + img + "";
    }
}
