package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * Creates a set of terms for the internationalization script builder.
     *
     * @return A set of terms for the internationalization script builder.
     */
    private Terms createTerms() {
        Terms terms = new Terms();
        Term term = new Term();
        term.setKey("language");
        term.setTranslations(Map.of("en", "Language"));
        terms.setTerms(Set.of(term));
        return terms;
    }

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        sweden.setNextElectionDate("2022-09-11");
        AreaConfiguration latvia = new AreaConfiguration();
        latvia.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        latvia.setNextElectionDate("2022-10-01");
        AreaConfiguration bulgaria = new AreaConfiguration();
        bulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        bulgaria.setNextElectionDate("2022-10-02");
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, latvia, bulgaria));
        return websiteConfiguration;
    }

    /**
     * Verifies that the website is build correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        Map<Path, String> map = new HashMap<Path, String>();
        map.put(Paths.get("index.html"), new IndexPageBuilder(createWebsiteConfiguration()).build().asString());
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createTerms()).build());
        WebsiteBuilder builder = new WebsiteBuilder(createWebsiteConfiguration(), createTerms(), Collections.EMPTY_MAP);
        assertEquals(map, builder.build().asMap());
    }

}
