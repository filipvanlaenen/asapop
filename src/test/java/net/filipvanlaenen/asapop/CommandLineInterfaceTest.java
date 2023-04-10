package net.filipvanlaenen.asapop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>CommandLineInterface</code> class.
 */
public class CommandLineInterfaceTest {
    /**
     * Verifies that the name of an area is added to the terms if translations are provided.
     */
    @Test
    public void translationOfAnAreaShouldBeAddedToTerms() {
        Terms terms = new Terms();
        terms.setTerms(new HashSet<Term>());
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        sweden.setTranslations(Map.of("en", "Sweden"));
        websiteConfiguration.setAreaConfigurations(Set.of(sweden));
        CommandLineInterface.Command.addAreaTerms(terms, websiteConfiguration);
        assertEquals(1, terms.getTerms().size());
        Term term = terms.getTerms().iterator().next();
        assertEquals("_area_se", term.getKey());
        assertEquals("Sweden", term.getTranslations().get("en"));
    }

    /**
     * Verifies that an area without translations doesn't add a term.
     */
    @Test
    public void areaWithoutTranslationShouldNotAddToTerms() {
        Terms terms = new Terms();
        terms.setTerms(new HashSet<Term>());
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        websiteConfiguration.setAreaConfigurations(Set.of(sweden));
        CommandLineInterface.Command.addAreaTerms(terms, websiteConfiguration);
        assertEquals(0, terms.getTerms().size());
    }
}
