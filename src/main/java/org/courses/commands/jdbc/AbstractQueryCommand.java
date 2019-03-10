package org.courses.commands.jdbc;

import org.courses.Entities.Entyty;
import org.courses.Entities.Type;

import java.util.Scanner;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractQueryCommand {
    protected String dbFile;

    //todo factory or other..
    protected Connection connect() throws SQLException {
        String url = connectionString();
        return DriverManager.getConnection(url);
    }

    private String connectionString() {
        Path path = Paths.get(dbFile);
        return String.format("jdbc:sqlite:%s", path.toAbsolutePath());

    }

//    void insert(String table, String columns, String values) throws SQLException {
//        Connection connection = connect();
//        Statement statement = connection.createStatement();
//        statement.execute(String.format("INSERT INTO %s" +
//                "(%s) " +
//                "VALUES" +
//                "(%s)", table, columns, values));
//        statement.close();
//        connection.close();
//    }

    void insert(Entyty entity) throws SQLException {

        Class classEntity = entity.getClass();
        String table = classEntity.getSimpleName();

        StringBuilder sb = new StringBuilder();
        Field[] fields = classEntity.getDeclaredFields();
        String columns = ProcessingColumn(fields);
//        for (Field field : fields){
//
//
//
//            columns = columns + (columns.isEmpty()?"":", ")+ field.getName();//как можно корректней проверить?
////            if(columns.isEmpty()){
////                columns = field.getName();
////            }
////            else{
////                columns = columns + ", " + field.getName();
////            }
//        }
        System.out.println("Enter values: ");
        Scanner scanner = new Scanner(System.in);
        String values = "\""+ scanner.nextLine() + "\"";

        Connection connection = connect();
        Statement statement = connection.createStatement();
        String sql = String.format("INSERT INTO %s" +
                "(%s) " +
                "VALUES" +
                "(%s)", table, columns, values);
        System.out.println(sql);

        statement.execute(sql);
        statement.close();
        connection.close();
    }

    String ProcessingColumn(Field[] columns)
    {
        StringBuilder sb = new StringBuilder();
        for (Field column: columns)
        {
            Class typeColumn = column.getType();

            //1
            if (typeColumn == String.class)
            {
                sb.append(sb.length() != 0 ? ", \"" + column.getName() + "\"" : "\"" + column.getName() + "\"");//что делать?
            }
            else
            {
                sb.append(sb.length() != 0 ? ", " + column.getName() : column.getName());
            }
            /////////////////////

            //2
//            if (typeColumn == String.class)
//            {
//                sb.append(" \"" + column.getName() + "\"");//что делать?
//            }
//            else
//            {
//                sb.append(" " + column.getName());
//            }
//
//            sb.deleteCharAt(0);
//            return sb.toString().replace(" ", ", ");
            /////////////////////

        }

        return sb.toString();
    }
}
