package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Class building the website.
 */
public class WebsiteBuilder {
    /**
     * Builds the website.
     *
     * @return The website
     */
    public Website build() {
        Website website = new Website();
        website.put("index.html", buildIndexPageContent());
        return website;
    }

    /**
     * Builds the content of the index page.
     *
     * @return The content of the index page
     */
    Html buildIndexPageContent() {
        Html html = new Html();
        Head head = new Head();
        html.addElement(head);
        head.addElement(new Title("ASAPOP Website"));
        StringBuffer style = new StringBuffer();
        style.append(".privacy-note { text-align: center; }");
        head.addElement(new Style(style.toString()));
        Body body = new Body();
        html.addElement(body);
        body.addElement(new Div("Privacy note: this website is hosted on Google Cloud.").clazz("privacy-note"));
        return html;
    }
}
