package model;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import main.Main;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TreeMap;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class BoardModel extends JTable implements MouseListener {

    /**
     * A Map which converts Cells from the format E7 to two integers for
     * the JTable
     */
    private static TreeMap<Character, Integer> rowToInt =
            new TreeMap<Character, Integer>();

    /**
     * A small static block for the conversion-Map
     */
    static {
        rowToInt.put('A', 0);
        rowToInt.put('B', 1);
        rowToInt.put('C', 2);
        rowToInt.put('D', 3);
        rowToInt.put('E', 4);
        rowToInt.put('F', 5);
        rowToInt.put('G', 6);
        rowToInt.put('H', 7);
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
            System.out.println(p.toString());
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
        return new ImageIcon("graphics/" +
                parts[1] +
                "_" +
                parts[0] +
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
     * @param sq The square, which should be converted
     * @return The tuple specifying the row and column or null.
     */
    @Nullable
    public static int[] getRowColPair(Square sq) {
        char[] parts = sq.toString().toCharArray();
        if (sq == Square.NONE) {
            return null;
        }
        return new int[]{Character.getNumericValue(parts[1]) - 1,
                rowToInt.get(parts[0])};
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
        return comp;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return ImageIcon.class;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
