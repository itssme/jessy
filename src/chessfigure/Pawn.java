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

    private boolean isAtStartPosition = true;

    public Pawn(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

    @Override
    public void calculateMove() {
        if (this.isWhite() == true) {
            if (this.isAtStartPosition == true) {
                this.positionIsMovable(
                        new Position(this.getPos().getRow() - 2,
                                this.getPos().getCol()));
                this.isAtStartPosition = false;
            } else {

                this.positionIsMovable(
                        new Position(this.getPos().getRow() - 1,
                                this.getPos().getCol()));
            }
        } else {
            if (this.isAtStartPosition == true) {
                this.positionIsMovable(new Position(
                        this.getPos().getRow() + 2,
                        this.getPos().getCol()));
                this.isAtStartPosition = false;
            } else {
                this.positionIsMovable(new Position(
                        this.getPos().getRow() + 1,
                        this.getPos().getCol()));
            }
        }
    }
}