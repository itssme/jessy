package model;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.sun.istack.internal.Nullable;
import logging.LoggingSingleton;
import main.Main;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class BoardModel extends JTable {

    private Piece selected = null;
    private Square selectedStartSquare = null;

    /**
     * An ArrayList which holds all the possibleMoves
     */
    private static ArrayList<Square> possibleMoves = new ArrayList<>();

    /**
     * A Map which converts Cells from the format E7 to two integers for
     * the JTable
     */
    private static TreeMap<Character, Integer> colToInt =
            new TreeMap<Character, Integer>();
    private static TreeMap<Integer, Character> intToCol =
            new TreeMap<>();
    private static TreeMap<Integer, Integer> libToInt =
            new TreeMap<>();

    /**
     * A small static block for the conversion-Map
     */
    static {
        colToInt.put('A', 0);
        colToInt.put('B', 1);
        colToInt.put('C', 2);
        colToInt.put('D', 3);
        colToInt.put('E', 4);
        colToInt.put('F', 5);
        colToInt.put('G', 6);
        colToInt.put('H', 7);

        intToCol.put(0, 'A');
        intToCol.put(1, 'B');
        intToCol.put(2, 'C');
        intToCol.put(3, 'D');
        intToCol.put(4, 'E');
        intToCol.put(5, 'F');
        intToCol.put(6, 'G');
        intToCol.put(7, 'H');

        libToInt.put(0, 8);
        libToInt.put(1, 7);
        libToInt.put(2, 6);
        libToInt.put(3, 5);
        libToInt.put(4, 4);
        libToInt.put(5, 3);
        libToInt.put(6, 2);
        libToInt.put(7, 1);


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
    }

    /**
     * InitBoard is mainly there for drawing the figures onto the board
     */
    private void initBoard() {
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
        String col = "white";
        if (parts[0].equals("black")) {
            col = "black";
        } else {
            col = "white";
        }
        return new ImageIcon("graphics/" +
                parts[1] +
                "_" +
                col +
                ".png");
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
        this.repaint();
    }

    /**
     * This function converts the E7 format of the library to a tuple of
     * integers
     *
     * Format: Pair[0] = row
     * Format: Pair[1] = col
     * @param sq The square, which should be converted
     * @return The tuple specifying the row and column or null.
     */
    @Nullable
    public static int[] getRowColPair(Square sq) {
        char[] parts = sq.toString().toCharArray();
        if (sq == Square.NONE) {
            return null;
        }
        return new int[]{
                (Character.getNumericValue(parts[1]) - 8) * (-1),
                colToInt.get(parts[0])
        };
    }

    /**
     * GetSquare returns an Enum-Element of the chesslibrary based on column
     * and row
     *
     * @param row The row of the Piece
     * @param col The column of the Piece
     * @return The Enum-Value of NONE
     */
    public static Square getSquare(int row, int col) {
        return Square.fromValue("" + intToCol.get(col) + libToInt.get(row));
    }

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
        if (selected != null && selectedStartSquare != null) {
            try {
                MoveGenerator.getInstance().generateLegalMoves(Main.CHESSGAMEBOARD).forEach(move -> {
                    if (move.getFrom().compareTo(selectedStartSquare) == 0) {
                        int[] rowCol = getRowColPair(move.getTo());
                        if (rowCol[0] == row && rowCol[1] == column) {
                            comp.setBackground(new Color(93, 205, 232, 50));
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
            Main.CHESSGAMEBOARD.doMove(new Move(selectedStartSquare, getSquare(row, col)));
            possibleMoves.clear();
        }
        selected = Main.CHESSGAMEBOARD.getPiece(getSquare(row, col));
        selectedStartSquare = getSquare(row, col);
        this.repaint();
        return getSquare(row, col);
    }
}
