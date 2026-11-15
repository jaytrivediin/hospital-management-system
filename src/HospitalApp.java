package myproject;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class HospitalApp extends JFrame {

    private CardLayout mainCardLayout;
    private JPanel mainContainer;

    private CardLayout deptCardLayout;
    private JPanel deptContainer;

    private JLabel doctorImageLabel;
    private JLabel doctorNameLabel;
    private JLabel doctorDetailsLabel;
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsTableModel;
    private JTextField patientSearchField;

    private final Color THEME_BLUE = new Color(38, 34, 98);
    private final Color THEME_RED = new Color(237, 28, 36);
    private final Color THEME_BG = Color.WHITE;
    private final Color THEME_LIGHT_GRAY = new Color(245, 245, 250);

    public HospitalApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Hospital Dashboard");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(THEME_BG);
        setLayout(new BorderLayout());

        add(buildGlobalHeader(), BorderLayout.NORTH);
        add(buildGlobalFooter(), BorderLayout.SOUTH);

        mainCardLayout = new CardLayout();
        mainContainer = new JPanel(mainCardLayout);

        mainContainer.add(buildWelcomeScreen(), "Welcome");
        mainContainer.add(buildLoginScreen(), "Login");
        mainContainer.add(buildSignUpScreen(), "SignUp");
        mainContainer.add(buildDashboardScreen(), "Dashboard");
        mainContainer.add(buildDepartmentScreen(), "Department");
        mainContainer.add(buildPatientRecordsScreen(), "PatientRecords");

        add(mainContainer, BorderLayout.CENTER);
        mainCardLayout.show(mainContainer, "Welcome");
    }

    private JPanel buildGlobalHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(THEME_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, THEME_RED));

        JLabel logoLabel = getLogoLabel(350, 65);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(logoLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("HOSPITAL MANAGEMENT SYSTEM", SwingConstants.RIGHT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(THEME_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        header.add(titleLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildGlobalFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(THEME_BLUE);
        footer.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, THEME_RED));

        JLabel footerText = new JLabel("© 2026 Poornima Institute of Engineering & Technology", SwingConstants.CENTER);
        footerText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        footerText.setForeground(Color.WHITE);
        footerText.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footer.add(footerText, BorderLayout.CENTER);

        return footer;
    }

    private JPanel buildWelcomeScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(THEME_BG);
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 20));
        buttonPanel.setOpaque(false);

        JButton loginButton = createThemedButton("Login", THEME_BLUE, Color.WHITE);
        JButton signUpButton = createThemedButton("Sign Up", THEME_RED, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 60));

        loginButton.addActionListener(e -> mainCardLayout.show(mainContainer, "Login"));
        signUpButton.addActionListener(e -> mainCardLayout.show(mainContainer, "SignUp"));

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        JLabel welcomeLabel = new JLabel("Welcome to the Portal", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(THEME_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel buildLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(THEME_BG);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 25));
        formPanel.setBackground(THEME_LIGHT_GRAY);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(THEME_BLUE, 3),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        JLabel userLabel = new JLabel("Name / User ID:");
        userLabel.setForeground(THEME_BLUE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(userLabel);

        JTextField userIdField = new JTextField(15);
        userIdField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(userIdField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(THEME_BLUE);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(passLabel);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(passwordField);

        JButton backButton = createThemedButton("Back", THEME_LIGHT_GRAY, THEME_BLUE);
        JButton submitButton = createThemedButton("Submit", THEME_BLUE, Color.WHITE);

        backButton.addActionListener(e -> mainCardLayout.show(mainContainer, "Welcome"));
        submitButton.addActionListener(e -> {

            String userId = userIdField.getText().trim();
            char[] password = passwordField.getPassword();

            if (userId.isEmpty() || password.length == 0) {

                JOptionPane.showMessageDialog(
                        this, "Please enter User ID and Password!");

                java.util.Arrays.fill(password, '\0');
                return;
            }

            try {

                boolean authenticated =
                        DatabaseService.authenticateUser(userId, password);

                if (authenticated) {

                    JOptionPane.showMessageDialog(
                            this, "Login Successful!");

                    passwordField.setText("");
                    mainCardLayout.show(mainContainer, "Dashboard");

                } else {

                    JOptionPane.showMessageDialog(
                            this, "Invalid User ID or Password!");
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this, "Login Error: " + ex.getMessage());

            } finally {
                java.util.Arrays.fill(password, '\0');
            }
        });


        formPanel.add(backButton);
        formPanel.add(submitButton);
        panel.add(formPanel);

        return panel;
    }

    private JPanel buildSignUpScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(THEME_BG);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 20));
        formPanel.setBackground(THEME_LIGHT_GRAY);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(THEME_BLUE, 3),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JTextField userIdField = new JTextField(15);
        addThemedLabelAndField(formPanel, "Name / User ID:", userIdField);

        JTextField mobileField = new JTextField(15);
        ((AbstractDocument) mobileField.getDocument())
                .setDocumentFilter(new NumericFilter());
        addThemedLabelAndField(formPanel, "Mobile Number:", mobileField);

        JPasswordField passwordField = new JPasswordField(15);
        addThemedLabelAndField(formPanel, "Password:", passwordField);

        JTextField emailField = new JTextField(15);
        addThemedLabelAndField(formPanel, "Gmail:", emailField);

        JButton backButton = createThemedButton(
                "Back", THEME_LIGHT_GRAY, THEME_BLUE);

        JButton submitButton = createThemedButton(
                "Submit", THEME_RED, Color.WHITE);

        backButton.addActionListener(e ->
                mainCardLayout.show(mainContainer, "Welcome"));

        submitButton.addActionListener(e -> {

            String userId = userIdField.getText().trim();
            String mobile = mobileField.getText().trim();
            String email = emailField.getText().trim();
            char[] password = passwordField.getPassword();

            if (userId.isEmpty() || mobile.isEmpty()
                    || email.isEmpty() || password.length == 0) {

                JOptionPane.showMessageDialog(
                        this, "Please fill all fields!");

                java.util.Arrays.fill(password, '\0');
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid email address.");

                java.util.Arrays.fill(password, '\0');
                return;
            }

            if (mobile.length() < 10 || mobile.length() > 15) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid mobile number.");

                java.util.Arrays.fill(password, '\0');
                return;
            }

            if (password.length < 8) {
                JOptionPane.showMessageDialog(
                        this, "Password must contain at least 8 characters.");

                java.util.Arrays.fill(password, '\0');
                return;
            }

            try {
                DatabaseService.registerUser(
                        userId, mobile, email, password);

                JOptionPane.showMessageDialog(
                        this, "Registration Successful!");

                userIdField.setText("");
                mobileField.setText("");
                emailField.setText("");
                passwordField.setText("");

                mainCardLayout.show(mainContainer, "Login");

            } catch (java.sql.SQLIntegrityConstraintViolationException ex) {

                JOptionPane.showMessageDialog(
                        this, "User ID or Email already exists!");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this, "Registration Failed: " + ex.getMessage());

            } finally {
                java.util.Arrays.fill(password, '\0');
            }
        });

        formPanel.add(backButton);
        formPanel.add(submitButton);

        panel.add(formPanel);

        return panel;
    }

    private JPanel buildDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(THEME_BG);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(THEME_BG);

        JLabel dashboardLogo = new JLabel("", SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("images/pietlogo.png");
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(600, 110, Image.SCALE_SMOOTH);
                dashboardLogo.setIcon(new ImageIcon(img));
            } else {
                dashboardLogo.setText("PIET LOGO");
                dashboardLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
                dashboardLogo.setForeground(THEME_BLUE);
            }
        } catch (Exception e) {
            dashboardLogo.setText("PIET LOGO");
            dashboardLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            dashboardLogo.setForeground(THEME_BLUE);
        }
        dashboardLogo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));

        JLabel titleLabel = new JLabel("Select Department", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(THEME_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        topPanel.add(dashboardLogo, BorderLayout.NORTH);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel departmentPanel = new JPanel(new GridLayout(3, 3, 25, 25));
        departmentPanel.setBackground(THEME_BG);
        departmentPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 80, 80));

        String[] departments = {
                "Cardiology", "ENT", "Neurologist",
                "Dermatologist", "Ophthalmology", "Gynecologist", "General Physician"
        };

        for (String dept : departments) {
            JButton deptButton = createThemedButton(dept, THEME_BLUE, Color.WHITE);
            deptButton.addActionListener(e -> {
                updateDoctorProfile(dept);
                deptCardLayout.show(deptContainer, "Profile");
                mainCardLayout.show(mainContainer, "Department");
            });
            departmentPanel.add(deptButton);
        }

        JButton recordsButton = createThemedButton("Patient Records", THEME_LIGHT_GRAY, THEME_BLUE);
        recordsButton.addActionListener(e -> {
            mainCardLayout.show(mainContainer, "PatientRecords");
            loadAppointments("");
        });
        departmentPanel.add(recordsButton);

        JButton logoutButton = createThemedButton("Logout", THEME_RED, Color.WHITE);
        logoutButton.addActionListener(e -> mainCardLayout.show(mainContainer, "Welcome"));
        departmentPanel.add(logoutButton);

        panel.add(departmentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildDepartmentScreen() {
        deptCardLayout = new CardLayout();
        deptContainer = new JPanel(deptCardLayout);

        deptContainer.add(buildDoctorProfileView(), "Profile");
        deptContainer.add(buildAppointmentFormView(), "Form");

        return deptContainer;
    }

    private JPanel buildDoctorProfileView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(THEME_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(THEME_LIGHT_GRAY);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(THEME_BLUE, 3),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        doctorImageLabel = new JLabel();
        doctorImageLabel.setPreferredSize(new Dimension(200, 200));
        doctorImageLabel.setMaximumSize(new Dimension(200, 200));
        doctorImageLabel.setOpaque(true);
        doctorImageLabel.setBackground(Color.WHITE);
        doctorImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        doctorImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doctorImageLabel.setBorder(BorderFactory.createLineBorder(THEME_BLUE, 3));

        doctorNameLabel = new JLabel("Doctor Name");
        doctorNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        doctorNameLabel.setForeground(THEME_RED);
        doctorNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        doctorDetailsLabel = new JLabel("Qualifications");
        doctorDetailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        doctorDetailsLabel.setForeground(THEME_BLUE);
        doctorDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton proceedButton = createThemedButton("Proceed to Book", THEME_BLUE, Color.WHITE);
        proceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedButton.setMaximumSize(new Dimension(250, 45));
        proceedButton.addActionListener(e -> deptCardLayout.show(deptContainer, "Form"));

        JButton backButton = createThemedButton("Back to Dashboard", THEME_LIGHT_GRAY, THEME_BLUE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(250, 45));
        backButton.addActionListener(e -> mainCardLayout.show(mainContainer, "Dashboard"));

        card.add(doctorImageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(doctorNameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(doctorDetailsLabel);
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        card.add(proceedButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(backButton);

        panel.add(card);
        return panel;
    }

    private JPanel buildAppointmentFormView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(THEME_BG);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 15, 15));
        formPanel.setBackground(THEME_LIGHT_GRAY);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(THEME_BLUE, 3),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JTextField nameField = new JTextField(20);
        addThemedLabelAndField(formPanel, "Patient Name:", nameField);

        JTextField ageField = new JTextField(20);
        ((AbstractDocument) ageField.getDocument())
                .setDocumentFilter(new NumericFilter());
        addThemedLabelAndField(formPanel, "Age:", ageField);

        JComboBox<String> sexBox =
                new JComboBox<>(new String[]{"Male", "Female", "Other"});
        sexBox.setBackground(Color.WHITE);
        sexBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel sexLabel = new JLabel("Sex:");
        sexLabel.setForeground(THEME_BLUE);
        sexLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(sexLabel);
        formPanel.add(sexBox);

        JTextField phoneField = new JTextField(20);
        ((AbstractDocument) phoneField.getDocument())
                .setDocumentFilter(new NumericFilter());
        addThemedLabelAndField(formPanel, "Phone Number:", phoneField);

        JTextField addressField = new JTextField(20);
        addThemedLabelAndField(formPanel, "Address:", addressField);

        JTextField emergencyField = new JTextField(20);
        ((AbstractDocument) emergencyField.getDocument())
                .setDocumentFilter(new NumericFilter());
        addThemedLabelAndField(formPanel, "Emergency Contact:", emergencyField);

        JButton backButton = createThemedButton(
                "Back to Profile", THEME_LIGHT_GRAY, THEME_BLUE);
        JButton bookButton = createThemedButton(
                "Confirm Appointment", THEME_RED, Color.WHITE);

        backButton.addActionListener(e ->
                deptCardLayout.show(deptContainer, "Profile"));

        bookButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String emergency = emergencyField.getText().trim();
            String sex = (String) sexBox.getSelectedItem();

            if (name.isEmpty() || age.isEmpty() || phone.isEmpty()
                    || address.isEmpty() || emergency.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Please fill all appointment details!");
                return;
            }

            try {
                int patientAge = Integer.parseInt(age);

                if (patientAge < 1 || patientAge > 120) {
                    JOptionPane.showMessageDialog(
                            this, "Please enter a valid age between 1 and 120.");
                    return;
                }

                if (phone.length() != 10 || emergency.length() != 10) {
                    JOptionPane.showMessageDialog(
                            this, "Enter valid 10-digit contact numbers.");
                    return;
                }

                Appointment appointment = new Appointment(
                        name, age, sex, phone, address, emergency
                );

                int appointmentId =
                        DatabaseService.saveAppointment(appointment);

                JOptionPane.showMessageDialog(
                        this,
                        "Appointment Booked Successfully!\nAppointment ID: "
                                + appointmentId
                );

                nameField.setText("");
                ageField.setText("");
                phoneField.setText("");
                addressField.setText("");
                emergencyField.setText("");
                sexBox.setSelectedIndex(0);

                mainCardLayout.show(mainContainer, "Dashboard");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid age.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Appointment Booking Failed: " + ex.getMessage()
                );
            }
        });

        formPanel.add(backButton);
        formPanel.add(bookButton);

        panel.add(formPanel);
        return panel;
    }

    private JPanel buildPatientRecordsScreen() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(THEME_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel("Patient Records Management", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        heading.setForeground(THEME_BLUE);
        panel.add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(THEME_BG);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(THEME_LIGHT_GRAY);

        JLabel searchLabel = new JLabel("Search by Patient Name or Appointment ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(THEME_BLUE);

        patientSearchField = new JTextField(22);
        JButton searchButton = createThemedButton("Search", THEME_BLUE, Color.WHITE);
        JButton showAllButton = createThemedButton("Show All", THEME_LIGHT_GRAY, THEME_BLUE);

        searchButton.addActionListener(e -> loadAppointments(patientSearchField.getText().trim()));
        patientSearchField.addActionListener(e -> loadAppointments(patientSearchField.getText().trim()));
        showAllButton.addActionListener(e -> {
            patientSearchField.setText("");
            loadAppointments("");
        });

        searchPanel.add(searchLabel);
        searchPanel.add(patientSearchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        String[] columns = {
                "Appointment ID", "Patient Name", "Age", "Sex",
                "Phone Number", "Address", "Emergency Contact", "Booking Date"
        };

        appointmentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(appointmentsTableModel);
        appointmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appointmentsTable.setRowHeight(25);
        appointmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] widths = {105, 150, 55, 75, 120, 220, 140, 160};
        for (int i = 0; i < widths.length; i++) {
            appointmentsTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(THEME_BG);

        JButton refreshButton = createThemedButton("Refresh", THEME_BLUE, Color.WHITE);
        JButton updateButton = createThemedButton("Update Selected", THEME_LIGHT_GRAY, THEME_BLUE);
        JButton deleteButton = createThemedButton("Delete Selected", THEME_RED, Color.WHITE);
        JButton backButton = createThemedButton("Back to Dashboard", THEME_LIGHT_GRAY, THEME_BLUE);

        refreshButton.addActionListener(e -> loadAppointments(patientSearchField.getText().trim()));
        updateButton.addActionListener(e -> updateSelectedAppointment());
        deleteButton.addActionListener(e -> deleteSelectedAppointment());
        backButton.addActionListener(e -> mainCardLayout.show(mainContainer, "Dashboard"));

        actionPanel.add(refreshButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        actionPanel.add(backButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private void loadAppointments(String searchText) {
        if (appointmentsTableModel == null) {
            return;
        }

        try {
            appointmentsTableModel.setRowCount(0);

            for (Object[] row : DatabaseService.getAppointments(searchText)) {
                appointmentsTableModel.addRow(row);
            }

            if (appointmentsTableModel.getRowCount() == 0) {
                // An empty table is valid; it may simply have no matching appointments.
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load patient records: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateSelectedAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update.");
            return;
        }

        int modelRow = appointmentsTable.convertRowIndexToModel(selectedRow);

        int appointmentId = Integer.parseInt(
                appointmentsTableModel.getValueAt(modelRow, 0).toString());
        String currentName = appointmentsTableModel.getValueAt(modelRow, 1).toString();
        String currentAge = appointmentsTableModel.getValueAt(modelRow, 2).toString();
        String currentSex = appointmentsTableModel.getValueAt(modelRow, 3).toString();
        String currentPhone = appointmentsTableModel.getValueAt(modelRow, 4).toString();
        String currentAddress = appointmentsTableModel.getValueAt(modelRow, 5).toString();
        String currentEmergency = appointmentsTableModel.getValueAt(modelRow, 6).toString();

        JTextField nameField = new JTextField(currentName);
        JTextField ageField = new JTextField(currentAge);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField addressField = new JTextField(currentAddress);
        JTextField emergencyField = new JTextField(currentEmergency);
        JComboBox<String> sexBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        sexBox.setSelectedItem(currentSex);

        JPanel editPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        editPanel.add(new JLabel("Patient Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Age:"));
        editPanel.add(ageField);
        editPanel.add(new JLabel("Sex:"));
        editPanel.add(sexBox);
        editPanel.add(new JLabel("Phone Number:"));
        editPanel.add(phoneField);
        editPanel.add(new JLabel("Address:"));
        editPanel.add(addressField);
        editPanel.add(new JLabel("Emergency Contact:"));
        editPanel.add(emergencyField);

        int result = JOptionPane.showConfirmDialog(
                this, editPanel, "Update Appointment #" + appointmentId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String emergency = emergencyField.getText().trim();
        String sex = (String) sexBox.getSelectedItem();

        if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()
                || address.isEmpty() || emergency.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age < 1 || age > 120) {
                JOptionPane.showMessageDialog(this, "Enter a valid age between 1 and 120.");
                return;
            }
            if (!phone.matches("\\d{10}") || !emergency.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone and emergency contact must be 10 digits.");
                return;
            }

            boolean updated = DatabaseService.updateAppointment(
                    appointmentId, name, age, sex, phone, address, emergency
            );

            if (updated) {
                JOptionPane.showMessageDialog(this, "Appointment updated successfully.");
                loadAppointments(patientSearchField.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "No appointment was updated.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, "Update failed: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to delete.");
            return;
        }

        int modelRow = appointmentsTable.convertRowIndexToModel(selectedRow);
        int appointmentId = Integer.parseInt(
                appointmentsTableModel.getValueAt(modelRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete appointment #" + appointmentId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = DatabaseService.deleteAppointment(appointmentId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully.");
                loadAppointments(patientSearchField.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Appointment was not found.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, "Delete failed: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateDoctorProfile(String departmentName) {
        String mockName = "";
        String mockDetails = "";
        String imagePath = "";

        switch (departmentName) {
            case "Cardiology":
                mockName = "Dr. Aditya Shukla";
                mockDetails = "MBBS , MD, FACC - 20 Years Experience";
                imagePath = "images/cardiology.jpg";
                break;
            case "ENT":
                mockName = "Dr. Seema Mishra";
                mockDetails = "MBBS , MS, ENT Specialist - 12 Years Experience";
                imagePath = "images/ent.jpg";
                break;
            case "Neurologist":
                mockName = "Dr. Narayan Dave";
                mockDetails = "MBBS , MD, DM Neurology - 18 Years Experience";
                imagePath = "images/neurologist.jpg";
                break;
            case "Dermatologist":
                mockName = "Dr. Vikram Singh ";
                mockDetails = "MBBS , MD, Dermatology - 9 Years Experience";
                imagePath = "images/dermatologist.jpg";
                break;
            case "Ophthalmology":
                mockName = "Dr. Meet Sharma";
                mockDetails = "MBBS , MS, Ophthalmology - 15 Years Experience";
                imagePath = "images/ophthalmology.jpg";
                break;
            case "Gynecologist":
                mockName = "Dr. Neha Vyas";
                mockDetails = "MBBS , MD, OB/GYN - 15 Years Experience";
                imagePath = "images/gynecologist.jpg";
                break;
            case "General Physician":
                mockName = "Dr. Lalit Ojha";
                mockDetails = "MBBS, MD - 10 Years Experience";
                imagePath = "images/physician.jpg";
                break;
        }

        doctorNameLabel.setText(mockName);
        doctorDetailsLabel.setText(mockDetails);
        setDoctorImageFromFile(imagePath);
    }

    private void setDoctorImageFromFile(String imagePath) {
        try {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            if (originalIcon.getIconWidth() == -1) {
                throw new Exception("File not found");
            }
            Image scaledImg = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            doctorImageLabel.setIcon(new ImageIcon(scaledImg));
            doctorImageLabel.setText("");
        } catch (Exception e) {
            doctorImageLabel.setIcon(null);
            doctorImageLabel.setText("Image Not Found");
        }
    }

    private JLabel getLogoLabel(int width, int height) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("images/pietlogo.png");
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                label.setText("PIET LOGO");
                label.setFont(new Font("Segoe UI", Font.BOLD, 18));
                label.setForeground(THEME_BLUE);
            }
        } catch (Exception e) {
            label.setText("PIET LOGO");
            label.setFont(new Font("Segoe UI", Font.BOLD, 18));
            label.setForeground(THEME_BLUE);
        }
        return label;
    }

    private JButton createThemedButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setRolloverEnabled(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(THEME_BLUE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private void addThemedLabelAndField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(THEME_BLUE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(label);
        panel.add(field);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalApp app = new HospitalApp();
            app.setVisible(true);
        });
    }
}

class NumericFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if (string.matches("\\d+")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        if (text.matches("\\d+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

class User {
    private String userId;
    private String mobile;
    private String email;

    public User(String userId, String mobile, String email) {
        this.userId = userId;
        this.mobile = mobile;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
}

class Appointment {
    private String patientName;
    private String age;
    private String sex;
    private String phoneNumber;
    private String address;
    private String emergencyContact;

    public Appointment(String patientName, String age, String sex, String phoneNumber, String address, String emergencyContact) {
        this.patientName = patientName;
        this.age = age;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emergencyContact = emergencyContact;
    }

    public String getPatientName() { return patientName; }
    public String getAge() { return age; }
    public String getSex() { return sex; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getEmergencyContact() { return emergencyContact; }
}

class DatabaseMock {
    public static void authenticateUser(String userId, String password) {
    }

    public static void registerUser(User user) {
    }

    public static void saveAppointment(Appointment appt) {
    }
}