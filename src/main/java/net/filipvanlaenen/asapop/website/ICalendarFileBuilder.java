package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.model.ElectionDate.ExpectedDay;
import net.filipvanlaenen.asapop.model.ElectionDate.Qualifier;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

public class ICalendarFileBuilder {
    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * Today's day.
     */
    private final LocalDate now;
    private final Terms terms;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    ICalendarFileBuilder(final WebsiteConfiguration websiteConfiguration, final Elections elections,
            final LocalDate now, final Terms terms) {
        this.websiteConfiguration = websiteConfiguration;
        this.elections = elections;
        this.now = now;
        this.terms = terms;
    }

    String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\n");
        sb.append("VERSION:2.0\n");
        sb.append("PRODID:-//ASAPOP//");
        sb.append(websiteConfiguration.getId());
        sb.append("//");
        sb.append(websiteConfiguration.getName());
        sb.append("//EN\n");
        List<Election> nextElections = new ArrayList<Election>(elections.getNextElections(now));
        for (Election nextElection : nextElections) {
            ElectionDate nextElectionDate = nextElection.getNextElectionDate(now);
            String areaCode = nextElection.areaCode();
            Term areaTerm = terms.getTerm("_area_" + areaCode);
            if (areaCode != null && areaTerm != null && nextElectionDate.qualifier().equals(Qualifier.EXACT_DATE)
                    && nextElectionDate instanceof ExpectedDay) {
                String isoDate = nextElectionDate.getEndDate().format(DateTimeFormatter.BASIC_ISO_DATE);
                sb.append("BEGIN:VEVENT\n");
                sb.append("SUMMARY:🗳️");
                sb.append(Character.toChars(127365 + areaCode.charAt(0)));
                sb.append(Character.toChars(127365 + areaCode.charAt(1)));
                sb.append(" Election in ");
                sb.append(areaTerm.getTranslations().get("en"));
                sb.append(": ");
                sb.append(terms.getTerm(nextElection.electionType().getTermKey()).getTranslations().get("en"));
                sb.append("\n");
                sb.append("DTSTART:");
                sb.append(isoDate);
                sb.append("\n");
                sb.append("DTEND:");
                sb.append(isoDate);
                sb.append("\n");
                sb.append("END:VEVENT\n");
            }
        }
        // TODO: Log a warning if no event was included.
        sb.append("END:VCALENDAR\n");
        return sb.toString();
    }
}
