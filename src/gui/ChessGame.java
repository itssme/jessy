package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-20
 * Project: jessy
 * Desc.:
 */
public class ChessGame extends JFrame {

    public static void play() {
        ChessGame game = new ChessGame();
        game.setVisible(true);
    }

    private ChessGame() {
        super("jessy");
        this.setMinimumSize(new Dimension(1280, 720));
        this.setLayout(new GridBagLayout());
        JTable board = new JTable(new DefaultTableModel(8, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        this.add(this.createBoard(board, gbc), gbc);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JTable createBoard(JTable board, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        board.setMaximumSize(new Dimension((int) (1280 * 0.75),
                (int) (720 * 0.75)));
        board.setRowHeight(60);
        for (int i = 0; i < board.getColumnModel().getColumnCount(); i++) {
            board.getColumnModel().getColumn(i).setPreferredWidth(60);
        }
        board.setRowSelectionAllowed(false);
        board.doLayout();
        return board;
    }

    private JList<String> createScoreList(JList<String> target, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 0;
        return null;
    }

}
