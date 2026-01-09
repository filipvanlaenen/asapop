package net.filipvanlaenen.asapop.model;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.nombrajkolektoj.integers.ModifiableIntegerMap;
import net.filipvanlaenen.nombrajkolektoj.integers.ModifiableSortedIntegerMap;
import net.filipvanlaenen.nombrajkolektoj.integers.SortedIntegerMap;

/**
 * A class holding all opinion polls, with indexes.
 */
public class OpinionPollsStore {
    private static int numberOfOpinionPolls = 0;
    private static ModifiableIntegerMap<String> numberOfOpinionPollsByArea = ModifiableIntegerMap.<String>empty();
    private static ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfOpinionPollsByYearByArea =
            ModifiableMap.empty();
    private static int numberOfResponseScenarios = 0;
    private static ModifiableIntegerMap<String> numberOfResponseScenariosByArea = ModifiableIntegerMap.<String>empty();
    private static ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResponseScenariosByYearByArea =
            ModifiableMap.empty();
    private static int numberOfResultsValues = 0;
    private static ModifiableIntegerMap<String> numberOfResultValuesByArea = ModifiableIntegerMap.<String>empty();
    private static ModifiableSortedIntegerMap<Integer> numberOfResultValuesByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResultValuesByYearByArea =
            ModifiableMap.empty();

    public static void addAll(final String areaCode, final Collection<OpinionPoll> opinionPolls) {
        if (!numberOfOpinionPollsByArea.containsKey(areaCode)) {
            numberOfOpinionPollsByArea.add(areaCode, 0);
            numberOfResponseScenariosByArea.add(areaCode, 0);
            numberOfResultValuesByArea.add(areaCode, 0);
            numberOfOpinionPollsByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
            numberOfResponseScenariosByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
            numberOfResultValuesByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
        }
        ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByYearForThisArea =
                numberOfOpinionPollsByYearByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByYearForThisArea =
                numberOfResponseScenariosByYearByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResultValuesByYearForThisArea =
                numberOfResultValuesByYearByArea.get(areaCode);
        for (OpinionPoll opinionPoll : opinionPolls) {
            numberOfOpinionPolls += 1;
            numberOfOpinionPollsByArea.augment(areaCode, 1);
            int thisNumberOfResponseScenarios = opinionPoll.getNumberOfResponseScenarios();
            numberOfResponseScenarios += thisNumberOfResponseScenarios;
            numberOfResponseScenariosByArea.augment(areaCode, thisNumberOfResponseScenarios);
            int thisNumberOfResultValues = opinionPoll.getNumberOfResultValues();
            numberOfResultsValues += thisNumberOfResultValues;
            numberOfResultValuesByArea.augment(areaCode, thisNumberOfResultValues);
            int year = opinionPoll.getEndDate().getYear();
            if (numberOfOpinionPollsByYear.containsKey(year)) {
                numberOfOpinionPollsByYear.augment(year, 1);
                numberOfResponseScenariosByYear.augment(year, thisNumberOfResponseScenarios);
                numberOfResultValuesByYear.augment(year, thisNumberOfResultValues);
            } else {
                numberOfOpinionPollsByYear.add(year, 1);
                numberOfResponseScenariosByYear.add(year, thisNumberOfResponseScenarios);
                numberOfResultValuesByYear.add(year, thisNumberOfResultValues);
            }
            if (numberOfOpinionPollsByYearForThisArea.containsKey(year)) {
                numberOfOpinionPollsByYearForThisArea.augment(year, 1);
                numberOfResponseScenariosByYearForThisArea.augment(year, thisNumberOfResponseScenarios);
                numberOfResultValuesByYearForThisArea.augment(year, thisNumberOfResultValues);
            } else {
                numberOfOpinionPollsByYearForThisArea.add(year, 1);
                numberOfResponseScenariosByYearForThisArea.add(year, thisNumberOfResponseScenarios);
                numberOfResultValuesByYearForThisArea.add(year, thisNumberOfResultValues);
            }
        }
    }

    public static int getNumberOfOpinionPolls() {
        return numberOfOpinionPolls;
    }

    public static int getNumberOfOpinionPolls(String areaCode) {
        return numberOfOpinionPollsByArea.get(areaCode);
    }

    public static int getNumberOfOpinionPolls(String areaCode, int thisYear) {
        return numberOfOpinionPollsByYearByArea.get(areaCode).get(thisYear, 0);
    }

    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByYear() {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(), numberOfOpinionPollsByYear);
    }

    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByYear(String areaCode) {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(),
                numberOfOpinionPollsByYearByArea.get(areaCode));
    }

    public static int getNumberOfResponseScenarios() {
        return numberOfResponseScenarios;
    }

    public static int getNumberOfResponseScenarios(String areaCode) {
        return numberOfResponseScenariosByArea.get(areaCode);
    }

    public static int getNumberOfResponseScenarios(String areaCode, int thisYear) {
        return numberOfResponseScenariosByYearByArea.get(areaCode).get(thisYear, 0);
    }

    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByYear() {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(), numberOfResponseScenariosByYear);
    }

    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByYear(String areaCode) {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(),
                numberOfResponseScenariosByYearByArea.get(areaCode));
    }

    public static int getNumberOfResultValues() {
        return numberOfResultsValues;
    }

    public static int getNumberOfResultValues(String areaCode) {
        return numberOfResultValuesByArea.get(areaCode);
    }

    public static int getNumberOfResultValues(String areaCode, int thisYear) {
        return numberOfResultValuesByYearByArea.get(areaCode).get(thisYear, 0);
    }

    public static SortedIntegerMap<Integer> getNumberOfResultValuesByYear() {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(), numberOfResultValuesByYear);
    }

    public static SortedIntegerMap<Integer> getNumberOfResultValuesByYear(String areaCode) {
        // TODO: Replace with the of factory method
        return new SortedIntegerMap.SortedTreeMap<>(Comparator.naturalOrder(),
                numberOfResultValuesByYearByArea.get(areaCode));
    }

    public static boolean hasOpinionPolls(String areaCode) {
        // TODO: A better test to check whether an area has opinion polls
        return numberOfOpinionPollsByArea.containsKey(areaCode);
    }
}
