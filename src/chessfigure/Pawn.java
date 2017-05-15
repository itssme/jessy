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

    private Position startPosition;

    public Pawn(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
        this.startPosition = pos;
    }

    @Override
    public void calculateMove() {
        this.resetMoves();
        if (this.isWhite() == true) {
            if (this.pos.equals(startPosition)) {
                for (int i = 1; i < 3; i++) {
                    if (!this.positionIsMovable(
                            new Position(this.getPos().getRow() - i,
                                    this.getPos().getCol()))) {
                        break;
                    }
                }

            } else {

                this.positionIsMovable(
                        new Position(this.getPos().getRow() - 1,
                                this.getPos().getCol()));
            }
        } else {
            if (this.pos.equals(startPosition)) {
                for (int i = 1; i < 3; i++) {
                    if (!this.positionIsMovable(new Position(
                            this.getPos().getRow() + i,
                            this.getPos().getCol()))) {
                        break;
                    }
                }
            } else {
                this.positionIsMovable(new Position(
                        this.getPos().getRow() + 1,
                        this.getPos().getCol()));
            }
        }
    }
}