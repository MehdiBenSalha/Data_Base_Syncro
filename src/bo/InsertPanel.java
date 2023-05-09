package bo;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;

import javax.swing.*;


public class InsertPanel extends JPanel{
    private Connection connection = null;
    private Statement statement = null;
    public JTextField regionTextFld;
    public JTextField productTextFld;
    public JTextField quantityTextFld;
    public JTextField costTextFld;
    public JTextField amtTextFld;
    public JTextField taxTextFld;
    public JTextField totalTextFld;
    public JTextArea textArea;
    public JButton submitBtn;

    Branch_Office_Table branchOfficeTable;

    public InsertPanel(int i, Branch_Office_Table branchOfficeTable) {
        switch (i) {
            case 1: setBackground(Color.DARK_GRAY);
            break;
            case 2: setBackground(new Color(0x7e4bb4));
            break;
            case 3: setBackground(new Color(0x7c1b1b));
            break;
            default:setBackground(Color.black);
        }
        this.branchOfficeTable = branchOfficeTable;
        setPreferredSize(new Dimension(1000, 1000));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1000, 1000));

        JLabel regionLabel = new JLabel("Region: ");
        regionTextFld = new JTextField(15);

        JLabel productLabel = new JLabel("Product: ");
        productTextFld = new JTextField(20);

        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityTextFld = new JTextField(10);

        JLabel costLabel = new JLabel("Cost: ");
        costTextFld = new JTextField(10);

        JLabel amtLabel = new JLabel("AMT: ");
        amtTextFld = new JTextField(10);

        JLabel taxLabel = new JLabel("Tax: ");
        taxTextFld = new JTextField(10);

        JLabel totalLabel = new JLabel("Total: ");
        totalTextFld = new JTextField(10);

        submitBtn = new JButton("Submit");
        ButtonListener buttonListener = new ButtonListener(i);
        submitBtn.addActionListener(buttonListener);

        textArea = new JTextArea(10, 30);

       //add current date
        panel.add(regionLabel);
        panel.add(regionTextFld);
        panel.add(productLabel);
        panel.add(productTextFld);
        panel.add(quantityLabel);
        panel.add(quantityTextFld);
        panel.add(costLabel);
        panel.add(costTextFld);
        panel.add(amtLabel);
        panel.add(amtTextFld);
        panel.add(taxLabel);
        panel.add(taxTextFld);
        panel.add(totalLabel);
        panel.add(totalTextFld);
        panel.add(submitBtn);
        panel.add(textArea);
        add(panel, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
    }

    public class ButtonListener implements ActionListener{
        int i;
        public ButtonListener(int i){
            this.i=i;
        }
        public void actionPerformed(ActionEvent e){
            try {
                Calendar calendar = Calendar.getInstance();
                java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

                String url = "jdbc:mysql://localhost:3306/branch_office_" + Integer.toString(i);
                String user="root";
                String password = "Mahdiisthebest@1920";
                connection = DriverManager.getConnection(
                        url, user, password);

                    String region = regionTextFld.getText();
                    String product = productTextFld.getText();
                    int qty = Integer.parseInt(quantityTextFld.getText());
                    float cost = Float.parseFloat(costTextFld.getText());
                    double amt = Double.parseDouble(amtTextFld.getText());
                    float tax = Float.parseFloat(taxTextFld.getText());
                    double total = Double.parseDouble(totalTextFld.getText());


                // the mysql insert statement
                String query = " INSERT INTO product_sale(date, region, product, qty, cost, amt, tax, total)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                // create the mysql insert statement
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDate(1, startDate);
                statement.setString(2, region);
                statement.setString(3, product);
                statement.setInt(4, qty);
                statement.setFloat(5, cost);
                statement.setDouble(6, amt);
                statement.setFloat(7, tax);
                statement.setDouble(8, total);

                // execute the preparedstatement
                statement.execute();

                System.out.println(statement);

                connection.close();

                regionTextFld.setText("");
                productTextFld.setText("");
                quantityTextFld.setText("");
                costTextFld.setText("");
                amtTextFld.setText("");
                taxTextFld.setText("");
                totalTextFld.setText("");
                System.out.println("Succesfully Added");
               branchOfficeTable.fillTable();
                System.out.println("Succesfully Added");

            } catch (Exception exception){

            }
        }
    }
}
