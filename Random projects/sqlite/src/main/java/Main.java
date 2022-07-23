import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
        public static void main(String[] args) {
            String url = "jdbc:sqlite:";

            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(url);

            try (Connection con = dataSource.getConnection()) {
                if (con.isValid(5)) {
                    System.out.println("Connection is valid.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
