package myproject;

import java.sql.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    public static void registerUser(
            String userId,
            String mobile,
            String email,
            char[] password)
            throws SQLException, GeneralSecurityException {

        String hashedPassword = PasswordUtil.hashPassword(password);

        String sql = "INSERT INTO users (user_id, mobile, email, password_hash) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, mobile);
            ps.setString(3, email);
            ps.setString(4, hashedPassword);
            ps.executeUpdate();
        }
    }

    public static boolean authenticateUser(
            String userId,
            char[] password)
            throws SQLException, GeneralSecurityException {

        String sql = "SELECT password_hash FROM users WHERE user_id = ? OR email = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return PasswordUtil.verifyPassword(
                            password, rs.getString("password_hash"));
                }
            }
        }

        return false;
    }

    public static int saveAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments " +
                "(patient_name, age, sex, phone_number, address, emergency_contact) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, appointment.getPatientName());
            ps.setInt(2, Integer.parseInt(appointment.getAge()));
            ps.setString(3, appointment.getSex());
            ps.setString(4, appointment.getPhoneNumber());
            ps.setString(5, appointment.getAddress());
            ps.setString(6, appointment.getEmergencyContact());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Appointment ID could not be generated.");
    }

    public static List<Object[]> getAppointments(String searchText) throws SQLException {
        List<Object[]> appointments = new ArrayList<>();

        String sql = "SELECT appointment_id, patient_name, age, sex, phone_number, " +
                "address, emergency_contact, booking_date FROM appointments " +
                "WHERE CAST(appointment_id AS CHAR) LIKE ? OR patient_name LIKE ? " +
                "ORDER BY booking_date DESC";

        String search = (searchText == null || searchText.trim().isEmpty())
                ? "%"
                : "%" + searchText.trim() + "%";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, search);
            ps.setString(2, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Object[]{
                            rs.getInt("appointment_id"),
                            rs.getString("patient_name"),
                            rs.getInt("age"),
                            rs.getString("sex"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getString("emergency_contact"),
                            rs.getTimestamp("booking_date")
                    });
                }
            }
        }

        return appointments;
    }

    public static boolean updateAppointment(
            int appointmentId,
            String patientName,
            int age,
            String sex,
            String phoneNumber,
            String address,
            String emergencyContact) throws SQLException {

        String sql = "UPDATE appointments SET patient_name = ?, age = ?, sex = ?, " +
                "phone_number = ?, address = ?, emergency_contact = ? " +
                "WHERE appointment_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patientName);
            ps.setInt(2, age);
            ps.setString(3, sex);
            ps.setString(4, phoneNumber);
            ps.setString(5, address);
            ps.setString(6, emergencyContact);
            ps.setInt(7, appointmentId);

            return ps.executeUpdate() > 0;
        }
    }

    public static boolean deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        }
    }
}
