package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.P;
import net.filipvanlaenen.txhtmlj.Title;

public class WebsiteBuilder {
    public Website build() {
        Website website = new Website();
        website.put("index.html", buildIndexPage());
        return website;
    }

    private Html buildIndexPage() {
        Html html = new Html();
        Head head = new Head();
        html.addElement(head);
        head.addElement(new Title("ASAPOP Website"));
        Body body = new Body();
        html.addElement(body);
        body.addElement(new P("Privacy note: this website is hosted on Google Cloud."));
        return html;
    }
}
