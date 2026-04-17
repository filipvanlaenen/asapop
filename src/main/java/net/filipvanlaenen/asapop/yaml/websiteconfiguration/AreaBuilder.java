package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import java.time.LocalDate;

import net.filipvanlaenen.asapop.model.Area;
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

public class AreaBuilder {
    public static void build(final WebsiteConfiguration websiteConfiguration, final LocalDate now) {
        Token token = Laconic.LOGGER.logMessage("Extracting and validating the areas.");
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            buildAndAddArea(areaConfiguration, now, token);
        }
    }

    private static void buildAndAddArea(final AreaConfiguration areaConfiguration, final LocalDate now,
            final Token token) {
        String areaCode = areaConfiguration.getAreaCode();
        Token areaToken = Laconic.LOGGER.logMessage(token, "Extracting and validating the area %s.", areaCode);
        ModifiableCollection<net.filipvanlaenen.asapop.model.ElectedBody> electedBodies = ModifiableCollection.empty();
        if (areaConfiguration.getElectedBodies() != null) {
            for (ElectedBody electedBody : areaConfiguration.getElectedBodies()) {
                electedBodies.add(buildElectedBody(electedBody, now, areaToken));
            }
        }
        ModifiableCollection<net.filipvanlaenen.asapop.model.ElectedOffice> electedOffices =
                ModifiableCollection.empty();
        if (areaConfiguration.getElectedOffices() != null) {
            for (ElectedOffice electedOffice : areaConfiguration.getElectedOffices()) {
                electedOffices.add(buildElectedOffice(electedOffice, now, areaToken));
            }
        }
        Area.add(new Area(areaCode, electedBodies, electedOffices));
    }

    private static net.filipvanlaenen.asapop.model.ElectedBody buildElectedBody(final ElectedBody electedBody,
            final LocalDate now, final Token areaToken) {
        boolean defunct = electedBody.getDefunct() == Boolean.TRUE;
        Token electedBodyToken = Laconic.LOGGER.logMessage(areaToken, "Extracting and validating the elected body %s.",
                electedBody.getId());
        Map<Language, String> translatedNames = electedBody.getTranslatedNames().stream()
                .map(e -> new Map.Entry<Language, String>(Language.parse(e.key()), e.value()))
                .collect(Collectors.toMap(e -> e.key(), e -> e.value()));
        ModifiableOrderedCollection<OrderedValueCollection<ElectionDate>> elections =
                ModifiableOrderedCollection.empty();
        for (String electionDate : electedBody.getElectionDates()) {
            elections.add(buildElectionDateCollection(electionDate));
        }
        Laconic.LOGGER.logMessage("Validating for future election dates against the date %s.", now.toString(),
                electedBodyToken);
        if (!defunct && !hasFutureDate(elections, now)) {
            Laconic.LOGGER.logError("No election dates set in the future.", electedBodyToken);
        }
        return new net.filipvanlaenen.asapop.model.ElectedBody(electedBody.getProperNames(), translatedNames, elections,
                defunct);
    }

    private static net.filipvanlaenen.asapop.model.ElectedOffice buildElectedOffice(final ElectedOffice electedOffice,
            final LocalDate now, final Token areaToken) {
        Token electedOfficeToken = Laconic.LOGGER.logMessage(areaToken,
                "Extracting and validating the elected office %s.", electedOffice.getId());
        Map<Language, String> translatedNames = electedOffice.getTranslatedNames().stream()
                .map(e -> new Map.Entry<Language, String>(Language.parse(e.key()), e.value()))
                .collect(Collectors.toMap(e -> e.key(), e -> e.value()));
        ModifiableOrderedCollection<OrderedValueCollection<ElectionDate>> elections =
                ModifiableOrderedCollection.empty();
        for (String electionDate : electedOffice.getElectionDates()) {
            elections.add(buildElectionDateCollection(electionDate));
        }
        Laconic.LOGGER.logMessage("Validating for future election dates against the date %s.", now.toString(),
                electedOfficeToken);
        if (!hasFutureDate(elections, now)) {
            Laconic.LOGGER.logError("No election dates set in the future.", electedOfficeToken);
        }
        return new net.filipvanlaenen.asapop.model.ElectedOffice(electedOffice.getProperNames(), translatedNames,
                elections);
    }

    private static OrderedValueCollection<ElectionDate> buildElectionDateCollection(final String date) {
        String[] singleDates = date.split("\\+");
        ModifiableOrderedCollection<ElectionDate> dates = ModifiableOrderedCollection.empty();
        for (String singleDate : singleDates) {
            boolean annulled = singleDate.startsWith("(");
            if (!annulled) {
                ElectionDate ed = ElectionDate.parse(singleDate);
                dates.addLast(ed);
            }
        }
        return OrderedValueCollection.of(dates);
    }

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
