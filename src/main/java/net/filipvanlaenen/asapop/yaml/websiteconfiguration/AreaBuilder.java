package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import java.time.LocalDate;

import net.filipvanlaenen.asapop.model.Area;
import net.filipvanlaenen.asapop.model.ElectedBody;
import net.filipvanlaenen.asapop.model.ElectedOffice;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;
import net.filipvanlaenen.kolektoj.collectors.Collectors;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Utility class setting up the object model for the areas and their dependencies from the website configuration.
 */
public final class AreaBuilder {
    /**
     * Private constructor to prevent instantiation.
     */
    private AreaBuilder() {
    }

    /**
     * Builds the object model for the areas and their dependences from the website configuration.
     *
     * @param websiteConfiguration The website configuration.
     * @param now                  Today's date.
     */
    public static void build(final WebsiteConfiguration websiteConfiguration, final LocalDate now) {
        Token token = Laconic.LOGGER.logMessage("Extracting and validating the areas.");
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            buildAndAddArea(areaConfiguration, now, token);
        }
    }

    /**
     * Builds an area and its dependencies, and adds it to the areas cache.
     *
     * @param areaConfiguration The area configuration.
     * @param now               Today's date.
     * @param token             The Laconic logging token.
     */
    private static void buildAndAddArea(final AreaConfiguration areaConfiguration, final LocalDate now,
            final Token token) {
        String areaCode = areaConfiguration.getAreaCode();
        Token areaToken = Laconic.LOGGER.logMessage(token, "Extracting and validating the area %s.", areaCode);
        ModifiableCollection<net.filipvanlaenen.asapop.model.ElectedBody> electedBodies = ModifiableCollection.empty();
        if (areaConfiguration.getElectedBodies() != null) {
            for (ElectedBodyConfiguration electedBody : areaConfiguration.getElectedBodies()) {
                electedBodies.add(buildElectedBody(electedBody, now, areaToken));
            }
        }
        ModifiableCollection<net.filipvanlaenen.asapop.model.ElectedOffice> electedOffices =
                ModifiableCollection.empty();
        if (areaConfiguration.getElectedOffices() != null) {
            for (ElectedOfficeConfiguration electedOffice : areaConfiguration.getElectedOffices()) {
                electedOffices.add(buildElectedOffice(electedOffice, now, areaToken));
            }
        }
        Area.add(new Area(areaCode, electedBodies, electedOffices));
    }

    /**
     * Builds an elected body.
     *
     * @param electedBodyConfiguration The elected body configuration.
     * @param now                      Today's date.
     * @param areaToken                The Laconic logging token.
     * @return The object model for the elected body.
     */
    private static ElectedBody buildElectedBody(final ElectedBodyConfiguration electedBodyConfiguration,
            final LocalDate now, final Token areaToken) {
        boolean defunct = electedBodyConfiguration.getDefunct() == Boolean.TRUE;
        Token electedBodyToken = Laconic.LOGGER.logMessage(areaToken, "Extracting and validating the elected body %s.",
                electedBodyConfiguration.getId());
        Map<Language, String> translatedNames = electedBodyConfiguration.getTranslatedNames().stream()
                .map(e -> new Map.Entry<Language, String>(Language.parse(e.key()), e.value()))
                .collect(Collectors.toMap(e -> e.key(), e -> e.value()));
        ModifiableOrderedCollection<OrderedValueCollection<ElectionDate>> elections =
                ModifiableOrderedCollection.empty();
        for (String electionDate : electedBodyConfiguration.getElectionDates()) {
            elections.add(buildElectionDateCollection(electionDate));
        }
        Laconic.LOGGER.logMessage("Validating for future election dates against the date %s.", now.toString(),
                electedBodyToken);
        if (!defunct && !hasFutureDate(elections, now)) {
            Laconic.LOGGER.logError("No election dates set in the future.", electedBodyToken);
        }
        return new ElectedBody(electedBodyConfiguration.getId(), electedBodyConfiguration.getProperNames(),
                translatedNames, elections, defunct);
    }

    /**
     * Builds an elected office.
     *
     * @param electedOfficeConfiguration The elected office configuration.
     * @param now                        Today's date.
     * @param areaToken                  The Laconic logging token.
     * @return The object model for the elected office.
     */
    private static ElectedOffice buildElectedOffice(final ElectedOfficeConfiguration electedOfficeConfiguration,
            final LocalDate now, final Token areaToken) {
        Token electedOfficeToken = Laconic.LOGGER.logMessage(areaToken,
                "Extracting and validating the elected office %s.", electedOfficeConfiguration.getId());
        Map<Language, String> translatedNames = electedOfficeConfiguration.getTranslatedNames().stream()
                .map(e -> new Map.Entry<Language, String>(Language.parse(e.key()), e.value()))
                .collect(Collectors.toMap(e -> e.key(), e -> e.value()));
        ModifiableOrderedCollection<OrderedValueCollection<ElectionDate>> elections =
                ModifiableOrderedCollection.empty();
        for (String electionDate : electedOfficeConfiguration.getElectionDates()) {
            elections.add(buildElectionDateCollection(electionDate));
        }
        Laconic.LOGGER.logMessage("Validating for future election dates against the date %s.", now.toString(),
                electedOfficeToken);
        if (!hasFutureDate(elections, now)) {
            Laconic.LOGGER.logError("No election dates set in the future.", electedOfficeToken);
        }
        return new ElectedOffice(electedOfficeConfiguration.getId(), electedOfficeConfiguration.getProperNames(),
                translatedNames, elections);
    }

    /**
     * Builds an ordered collection of election dates from a election dates description.
     *
     * @param electionDatesDescription A string describing the election dates for an election.
     * @return An ordered value collection containing the election dates for an election.
     */
    private static OrderedValueCollection<ElectionDate> buildElectionDateCollection(
            final String electionDatesDescription) {
        String[] singleDates = electionDatesDescription.split("\\+");
        ModifiableOrderedCollection<ElectionDate> dates = ModifiableOrderedCollection.empty();
        for (String singleDate : singleDates) {
            boolean annulled = singleDate.startsWith("(");
            if (!annulled) {
                String[] simpleDates = singleDate.split("&");
                String simpleDate = simpleDates[simpleDates.length - 1];
                ElectionDate ed = ElectionDate.parse(simpleDate);
                dates.addLast(ed);
            }
        }
        return OrderedValueCollection.of(dates);
    }

    /**
     * Checks whether a collection with elections has an election date set in the future relative to a provided date.
     *
     * @param elections A collection of election dates.
     * @param now       Today's date.
     * @return True if one of the election dates is in the future.
     */
    private static boolean hasFutureDate(final OrderedCollection<OrderedValueCollection<ElectionDate>> elections,
            final LocalDate now) {
        for (OrderedValueCollection<ElectionDate> electionDates : elections) {
            for (ElectionDate electionDate : electionDates) {
                if (!electionDate.getEndDate().isBefore(now)) {
                    return true;
                }
            }
        }
        return false;
    }
}
