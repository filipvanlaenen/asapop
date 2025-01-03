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
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class building the iCalendar file.
 */
public class ICalendarFileBuilder {
    /**
     * The character base for the Unicode flag emojis.
     */
    private static final int UNICODE_FLAG_BASE = 127365;
    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * Today's day.
     */
    private final LocalDate now;
    /**
     * The terms.
     */
    private final Terms terms;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking the website configuration, the elections, the current date and the terms as its parameters.
     *
     * @param websiteConfiguration The website configuration.
     * @param elections            The elections.
     * @param now                  The current date.
     * @param terms                The terms.
     */
    ICalendarFileBuilder(final WebsiteConfiguration websiteConfiguration, final Elections elections,
            final LocalDate now, final Terms terms) {
        this.websiteConfiguration = websiteConfiguration;
        this.elections = elections;
        this.now = now;
        this.terms = terms;
    }

    /**
     * Builds the iCalendar file.
     *
     * @param token The Laconic logging token.
     * @return The content of the iCalendar file.
     */
    String build(final Token token) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\n");
        sb.append("VERSION:2.0\n");
        sb.append("PRODID:-//ASAPOP//");
        sb.append(websiteConfiguration.getId());
        sb.append("//");
        sb.append(websiteConfiguration.getName());
        sb.append("//EN\n");
        List<Election> nextElections = new ArrayList<Election>(elections.getNextElections(now));
        int numberOfItems = 0;
        for (Election nextElection : nextElections) {
            ElectionDate nextElectionDate = nextElection.getNextElectionDate(now);
            String areaCode = nextElection.areaCode();
            if (areaCode != null && nextElectionDate.qualifier().equals(Qualifier.EXACT_DATE)
                    && nextElectionDate instanceof ExpectedDay) {
                Term areaTerm = terms.getTerm("_area_" + areaCode);
                if (areaTerm != null) {
                    String isoDate = nextElectionDate.getEndDate().format(DateTimeFormatter.BASIC_ISO_DATE);
                    sb.append("BEGIN:VEVENT\n");
                    sb.append("SUMMARY:🗳️");
                    sb.append(Character.toChars(UNICODE_FLAG_BASE + areaCode.charAt(0)));
                    sb.append(Character.toChars(UNICODE_FLAG_BASE + areaCode.charAt(1)));
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
                    numberOfItems++;
                }
            }
        }
        if (numberOfItems == 0) {
            Laconic.LOGGER.logError("No events written to the iCalendar file.", token);
        }
        sb.append("END:VCALENDAR\n");
        return sb.toString();
    }
}
