package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.nombrajkolektoj.integers.ModifiableIntegerMap;
import net.filipvanlaenen.nombrajkolektoj.integers.ModifiableSortedIntegerMap;
import net.filipvanlaenen.nombrajkolektoj.integers.OrderedIntegerCollection;
import net.filipvanlaenen.nombrajkolektoj.integers.SortedIntegerMap;

/**
 * A class holding all opinion polls, with indexes.
 */
public final class OpinionPollsStore {
    /**
     * An ordered integer collection with the numbers of all the months.
     */
    private static final OrderedIntegerCollection ALL_MONTHS = OrderedIntegerCollection.createSequence(i -> i + 1, 12);
    /**
     * The most recent date registered on an opinion poll, indexed by area.
     */
    private static ModifiableMap<String, LocalDate> mostRecentDateByArea = ModifiableMap.<String, LocalDate>empty();
    /**
     * The total number of opinion polls in the store.
     */
    private static int numberOfOpinionPolls = 0;
    /**
     * An integer map with the number of opinion polls, indexed by area.
     */
    private static ModifiableIntegerMap<String> numberOfOpinionPollsByArea = ModifiableIntegerMap.<String>empty();
    /**
     * An integer map with the number of opinion polls, indexed by month.
     */
    // TODO: Switch to UpdatableSortedIntegerMap
    private static ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByMonth =
            ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
    /**
     * An integer map with the number of opinion polls, indexed by month and area.
     */
    // TODO: Switch to UpdatableSortedIntegerMap
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfOpinionPollsByMonthByArea =
            ModifiableMap.empty();
    /**
     * An integer map with the number of opinion polls, indexed by year.
     */
    private static ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    /**
     * An integer map with the number of opinion polls, indexed by year and area.
     */
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfOpinionPollsByYearByArea =
            ModifiableMap.empty();
    /**
     * The total number of response scenarios in the store.
     */
    private static int numberOfResponseScenarios = 0;
    /**
     * An integer map with the number of response scenarios, indexed by area.
     */
    private static ModifiableIntegerMap<String> numberOfResponseScenariosByArea = ModifiableIntegerMap.<String>empty();
    /**
     * An integer map with the number of response scenarios, indexed by month.
     */
    // TODO: Switch to UpdatableSortedIntegerMap
    private static ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByMonth =
            ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
    // TODO: Switch to UpdatableSortedIntegerMap
    /**
     * An integer map with the number of response scenarios, indexed by month and area.
     */
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResponseScenariosByMonthByArea =
            ModifiableMap.empty();
    /**
     * An integer map with the number of response scenarios, indexed by year.
     */
    private static ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    /**
     * An integer map with the number of response scenarios, indexed by year and area.
     */
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResponseScenariosByYearByArea =
            ModifiableMap.empty();
    /**
     * The total number of result values in the store.
     */
    private static int numberOfResultValues = 0;
    /**
     * An integer map with the number of result values, indexed by area.
     */
    private static ModifiableIntegerMap<String> numberOfResultValuesByArea = ModifiableIntegerMap.<String>empty();
    /**
     * An integer map with the number of result values, indexed by month.
     */
    // TODO: Switch to UpdatableSortedIntegerMap
    private static ModifiableSortedIntegerMap<Integer> numberOfResultValuesByMonth =
            ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
    /**
     * An integer map with the number of result values, indexed by month and area.
     */
    // TODO: Switch to UpdatableSortedIntegerMap
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResultValuesByMonthByArea =
            ModifiableMap.empty();
    /**
     * An integer map with the number of result values, indexed by year.
     */
    private static ModifiableSortedIntegerMap<Integer> numberOfResultValuesByYear =
            ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
    /**
     * An integer map with the number of result values, indexed by year and area.
     */
    private static ModifiableMap<String, ModifiableSortedIntegerMap<Integer>> numberOfResultValuesByYearByArea =
            ModifiableMap.empty();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private OpinionPollsStore() {
    }

    /**
     * Adds a collection opinion polls associated with an area.
     *
     * @param areaCode     The code for the area.
     * @param opinionPolls A collection opinion polls.
     */
    public static void addAll(final String areaCode, final Collection<OpinionPoll> opinionPolls) {
        if (!numberOfOpinionPollsByArea.containsKey(areaCode)) {
            numberOfOpinionPollsByArea.add(areaCode, 0);
            numberOfResponseScenariosByArea.add(areaCode, 0);
            numberOfResultValuesByArea.add(areaCode, 0);
            numberOfOpinionPollsByMonthByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS));
            numberOfOpinionPollsByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
            numberOfResponseScenariosByMonthByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS));
            numberOfResponseScenariosByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
            numberOfResultValuesByMonthByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS));
            numberOfResultValuesByYearByArea.add(areaCode,
                    ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder()));
        }
        ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByMonthForThisArea =
                numberOfOpinionPollsByMonthByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfOpinionPollsByYearForThisArea =
                numberOfOpinionPollsByYearByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByMonthForThisArea =
                numberOfResponseScenariosByMonthByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResponseScenariosByYearForThisArea =
                numberOfResponseScenariosByYearByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResultValuesByMonthForThisArea =
                numberOfResultValuesByMonthByArea.get(areaCode);
        ModifiableSortedIntegerMap<Integer> numberOfResultValuesByYearForThisArea =
                numberOfResultValuesByYearByArea.get(areaCode);
        for (OpinionPoll opinionPoll : opinionPolls) {
            LocalDate endDate = opinionPoll.getEndDate();
            if (!mostRecentDateByArea.containsKey(areaCode)) {
                mostRecentDateByArea.add(areaCode, endDate);
            } else if (endDate.isAfter(mostRecentDateByArea.get(areaCode))) {
                mostRecentDateByArea.put(areaCode, endDate);
            }
            numberOfOpinionPolls += 1;
            numberOfOpinionPollsByArea.augment(areaCode, 1);
            int thisNumberOfResponseScenarios = opinionPoll.getNumberOfResponseScenarios();
            numberOfResponseScenarios += thisNumberOfResponseScenarios;
            numberOfResponseScenariosByArea.augment(areaCode, thisNumberOfResponseScenarios);
            int thisNumberOfResultValues = opinionPoll.getNumberOfResultValues();
            numberOfResultValues += thisNumberOfResultValues;
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
            int month = opinionPoll.getEndDate().getMonthValue();
            numberOfOpinionPollsByMonth.augment(month, 1);
            numberOfOpinionPollsByMonthForThisArea.augment(month, 1);
            numberOfResponseScenariosByMonth.augment(month, thisNumberOfResponseScenarios);
            numberOfResponseScenariosByMonthForThisArea.augment(month, thisNumberOfResponseScenarios);
            numberOfResultValuesByMonth.augment(month, thisNumberOfResultValues);
            numberOfResultValuesByMonthForThisArea.augment(month, thisNumberOfResultValues);
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

    /**
     * Clears all the data in the store.
     */
    public static void clear() {
        mostRecentDateByArea = ModifiableMap.<String, LocalDate>empty();
        numberOfOpinionPolls = 0;
        numberOfOpinionPollsByArea = ModifiableIntegerMap.<String>empty();
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfOpinionPollsByMonth = ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfOpinionPollsByMonthByArea = ModifiableMap.empty();
        numberOfOpinionPollsByYear = ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
        numberOfOpinionPollsByYearByArea = ModifiableMap.empty();
        numberOfResponseScenarios = 0;
        numberOfResponseScenariosByArea = ModifiableIntegerMap.<String>empty();
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfResponseScenariosByMonth =
                ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfResponseScenariosByMonthByArea = ModifiableMap.empty();
        numberOfResponseScenariosByYear = ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
        numberOfResponseScenariosByYearByArea = ModifiableMap.empty();
        numberOfResultValues = 0;
        numberOfResultValuesByArea = ModifiableIntegerMap.<String>empty();
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfResultValuesByMonth = ModifiableSortedIntegerMap.<Integer>of(Comparator.naturalOrder(), 0, ALL_MONTHS);
        // TODO: Switch to UpdatableSortedIntegerMap
        numberOfResultValuesByMonthByArea = ModifiableMap.empty();
        numberOfResultValuesByYear = ModifiableSortedIntegerMap.<Integer>empty(Comparator.naturalOrder());
        numberOfResultValuesByYearByArea = ModifiableMap.empty();
    }

    /**
     * Returns the most recent date on an opinion poll for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return The most recent date on an opinion poll in the store.
     */
    public static LocalDate getMostRecentDate(final String areaCode) {
        return mostRecentDateByArea.get(areaCode);
    }

    /**
     * Returns the total number of opinion polls in the store.
     *
     * @return The total number of opinion polls in the store.
     */
    public static int getNumberOfOpinionPolls() {
        return numberOfOpinionPolls;
    }

    /**
     * Returns the number of opinion polls for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return The number of opinion polls for an area in the store.
     */
    public static int getNumberOfOpinionPolls(final String areaCode) {
        return numberOfOpinionPollsByArea.get(areaCode);
    }

    /**
     * Returns the number of opinion polls for an area and a given year in the store.
     *
     * @param areaCode The code for the area.
     * @param year     The year.
     * @return The number of opinion polls for an area and a given year in the store.
     */
    public static int getNumberOfOpinionPolls(final String areaCode, final int year) {
        return numberOfOpinionPollsByYearByArea.get(areaCode).get(year, 0);
    }

    /**
     * Returns a map with the number of opinion polls by month in the store.
     *
     * @return A map with the number of opinion polls by month in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByMonth() {
        return SortedIntegerMap.of(numberOfOpinionPollsByMonth);
    }

    /**
     * Returns a map with the number of opinion polls by month for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of opinion polls by month for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByMonth(final String areaCode) {
        return SortedIntegerMap.of(numberOfOpinionPollsByMonthByArea.get(areaCode));
    }

    /**
     * Returns a map with the number of opinion polls by year in the store.
     *
     * @return A map with the number of opinion polls by year in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByYear() {
        return SortedIntegerMap.of(numberOfOpinionPollsByYear);
    }

    /**
     * Returns a map with the number of opinion polls by year for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of opinion polls by year for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfOpinionPollsByYear(final String areaCode) {
        return SortedIntegerMap.of(numberOfOpinionPollsByYearByArea.get(areaCode));
    }

    /**
     * Returns the total number of response scenarios in the store.
     *
     * @return The total number of response scenarios in the store.
     */
    public static int getNumberOfResponseScenarios() {
        return numberOfResponseScenarios;
    }

    /**
     * Returns the number of response scenarios for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return The number of response scenarios for an area in the store.
     */
    public static int getNumberOfResponseScenarios(final String areaCode) {
        return numberOfResponseScenariosByArea.get(areaCode);
    }

    /**
     * Returns the number of response scenarios for an area and a given year in the store.
     *
     * @param areaCode The code for the area.
     * @param year     The year.
     * @return The number of response scenarios for an area and a given year in the store.
     */
    public static int getNumberOfResponseScenarios(final String areaCode, final int year) {
        return numberOfResponseScenariosByYearByArea.get(areaCode).get(year, 0);
    }

    /**
     * Returns a map with the number of response scenarios by month in the store.
     *
     * @return A map with the number of response scenarios by month in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByMonth() {
        return SortedIntegerMap.of(numberOfResponseScenariosByMonth);
    }

    /**
     * Returns a map with the number of response scenarios by month for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of response scenarios by month for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByMonth(final String areaCode) {
        return SortedIntegerMap.of(numberOfResponseScenariosByMonthByArea.get(areaCode));
    }

    /**
     * Returns a map with the number of response scenarios by year in the store.
     *
     * @return A map with the number of response scenarios by year in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByYear() {
        return SortedIntegerMap.of(numberOfResponseScenariosByYear);
    }

    /**
     * Returns a map with the number of response scenarios by year for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of response scenarios by year for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResponseScenariosByYear(final String areaCode) {
        return SortedIntegerMap.of(numberOfResponseScenariosByYearByArea.get(areaCode));
    }

    /**
     * Returns the total number of result values in the store.
     *
     * @return The total number of result values in the store.
     */
    public static int getNumberOfResultValues() {
        return numberOfResultValues;
    }

    /**
     * Returns the number of result values for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return The number of result values for an area in the store.
     */
    public static int getNumberOfResultValues(final String areaCode) {
        return numberOfResultValuesByArea.get(areaCode);
    }

    /**
     * Returns the number of result values for an area and a given year in the store.
     *
     * @param areaCode The code for the area.
     * @param year     The year.
     * @return The number of result values for an area and a given year in the store.
     */
    public static int getNumberOfResultValues(final String areaCode, final int year) {
        return numberOfResultValuesByYearByArea.get(areaCode).get(year, 0);
    }

    /**
     * Returns a map with the number of result values by month in the store.
     *
     * @return A map with the number of result values by month in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResultValuesByMonth() {
        return SortedIntegerMap.of(numberOfResultValuesByMonth);
    }

    /**
     * Returns a map with the number of result values by month for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of result values by month for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResultValuesByMonth(final String areaCode) {
        return SortedIntegerMap.of(numberOfResultValuesByMonthByArea.get(areaCode));
    }

    /**
     * Returns a map with the number of result values by year in the store.
     *
     * @return A map with the number of result values by year in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResultValuesByYear() {
        return SortedIntegerMap.of(numberOfResultValuesByYear);
    }

    /**
     * Returns a map with the number of result values by year for an area in the store.
     *
     * @param areaCode The code for the area.
     * @return A map with the number of result values by year for an area in the store.
     */
    public static SortedIntegerMap<Integer> getNumberOfResultValuesByYear(final String areaCode) {
        return SortedIntegerMap.of(numberOfResultValuesByYearByArea.get(areaCode));
    }

    /**
     * Returns whether there are opinion polls stored in the store for an area.
     *
     * @param areaCode The code for the area.
     * @return True if the store contains a poll for the area, false otherwise.
     */
    public static boolean hasOpinionPolls(final String areaCode) {
        return numberOfOpinionPollsByArea.containsKey(areaCode);
    }
}
