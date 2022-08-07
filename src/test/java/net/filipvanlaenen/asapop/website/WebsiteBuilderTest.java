package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * Verifies that the index page is built correctly.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectly() {
        String expected = createIndexPageContent();
        assertEquals(expected, new WebsiteBuilder().buildIndexPageContent().asString());
    }

    /**
     * Verifies that the website is build correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("index.html", createIndexPageContent());
        assertEquals(map, new WebsiteBuilder().build().asMap());
    }

    /**
     * Creates the content of the index page.
     *
     * @return The content of the index page.
     */
    private String createIndexPageContent() {
        Html html = new Html();
        Head head = new Head();
        html.addElement(head);
        head.addElement(new Title("ASAPOP Website"));
        StringBuffer style = new StringBuffer();
        style.append(".privacy-note { text-align: center; }");
        head.addElement(new Style(style.toString()));
        Body body = new Body();
        html.addElement(body);
        Div privacyNote = new Div().clazz("privacy-note");
        privacyNote.addContent("Privacy note: this website is hosted on Google Cloud.");
        body.addElement(privacyNote);
        return html.asString();
    }
}
