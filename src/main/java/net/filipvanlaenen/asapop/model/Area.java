package net.filipvanlaenen.asapop.model;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;

public class Area {
    private static final ModifiableMap<String, Area> instances = ModifiableMap.<String, Area>empty();
    private final String id;
    private final Collection<ElectedBody> electedBodies;

    public static void add(Area area) {
        instances.add(area.getId(), area);
    }

    public static Collection<Area> getAll() {
        return instances.getValues();
    }

    public Area(final String id, final Collection<ElectedBody> electedBodies) {
        this.id = id;
        this.electedBodies = Collection.of(electedBodies);
    }

    public Collection<ElectedBody> getElectedBodies() {
        return electedBodies;
    }

    public String getId() {
        return id;
    }
}
