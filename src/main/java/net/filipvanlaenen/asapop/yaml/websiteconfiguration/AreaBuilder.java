package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import net.filipvanlaenen.asapop.model.Area;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;
import net.filipvanlaenen.kolektoj.collectors.Collectors;

public class AreaBuilder {
    public static void build(final WebsiteConfiguration websiteConfiguration) {
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            buildAndAddArea(areaConfiguration);
        }
    }

    private static void buildAndAddArea(final AreaConfiguration areaConfiguration) {
        ModifiableCollection<net.filipvanlaenen.asapop.model.ElectedBody> electedBodies = ModifiableCollection.empty();
        if (areaConfiguration.getElectedBodies() != null) {
            for (ElectedBody electedBody : areaConfiguration.getElectedBodies()) {
                electedBodies.add(buildElectedBody(electedBody));
            }
        }
        Area.add(new Area(areaConfiguration.getAreaCode(), electedBodies));
    }

    private static net.filipvanlaenen.asapop.model.ElectedBody buildElectedBody(final ElectedBody electedBody) {
        Map<Language, String> translatedNames = electedBody.getTranslatedNames().stream()
                .map(e -> new Map.Entry<Language, String>(Language.parse(e.key()), e.value()))
                .collect(Collectors.toMap(e -> e.key(), e -> e.value()));
        ModifiableMap<Integer, OrderedValueCollection<ElectionDate>> elections = ModifiableMap.empty();
        for (Map.Entry<Integer, String> electionDate : electedBody.getElectionDates()) {
            elections.add(electionDate.key(), buildElectionDateCollection(electionDate.value()));
        }
        return new net.filipvanlaenen.asapop.model.ElectedBody(electedBody.getProperNames(), translatedNames,
                elections);
    }

    private static OrderedValueCollection<ElectionDate> buildElectionDateCollection(final String date) {
        String[] singleDates = date.split("\\+");
        ModifiableOrderedCollection<ElectionDate> dates = ModifiableOrderedCollection.empty();
        for (String singleDate : singleDates) {
            boolean annulled = singleDate.startsWith("(");
            if (!annulled) {
                // TODO: Log a warning if the parsing fails
                ElectionDate ed = ElectionDate.parse(singleDate);
                dates.addLast(ed);
            }
        }
        // TODO: Log a warning if there's no future date
        return OrderedValueCollection.of(dates);
    }
}
