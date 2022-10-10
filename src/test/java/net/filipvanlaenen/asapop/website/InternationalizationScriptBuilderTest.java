package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Unit tests on the <code>InternationalizationScriptBuilder</code> class.
 */
public class InternationalizationScriptBuilderTest {
    /**
     * Creates a set of terms for the internationalization script builder.
     *
     * @return A set of terms for the internationalization script builder.
     */
    private Terms createTerms() {
        Terms terms = new Terms();
        Term termCountry = new Term();
        termCountry.setKey("country");
        termCountry.setTranslations(Map.of("en", "Country"));
        Term termLanguage = new Term();
        termLanguage.setKey("language");
        termLanguage.setTranslations(Map.of("en", "Language"));
        Term termMainPage = new Term();
        termMainPage.setKey("main-page");
        termMainPage.setTranslations(Map.of("en", "Main Page"));
        terms.setTerms(Set.of(termCountry, termLanguage, termMainPage));
        return terms;
    }

    /**
     * Verifies that the script contains a switch statement for English.
     */
    @Test
    public void scriptShouldContainSwitchForEnglishLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("case \"en\":"));
    }

    /**
     * Verifies that the switch statement for German comes before the switch statement for English.
     */
    @Test
    public void switchStatementForGermanShouldComeBeforeSwitchStatementForEnglish() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        String pageContent = builder.build();
        assertTrue(pageContent.indexOf("case \"de\":") < pageContent.indexOf("case \"en\":"));
    }

    /**
     * Verifies that the script contains a load statement for the English term for language.
     */
    @Test
    public void scriptShouldContainEnglishTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("$('.language').text(\"Language\");"));
    }

    /**
     * Verifies that the script contains a substitute load statement for the Dutch term for language.
     */
    @Test
    public void scriptShouldContainSubstituteDutchTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("$('.language').text(\"[Language]\");"));
    }

    /**
     * Verifies that the load statement for country comes before the load statement for language.
     */
    @Test
    public void englishTermForCountryShouldComeBeforeEnglishTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        String pageContent = builder.build();
        assertTrue(pageContent.indexOf("$('.country').text(\"Country\");") < pageContent
                .indexOf("$('.language').text(\"Language\");"));
        assertTrue(pageContent.indexOf("$('.language').text(\"Language\");") < pageContent
                .indexOf("$('.main-page').text(\"Main Page\");"));
    }

    /**
     * Verifies that the script contains the <code>initializeLanguage</code> function.
     */
    @Test
    public void scriptShouldContainInitializeLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("initializeLanguage()"));
    }

    /**
     * Verifies that the script contains the <code>loadLanguage</code> function.
     */
    @Test
    public void scriptShouldContainLoadLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("loadLanguage()"));
    }
}
