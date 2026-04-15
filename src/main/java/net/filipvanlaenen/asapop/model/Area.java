package net.filipvanlaenen.asapop.model;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;

public class Area {
    private static final ModifiableMap<String, Area> instances = ModifiableMap.<String, Area>empty();
    private final String id;
    private final Collection<ElectedBody> electedBodies;
    private final Collection<ElectedOffice> electedOffices;

    public static void add(Area area) {
        instances.add(area.getId(), area);
    }

    public static Collection<Area> getAll() {
        return instances.getValues();
    }

    public Area(final String id, final Collection<ElectedBody> electedBodies,
            final Collection<ElectedOffice> electedOffices) {
        this.id = id;
        this.electedBodies = Collection.of(electedBodies);
        this.electedOffices = Collection.of(electedOffices);
    }

    public Collection<ElectedBody> getElectedBodies() {
        return electedBodies;
    }

    public Collection<ElectedOffice> getElectedOffices() {
        return electedOffices;
    }

    public String getId() {
        return id;
    }
}
