package net.filipvanlaenen.asapop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.website.Internationalization;
import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;

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
        terms.setTerms(new Term[] {});
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        sweden.setTranslations(Map.of("en", "Sweden"));
        websiteConfiguration.setAreaConfigurations(Set.of(sweden));
        Internationalization internationalization = new Internationalization(terms);
        CommandLineInterface.Command.addAreaTranslations(internationalization, websiteConfiguration);
        assertTrue(internationalization.containsKey("_area_se"));
        assertEquals("Sweden", internationalization.getTranslation("_area_se", Language.ENGLISH));
    }

    /**
     * Verifies that an area without translations doesn't add a term.
     */
    @Test
    public void areaWithoutTranslationShouldNotAddToTerms() {
        Terms terms = new Terms();
        terms.setTerms(new Term[] {});
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        websiteConfiguration.setAreaConfigurations(Set.of(sweden));
        Internationalization internationalization = new Internationalization(terms);
        CommandLineInterface.Command.addAreaTranslations(internationalization, websiteConfiguration);
        assertFalse(internationalization.containsKey("_area_se"));
    }

    /**
     * Verifies that the name of a subdivision is added to the terms if translations are provided.
     */
    @Test
    public void translationOfASubdivisionShouldBeAddedToTerms() {
        Terms terms = new Terms();
        terms.setTerms(new Term[] {});
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration belgium = new AreaConfiguration();
        belgium.setAreaCode("be");
        AreaSubdivisionConfiguration flanders = new AreaSubdivisionConfiguration();
        flanders.setAreaCode("vlg");
        flanders.setTranslations(Map.of("en", "Flanders"));
        AreaSubdivisionConfiguration[] subdivisions = new AreaSubdivisionConfiguration[] {flanders};
        belgium.setSubdivisions(subdivisions);
        websiteConfiguration.setAreaConfigurations(Set.of(belgium));
        Internationalization internationalization = new Internationalization(terms);
        CommandLineInterface.Command.addAreaTranslations(internationalization, websiteConfiguration);
        assertTrue(internationalization.containsKey("_area_be-vlg"));
        assertEquals("Flanders", internationalization.getTranslation("_area_be-vlg", Language.ENGLISH));
    }
}
