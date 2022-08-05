package net.filipvanlaenen.asapop.website;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.txhtmlj.Html;

/**
 * Class representing a website, i.e. a set of pages with content.
 */
public class Website {
    /**
     * The map holding the page names and their content.
     */
    private final Map<String, String> map = new HashMap<String, String>();

    /**
     * Returns the content of the website as a map with the names and the contents of the website pages.
     *
     * @return A map containing the names and the contents of the website pages.
     */
    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Sets the content of a page.
     *
     * @param path    The path to the page.
     * @param content The content of the page.
     */
    void put(final String path, final Html content) {
        map.put(path, content.asString());
    }
}
