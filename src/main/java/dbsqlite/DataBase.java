package dbsqlite;

import java.sql.*;

public class DataBase {
    private static String path = "posts.sqlite";

    public static void main(String[] args) {
//        createNewDatabase(path);
//        createNewTable();
        DataBase app = new DataBase();
        // insert three new rows
        app.insert("Raw Materials", 3000);
        app.insert("Semifinished Goods", 4000);
        app.insert("Finished Goods", 5000);
    }

    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path;

        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE IF NOT EXISTS Users (
                	id integer PRIMARY KEY,
                	name text NOT NULL,
                	capacity real
                );""";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("executed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param name name
     * @param capacity
     */
    public void insert(String name, double capacity) {
        String sql = "INSERT INTO main.Users(name,capacity) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement prepareStatement = conn.prepareStatement(sql)) {
            prepareStatement.setString(1, name);
            prepareStatement.setDouble(2, capacity);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
