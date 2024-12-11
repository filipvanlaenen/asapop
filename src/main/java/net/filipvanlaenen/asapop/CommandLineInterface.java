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
import java.time.LocalDate;
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
import net.filipvanlaenen.asapop.exporter.RopfExporter;
import net.filipvanlaenen.asapop.exporter.SaporDirectory;
import net.filipvanlaenen.asapop.exporter.SaporExporter;
import net.filipvanlaenen.asapop.filecache.SampledHypergeometricDistributionsFileCache;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.website.Website;
import net.filipvanlaenen.asapop.website.WebsiteBuilder;
import net.filipvanlaenen.asapop.yaml.Analysis;
import net.filipvanlaenen.asapop.yaml.AnalysisBuilder;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionData;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.ElectionsBuilder;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

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
            Command.valueOf(args[0].toUpperCase()).execute(args);
        } catch (IllegalArgumentException iae) {
            printUsage();
        }
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
        System.out.println("  format <ropf-file-name>");
        System.out.println("  provide <ropf-file-name> <sapor-dir-name> <sapor-configuration-yaml-file-name>");
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CommandLineInterface() {
    }

    /**
     * Enumeration with the available commands.
     */
    public enum Command {
        /**
         * Command to read a ROPF file and a YAML file with election specific data, analyze the opinion polls and write
         * the results to a YAML file.
         */
        ANALYZE {
            @Override
            void execute(final String[] args) throws IOException {
                String inputFileName = args[1];
                String electionDataFileName = args[2];
                String outputFileName = args[THREE];
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", inputFileName);
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(token, ropfContent);
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
        BUILD {
            @Override
            void execute(final String[] args) throws IOException {
                String siteDirName = args[1];
                String siteConfigurationFileName = args[2];
                String ropfDirName = args[THREE];
                String customStyleSheetFileName = args[FOUR];
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                File siteConfigurationFile = new File(siteConfigurationFileName);
                WebsiteConfiguration websiteConfiguration =
                        objectMapper.readValue(siteConfigurationFile, WebsiteConfiguration.class);
                Terms terms = objectMapper.readValue(readResource("/internationalization.yaml"), Terms.class);
                addAreaTerms(terms, websiteConfiguration);
                Map<String, ElectionData> electionDataFiles =
                        readElectionDataFiles(websiteConfiguration, siteConfigurationFile.getParent());
                LocalDate now = LocalDate.now();
                Elections elections =
                        ElectionsBuilder.extractAndValidateElections(websiteConfiguration, electionDataFiles, now);
                Map<String, OpinionPolls> parliamentaryOpinionPollsMap =
                        readAllParliamentaryOpinionPolls(ropfDirName, websiteConfiguration);
                Map<String, OpinionPolls> presidentialOpinionPollsMap =
                        readAllPresidentialOpinionPolls(ropfDirName, websiteConfiguration);
                String baseStyleSheetContent = readResource("/base.css");
                String customStyleSheetContent = String.join("\n", readFile(customStyleSheetFileName));
                String navigationScriptContent = readResource("/navigation.js");
                String sortingScriptContent = readResource("/sorting.js");
                String tooltipScriptContent = readResource("/tooltip.js");
                Website website = new WebsiteBuilder(websiteConfiguration, terms, parliamentaryOpinionPollsMap,
                        presidentialOpinionPollsMap, elections, baseStyleSheetContent, customStyleSheetContent,
                        navigationScriptContent, sortingScriptContent, tooltipScriptContent, now).build();
                writeFiles(siteDirName, website.asMap());
            }
        },
        /**
         * Command to read an ROPF file and convert it to another format.
         */
        CONVERT {
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
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", inputFileName);
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(token, ropfContent);
                OpinionPolls opinionPolls = richOpinionPollsFile.getOpinionPolls();
                String outputContent = "";
                if (outputFileName.endsWith(".csv")) {
                    outputContent = EopaodCsvExporter.export(opinionPolls, area, null, electoralListKeySets);
                }
                writeFile(outputFileName, outputContent);
            }
        },
        /**
         * Command to format an ROPF file.
         */
        FORMAT {
            @Override
            void execute(final String[] args) throws IOException {
                String ropfFileName = args[1];
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", ropfFileName);
                String[] ropfContent = readFile(ropfFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(token, ropfContent);
                writeFile(ropfFileName, RopfExporter.export(richOpinionPollsFile));
            }
        },
        /**
         * Command to provide SAPOR files.
         */
        PROVIDE {
            @Override
            void execute(final String[] args) throws IOException {
                String inputFileName = args[1];
                String saporDirName = args[2];
                String saporConfigurationFileName = args[THREE];
                Token inputFileToken = Laconic.LOGGER.logMessage("Parsing file %s.", inputFileName);
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(inputFileToken, ropfContent);
                OpinionPolls opinionPolls = richOpinionPollsFile.getOpinionPolls();
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                Token configurationFileToken =
                        Laconic.LOGGER.logMessage("Loading configuration file %s.", saporConfigurationFileName);
                SaporConfiguration saporConfiguration =
                        objectMapper.readValue(new File(saporConfigurationFileName), SaporConfiguration.class);
                SaporExporter saporExporter = new SaporExporter(saporConfiguration);
                SaporDirectory saporDirectory =
                        saporExporter.export(opinionPolls, configurationFileToken, inputFileToken);
                writeFiles(saporDirName, saporDirectory.asMap());
            }
        };

        /**
         * Adds the translations of all the areas to the terms.
         *
         * @param terms                The terms to add the translations to.
         * @param websiteConfiguration The website configuration to extract the translations of the areas from.
         */
        static void addAreaTerms(final Terms terms, final WebsiteConfiguration websiteConfiguration) {
            for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
                String areaCode = areaConfiguration.getAreaCode();
                if (areaConfiguration.getTranslations() != null) {
                    Term term = new Term();
                    term.setKey("_area_" + areaCode);
                    term.setTranslations(areaConfiguration.getTranslations());
                    terms.getTerms().add(term);
                }
                if (areaConfiguration.getSubdivsions() != null) {
                    for (AreaSubdivisionConfiguration subdivision : areaConfiguration.getSubdivsions()) {
                        if (subdivision.getTranslations() != null) {
                            Term term = new Term();
                            term.setKey("_area_" + areaCode + "-" + subdivision.getAreaCode());
                            term.setTranslations(subdivision.getTranslations());
                            terms.getTerms().add(term);
                        }
                    }
                }
            }
        }

        /**
         * Executes the command, passing the arguments from the command line.
         *
         * @param args The arguments from the command line.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        abstract void execute(String[] args) throws IOException;

        /**
         * Reads all the opinion polls related to parliamentary elections.
         *
         * @param ropfDirName          The directory where the ROPF files reside.
         * @param websiteConfiguration The configuration for the website.
         * @return A map with all opinion polls related to parliamentary elections.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        private static Map<String, OpinionPolls> readAllParliamentaryOpinionPolls(final String ropfDirName,
                final WebsiteConfiguration websiteConfiguration) throws IOException {
            Token token = Laconic.LOGGER.logMessage("Parsing parliamentary opinion polls files from directory %s.",
                    ropfDirName);
            Map<String, OpinionPolls> opinionPollsMap = new HashMap<String, OpinionPolls>();
            Set<String> areaCodes =
                    websiteConfiguration.getAreaConfigurations().stream().filter(ac -> ac.getAreaCode() != null)
                            .map(areaConfigutation -> areaConfigutation.getAreaCode()).collect(Collectors.toSet());
            for (String areaCode : areaCodes) {
                Path ropfPath = Paths.get(ropfDirName, areaCode + ".ropf");
                if (Files.exists(ropfPath)) {
                    Token fileToken =
                            Laconic.LOGGER.logMessage(token, "Parsing file %s.", ropfPath.getFileName().toString());
                    String[] ropfContent = readFile(ropfPath);
                    RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(fileToken, ropfContent);
                    opinionPollsMap.put(areaCode, richOpinionPollsFile.getOpinionPolls());
                }
            }
            return opinionPollsMap;
        }

        /**
         * Reads all the opinion polls related to presidential elections.
         *
         * @param ropfDirName          The directory where the ROPF files reside.
         * @param websiteConfiguration The configuration for the website.
         * @return A map with all opinion polls related to presidential elections.
         * @throws IOException Thrown if something related to IO goes wrong.
         */
        private static Map<String, OpinionPolls> readAllPresidentialOpinionPolls(final String ropfDirName,
                final WebsiteConfiguration websiteConfiguration) throws IOException {
            Token token = Laconic.LOGGER
                    .logMessage("Parsing presidential election opinion polls files from directory %s.", ropfDirName);
            Map<String, OpinionPolls> opinionPollsMap = new HashMap<String, OpinionPolls>();
            Set<AreaConfiguration> areasWithPresidentialElections =
                    websiteConfiguration
                            .getAreaConfigurations().stream().filter(ac -> ac.getAreaCode() != null
                                    && ac.getElections() != null && ac.getElections().getPresidential() != null)
                            .collect(Collectors.toSet());
            Set<String> presidentialOpinionPollCodes = new HashSet<String>();
            for (AreaConfiguration areaConfiguration : areasWithPresidentialElections) {
                String areaCode = areaConfiguration.getAreaCode();
                for (int index : areaConfiguration.getElections().getPresidential().getDates().keySet()) {
                    presidentialOpinionPollCodes.add(areaCode + "_p" + index);
                }
            }
            for (String presidentialOpinionPollCode : presidentialOpinionPollCodes) {
                Path ropfPath = Paths.get(ropfDirName, presidentialOpinionPollCode + ".ropf");
                if (Files.exists(ropfPath)) {
                    Token fileToken =
                            Laconic.LOGGER.logMessage(token, "Parsing file %s.", ropfPath.getFileName().toString());
                    String[] ropfContent = readFile(ropfPath);
                    RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(fileToken, ropfContent);
                    opinionPollsMap.put(presidentialOpinionPollCode, richOpinionPollsFile.getOpinionPolls());
                }
            }
            return opinionPollsMap;
        }

        /**
         * Reads the election data files from a directory according to the website configuration and puts them in a map.
         *
         * @param websiteConfiguration The website configuration.
         * @param dir                  The directory to read the election data files from.
         * @return A map with the election data files.
         */
        private static Map<String, ElectionData> readElectionDataFiles(final WebsiteConfiguration websiteConfiguration,
                final String dir) {
            Map<String, ElectionData> result = new HashMap<String, ElectionData>();
            Set<AreaConfiguration> areaConfigurations = websiteConfiguration.getAreaConfigurations();
            if (areaConfigurations == null) {
                return result;
            }
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            for (AreaConfiguration areaConfiguration : areaConfigurations) {
                String areaCode = areaConfiguration.getAreaCode();
                ElectionLists electionLists = areaConfiguration.getElections();
                if (electionLists != null) {
                    ElectionList nationalElections = electionLists.getNational();
                    if (nationalElections != null) {
                        for (int i : nationalElections.getDates().keySet()) {
                            File electionDataFile = new File(dir + "/" + areaCode + "-" + i + ".yaml");
                            if (electionDataFile.exists()) {
                                try {
                                    result.put(areaCode + "-" + i,
                                            objectMapper.readValue(electionDataFile, ElectionData.class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            return result;
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
