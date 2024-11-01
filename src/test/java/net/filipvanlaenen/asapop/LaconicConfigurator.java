package net.filipvanlaenen.asapop;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import net.filipvanlaenen.laconic.Laconic;

/**
 * Helper class configuring Laconic.
 */
public final class LaconicConfigurator {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LaconicConfigurator() {
    }

    /**
     * Resets the output stream in the Laconic logger.
     *
     * @return The new output stream in the Laconic logger.
     */
    public static ByteArrayOutputStream resetLaconicOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Laconic.LOGGER.setPrintStream(printStream);
        Laconic.LOGGER.setPrefixWithTimestamp(false);
        return outputStream;
    }
}
