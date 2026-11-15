package myproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hospital_db";

    private static final String USER = "hospital_app";

    private static final String PASSWORD = "HospDb@2026!";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver is missing!", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

//buildPatientRecordsScreen()
//buildAppointmentFormView()
//loadAppointments()
//updateSelectedAppointment()
//& "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" -u root -p
//SHOW DATABASES;
//USE hospital_db;
//SHOW TABLES;
//SELECT * FROM appointments;
