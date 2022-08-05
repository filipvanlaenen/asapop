package net.filipvanlaenen.asapop.website;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.txhtmlj.Html;

public class Website {
    private final Map<String, String> map = new HashMap<String, String>();

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(map);
    }

    void add(String path, Html content) {
        map.put(path, content.asString());
    }
}
