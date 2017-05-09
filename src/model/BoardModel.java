package model;

import board.Position;
import chessfigure.*;
import logging.LoggingSingleton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
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

    static BoardModel boardReference;
    private ArrayList<ChessFigure> whiteFigures;
    private ArrayList<ChessFigure> blackFigures;
    private ChessFigure selected = null;

    public BoardModel(int rows, int cols) {
        super(new ChessBoardModel(rows, cols));
        this.getColumnModel().setColumnSelectionAllowed(false);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(true);
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
        this.doLayout();
        boardReference = this;
        this.drawFigures();
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
    }

    public void drawFigures() {
        blackFigures.forEach(new Consumer<ChessFigure>() {
            @Override
            public void accept(ChessFigure chessFigure) {
                //System.out.println(chessFigure.toString());
                ((ChessBoardModel) BoardModel.this.getModel()).setValueAt(
                        new ImageIcon(chessFigure.getImg()),
                        chessFigure.getPos().getRow(),
                        chessFigure.getPos().getCol());
            }
        });


        whiteFigures.forEach(new Consumer<ChessFigure>() {
            @Override
            public void accept(ChessFigure chessFigure) {
                //System.out.println(chessFigure);
                ((ChessBoardModel) BoardModel.this.getModel()).setValueAt(
                        new ImageIcon(chessFigure.getImg()),
                        chessFigure.getPos().getRow(),
                        chessFigure.getPos().getCol());

            }
        });

        this.refresh();
        this.revalidate();
        this.repaint();
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
        if (selected != null) {
            selected.getPossibleMoves().forEach(consumer -> {
                if (row == consumer.getRow() && column == consumer.getCol()) {
                    comp.setBackground(new Color(88, 115, 232, 100));
                }
            });
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
        Position click = new Position(row, col);
        if (this.selected != null) {
            LoggingSingleton.getInstance().logToFile(Level.INFO,
                    BoardModel.this.selected.toString());
            for (Iterator<ChessFigure> i = this.getWholeList().iterator();
                 i.hasNext(); ) {
                ChessFigure fig = i.next();
                ((ChessBoardModel) this.getModel()).setValueAt(
                        new ImageIcon(fig.getImg()),
                        fig.getPos().getRow(),
                        fig.getPos().getCol()
                );
            }
        }
        if (this.selected != null
                && !this.selected.getPos().equals(click)
                && this.selected.getPossibleMoves().contains(click)) {
            System.out.println("Selecting a new Position");
            System.out.println("Before:\n" + this.selected.toString());
            Position after = click;
            this.selected.setPos(after);
            System.out.println("After:\n" + this.selected.toString());
            this.drawFigures();
            //Connection.send_move(new Move(before, after));
        }
        if (this.selected != null && !this.selected.getPossibleMoves().contains(click)) {
            this.selected = figureAt(click);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        BoardModel src = (BoardModel) e.getSource();
        int row = src.getSelectedRow();
        int col = src.getSelectedColumn();
        if (this.selected == null) {
            this.selected = BoardModel.figureAt(new Position(row, col));
            this.revalidate();
            this.repaint();
        }
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

    public static ChessFigure figureAt(Position p) {
        ChessFigure[] wholeList = (ChessFigure[]) boardReference.getWholeList().
                toArray(new ChessFigure[0]);
        for (ChessFigure chessFig :
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
        this.revalidate();
        this.repaint();
    }

}
