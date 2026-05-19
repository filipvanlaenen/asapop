package net.filipvanlaenen.asapop.model;

import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;

/**
 * Class representing an elected body.
 */
public class ElectedBody extends ElectedPublicInstitution {
    /**
     * Constructor defining a new elected body.
     *
     * @param id              The ID.
     * @param properNames     The proper names.
     * @param translatedNames The translated names.
     * @param elections       The elections.
     * @param defunct         Whether the elected body is defunct.
     */
    public ElectedBody(final String id, final Map<String, String> properNames,
            final Map<Language, String> translatedNames,
            final OrderedCollection<OrderedValueCollection<ElectionDate>> elections, final boolean defunct) {
        super(id, properNames, translatedNames, elections, defunct);
    }
}
