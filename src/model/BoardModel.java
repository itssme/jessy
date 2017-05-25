package model;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import logging.LoggingSingleton;
import main.ChessGameController;
import main.Main;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

import static board.Move.getRowColPair;
import static board.Move.getSquare;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:   The model for the Graphic ChessBoard. This is mostly the Graphical
 *          Model of a ChessBoard, and not rather the underlying table.
 */
public class BoardModel extends JTable {

    /**
     * Which Piece was selected last, to paint the recommendations
     */
    private Piece selected = null;
    private Square selectedStartSquare = null;

    /**
     * Which move was made to paint the last move
     */
    private board.Move madeMove = null;


    public void setMadeMove(board.Move madeMove) {
        this.madeMove = madeMove;
    }

    /**
     * A reference to itself to access the Model from other classes
     */
    private static BoardModel ref;

    /**
     * An ArrayList which holds all the possibleMoves
     */
    private static ArrayList<Square> possibleMoves = new ArrayList<>();

    public void setSelected(Piece selected) {
        this.selected = selected;
    }

    public void setSelectedStartSquare(Square selectedStartSquare) {
        this.selectedStartSquare = selectedStartSquare;
    }

    /**
     * A small constructor which was inherited from DefaultTableModel.
     * It initializes all the necessary stuff and set attributes for the table.
     *
     * @param rows The amount of rows which shall be used
     * @param cols The amount of columns which shall be used
     */
    public BoardModel(int rows, int cols) {
        super(new ChessBoardModel(rows, cols));
        this.getColumnModel().setColumnSelectionAllowed(false);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(true);
        this.setSelectionBackground(Color.CYAN);
        this.setSelectionForeground(Color.CYAN);
        this.setMaximumSize(new Dimension((int) (1280 * 0.75),
                (int) (720 * 0.75)));
        this.setRowHeight(60);
        for (int i = 0; i < this.getColumnModel().getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setPreferredWidth(60);
        }
        this.doLayout();
        this.initBoard();
        ref = this;
    }

    /**
     * InitBoard is mainly there for drawing the figures onto the board
     */
    private void initBoard() {
        this.revalidate();
        System.gc();
        Runtime.getRuntime().gc();
        for (Piece p :
                Main.CHESSGAMEBOARD.boardToArray()) {
            Main.CHESSGAMEBOARD.getPieceLocation(p).forEach(square -> {
                int[] rowCol = getRowColPair(square);
                // FIXME: How does this work when getRowColPair returns null?
                drawFigures(getImageIconFromPiece(p.value()),
                        rowCol[0],
                        rowCol[1]);
            });
        }
        this.repaint();
    }

    /**
     * This is a simple conversion function for converting the Piece-values -
     * the description - to the filenames of the specific *.png
     *
     * @param pieceValue The textual representation of the specific Piece
     * @return The correct Image of null, to draw nothing
     */
    public ImageIcon getImageIconFromPiece(String pieceValue) {
        if (pieceValue.equals("NONE")) {
            return null;
        }
        String[] parts = pieceValue.toLowerCase().split("_");
        return new ImageIcon(this.getClass().getResource("graphics/" +
                parts[1] +
                "_" +
                parts[0] +
                        ".png"));
    }

    /**
     * This function draws the specified ImageIcon on the given row and column
     *
     * @param imageIcon The ImageIcon which should be drawn
     * @param row       The row, where to draw
     * @param col       The column, where to draw
     */
    public void drawFigures(ImageIcon imageIcon, int row, int col) {
        this.setValueAt(imageIcon, row, col);
    }


    /**
     * Prepares the renderer To draw the Board as a Black and White table with
     * the Figures layered on top and all the moves above that
     *
     * @param renderer The TableCellRenderer, which was passed down from the
     *                 JTable
     * @param row      Which row is currently edited
     * @param column   Which column is currently edited
     * @return The Component which is essentially each Cell
     */
    public Component prepareRenderer(TableCellRenderer renderer,
                                     int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        if ((row % 2) == 0) {
            if ((column % 2) == 0) {
                comp.setBackground(Color.BLACK);
            } else {
                comp.setBackground(Color.WHITE);
            }
        } else {
            if ((column % 2) == 0) {
                comp.setBackground(Color.WHITE);
            } else {
                comp.setBackground(Color.BLACK);
            }
        }
        if (madeMove != null) {
            if (row == madeMove.getFrom().getRow() &&
                    column == madeMove.getFrom().getCol()) {
                comp.setBackground(Color.GREEN);
            }
            if (row == madeMove.getTo().getRow() &&
                    column == madeMove.getTo().getCol()) {
                comp.setBackground(Color.GREEN);
            }
        }
        if (selected != null && selectedStartSquare != null) {
            try {
                MoveGenerator.getInstance().generateLegalMoves(Main.CHESSGAMEBOARD).forEach(move -> {
                    if (move.getFrom().compareTo(selectedStartSquare) == 0) {
                        int[] rowCol = getRowColPair(move.getTo());
                        if (rowCol[0] == row && rowCol[1] == column) {
                            comp.setBackground(
                                    new Color(93, 150, 255, 75));
                            possibleMoves.add(move.getTo());
                        }
                    }
                });
            } catch (MoveGeneratorException e) {
                LoggingSingleton.getInstance().severe(
                        "An error occured: " +
                                e.getLocalizedMessage());
            }
        }
        this.drawFigures(getImageIconFromPiece(
                Main.CHESSGAMEBOARD.getPiece(getSquare(row, column)).value()),
                row,
                column);
        return comp;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return ImageIcon.class;
    }

    /**
     * A method which handles mouseclicks
     *
     * @return The Square, where the user clicked
     */
    public Square mouseClick() {
        int row = this.getSelectedRow();
        int col = this.getSelectedColumn();
        if (selectedStartSquare != null &&
                selected != null &&
                possibleMoves.contains(getSquare(row, col))) {

            ChessGameController.connection.send_move(
                    board.Move.getMoveFromLib(
                            new Move(
                                    selectedStartSquare, getSquare(row, col))));
            ChessGameController.getGameController().
                    executeMove(
                            new Move(selectedStartSquare, getSquare(row, col)));
            possibleMoves.clear();
        }
        selected = Main.CHESSGAMEBOARD.getPiece(getSquare(row, col));
        selectedStartSquare = getSquare(row, col);
        this.repaint();
        return getSquare(row, col);
    }

    /**
     * Refreshes the Board and redraws all the figures.
     */
    public static void refresh() {
        ref.initBoard();
    }
}
