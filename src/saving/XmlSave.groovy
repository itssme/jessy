package saving

import board.Move
import groovy.xml.MarkupBuilder

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Chessfigure is an abstract class which all Chessfigures inherit from
 */
class XmlSave {

    def moves = [];

    public static void addMove(Move move) {
        moves.add([move.getFrom().getRow(), move.getFrom().getCol(),
                   move.getTo().getRow(), move.getTo().getCol()])
    }

    public static void save() {
        def f = new File("chessSave.xml")
        MarkupBuilder mb = new MarkupBuilder(new PrintWriter(f))
        final def each = mb.game() {
            moves.each { move ->
                move() {
                    fromrow(move[0])
                    fromcol(move[1])
                    torow(move[2])
                    tocol(move[3])
                }
            }
        }
        each
    }
}