package ho;

import java.sql.*;
import java.util.List;

public class DBInsertService {
    //Database
    public String url = "jdbc:mysql://localhost:3306/home_office";
    public String user="root";
    public String password = "Mahdiisthebest@1920";
    //Insert query
    public String query = "INSERT INTO product_sale(date, region, product, qty, cost, amt, tax, total, bo_num) values(?,?,?,?,?,?,?,?,?)";

    //insert method
    public void insert(List<Product> list) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            for(int i=0; i<list.size(); i++) {
                System.out.println(i);
                Product product = list.get(i);
                statement.setDate(1, new Date(product.getDate().getTime()));
                statement.setString(2, product.getRegion());
                statement.setString(3, product.getProduct());
                statement.setInt(4, product.getQty());
                statement.setFloat(5, product.getCost());
                statement.setDouble(6, product.getAmt());
                statement.setFloat(7, product.getTax());
                statement.setDouble(8, product.getTotal());
                statement.setInt(9,product.getBo_num());
                statement.executeUpdate();
            }
        }
    }
}
