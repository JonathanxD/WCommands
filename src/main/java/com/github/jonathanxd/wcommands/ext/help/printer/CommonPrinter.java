/*
 * 	WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *     Copyright (C) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *
 * 	GNU GPLv3
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jonathanxd.wcommands.ext.help.printer;

import com.github.jonathanxd.iutils.extra.primitivecontainers.IntContainer;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.text.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by jonathan on 12/03/16.
 */

/*
 Regex to match prints:

 All: (?P<cmd_type>(?P<main>\-\>)|(?P<sub>[-]{2,}'\>))\s(?P<cmd>(?P<command>[\w]+)(?P<optionalCmd>\?)?)\s(?P<arg>(?P<required>\[[\w]+\])|(?P<optionalArg><[\w]+>))?(?P<desc>\s+(?P<description>(?P<tag>\-)(?P<space>\s).+))?

 Groups:

 cmd_type->main|sub
 cmd -> command & optional?
 arg -> required|optionalArg
 desc -> tag & space & description

 Main or Sub Command: (?P<main>\-\>)|(?P<sub>[-]{2,}'\>)

 Command: (?P<command>[\w]+)(?P<optionalCmd>\?)?

 Argument: (?P<requiredArg>\[[\w]+\])|(?P<optionalArg><[\w]+>)

 Description: (?P<description>(?P<tag>\-)(?P<space>\s).+)

 */
public class CommonPrinter implements Printer {

    public static final Printer TO_SYS_OUT = new CommonPrinter(System.out, System.err, false);

    public static final PrintStream EMPTY_PRINTER = new EmptyPrinter();

    private final PrintStream stream;
    private final PrintStream errorStream;
    private final boolean printLabels;

    public CommonPrinter(PrintStream stream, PrintStream errorStream, boolean printLabels) {
        this.stream = stream;
        this.errorStream = errorStream;
        this.printLabels = printLabels;
    }


    public static IntContainer print(PrintStream printStream, CommandSpec commandSpec) {
        return print(printStream, commandSpec, new IntContainer(0));
    }

    public static IntContainer print(PrintStream printStream, CommandSpec commandSpec, IntContainer intContainer) {
        Map<CommandSpec, List<String>> toPrint = new LinkedHashMap<>();
        print(toPrint, commandSpec, null);

        appendDescriptions(toPrint, intContainer);

        toPrint.values().forEach(l -> l.forEach(printStream::println));

        return intContainer;
    }

    private static IntContainer appendDescriptions(Map<CommandSpec, List<String>> toPrint, IntContainer container) {

        toPrint.values().forEach(v -> v.forEach(str -> {
            if(container.get() < str.length()) {
                container.set(str.length() + 5);
            }
        }));

        toPrint.forEach(((commandSpec, stringList) -> {
            if(!stringList.isEmpty()) {
                String description = commandSpec.getDescription();

                if(description != null && !description.isEmpty()){
                    String at0 = stringList.get(0);

                    String formatted = String.format("%-"+container.get()+"s %s", at0, description(commandSpec));
                    stringList.set(0, formatted);
                }

            }
        }));

        return container;
    }

    public static void print(Map<CommandSpec, List<String>> toPrint, CommandSpec commandSpec, String lastLine) {

        String tag = lastLine == null ? "-> " : repeat('-', lastLine.length() - 2) + "'> ";

        String myLastLine = tag.concat(commandSpec.getName().getPlainString());

        toPrint.put(commandSpec, getWithAll(commandSpec, myLastLine));

        for (Text alias : commandSpec.getAliases()) {
            myLastLine = tag.concat(alias.getPlainString());

            toPrint.get(commandSpec).addAll(getWithAll(commandSpec, myLastLine));
        }

        for (CommandSpec childSpec : commandSpec.getSubCommands()) {
            print(toPrint, childSpec, myLastLine);
        }

    }

    public static List<String> getWithAll(CommandSpec commandSpec, String name) {

        StringJoiner stringJoiner = new StringJoiner(" ");

        for (ArgumentSpec<?, ?> argumentSpec : commandSpec.getArguments()) {

            String toAdd = argument(argumentSpec);

            stringJoiner.add(toAdd);
        }

        return new ArrayList<>(Collections.singletonList(stringJoiner.length() > 0 ? name + " " + stringJoiner.toString() : name));
    }

    public static String command(CommandSpec commandSpec) {

        if (commandSpec.isOptional()) {
            return commandSpec.getName().getPlainString() + "?";
        } else {
            return commandSpec.getName().getPlainString();
        }
    }

    public static String description(CommandSpec commandSpec) {
        return "- ".concat(commandSpec.getDescription());
    }

    public static String argument(ArgumentSpec<?, ?> argumentSpec) {
        if (argumentSpec.isOptional()) {
            return "<" + argumentSpec.getId() + ">";
        } else {
            return "[" + argumentSpec.getId() + "]";
        }
    }

    public static String repeat(char c, int times) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < times; ++x) sb.append(c);

        return sb.toString();
    }

    @Override
    public void printCommands(CommandList commandSpecs, InformationRegister informationRegister) {
        if (printLabels) {
            stream.println("--------   Label  --------");

            stream.println();

            stream.println(" [ ] - Required Argument");
            stream.println(" < > - Optional Argument");
            stream.println("  ?  - Optional Command");
            stream.println(" ->  - Main Command");
            stream.println(" -'> - Sub commands");

            stream.println(" -   - Description");

            stream.println();

            stream.println("--------   Label  --------");

            stream.println();

        }

        stream.println("-------- Commands --------");

        stream.println();

        Result result = Result.NO_COMMANDS;

        IntContainer container = null;

        for (CommandSpec commandSpec : commandSpecs) {
            if(container == null) {
                container = print(stream, commandSpec);
            } else {
                print(stream, commandSpec, container);
            }

            stream.println();

            if (result != Result.OK) {
                result = Result.OK;
            }
        }

        printEnd(result);

    }

    public void printEnd(Result result) {
        switch (result) {
            case OK: {
                stream.println("-------- Commands --------");
                break;
            }
            case NO_COMMANDS: {
                stream.println("       No Commands");
                stream.println();
                stream.println("-------- Commands --------");
                break;
            }
        }
    }

    @Override
    public void printString(String str) {
        stream.println(str);
    }

    @Override
    public void printByte(byte b) {
        stream.write(b);
    }

    @Override
    public void printByteArray(byte[] b) {
        try {
            stream.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printError(Object o) {
        errorStream.println(o);
    }

    @Override
    public void flush() {
        stream.flush();
        errorStream.flush();
    }

    enum Result {
        OK,
        NO_COMMANDS
    }

    private static final class EmptyPrinter extends PrintStream {

        public EmptyPrinter() {
            super(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            });
        }
    }
}


/*
-> say [message]
-> hi  [message]
-----> special [message]
-------------> fan [message]
 */