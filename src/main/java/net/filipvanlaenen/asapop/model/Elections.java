package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class holding elections indexed by area and election type.
 */
public class Elections {
    /**
     * A map holding the elections indexed by area code and election type.
     */
    private final Map<String, Map<ElectionType, List<Election>>> map =
            new HashMap<String, Map<ElectionType, List<Election>>>();

    /**
     * Adds an election with a set of dates on an area for an election type.
     *
     * @param areaCode     The area code.
     * @param electionType The election type.
     * @param date         A set of dates encoded in a string.
     */
    public void addElection(final String areaCode, final ElectionType electionType, final String date) {
        String[] singleDates = date.split("\\+");
        List<ElectionDate> dates = new ArrayList<ElectionDate>();
        List<List<ElectionDate>> datesAnnulled = new ArrayList<List<ElectionDate>>();
        for (String singleDate : singleDates) {
            boolean annulled = singleDate.startsWith("(");
            ElectionDate ed =
                    ElectionDate.parse(annulled ? singleDate.substring(1, singleDate.length() - 1) : singleDate);
            if (annulled) {
                if (datesAnnulled.size() <= dates.size()) {
                    datesAnnulled.add(new ArrayList<ElectionDate>());
                }
                datesAnnulled.get(dates.size()).add(ed);
            } else {
                dates.add(ed);
                if (datesAnnulled.size() < dates.size()) {
                    datesAnnulled.add(new ArrayList<ElectionDate>());
                }
            }
        }
        if (!map.containsKey(areaCode)) {
            map.put(areaCode, new HashMap<ElectionType, List<Election>>());
        }
        Map<ElectionType, List<Election>> areaElections = map.get(areaCode);
        if (!areaElections.containsKey(electionType)) {
            areaElections.put(electionType, new ArrayList<Election>());
        }
        areaElections.get(electionType).add(new Election(areaCode, electionType, dates, datesAnnulled));
    }

    /**
     * Calculates what's the next election given the set of elections for an area for an election type and starting from
     * a certain date.
     *
     * @param electionsAtArea The elections at an area.
     * @param electionType    The election type.
     * @param now             The date to calculate the next election from.
     * @return The first election of the type at the area after the provided date.
     */
    private Election calculateNextElection(final Map<ElectionType, List<Election>> electionsAtArea,
            final ElectionType electionType, final LocalDate now) {
        List<Election> electionsOfTypeAtArea = electionsAtArea.get(electionType);
        if (electionsOfTypeAtArea == null) {
            return null;
        }
        Election result = null;
        ElectionDate resultNextElectionDate = null;
        for (Election election : electionsOfTypeAtArea) {
            ElectionDate nextElectionDate = election.getNextElectionDate(now);
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (nextElectionDate != null
                    && (result == null || nextElectionDate.compareTo(resultNextElectionDate) < 0)) {
                result = election;
                resultNextElectionDate = nextElectionDate;
            }
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Elections) {
            Elections otherElections = (Elections) obj;
            return map.equals(otherElections.map);
        } else {
            return false;
        }
    }

    /**
     * Returns the first election after a given date of a type in an area.
     *
     * @param areaCode     The area code.
     * @param electionType The election type.
     * @param now          The date to calculate the next election from.
     * @return The first election of the requested type at the area after the provided date.
     */
    public Election getNextElection(final String areaCode, final ElectionType electionType, final LocalDate now) {
        if (map.containsKey(areaCode)) {
            return calculateNextElection(map.get(areaCode), electionType, now);
        } else {
            return null;
        }
    }

    /**
     * Returns the first elections for all areas and all types after a given date.
     *
     * @param now The date to calculate the next elections from.
     * @return A set with all first elections after the provided date.
     */
    public Set<Election> getNextElections(final LocalDate now) {
        Set<Election> result = new HashSet<Election>();
        for (String areaCode : map.keySet()) {
            result.addAll(getNextElections(areaCode, now));
        }
        return result;
    }

    /**
     * Returns the first elections of all types for an area after a given date.
     *
     * @param areaCode The area code.
     * @param now      The date to calculate the next elections from.
     * @return A set with all the first elections after the provided date for the area.
     */
    private Set<Election> getNextElections(final String areaCode, final LocalDate now) {
        Map<ElectionType, List<Election>> electionsAtArea = map.get(areaCode);
        if (electionsAtArea == null) {
            return Collections.emptySet();
        }
        Set<Election> result = new HashSet<Election>();
        for (ElectionType electionType : electionsAtArea.keySet()) {
            Election nextElection = calculateNextElection(electionsAtArea, electionType, now);
            if (nextElection != null) {
                result.add(nextElection);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
