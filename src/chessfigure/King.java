package chessfigure;

import board.Position;

/**
 * Name:    Joel Klimont
 * Class:   3CH
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   The King chessfigure
 */
public class King extends ChessFigure {

    public King(Position pos, String img, boolean isWhite) {
        super(pos, img, isWhite, false);
    }

    @Override
    public void calculateMove() {
        this.positionIsMovable(new Position(this.pos.getRow() + 1, this.pos.getCol()));
        this.positionIsMovable(new Position(this.pos.getRow() + 1, this.pos.getCol() + 1));
        this.positionIsMovable(new Position(this.pos.getRow(), this.pos.getCol() + 1));
        this.positionIsMovable(new Position(this.pos.getRow() - 1, this.pos.getCol() + 1));
        this.positionIsMovable(new Position(this.pos.getRow() - 1, this.pos.getCol()));
        this.positionIsMovable(new Position(this.pos.getRow() - 1, this.pos.getCol() - 1));
        this.positionIsMovable(new Position(this.pos.getRow(), this.pos.getCol() - 1));
        this.positionIsMovable(new Position(this.pos.getRow() + 1, this.pos.getCol() - 1));
    }
}
