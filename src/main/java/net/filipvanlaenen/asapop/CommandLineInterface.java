package net.filipvanlaenen.asapop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
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
import net.filipvanlaenen.asapop.model.OpinionPollsStore;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.website.Internationalization;
import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.asapop.website.Website;
import net.filipvanlaenen.asapop.website.WebsiteBuilder;
import net.filipvanlaenen.asapop.yaml.Analysis;
import net.filipvanlaenen.asapop.yaml.AnalysisBuilder;
import net.filipvanlaenen.asapop.yaml.ElectionData;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.ScrapeConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.AreaBuilder;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.ElectedBodyConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.ElectedOfficeConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.ElectionList;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.ElectionLists;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.ElectionsBuilder;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.collectors.Collectors;
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
     * The integer number five.
     */
    private static final int FIVE = 5;

    /**
     * The main entry point for the command line interface.
     *
     * @param args The arguments.
     * @throws IOException Thrown if something related to IO goes wrong.
     */
    public static void main(final String... args) throws IOException, InterruptedException {
        if (args.length < 1) {
            printUsage();
            return;
        }
        SampledHypergeometricDistributionsFileCache.toggleOn();
        try {
            Command.valueOf(args[0].toUpperCase()).execute(args);
        } catch (IllegalArgumentException iae) {
            Laconic.LOGGER.logError("An exception occurred: %s:", iae.getMessage());
            iae.printStackTrace();
        }
    }

    /**
     * Prints the usage to the command line.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  analyze <ropf-file-name> <election-yaml-file-name> <analysis-result-yaml-file-name>");
        System.out.println("  build <site-dir-name> <website-configuration-yaml-file-name> <ropf-dir-name>"
                + " <custom-style-sheet-file-name>");
        System.out.println("  convert <ropf-file-name> <csv-file-name> <electoral-list-key>+ [-a=<area>]");
        System.out.println("  format <ropf-file-name> [-o=<ID-combinations>]");
        System.out.println("  parse <ropf-file-name>");
        System.out.println("  provide <ropf-file-name> <sapor-dir-name> <sapor-configuration-yaml-file-name>");
        System.out.println("  scrape <ropf-dir-name> <scrape-configuration-dir-name> <contact-info> <scrape-cache-dir>"
                + " [-c=<country-code>] [-i=<country-code>[,<country-code>]+] [-v]");
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
                AnalysisEngine engine =
                        new AnalysisEngine(richOpinionPollsFile.getOpinionPollsDeprecated(), electionData);
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
                LocalDate now = LocalDate.now();
                AreaBuilder.build(websiteConfiguration, now);
                Terms terms = objectMapper.readValue(readResource("/internationalization.yaml"), Terms.class);
                Internationalization internationalization = new Internationalization(terms);
                addAreaTranslations(internationalization, websiteConfiguration);
                Map<String, ElectionData> electionDataFiles =
                        readElectionDataFiles(websiteConfiguration, siteConfigurationFile.getParent());
                Elections elections =
                        ElectionsBuilder.extractAndValidateElections(websiteConfiguration, electionDataFiles);
                Map<String, OpinionPolls> parliamentaryOpinionPollsMap =
                        readAllParliamentaryOpinionPolls(ropfDirName, websiteConfiguration);
                Map<String, OpinionPolls> presidentialOpinionPollsMap =
                        readAllPresidentialOpinionPolls(ropfDirName, websiteConfiguration);
                String baseStyleSheetContent = readResource("/base.css");
                String customStyleSheetContent = String.join("\n", readFile(customStyleSheetFileName));
                String navigationScriptContent = readResource("/navigation.js");
                String sortingScriptContent = readResource("/sorting.js");
                String tooltipScriptContent = readResource("/tooltip.js");
                Website website =
                        new WebsiteBuilder(websiteConfiguration, internationalization, parliamentaryOpinionPollsMap,
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
                ModifiableOrderedCollection<Set<String>> electoralListKeySets =
                        ModifiableOrderedCollection.<Set<String>>empty();
                for (int i = 0; i < noOfElectoralListKeySets; i++) {
                    electoralListKeySets.add(new HashSet<String>(Arrays.asList(args[i + THREE].split("\\+"))));
                }
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", inputFileName);
                String[] ropfContent = readFile(inputFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(token, ropfContent);
                OpinionPolls opinionPolls = richOpinionPollsFile.getOpinionPollsDeprecated();
                String outputContent = "";
                if (outputFileName.endsWith(".csv")) {
                    outputContent = EopaodCsvExporter.export(opinionPolls, area, null, electoralListKeySets,
                            OrderedCollection.empty());
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
                OrderedCollection<Collection<String>> idCombinations = OrderedCollection.<Collection<String>>empty();
                if (args.length > 2 && args[2].startsWith("-o=")) {
                    String idCombinationsOption = args[2];
                    String idCombinationsString = idCombinationsOption.substring(THREE, idCombinationsOption.length());
                    String[] idCombinationsArray = idCombinationsString.split(",");
                    ModifiableOrderedCollection<Collection<String>> result =
                            ModifiableOrderedCollection.<Collection<String>>empty();
                    for (String idCombination : idCombinationsArray) {
                        result.add(Collection.of(idCombination.split("\\+")));
                    }
                    idCombinations = result;
                }
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", ropfFileName);
                String[] ropfContent = readFile(ropfFileName);
                RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(token, ropfContent);
                writeFile(ropfFileName, RopfExporter.export(richOpinionPollsFile, idCombinations));
            }
        },
        /**
         * Command to parse an ROPF file.
         */
        PARSE {
            @Override
            void execute(final String[] args) throws IOException {
                String ropfFileName = args[1];
                Token token = Laconic.LOGGER.logMessage("Parsing file %s.", ropfFileName);
                Laconic.LOGGER.setPrintStream(System.out);
                String[] ropfContent = readFile(ropfFileName);
                RichOpinionPollsFile.parse(token, ropfContent);
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
                OpinionPolls opinionPolls = richOpinionPollsFile.getOpinionPollsDeprecated();
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
        },
        /**
         * Command to scrape ROPF files.
         */
        SCRAPE {
            private static final String BASE_URL = "https://en.wikipedia.org/wiki/";

            private void checkForNextElectionPages(final String scrapeConfigurationFileName,
                    final String[] possibleNextElectionPageNames, final String cacheDirName, final String userAgent,
                    final boolean verbose, final ModifiableSortedCollection<String> results,
                    final Token scrapeConfigurationFileToken) throws IOException, InterruptedException {
                boolean nextElectionPageAppeared = false;
                for (String possibleNextElectionPageName : possibleNextElectionPageNames) {
                    Token wikipediaPageToken = Laconic.LOGGER.logMessage(scrapeConfigurationFileToken,
                            "Checking whether the English Wikipedia page %s exists.", possibleNextElectionPageName);
                    String content = loadPage(cacheDirName, possibleNextElectionPageName, userAgent,
                            scrapeConfigurationFileToken);
                    if (content != null) {
                        Laconic.LOGGER.logError("Page exists.", wikipediaPageToken);
                        nextElectionPageAppeared = true;
                    }
                }
                if (nextElectionPageAppeared) {
                    results.add(scrapeConfigurationFileName + ": Possible next election page exists.");
                } else if (verbose) {
                    results.add(scrapeConfigurationFileName + ": None of the possible next election pages exists yet.");
                }
            }

            private void checkForOpinionPollsSection(final String scrapeConfigurationFileName,
                    final String nextElectionPageName, final String cacheDirName, final String userAgent,
                    final boolean verbose, final ModifiableSortedCollection<String> results,
                    final Token scrapeConfigurationFileToken) throws IOException, InterruptedException {
                Token nextElectionPageToken = Laconic.LOGGER.logMessage(scrapeConfigurationFileToken,
                        "Looking for the next election page %s.", nextElectionPageName);
                String page = loadPage(cacheDirName, nextElectionPageName, userAgent, nextElectionPageToken);
                boolean opinionPollsHeaderFound = false;
                int i = 0;
                while (i >= 0 && !opinionPollsHeaderFound) {
                    int headerStart = Math.min(page.indexOf("<h", i), page.indexOf("<H", i));
                    Token headerToken =
                            Laconic.LOGGER.logMessage(nextElectionPageToken, "Header detected at %d.", headerStart);
                    if (headerStart == -1) {
                        i = headerStart;
                    } else {
                        int headerEnd = Math.min(page.indexOf("</h", headerStart), page.indexOf("</H", headerStart));
                        String header = page.substring(headerStart, headerEnd);
                        if (header.toLowerCase().contains("Opinion polls")) {
                            Laconic.LOGGER.logError("Opinion polls section exists.", headerToken);
                            opinionPollsHeaderFound = true;
                        }
                        i = headerStart + 1;
                    }
                }
                if (opinionPollsHeaderFound) {
                    results.add(scrapeConfigurationFileName + ": Opinion polls section exists.");
                } else if (verbose) {
                    results.add(scrapeConfigurationFileName + ": No opinion polls section exists yet.");
                }
            }

            @Override
            void execute(final String[] args) throws IOException, InterruptedException {
                String ropfDirName = args[1];
                String scrapeConfigurationDirName = args[2];
                String contactInfo = args[THREE];
                String cacheDirName = args[FOUR];
                String countryCode = null;
                Collection<String> ropfIgnoreList = Collection.empty();
                boolean verbose = false;
                if (args.length > FOUR) {
                    for (int i = FIVE; i < args.length; i++) {
                        String arg = args[i];
                        if (arg.startsWith("-c=")) {
                            countryCode = arg.substring(THREE);
                        } else if (arg.startsWith("-i=")) {
                            ropfIgnoreList = Collection.of(arg.substring(THREE).split(","));
                        } else if (args[i].equals("-v")) {
                            verbose = true;
                        }
                    }
                }
                Token ropfToken = Laconic.LOGGER.logMessage("Reading the ROPF files from %s.", ropfDirName);
                Token scrapeToken = Laconic.LOGGER.logMessage("Searching for scrape configuration files in %s.",
                        scrapeConfigurationDirName);
                Path ropfDir = Paths.get(ropfDirName);
                Collection<Path> ropfPaths = Files.list(ropfDir).filter(path -> path.toString().endsWith(".ropf"))
                        .collect(Collectors.toCollection());
                ModifiableSortedCollection<String> results =
                        ModifiableSortedCollection.empty(Comparator.naturalOrder());
                ModifiableCollection<String> scrapeConfigurationFileNames = ModifiableCollection.empty();
                String userAgent = "AsapopWikiBot/1.0 (contact: " + contactInfo + ")";
                for (Path ropfPath : ropfPaths) {
                    String ropfFileName = ropfPath.getFileName().toString();
                    if (ropfIgnoreList.contains(ropfFileName)
                            || countryCode != null && !(countryCode + ".ropf").equals(ropfFileName)) {
                        continue;
                    }
                    scrapeForRopfFile(ropfPath, scrapeConfigurationDirName, cacheDirName, userAgent, verbose, results,
                            scrapeConfigurationFileNames, ropfToken, scrapeToken);
                }
                Path scrapeConfigurationDir = Paths.get(scrapeConfigurationDirName);
                Collection<Path> scrapeConfigurationPaths = Files.list(scrapeConfigurationDir)
                        .filter(path -> path.toString().endsWith(".yaml")).collect(Collectors.toCollection());
                for (Path scrapeConfigurationPath : scrapeConfigurationPaths) {
                    String scrapeConfigurationFileName = scrapeConfigurationPath.getFileName().toString();
                    if (scrapeConfigurationFileNames.contains(scrapeConfigurationFileName)
                            || countryCode != null && !(countryCode + ".yaml").equals(scrapeConfigurationFileName)) {
                        continue;
                    }
                    scrapeWithoutRopfFile(scrapeConfigurationPath, cacheDirName, userAgent, verbose, results,
                            scrapeToken);
                }
                System.out.println();
                System.out.println("Results:");
                System.out.println(String.join("\n", results));
            }

            private HttpResponse<String> getWikipediaPage(final String userAgent,
                    final String possibleNextElectionPageName) throws IOException, InterruptedException {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + possibleNextElectionPageName))
                        .header("User-Agent", userAgent).GET().build();
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            private String loadPage(final String cacheDirName, final String pageName, final String userAgent,
                    final Token token) throws IOException, InterruptedException {
                Path cachedPagePath = Paths.get(cacheDirName, pageName);
                Laconic.LOGGER.logMessage(token, "Checking presence of the page in the cache.",
                        cachedPagePath.toString());
                LocalTime now = LocalTime.now();
                if (Files.exists(cachedPagePath) && Files.getLastModifiedTime(cachedPagePath).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalTime().isAfter(now.minusHours(24))) {
                    Laconic.LOGGER.logMessage(token, "Reading the page from the cache.");
                    return String.join("\n", readFile(cachedPagePath));
                } else {
                    Laconic.LOGGER.logMessage(token, "Donwloading the page from Wikipedia.");
                    HttpResponse<String> response = getWikipediaPage(userAgent, pageName);
                    if (response.statusCode() == 200) {
                        String content = response.body();
                        Laconic.LOGGER.logMessage(token, "Writing the page to the cache.");
                        writeFile(cachedPagePath, content);
                        return content;
                    } else {
                        Laconic.LOGGER.logMessage(token, "Page not present on Wikipedia.");
                        return null;
                    }
                }
            }

            private ScrapeConfiguration readScrapeConfigurationFile(final Token scrapeConfigurationFileToken,
                    final Path scrapeConfigurationPath) throws IOException, StreamReadException, DatabindException {
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                Laconic.LOGGER.logMessage(scrapeConfigurationFileToken, "Loading the scrape configuration file.");
                ScrapeConfiguration scrapeConfiguration =
                        objectMapper.readValue(scrapeConfigurationPath.toFile(), ScrapeConfiguration.class);
                return scrapeConfiguration;
            }

            private void scrapeForRopfFile(final Path ropfPath, final String scrapeConfigurationDirName,
                    final String cacheDirName, final String userAgent, final boolean verbose,
                    final ModifiableSortedCollection<String> results,
                    final ModifiableCollection<String> scrapeConfigurationFileNames, final Token ropfToken,
                    final Token scrapeToken)
                    throws IOException, StreamReadException, DatabindException, InterruptedException {
                String ropfFileName = ropfPath.getFileName().toString();
                Token ropfFileToken = Laconic.LOGGER.logMessage(ropfToken, "Scraping for ROPF file %s.", ropfFileName);
                String scrapeConfigurationFileName = ropfFileName.replace("ropf", "yaml");
                scrapeConfigurationFileNames.add(scrapeConfigurationFileName);
                Token scrapeConfigurationFileToken = Laconic.LOGGER.logMessage(scrapeToken,
                        "Looking for scrape configuration file %s.", scrapeConfigurationFileName);
                Path scrapeConfigurationPath = Path.of(scrapeConfigurationDirName, scrapeConfigurationFileName);
                if (Files.exists(scrapeConfigurationPath)) {
                    ScrapeConfiguration scrapeConfiguration =
                            readScrapeConfigurationFile(scrapeConfigurationFileToken, scrapeConfigurationPath);
                    String[] possibleNextElectionPageNames = scrapeConfiguration.getPossibleNextElectionPageNames();
                    String nextElectionPageName = scrapeConfiguration.getNextElectionPageName();
                    if (possibleNextElectionPageNames != null) {
                        checkForNextElectionPages(scrapeConfigurationFileName, possibleNextElectionPageNames,
                                cacheDirName, userAgent, verbose, results, scrapeConfigurationFileToken);
                    } else if (nextElectionPageName != null) {
                        checkForOpinionPollsSection(scrapeConfigurationFileName, nextElectionPageName, cacheDirName,
                                userAgent, verbose, results, scrapeConfigurationFileToken);
                    } else {
                        Laconic.LOGGER.logMessage(ropfFileToken, "Parsing the ROPF file.");
                        String[] ropfContent = readFile(ropfPath);
                        RichOpinionPollsFile richOpinionPollsFile =
                                RichOpinionPollsFile.parse(ropfFileToken, ropfContent);
                        // TODO: Scrape
                        results.add(ropfFileName + ": Scraping not implemented yet.");
                    }
                } else {
                    Laconic.LOGGER.logError("No scrape configuration file found for the ROPF file.",
                            scrapeConfigurationFileToken, ropfFileToken);
                    results.add(ropfFileName + ": No scrape configuration file found.");
                }
            }

            private void scrapeWithoutRopfFile(final Path scrapeConfigurationPath, final String cacheDirName,
                    final String userAgent, final boolean verbose, final ModifiableSortedCollection<String> results,
                    final Token scrapeToken)
                    throws IOException, StreamReadException, DatabindException, InterruptedException {
                String scrapeConfigurationFileName = scrapeConfigurationPath.getFileName().toString();
                Token scrapeConfigurationFileToken = Laconic.LOGGER.logMessage(scrapeToken,
                        "Reading scrape configuration file %s.", scrapeConfigurationFileName);
                ScrapeConfiguration scrapeConfiguration =
                        readScrapeConfigurationFile(scrapeConfigurationFileToken, scrapeConfigurationPath);
                String[] possibleNextElectionPageNames = scrapeConfiguration.getPossibleNextElectionPageNames();
                String nextElectionPageName = scrapeConfiguration.getNextElectionPageName();
                if (possibleNextElectionPageNames != null) {
                    checkForNextElectionPages(scrapeConfigurationFileName, possibleNextElectionPageNames, cacheDirName,
                            userAgent, verbose, results, scrapeConfigurationFileToken);
                } else if (nextElectionPageName != null) {
                    checkForOpinionPollsSection(scrapeConfigurationFileName, nextElectionPageName, cacheDirName,
                            userAgent, verbose, results, scrapeConfigurationFileToken);
                }
            }
        };

        /**
         * Adds the translations from all the areas to the internationalization dictionary.
         *
         * @param internationalization The internationalization dictionary to add the translations to.
         * @param websiteConfiguration The website configuration to extract the translations from the areas to.
         */
        static void addAreaTranslations(final Internationalization internationalization,
                final WebsiteConfiguration websiteConfiguration) {
            for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
                String areaCode = areaConfiguration.getAreaCode();
                if (areaConfiguration.getTranslations() != null) {
                    internationalization.addTranslations("_area_" + areaCode, areaConfiguration.getTranslations());
                }
                if (areaConfiguration.getSubdivisions() != null) {
                    for (AreaSubdivisionConfiguration subdivision : areaConfiguration.getSubdivisions()) {
                        if (subdivision.getTranslations() != null) {
                            internationalization.addTranslations("_area_" + areaCode + "-" + subdivision.getAreaCode(),
                                    subdivision.getTranslations());
                        }
                    }
                }
                if (areaConfiguration.getElectedBodies() != null) {
                    for (ElectedBodyConfiguration electedBody : areaConfiguration.getElectedBodies()) {
                        if (electedBody.getTranslatedNames() != null) {
                            internationalization.addTranslations("_electedBody_" + areaCode + "_" + electedBody.getId(),
                                    mergeProperAndTranslatedNames(electedBody.getProperNames(),
                                            electedBody.getTranslatedNames()));
                        }
                    }
                }
                if (areaConfiguration.getElectedOffices() != null) {
                    for (ElectedOfficeConfiguration electedOffice : areaConfiguration.getElectedOffices()) {
                        if (electedOffice.getTranslatedNames() != null) {
                            internationalization.addTranslations(
                                    "_electedOffice_" + areaCode + "_" + electedOffice.getId(),
                                    mergeProperAndTranslatedNames(electedOffice.getProperNames(),
                                            electedOffice.getTranslatedNames()));
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
        abstract void execute(String[] args) throws IOException, InterruptedException;

        /**
         * Merges two maps, one with the proper names and one with the translated names, into a single map. Only the
         * names for the languages in the <code>Language</code> enumeration are added from the proper names.
         *
         * @param properNames     The proper names.
         * @param translatedNames The translated names.
         * @return A new map with proper names for supported languages added if they were missing in the translated
         *         names.
         */
        private static Map<String, String> mergeProperAndTranslatedNames(final Map<String, String> properNames,
                final Map<String, String> translatedNames) {
            ModifiableMap<String, String> result = ModifiableMap.of(translatedNames);
            for (Language language : Language.values()) {
                String id = language.getId();
                if (!result.containsKey(id) && properNames.containsKey(id)) {
                    result.add(id, properNames.get(id));
                }
            }
            return result;
        }

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
            ModifiableMap<String, OpinionPolls> opinionPollsMap = ModifiableMap.<String, OpinionPolls>empty();
            Set<String> areaCodes = websiteConfiguration.getAreaConfigurations().stream()
                    .filter(ac -> ac.getAreaCode() != null).map(areaConfigutation -> areaConfigutation.getAreaCode())
                    .collect(java.util.stream.Collectors.toSet());
            for (String areaCode : areaCodes) {
                Path ropfPath = Paths.get(ropfDirName, areaCode + ".ropf");
                if (Files.exists(ropfPath)) {
                    Token fileToken =
                            Laconic.LOGGER.logMessage(token, "Parsing file %s.", ropfPath.getFileName().toString());
                    String[] ropfContent = readFile(ropfPath);
                    RichOpinionPollsFile richOpinionPollsFile = RichOpinionPollsFile.parse(fileToken, ropfContent);
                    opinionPollsMap.put(areaCode, richOpinionPollsFile.getOpinionPollsDeprecated());
                    OpinionPollsStore.addAll(areaCode, richOpinionPollsFile.getOpinionPolls());
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
            ModifiableMap<String, OpinionPolls> opinionPollsMap = ModifiableMap.<String, OpinionPolls>empty();
            Set<AreaConfiguration> areasWithPresidentialElections =
                    websiteConfiguration.getAreaConfigurations().stream()
                            .filter(ac -> ac.getAreaCode() != null && ac.getElections() != null
                                    && ac.getElections().getPresidential() != null)
                            .collect(java.util.stream.Collectors.toSet());
            Set<String> presidentialOpinionPollCodes = new HashSet<String>();
            for (AreaConfiguration areaConfiguration : areasWithPresidentialElections) {
                String areaCode = areaConfiguration.getAreaCode();
                for (int index : areaConfiguration.getElections().getPresidential().getDates().getKeys()) {
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
                    opinionPollsMap.put(presidentialOpinionPollCode, richOpinionPollsFile.getOpinionPollsDeprecated());
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
            ModifiableMap<String, ElectionData> result = ModifiableMap.<String, ElectionData>empty();
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
                        for (int i : nationalElections.getDates().getKeys()) {
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
            for (Map.Entry<Path, String> entry : fileNamesAndContents) {
                Path path = Paths.get(baseDir, entry.key().toString());
                Files.createDirectories(path.getParent());
                writeFile(path, entry.value());
            }
        }
    }
}
