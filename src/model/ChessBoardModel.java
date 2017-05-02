package model;

import javax.swing.table.DefaultTableModel;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    02.05.17
 * Poject:  jessy
 * Package: model
 * Desc.:
 */
public class ChessBoardModel extends DefaultTableModel {

    public ChessBoardModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
