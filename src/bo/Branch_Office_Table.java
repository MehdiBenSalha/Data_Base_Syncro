package bo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class Branch_Office_Table {
    final Object[] column = {"Date","Region","Product","Quantity","Cost","AMT","Tax","Total"};
    private JScrollPane scrollPane;
    private JTable dataTable;
    DefaultTableModel defaultTableModel;

    private Connection connection = null;
    private Statement statement = null;
    int index;
    public Branch_Office_Table(int index){
        Object[][] data = {};
        this.index = index;
        this.defaultTableModel = new DefaultTableModel(data, this.column);
        this.dataTable =new JTable(defaultTableModel);
        this.dataTable.setBounds(30,40,200,300);
        this.scrollPane = new JScrollPane(this.dataTable);
        try {
            this.fillTable();
        } catch (SQLException sqlException){

        }

    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void fillTable() throws SQLException {
        defaultTableModel.setRowCount(0);
        DBRetrieveService dbRetrieveService = new DBRetrieveService(index, true);
        List<Product> list = dbRetrieveService.retrieve();
        for (Product product : list){
            defaultTableModel.addRow(new Object[]{product.getDate().toString(),
                    product.getRegion(),
                    product.getProduct(),
                    Integer.toString(product.getQty()),
                    Float.toString(product.getCost()),
                    Double.toString(product.getAmt()),
                    Float.toString(product.getTax()),
                    Double.toString(product.getTotal()),
            });
        }

    }
}