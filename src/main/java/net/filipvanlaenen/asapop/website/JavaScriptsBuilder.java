package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.yaml.Terms;

public class JavaScriptsBuilder {
    private final Terms terms;

    public JavaScriptsBuilder(final Terms terms) {
        this.terms = terms;
    }

    public Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        result.put(Paths.get("_js", "internationalization.js"), new InternationalizationScriptBuilder(terms).build());
        return result;
    }
}
