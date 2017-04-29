package model;

import board.Position;
import chessfigure.*;
import logging.Logging;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class BoardModel extends JTable implements MouseListener {

    @NotNull
    static BoardModel boardReference;
    private ArrayList<ChessFigure> whiteFigures;
    private ArrayList<ChessFigure> blackFigures;

    public BoardModel(int rows, int cols) {
        super(new DefaultTableModel(rows,
                cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        this.getColumnModel().setColumnSelectionAllowed(true);
        this.setColumnSelectionAllowed(true);
        this.setRowSelectionAllowed(true);
        this.setCellSelectionEnabled(true);
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setSelectionBackground(Color.CYAN);
        this.setSelectionForeground(Color.CYAN);
        this.genWhite();
        this.genBlack();
        this.setMaximumSize(new Dimension((int) (1280 * 0.75),
                (int) (720 * 0.75)));
        this.setRowHeight(60);
        for (int i = 0; i < this.getColumnModel().getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setPreferredWidth(60);
        }
        this.setRowSelectionAllowed(false);
        this.doLayout();
        boardReference = this;

    }

    public ArrayList<ChessFigure> getWhiteFigures() {
        return whiteFigures;
    }

    public ArrayList<ChessFigure> getBlackFigures() {
        return blackFigures;
    }

    private void genWhite() {
        whiteFigures = new ArrayList<>();
        whiteFigures.add(new Rook(new Position(7, 0),
                "graphics/rook_white.png", true));
        whiteFigures.add(new Knight(new Position(7, 1),
                "graphics/knight_white.png", true));
        whiteFigures.add(new Bishop(new Position(7, 2),
                "graphics/bishop_white.png", true));
        whiteFigures.add(new King(new Position(7, 3),
                "graphics/king_white.png", true));
        whiteFigures.add(new Queen(new Position(7, 4),
                "graphics/queen_white.png", true));
        whiteFigures.add(new Bishop(new Position(7, 5),
                "graphics/bishop_white.png", true));
        whiteFigures.add(new Knight(new Position(7, 6),
                "graphics/knight_white.png", true));
        whiteFigures.add(new Rook(new Position(7, 7),
                "graphics/rook_white.png", true));

        for (int i = 0; i < 8; i++) {
            whiteFigures.add(new Pawn(new Position(6, i),
                    "graphics/pawn_white.png", true));
        }

        whiteFigures.forEach(new Consumer<ChessFigure>() {
            @Override
            public void accept(ChessFigure chessFigure) {
                BoardModel.this.setValueAt(new ImageIcon(chessFigure.getImg()),
                        chessFigure.getPos().getRow(),
                        chessFigure.getPos().getCol());
            }
        });
    }

    private void genBlack() {
        blackFigures = new ArrayList<>();
        blackFigures.add(new Rook(new Position(0, 0),
                "graphics/rook_black.png", false));
        blackFigures.add(new Knight(new Position(0, 1),
                "graphics/knight_black.png", false));
        blackFigures.add(new Bishop(new Position(0, 2),
                "graphics/bishop_black.png", false));
        blackFigures.add(new King(new Position(0, 3),
                "graphics/king_black.png", false));
        blackFigures.add(new Queen(new Position(0, 4),
                "graphics/queen_black.png", false));
        blackFigures.add(new Bishop(new Position(0, 5),
                "graphics/bishop_black.png", false));
        blackFigures.add(new Knight(new Position(0, 6),
                "graphics/knight_black.png", false));
        blackFigures.add(new Rook(new Position(0, 7),
                "graphics/rook_black.png", false));

        for (int i = 0; i < 8; i++) {
            blackFigures.add(new Pawn(new Position(1, i),
                    "graphics/pawn_black.png", false));
        }

        blackFigures.forEach(new Consumer<ChessFigure>() {
            @Override
            public void accept(ChessFigure chessFigure) {
                BoardModel.this.setValueAt(new ImageIcon(chessFigure.getImg()),
                        chessFigure.getPos().getRow(),
                        chessFigure.getPos().getCol());
            }
        });
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
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        JTable target = (JTable) evt.getSource();
        int row = target.getSelectedRow();
        int col = target.getSelectedColumn();
        ChessFigure ch = figureAt(new Position(row, col));
        if (ch != null) {
            Logging.logToFile(Level.INFO, ch.toString());
            ch.getPossibleMoves().forEach(new Consumer<Position>() {
                @Override
                public void accept(Position position) {
                    ((DefaultTableCellRenderer)BoardModel.this.getCellRenderer(position.getRow(), position.getCol())).setBackground(Color.CYAN);
                    BoardModel.this.refresh();
                }
            });
        }

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

    public ArrayList<ChessFigure> getWholeList() {
        ArrayList<ChessFigure> wholeList = new ArrayList<>();
        wholeList.addAll(getWhiteFigures());
        wholeList.addAll(getBlackFigures());
        return wholeList;
    }

    @Nullable
    public static ChessFigure figureAt(Position p) {
        ChessFigure[] wholeList = (ChessFigure[]) boardReference.getWholeList().toArray(new ChessFigure[0]);
        for (ChessFigure chessFig:
             wholeList) {
            if (chessFig.getPos().equals(p)) {
                return chessFig;
            }
        }
        return null;
    }

    public void refresh() {
        this.getWholeList().forEach(new Consumer<ChessFigure>() {
            @Override
            public void accept(ChessFigure chessFigure) {
                chessFigure.calculateMove();
            }
        });
        this.repaint();
    }


}
