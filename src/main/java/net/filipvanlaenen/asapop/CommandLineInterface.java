package net.filipvanlaenen.asapop;

/**
 * Class implementing a command line interface.
 */
public final class CommandLineInterface {
    /**
     * The main entry point for the command line interface.
     *
     * @param args The arguments.
     */
    public static void main(final String... args) {
        printUsage();
    }

    /**
     * Prints the usage to the command line.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  TBD");
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CommandLineInterface() {
    }
}
