package bo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRetrieveService {
    private int BO_index;

    public DBRetrieveService(int BO_index, boolean sent) {
        this.BO_index = BO_index;
        this.url = "jdbc:mysql://localhost:3306/branch_office_" + Integer.toString(BO_index);
        query = "SELECT * FROM product_sale" + (sent ? "" : " where sent=FALSE");
    }

    //Database
    public String url;
    public String user="root";
    public String password = "Mahdiisthebest@1920";
    //Query to get all elements
    public String query;
    //Data retrieve methods
    public List<Product> retrieve() throws SQLException{
        System.out.println(this.url);
        List<Product> result = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet resultSet = pst.executeQuery()
        ) {

            while(resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setDate(resultSet.getDate("date"));
                product.setRegion(resultSet.getString("region"));
                product.setProduct(resultSet.getString("product"));
                product.setQty(resultSet.getInt("qty"));
                product.setCost(resultSet.getFloat("cost"));
                product.setAmt(resultSet.getDouble("amt"));
                product.setTax(resultSet.getFloat("tax"));
                product.setTotal(resultSet.getDouble("total"));
                product.setBo_num(BO_index);
                result.add(product);
            }

            return result;
        }

    }
}

