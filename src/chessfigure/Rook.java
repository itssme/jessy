package chessfigure;

import board.Position;
import model.BoardModel;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The Rook or "Tower" chessfigure
 */
public class Rook extends ChessFigure {

    public Rook(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

    @Override
    public void calculateMove() {
        for (int i = 1; i < 8; i++) {
            if (!this.positionIsMovable(new Position(this.pos.getRow() + i, this.pos.getCol()))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (!this.positionIsMovable(new Position(this.pos.getRow(), this.pos.getCol() + i))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (!this.positionIsMovable(new Position(this.pos.getRow() - i, this.pos.getCol()))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (!this.positionIsMovable(new Position(this.pos.getRow(), this.pos.getCol() - i))) {
                break;
            }
        }
    }
}
