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
        if (this.isAtStartPosition) {
            this.positionIsMovable(
                    new Position((isWhite()) ? this.pos.getRow() - 1 :
                            this.pos.getCol() + 1,
                            this.pos.getCol()));
            this.positionIsMovable(
                    new Position((isWhite()) ? this.pos.getRow() - 2 :
                            this.pos.getCol() + 2,
                            this.pos.getCol()));
            this.isAtStartPosition = false;
        } else {
            this.positionIsMovable(
                    new Position((isWhite()) ? this.pos.getRow() - 1 :
                            this.pos.getRow() + 1,
                            this.pos.getCol()));
        }
    }
}