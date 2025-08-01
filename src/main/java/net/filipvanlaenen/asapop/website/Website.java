package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.txhtmlj.Html;

/**
 * Class representing a website, i.e. a set of pages with content.
 */
public class Website {
    /**
     * The map holding the page paths and their content.
     */
    private final ModifiableMap<Path, String> map = ModifiableMap.<Path, String>empty();

    /**
     * Returns the content of the website as a map with the paths and the contents of the website pages.
     *
     * @return A map containing the paths and the contents of the website pages.
     */
    public Map<Path, String> asMap() {
        return Map.of(map);
    }

    /**
     * Sets the content of a page.
     *
     * @param path    The path to the page.
     * @param content The content of the page.
     */
    void put(final String path, final Html content) {
        put(path, content.asString());
    }

    /**
     * Sets the content of a page.
     *
     * @param path    The path to the page.
     * @param content The content of the page.
     */
    void put(final String path, final String content) {
        map.put(Paths.get(path), content);
    }

    /**
     * Sets the content of a number of pages.
     *
     * @param pages The pages to be added with their paths.
     */
    void putAll(final Map<Path, String> pages) {
        map.putAll(pages);
    }
}
