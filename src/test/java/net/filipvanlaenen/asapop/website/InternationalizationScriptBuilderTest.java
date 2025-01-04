package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>InternationalizationScriptBuilder</code> class.
 */
public class InternationalizationScriptBuilderTest {
    /**
     * Creates the internationalization dictionary.
     *
     * @return The internationalization dictionary.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        internationalization.addTranslations("country", Map.of("en", "Country"));
        internationalization.addTranslations("date", Map.of("en", "Date"));
        internationalization.addTranslations("language", Map.of("en", "Language"));
        internationalization.addTranslations("main-page", Map.of("en", "Main Page"));
        return internationalization;
    }

    /**
     * Verifies that the script contains a switch statement for English.
     */
    @Test
    public void scriptShouldContainSwitchForEnglishLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        assertTrue(builder.build().contains("case \"en\":"));
    }

    /**
     * Verifies that the switch statement for German comes before the switch statement for English.
     */
    @Test
    public void switchStatementForGermanShouldComeBeforeSwitchStatementForEnglish() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        String pageContent = builder.build();
        assertTrue(pageContent.indexOf("case \"de\":") < pageContent.indexOf("case \"en\":"));
    }

    /**
     * Verifies that the script contains a load statement for the English term for language.
     */
    @Test
    public void scriptShouldContainEnglishTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        assertTrue(builder.build().contains("$('.language').text(\"Language\");"));
    }

    /**
     * Verifies that the script contains a substitute load statement for the Dutch term for language.
     */
    @Test
    public void scriptShouldContainSubstituteDutchTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        assertTrue(builder.build().contains("$('.language').text(\"[Language]\");"));
    }

    /**
     * Verifies that the load statements for the terms are sorted by the therm's keys.
     */
    @Test
    public void loadStatementsForTermsShouldBeSortedByTermsKeys() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        String pageContent = builder.build();
        assertTrue(pageContent.indexOf("$('.country').text(\"Country\");") < pageContent
                .indexOf("$('.date').text(\"Date\");"));
        assertTrue(pageContent.indexOf("$('.date').text(\"Date\");") < pageContent
                .indexOf("$('.language').text(\"Language\");"));
        assertTrue(pageContent.indexOf("$('.language').text(\"Language\");") < pageContent
                .indexOf("$('.main-page').text(\"Main Page\");"));
    }

    /**
     * Verifies that the script contains the <code>initializeLanguage</code> function.
     */
    @Test
    public void scriptShouldContainInitializeLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        assertTrue(builder.build().contains("initializeLanguage()"));
    }

    /**
     * Verifies that the script contains the <code>loadLanguage</code> function.
     */
    @Test
    public void scriptShouldContainLoadLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createInternationalization());
        assertTrue(builder.build().contains("loadLanguage()"));
    }
}
