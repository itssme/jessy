package chessfigure;

import board.Position;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Bishop chessfigure
 */
public class Bishop extends ChessFigure {

    public Bishop(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

    @Override
    public void calculateMove() {
        for (int i = 1; i < 8; i++) {
            Position p = new Position(this.pos.getRow() + i,
                    this.pos.getCol() + i);
            if (!this.positionIsMovable(p)) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            Position p = new Position(this.pos.getRow() - i,
                    this.pos.getCol() + i);
            if (!this.positionIsMovable(p)) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            Position p = new Position(this.pos.getRow() + i,
                    this.pos.getCol() - i);
            if (!this.positionIsMovable(p)) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            Position p = new Position(this.pos.getRow() + i,
                    this.pos.getCol() + i);
            if (!this.positionIsMovable(p)) {
                break;
            }
        }

    }


}
