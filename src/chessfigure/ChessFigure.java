package chessfigure;

import board.MoveList;
import board.Position;
import model.BoardModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Chessfigure is an abstract class which all Chessfigures inherit from
 */
public abstract class ChessFigure {

    protected Position pos;
    private String img;
    private boolean canJump = false;
    private boolean isWhite = false;
    private MoveList<Position> possibleMoves;

    public ChessFigure(Position pos, String img, boolean isWhite, boolean canJump) {
        this.pos     = pos;
        this.img     = img;
        this.isWhite = isWhite;
        this.canJump = canJump;
        this.possibleMoves = new MoveList<>();
        //this.display();
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
        return "Position: " + pos.toString() + " | " + "Image: " + img + "\n" + "Faction: " +
                ((this.isWhite()) ? "White": "Black");
    }

    abstract public void calculateMove();

    private void display() {
        System.out.println(this.toString());
        System.out.println("" + this.getClass() + " standing at: " + this.pos.toString());
        possibleMoves.forEach(action -> System.out.println(action.toString()));
        System.out.println();
    }

    protected boolean isOfSameFaction(ChessFigure otherFigure) {
        return this.isWhite() == this.isWhite();
    }

    protected boolean positionIsMovable(Position p) {
        if (BoardModel.figureAt(p) != null) {
            return false;
        } else {
            this.possibleMoves.add(p.isValid());
            return true;
        }
    }

    public MoveList<Position> getPossibleMoves() {
        return possibleMoves;
    }
}
