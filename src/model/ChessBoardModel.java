package model;

import javax.swing.table.DefaultTableModel;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    02.05.17
 * Poject:  jessy
 * Package: model
 * Desc.:   The underlying Representation of the Table
 */
public class ChessBoardModel extends DefaultTableModel {

    /**
     * A simple constructor which is inherited by the Supertype
     *
     * @param rowCount    How many Rows should be used for this model
     * @param columnCount How many Columns should be used for this model
     */
    public ChessBoardModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    /**
     * An inherited method to indicate, whether the Cell is editable
     * @param row The Row is passed in to show if it is editable
     * @param column The Column is passed in to show if it is editable
     * @return For this model it is always false
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
