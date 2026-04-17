package net.filipvanlaenen.asapop.model;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Class representing an area.
 */
public class Area {
    /**
     * A map with all the instances.
     */
    private static final ModifiableMap<String, Area> INSTANCES = ModifiableMap.<String, Area>empty();

    /**
     * Adds an area to the map of instances.
     *
     * @param area The area to be added.
     */
    public static void add(final Area area) {
        INSTANCES.add(area.getId(), area);
    }

    /**
     * Returns the area with the provided ID.
     *
     * @param id The ID.
     * @return The area with the provided ID.
     */
    public static Area get(final String id) {
        return INSTANCES.get(id);
    }

    /**
     * Returns all instances.
     *
     * @return A collection with all the instances.
     */
    public static Collection<Area> getAll() {
        return INSTANCES.getValues();
    }

    /**
     * The ID.
     */
    private final String id;
    /**
     * The elected bodies of the area.
     */
    private final Collection<ElectedBody> electedBodies;
    /**
     * The elected offices of the aread.
     */
    private final Collection<ElectedOffice> electedOffices;

    /**
     * Constructor taking an ID, a collection with the elected bodies and a collection with the elected offices as its
     * parameters.
     *
     * @param id             The ID.
     * @param electedBodies  The elected bodies.
     * @param electedOffices The elected offices.
     */
    public Area(final String id, final Collection<ElectedBody> electedBodies,
            final Collection<ElectedOffice> electedOffices) {
        this.id = id;
        this.electedBodies = Collection.of(electedBodies);
        this.electedOffices = Collection.of(electedOffices);
    }

    /**
     * Returns the elected bodies.
     *
     * @return A collection with the elected bodies.
     */
    public Collection<ElectedBody> getElectedBodies() {
        return electedBodies;
    }

    /**
     * Returns the elected offices.
     *
     * @return A collection with the elected offices.
     */
    public Collection<ElectedOffice> getElectedOffices() {
        return electedOffices;
    }

    /**
     * Returns the ID.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }
}
