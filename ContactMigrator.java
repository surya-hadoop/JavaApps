import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactMigrator {
    public static void main(String[] args) {
        //if (args.length != 1) {
           // System.err.println("Usage: java ContactMigrator <csv-file>");
            //System.exit(1);
        //}

        String csvFile = "C:\\Surya_Dev\\Project_Alpha\\contacts.csv";
        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                env("DB_HOST", "localhost"),
                env("DB_PORT", "3306"),
                env("DB_NAME", "contact_vault")
        );
        String dbUser = env("DB_USER", "root");
        String dbPassword = env("DB_PASSWORD", "root_password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found. Add the driver jar to the classpath.");
            e.printStackTrace();
            System.exit(1);
        }
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            createContactsTable(conn);
            importCsv(conn, csvFile);
            System.out.println("CSV import completed.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static void createContactsTable(Connection conn) throws SQLException {
        String ddl = ""
                + "CREATE TABLE IF NOT EXISTS contacts ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "email VARCHAR(255), "
                + "phone VARCHAR(50)"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        }
    }

    private static void importCsv(Connection conn, String csvFile) throws IOException, SQLException {
        String insertSql = "INSERT INTO contacts (name, email, phone) VALUES (?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            String line = reader.readLine();
            if (line == null) {
                return;
            }

            boolean hasHeader = line.toLowerCase().contains("name") && line.toLowerCase().contains("email");
            if (!hasHeader) {
                processLine(ps, line);
            }

            while ((line = reader.readLine()) != null) {
                processLine(ps, line);
            }
        }
    }

    private static void processLine(PreparedStatement ps, String line) throws SQLException {
        String[] parts = line.split(",", -1);
        String name = parts.length > 0 ? parts[0].trim() : "";
        String email = parts.length > 1 ? parts[1].trim() : "";
        String phone = parts.length > 2 ? parts[2].trim() : "";

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.executeUpdate();
    }
}