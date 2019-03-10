package org.courses.commands.jdbc;

import org.courses.Entities.Entyty;

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

    void insert(Entyty entity) throws SQLException {

        Class classEntity = entity.getClass();
        String table = classEntity.getSimpleName();
        System.out.println(table);
        Field[] fields = classEntity.getDeclaredFields();

        String columns = ProcessingColumn(fields);
        String values = ProcessingValues(fields);

        Connection connection = connect();
        Statement statement = connection.createStatement();
        String sql = String.format("INSERT INTO %s" +
                "(%s) " +
                "VALUES" +
                "%s", table, columns, values);
        System.out.println(sql);

        statement.execute(sql);
        statement.close();
        connection.close();
    }

    //логично было бы обьеденить с ProcessingColumn() но вмешалась логика возможности ввода нескольких строк таблицы
    String ProcessingValues(Field[] columns)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbLine;
        boolean userConsentInput = false;
        do {
            sbLine = new StringBuilder();
            sb.append("(");
            for (Field column : columns) {
                sbLine.append(",").append(UserInputValues(column));
            }
            sb.append(sbLine.deleteCharAt(0));
            sb.append(")");
            userConsentInput = UserContinueQuestion();
            if (userConsentInput)
                sb.append(",");
        }
        while(userConsentInput);

        return sb.toString();
    }

    boolean UserContinueQuestion(){
        System.out.println("Continue entering? (Y/N)");//todo input restriction
        Scanner scanner = new Scanner(System.in);
        String res = scanner.nextLine();
        return res.equalsIgnoreCase("Y");
    }

    String UserInputValues(Field field){
        System.out.println(String.format("Please, enter values column \"%s\" :",field.getName()));
        Scanner scanner = new Scanner(System.in);
        String res = scanner.nextLine();
        //todo validation not Empty values
        if (res.isEmpty()){
            System.out.println("Error! Input empty value.");
            return UserInputValues(field);
        }

        if (field.getType() == String.class)
            return "\""+ res + "\"";

        return res;
    }

    String ProcessingColumn(Field[] columns)
    {
        StringBuilder sb = new StringBuilder();

        for (Field column : columns) {
            sb.append(",").append(column.getName());
        }
        return sb.deleteCharAt(0).toString();//либо отсекать один символ либо делать цыклическую проверку для формирования корректной строки
    }
}
