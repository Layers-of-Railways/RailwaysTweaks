package dev.ithundxr.railwaystweaks.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DumpDatabase {
    public static void dump() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String address = getProperty("address");
            String database = getProperty("database");
            String username = getProperty("username");
            String password = getProperty("password");

            MysqlConnectionPoolDataSource source = new MysqlConnectionPoolDataSource();
            source.setUrl("jdbc:mysql://" + address + "/" + database);
            source.setUser(username);
            source.setPassword(password);
            source.setDatabaseName(database);

            try (Connection conn = source.getConnection();
                 Statement stat = conn.createStatement();
                 FileWriter writer = new FileWriter(Paths.get(FabricLoader.getInstance().getGameDir().toString(), "export.sql").toString())) {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("Exporting table: " + tableName);
                    
                    String createTableQuery = getCreateTableQuery(conn, tableName);
                    writer.write(createTableQuery + ";\n\n");
                    
                    exportTableData(conn, tableName, writer);
                    writer.write("\n\n");
                }
            }
        } catch (Exception e) {
            RailwaysTweaks.LOGGER.error("Error occurred while dumping DB", e);
        }
    }

    private static String getCreateTableQuery(Connection connection, String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
        rs.next();
        String createTable = rs.getString(2); // The second column contains the CREATE TABLE statement.
        rs.close();
        stmt.close();
        return createTable;
    }

    private static void exportTableData(Connection connection, String tableName, FileWriter writer) throws SQLException, IOException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();

        while (rs.next()) {
            StringBuilder row = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                if (value == null) {
                    row.append("NULL");
                } else {
                    row.append("'").append(value.replace("'", "''")).append("'");
                }
                if (i < columnCount) row.append(", ");
            }
            row.append(");\n");
            writer.write(row.toString());
        }

        rs.close();
        stmt.close();
    }
    
    private static String getProperty(String name) {
        return System.getProperty("railwayTweaks.database." + name);
    }
}