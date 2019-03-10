package org.courses;

import org.courses.commands.Command;
import org.courses.commands.jdbc.AddManufactureCommand;
import org.courses.commands.jdbc.AddMaterialCommand;
import org.courses.commands.jdbc.AddTestTableCommand;
import org.courses.commands.jdbc.AddTypeCommand;

import java.util.HashMap;
import java.util.Map;

public class Program {
    static Map<String, Command> commands;

    static {
        commands = new HashMap<>();
//        commands.put("connect", new CreateDb());
//        commands.put("table", new CreateTable());
        commands.put("addtype", new AddTypeCommand());
        //commands.put("addTestTable", new AddTestTableCommand());
        commands.put("addMaterial", new AddMaterialCommand());
        commands.put("addManufacture", new AddManufactureCommand());
    }

    public static void main(String[] args) {
        String commandName = args[0];

        String[] params = new String[args.length - 1];
        System.arraycopy(args, 1, params, 0, params.length);

        Command command = commands.get(commandName);
        command.parse(params);
        command.execute();
    }
}
