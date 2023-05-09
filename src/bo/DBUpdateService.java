package bo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DBUpdateService {
    private int BO_index;

    public DBUpdateService(int BO_index) {
        this.BO_index = BO_index;
        this.url = "jdbc:mysql://localhost:3306/branch_office_" + Integer.toString(BO_index);
    }
    //Database
    public String url;
    public String user="root";
    public String password = "Mahdiisthebest@1920";
    //Query for setting the 'sent' attribute to TRUE.


    public String query = "UPDATE product_sale set sent = TRUE where id = ?";
    //Method for setting the 'sent' attribute to TRUE.


    public void update(List<Product> list) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            for(int i=0; i<list.size(); i++) {
                statement.setInt(1, list.get(i).getId());
                statement.executeUpdate();
            }
        }
    }
}
