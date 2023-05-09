package ho;

import ho.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRetrieveService {
    //Coordonnées de la base
    public String user="root";
    public String password = "Mahdiisthebest@1920";
    public String url = "jdbc:mysql://localhost:3306/home_office";

    //Query to get all elements
    public String query = "SELECT * FROM product_sale";
    //Data retrieve methods
    public List<Product> retrieve() throws SQLException {
        List<Product> result = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
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
                product.setBo_num(resultSet.getInt("bo_num"));
                result.add(product);
            }

            return result;
        }
    }
}

