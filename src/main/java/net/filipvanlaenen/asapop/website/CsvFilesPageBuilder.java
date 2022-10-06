package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.TBody;
import net.filipvanlaenen.txhtmlj.TD;
import net.filipvanlaenen.txhtmlj.TH;
import net.filipvanlaenen.txhtmlj.THead;
import net.filipvanlaenen.txhtmlj.TR;
import net.filipvanlaenen.txhtmlj.Table;

/**
 * Class building the page with an overview and links to the CSV files.
 */
final class CsvFilesPageBuilder extends PageBuilder {
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
        body.addElement(createHeader(true));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("csv-files"));
        Table table = new Table();
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("country"));
        tr.addElement(new TH(" ").clazz("csv-file"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            CsvConfiguration csvConfiguration = areaConfiguration.getCsvConfiguration();
            if (csvConfiguration != null) {
                TR areaTr = new TR();
                tBody.addElement(areaTr);
                areaTr.addElement(new TD(" ").clazz("_area_" + areaCode));
                TD td = new TD();
                areaTr.addElement(td);
                td.addElement(new A(areaCode + ".csv").href("_csv/" + areaCode + ".csv"));
            }
        }
        body.addElement(createFooter());
        return html;
    }
}