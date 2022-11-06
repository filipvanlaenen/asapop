package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Section;

class AreaIndexPagesBuilder extends PageBuilder {
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    AreaIndexPagesBuilder(final WebsiteConfiguration websiteConfiguration) {
        this.websiteConfiguration = websiteConfiguration;
    }

    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaCode != null) {
                result.put(Paths.get(areaCode, "index.html"), createAreaIndexPage(areaCode));
            }
        }
        return result;
    }

    String createAreaIndexPage(final String areaCode) {
        Html html = new Html();
        html.addElement(createHead(1));
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader(1));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("_area_" + areaCode));
        body.addElement(createFooter());
        return html.asString();
    }
}
