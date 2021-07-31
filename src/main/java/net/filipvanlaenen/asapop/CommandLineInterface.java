package net.filipvanlaenen.asapop;

import java.io.IOException;

/**
 * Class implementing a command line interface.
 */
public final class CommandLineInterface {
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
            }
        };

        /**
         * Executes the command, passing the arguments from the command line.
         *
         * @param args The arguments from the command line.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        abstract void execute(String[] args) throws IOException;
    }
}
