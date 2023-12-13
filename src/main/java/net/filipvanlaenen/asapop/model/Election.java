package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Class modeling an election. An election has an area code, an election type, as set of dates and a set of dates for
 * annulled rounds.
 *
 * @param areaCode      The area code for the election.
 * @param electionType  The election type.
 * @param number        The election number.
 * @param dates         The election dates.
 * @param datesAnnulled The dates for the rounds for which the results were annulled.
 * @param electionData  The election date.
 */
public record Election(String areaCode, ElectionType electionType, int number, List<ElectionDate> dates,
        List<List<ElectionDate>> datesAnnulled, ElectionData electionData) {
    /**
     * Constructor converting the collections into unmodifiable collections.
     *
     * @param areaCode      The area code for the election.
     * @param electionType  The election type.
     * @param number        The election number.
     * @param dates         The election dates.
     * @param datesAnnulled The dates for the rounds for which the results were annulled.
     * @param electionData  The election date.
     */
    public Election(final String areaCode, final ElectionType electionType, final int number,
            final List<ElectionDate> dates, final List<List<ElectionDate>> datesAnnulled,
            final ElectionData electionData) {
        this.areaCode = areaCode;
        this.electionType = electionType;
        this.number = number;
        this.dates = Collections.unmodifiableList(dates);
        this.datesAnnulled = Collections.unmodifiableList(
                datesAnnulled.stream().map(l -> Collections.unmodifiableList(l)).collect(Collectors.toList()));
        this.electionData = electionData;
    }

    /**
     * Returns the first future election date for this election.
     *
     * It is assumed that the dates are sorted chronologically.
     *
     * @param now Today's date.
     * @return The first future election date for this election.
     */
    public ElectionDate getNextElectionDate(final LocalDate now) {
        for (ElectionDate electionDate : dates) {
            if (!electionDate.getEndDate().isBefore(now)) {
                return electionDate;
            }
        }
        return null;
    }
}
