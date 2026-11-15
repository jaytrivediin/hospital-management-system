package myproject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnectionTest {

    public static void main(String[] args) {

        try (Connection con = DatabaseConnection.getConnection()) {

            System.out.println("Database Connected Successfully!");

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT DATABASE(), VERSION()");

            if (rs.next()) {
                System.out.println("Database: " + rs.getString(1));
                System.out.println("MySQL Version: " + rs.getString(2));
            }

        } catch (Exception e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

        }
    }
}