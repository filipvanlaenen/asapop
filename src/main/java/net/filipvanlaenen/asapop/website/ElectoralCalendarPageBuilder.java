package net.filipvanlaenen.asapop.website;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
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
 * Class building the page with the electoral calendar.
 */
final class ElectoralCalendarPageBuilder extends PageBuilder {
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     */
    ElectoralCalendarPageBuilder(final WebsiteConfiguration websiteConfiguration) {
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
        section.addElement(new H1(" ").clazz("electoral-calendar"));
        Table table = new Table();
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("date"));
        tr.addElement(new TH(" ").clazz("country"));
        tr.addElement(new TH(" ").clazz("election-type"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        List<AreaConfiguration> sortedAreaConfigurations = websiteConfiguration.getAreaConfigurations().stream()
                .filter(ac -> ac.getAreaCode() != null && ac.getNextElectionDate() != null)
                .collect(Collectors.toList());
        sortedAreaConfigurations.sort(new Comparator<AreaConfiguration>() {
            @Override
            public int compare(final AreaConfiguration ac0, final AreaConfiguration ac1) {
                return ac0.getNextElectionDate().compareTo(ac1.getNextElectionDate());
            }
        });
        for (AreaConfiguration areaConfiguration : sortedAreaConfigurations) {
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            areaTr.addElement(new TD(areaConfiguration.getNextElectionDate()));
            areaTr.addElement(new TD(" ").clazz("_area_" + areaConfiguration.getAreaCode()));
            areaTr.addElement(new TD(" ").clazz("parliament"));
        }
        body.addElement(createFooter());
        return html;
    }
}
