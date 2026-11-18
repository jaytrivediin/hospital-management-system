# Hospital Management System

A Java-based Hospital Management System backed by MySQL. The project demonstrates how a Java application connects to a relational database and manages appointment information.

## Features

- Connects the Java application to a MySQL database using JDBC.
- Supports appointment-related operations, including booking, viewing appointment details, and editing existing appointment information.
- Stores application data in MySQL so records can persist between application runs.

> The exact screens, fields, and available operations depend on the implementation in the source code.

## Technology Stack

- **Language:** Java
- **Database:** MySQL
- **Database connectivity:** JDBC (MySQL Connector/J)
- **IDE:** IntelliJ IDEA (project developed/run in IntelliJ IDEA)

## Project Structure

The source files are located in the `src` directory. Key files discussed during setup include:

```text
Hospital/
├── src/
│   ├── HospitalApp.java
│   └── ...
├── .gitignore
└── README.md
```

Your project may contain additional packages and Java files. Keep the structure already present in your local project.

## Prerequisites

Before running the application, make sure you have:

1. A Java Development Kit (JDK) compatible with the project.
2. MySQL Server installed and running.
3. MySQL Connector/J available to the project as a JDBC dependency.
4. IntelliJ IDEA or another Java IDE.

## Setup and Configuration

### 1. Clone the repository

```bash
git clone git@github.com:jaytrivediin/hospital-management-system.git
cd hospital-management-system
```

### 2. Create the database

Open MySQL Workbench or the MySQL command-line client and create the database expected by the connection code.

```sql
CREATE DATABASE your_database_name;
```

Use the database name and table definitions expected by the SQL statements in this project. If the project includes a `.sql` file, import that file instead of creating tables manually.

### 3. Configure the database connection

Find the database connection class or the connection settings used by the application (for example, `DatabaseConnection.java`). Set the JDBC URL, MySQL username, and password to match your local MySQL installation.

A JDBC URL commonly has this format:

```text
jdbc:mysql://localhost:3306/your_database_name
```

**Security:** Do not commit real database passwords, API keys, or other secrets to GitHub. Use local configuration or environment variables for credentials, and keep those files out of version control.

### 4. Configure the JDBC driver

Make sure MySQL Connector/J is added to the project dependencies. In IntelliJ IDEA, check the project/module dependencies and confirm that the MySQL Connector/J library is available at runtime.

### 5. Run the application

1. Open the cloned project in IntelliJ IDEA.
2. Wait for the project and dependencies to finish loading.
3. Confirm that MySQL Server is running and the database connection settings are correct.
4. Open `src/HospitalApp.java` (or the configured application entry point).
5. Run the `main` method.

## Typical Workflow

1. Start the application.
2. Enter the appointment information requested by the application.
3. Book/save the appointment.
4. View stored appointment records and their details.
5. Edit an existing appointment when required.

The available fields and exact workflow are determined by the application's current implementation.

## Troubleshooting

| Issue | Things to check |
|---|---|
| `Access denied for user` | Check the MySQL username and password, and confirm that the user has permission to access the database. |
| `Unknown database` | Confirm that the database exists and that its name matches the JDBC URL. |
| `No suitable driver` or driver/class not found | Add MySQL Connector/J to the project dependencies and verify that it is available at runtime. |
| Connection refused / communications link failure | Confirm that MySQL Server is running and that the host and port in the JDBC URL are correct. |
| Table does not exist | Create/import the required tables and confirm that the table names match the SQL queries in the code. |

## Git Workflow

To commit and push future changes:

```bash
git status
git add .
git commit -m "Describe your changes"
git push
```

## Notes

- This repository contains the application source code; MySQL Server and the database must be configured separately on each machine.
- Do not upload database passwords, private keys, or local IDE settings.
