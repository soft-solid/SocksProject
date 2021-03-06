package org.courses.commands.jdbc;

import org.courses.Entities.Material;
import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;

import java.sql.SQLException;

public class AddMaterialCommand extends AbstractQueryCommand implements Command {
    @Override
    public void parse(String[] args) {
        if (args.length > 0) {
            dbFile = args[0];
        }
        else {
            throw new CommandFormatException("DB file is not specified");
        }
    }

    @Override
    public void execute() {
        try {
            insert(new Material());
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
