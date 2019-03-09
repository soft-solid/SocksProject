package org.courses.commands.jdbc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractQueryCommand {
    protected String dbFile;

    protected Connection connect() throws SQLException {
        String url = connectionString();
        return DriverManager.getConnection(url);
    }

    private String connectionString() {
        Path path = Paths.get(dbFile);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());
    }

    void insert(String table, String columns, String values) throws SQLException {
        Connection connection = connect();
        Statement statement = connection.createStatement();
        statement.execute(String.format("INSERT INTO %s" +
                "(%s) " +
                "VALUES" +
                "(%s)", table, columns, values));
        statement.close();
        connection.close();
    }
}
