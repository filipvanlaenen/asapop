package net.filipvanlaenen.asapop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.exporter.EopaodPsvExporter;
import net.filipvanlaenen.asapop.filecache.SampledHypergeometricDistributionsFileCache;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.parser.Warning;
import net.filipvanlaenen.asapop.website.Website;
import net.filipvanlaenen.asapop.website.WebsiteBuilder;
import net.filipvanlaenen.asapop.yaml.Analysis;
import net.filipvanlaenen.asapop.yaml.AnalysisBuilder;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionData;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Class implementing a command line interface.
 */
public final class CommandLineInterface {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The integer number four.
     */
    private static final int FOUR = 4;

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
        SampledHypergeometricDistributionsFileCache.toggleOn();
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
        System.out.println("  analyze <ropf-file-name> <election-yaml-file-name> <analysis-result-yaml-file-name>");
        System.out.println(
                "  build <site-dir-name> <website-configuration-yaml-file-name> <custom-style-sheet-file-name>");
        System.out.println("  convert <ropf-file-name> <csv-file-name> <electoral-list-key>+ [-a=<area>]");
        System.out.println("  convert <ropf-file-name> <psv-file-name> <electoral-list-key>+ [-a=<area>]");
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
         * Command to read a ROPF file and a YAML file with election specific data, analyze the opinion polls and write
         * the results to a YAML file.
         */
        Analyze {
            @Override
            void execute(final String[] args) throws IOException {
                String inputFileName = args[1];
                String electionDataFileName = args[2];
                String outputFileName = args[THREE];
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(ropfContent);
                for (Warning warning : richOpinionPollsFile.getWarnings()) {
                    System.out.println(warning);
                }
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                ElectionData electionData = objectMapper.readValue(new File(electionDataFileName), ElectionData.class);
                AnalysisEngine engine = new AnalysisEngine(richOpinionPollsFile.getOpinionPolls(), electionData);
                engine.run();
                Analysis analysis = new AnalysisBuilder(engine).build();
                objectMapper.writeValue(new File(outputFileName), analysis);
            }
        },
        /**
         * Command to build the website.
         */
        Build {
            @Override
            void execute(final String[] args) throws IOException {
                String siteDirName = args[1];
                String siteConfigurationFileName = args[2];
                String ropfDirName = args[THREE];
                String customStyleSheetFileName = args[FOUR];
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                WebsiteConfiguration websiteConfiguration =
                        objectMapper.readValue(new File(siteConfigurationFileName), WebsiteConfiguration.class);
                Terms terms = objectMapper.readValue(readResource("/internationalization.yaml"), Terms.class);
                for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
                    if (areaConfiguration.getTranslations() != null) {
                        Term term = new Term();
                        term.setKey("_area_" + areaConfiguration.getAreaCode());
                        term.setTranslations(areaConfiguration.getTranslations());
                        terms.getTerms().add(term);
                    }
                }
                Map<String, OpinionPolls> opinionPollsMap = readAllOpinionPolls(ropfDirName, websiteConfiguration);
                String baseStyleSheetContent = readResource("/base.css");
                String customStyleSheetContent = String.join("\n", readFile(customStyleSheetFileName));
                Website website = new WebsiteBuilder(websiteConfiguration, terms, opinionPollsMap,
                        baseStyleSheetContent, customStyleSheetContent).build();
                writeFiles(siteDirName, website.asMap());
            }
        },
        /**
         * Command read an ROPF file and convert it to another format.
         */
        Convert {
            @Override
            void execute(final String[] args) throws IOException {
                String inputFileName = args[1];
                String outputFileName = args[2];
                int noOfElectoralListKeySets = args.length - THREE;
                String area = null;
                if (args[args.length - 1].startsWith("-a=")) {
                    String areaOption = args[args.length - 1];
                    area = areaOption.substring(THREE, areaOption.length());
                    noOfElectoralListKeySets--;
                }
                List<Set<String>> electoralListKeySets = new ArrayList<Set<String>>(noOfElectoralListKeySets);
                for (int i = 0; i < noOfElectoralListKeySets; i++) {
                    electoralListKeySets.add(new HashSet<String>(Arrays.asList(args[i + THREE].split("\\+"))));
                }
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(ropfContent);
                for (Warning warning : richOpinionPollsFile.getWarnings()) {
                    System.out.println(warning);
                }
                OpinionPolls opinionPolls = richOpinionPollsFile.getOpinionPolls();
                String outputContent = "";
                if (outputFileName.endsWith(".csv")) {
                    outputContent = EopaodCsvExporter.export(opinionPolls, area, electoralListKeySets);
                } else if (outputFileName.endsWith(".psv")) {
                    outputContent = EopaodPsvExporter.export(opinionPolls, area, electoralListKeySets);
                }
                writeFile(outputFileName, outputContent);
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
         * Reads all the opinion polls.
         *
         * @param ropfDirName          The directory where the ROPF files reside.
         * @param websiteConfiguration The configuration for the website.
         * @return A map with all opinion polls.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        private static Map<String, OpinionPolls> readAllOpinionPolls(final String ropfDirName,
                final WebsiteConfiguration websiteConfiguration) throws IOException {
            Map<String, OpinionPolls> opinionPollsMap = new HashMap<String, OpinionPolls>();
            Set<String> areaCodes =
                    websiteConfiguration.getAreaConfigurations().stream().filter(ac -> ac.getAreaCode() != null)
                            .map(areaConfigutation -> areaConfigutation.getAreaCode()).collect(Collectors.toSet());
            for (String areaCode : areaCodes) {
                Path ropfPath = Paths.get(ropfDirName, areaCode + ".ropf");
                if (Files.exists(ropfPath)) {
                    System.out.println("Going to parse " + areaCode + "...");
                    String[] ropfContent = readFile(ropfPath);
                    RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(ropfContent);
                    for (Warning warning : richOpinionPollsFile.getWarnings()) {
                        System.out.println(warning);
                    }
                    opinionPollsMap.put(areaCode, richOpinionPollsFile.getOpinionPolls());
                }
            }
            return opinionPollsMap;
        }

        /**
         * Utility method to read a file into an array of strings.
         *
         * @param fileName The name of the file to read from.
         * @return The content of the file, as an array of strings.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static String[] readFile(final String fileName) throws IOException {
            return readFile(Paths.get(fileName));
        }

        /**
         * Utility method to read a file into an array of strings.
         *
         * @param path The path to the file to read from.
         * @return The content of the file, as an array of strings.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static String[] readFile(final Path path) throws IOException {
            return Files.readAllLines(path, StandardCharsets.UTF_8).toArray(new String[] {});
        }

        /**
         * Utility method to read a resource into a string.
         *
         * @param resourceName The name of the resource to read from.
         * @return The content of the resource, as a string.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static String readResource(final String resourceName) throws IOException {
            InputStream in = CommandLineInterface.class.getResourceAsStream(resourceName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder resultStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
            return resultStringBuilder.toString();
        }

        /**
         * Utility method to write a string to a file.
         *
         * @param path    The path for the file.
         * @param content The string to be written to the file.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static void writeFile(final Path path, final String content) throws IOException {
            Files.writeString(path, content, StandardCharsets.UTF_8);
        }

        /**
         * Utility method to write a string to a file.
         *
         * @param fileName The name for the file.
         * @param content  The string to be written to the file.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static void writeFile(final String fileName, final String content) throws IOException {
            writeFile(Paths.get(fileName), content);
        }

        /**
         * Utility method to write a map with names and contents to files.
         *
         * @param baseDir              The base directory for the files.
         * @param fileNamesAndContents The file names and contents.
         * @throws IOException Thrown if an exception occurs related to IO.
         */
        private static void writeFiles(final String baseDir, final Map<Path, String> fileNamesAndContents)
                throws IOException {
            for (Entry<Path, String> entry : fileNamesAndContents.entrySet()) {
                Path path = Paths.get(baseDir, entry.getKey().toString());
                Files.createDirectories(path.getParent());
                writeFile(path, entry.getValue());
            }
        }
    }
}
