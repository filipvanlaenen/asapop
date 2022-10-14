package net.filipvanlaenen.asapop.website;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
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
    private class Entry {
        private final AreaConfiguration areaConfiguration;
        private final ElectionConfiguration electionConfiguration;

        public Entry(ElectionConfiguration electionConfiguration, AreaConfiguration areaConfiguration) {
            this.electionConfiguration = electionConfiguration;
            this.areaConfiguration = areaConfiguration;
        }

        public String getAreaCode() {
            return areaConfiguration.getAreaCode();
        }

        public String getNextElectionDate() {
            return electionConfiguration.getNextElectionDate();
        }

        public String getTypeAsClass() {
            return electionConfiguration.getType().toLowerCase();
        }
    }

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
        List<Entry> entries = new ArrayList<Entry>();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            if (areaConfiguration.getAreaCode() != null && areaConfiguration.getElectionConfigurations() != null) {
                for (ElectionConfiguration electionConfiguration : areaConfiguration.getElectionConfigurations()) {
                    if (electionConfiguration.getNextElectionDate() != null) {
                        entries.add(new Entry(electionConfiguration, areaConfiguration));
                    }
                }
            }
        }
        entries.sort(new Comparator<Entry>() {
            @Override
            public int compare(final Entry e1, final Entry e2) {
                return e1.getNextElectionDate().compareTo(e2.getNextElectionDate());
            }
        });
        for (Entry entry : entries) {
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            areaTr.addElement(new TD(entry.getNextElectionDate()));
            areaTr.addElement(new TD(" ").clazz("_area_" + entry.getAreaCode()));
            areaTr.addElement(new TD(" ").clazz(entry.getTypeAsClass()));
        }
        body.addElement(createFooter());
        return html;
    }
}
