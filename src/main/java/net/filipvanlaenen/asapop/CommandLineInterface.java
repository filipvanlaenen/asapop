package net.filipvanlaenen.asapop;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.filipvanlaenen.asapop.exporter.EopaodPsvExporter;
import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Class implementing a command line interface.
 */
public final class CommandLineInterface {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;

    /**
     * The main entry point for the command line interface.
     *
     * @param args The arguments.
     * @throws IOException Thrown if something related to IO goes wrong.
     */
    public static void main(final String... args) throws IOException {
        if (args.length < 1) {
            printUsage();
            return;
        }
        try {
            Command.valueOf(capitalizeWord(args[0])).execute(args);
        } catch (IllegalArgumentException iae) {
            printUsage();
        }
    }

    /**
     * Capitalizes a word.
     *
     * @param word A word to be capitalized.
     * @return The word with the first letter capitalized.
     */
    static String capitalizeWord(final String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    /**
     * Prints the usage to the command line.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  convert <ropf-file-name> <psv-file-name> <electoral-list-key>+");
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CommandLineInterface() {
    }

    /**
     * Enumeration with the available commands.
     */
    enum Command {
        /**
         * Command read an ROPF file and convert it to another format.
         */
        Convert {
            @Override
            void execute(final String[] args) throws IOException {
                String inputFileName = args[1];
                String outputFileName = args[2];
                String[] electoralListKeys = new String[args.length - THREE];
                for (int i = 0; i < args.length - THREE; i++) {
                    electoralListKeys[i] = args[i + THREE];
                }
                String[] ropfContent = readFile(inputFileName);
                OpinionPolls opinionPolls = OpinionPolls.parse(ropfContent);
                String psvContent = EopaodPsvExporter.export(opinionPolls, electoralListKeys);
                writeFile(outputFileName, psvContent);
            }
        };

        /**
         * Executes the command, passing the arguments from the command line.
         *
         * @param args The arguments from the command line.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        abstract void execute(String[] args) throws IOException;

        /**
         * Utility method to read a file into an array of strings.
         *
         * @param fileName The name of the file to read from.
         * @return The content of the file, as an array of strings.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static String[] readFile(final String fileName) throws IOException {
            return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8).toArray(new String[] {});
        }

        /**
         * Utility method to write a string to a file.
         *
         * @param fileName The name for the file.
         * @param content  The string to be written to the file.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static void writeFile(final String fileName, final String content) throws IOException {
            Files.writeString(Paths.get(fileName), content, StandardCharsets.UTF_8);
        }
    }
}
