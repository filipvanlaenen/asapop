package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Section;

/**
 * Class building the page with an overview and links to the CSV files.
 */
class CsvFilesPageBuilder extends PageBuilder {
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     */
    CsvFilesPageBuilder(final WebsiteConfiguration websiteConfiguration) {
        this.websiteConfiguration = websiteConfiguration;
    }

    /**
     * Builds the content of the CSV files page.
     *
     * @return The content of the CSV files page
     */
    Html build() {
        Html html = new Html();
        html.addElement(createHead());
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader());
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("csv-files"));
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            CsvConfiguration csvConfiguration = areaConfiguration.getCsvConfiguration();
            if (csvConfiguration != null) {
                section.addElement(new A(areaCode).href("_csv/" + areaCode + ".csv"));
            }
        }
        body.addElement(createFooter());
        return html;
    }
}
