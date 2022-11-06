package net.filipvanlaenen.asapop.website;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
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
        body.addElement(createHeader(PageBuilder.HeaderLink.CSV_FILES));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("csv-files"));
        Table table = new Table();
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("csv-file"));
        tr.addElement(new TH(" ").clazz("country"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        List<AreaConfiguration> sortedAreaConfigurations = websiteConfiguration.getAreaConfigurations().stream()
                .filter(ac -> ac.getAreaCode() != null && ac.getCsvConfiguration() != null)
                .collect(Collectors.toList());
        sortedAreaConfigurations.sort(new Comparator<AreaConfiguration>() {
            @Override
            public int compare(final AreaConfiguration ac0, final AreaConfiguration ac1) {
                return ac0.getAreaCode().compareTo(ac1.getAreaCode());
            }
        });
        for (AreaConfiguration areaConfiguration : sortedAreaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            TD tdCsvLink = new TD();
            areaTr.addElement(tdCsvLink);
            tdCsvLink.addElement(new A(areaCode + ".csv").href("_csv/" + areaCode + ".csv"));
            TD tdAreaName = new TD();
            areaTr.addElement(tdAreaName);
            tdAreaName.addElement(new A(" ").clazz("_area_" + areaCode).href(areaCode + "/index.html"));
        }
        body.addElement(createFooter());
        return html;
    }
}
