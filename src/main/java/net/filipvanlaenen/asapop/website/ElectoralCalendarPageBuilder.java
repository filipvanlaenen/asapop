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
import net.filipvanlaenen.txhtmlj.Span;
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
     * Class representing an entry in the electoral calendar.
     */
    private final class Entry {
        /**
         * The area configuration for the entry.
         */
        private final AreaConfiguration areaConfiguration;
        /**
         * The election configuration for the entry.
         */
        private final ElectionConfiguration electionConfiguration;
        /**
         * The expected date.
         */
        private final ExpectedDate expectedDate;

        /**
         * Constructs an entry based on the election and area configuration.
         *
         * @param electionConfiguration The election configuration.
         * @param areaConfiguration     The area configuration.
         */
        private Entry(final ElectionConfiguration electionConfiguration, final AreaConfiguration areaConfiguration) {
            this.electionConfiguration = electionConfiguration;
            this.areaConfiguration = areaConfiguration;
            this.expectedDate = ExpectedDate.parse(electionConfiguration.getNextElectionDate());
        }

        /**
         * Returns the area code for the entry.
         *
         * @return The area code.
         */
        private String getAreaCode() {
            return areaConfiguration.getAreaCode();
        }

        /**
         * Returns the next election date.
         *
         * @return The next election date.
         */
        private ExpectedDate getNextElectionDate() {
            return expectedDate;
        }

        /**
         * Returns the type of the election as a class attribute value.
         *
         * @return The type of the election as a class attribute value.
         */
        private String getTypeAsClass() {
            return electionConfiguration.getType().toLowerCase().replaceAll(" ", "-");
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
        body.addElement(createHeader(PageBuilder.HeaderLink.ELECTORAL_CALENDAR));
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
                int dateResult = e1.getNextElectionDate().compareTo(e2.getNextElectionDate());
                if (dateResult != 0) {
                    return dateResult;
                }
                int areaResult = e1.getAreaCode().compareTo(e2.getAreaCode());
                if (areaResult != 0) {
                    return areaResult;
                } else {
                    return e1.getTypeAsClass().compareTo(e2.getTypeAsClass());
                }
            }
        });
        for (Entry entry : entries) {
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            ExpectedDate nextElectionDate = entry.getNextElectionDate();
            if (nextElectionDate.isApproximate()) {
                TD cell = new TD();
                cell.addElement(new Span(" ").clazz("around"));
                cell.addContent(" " + entry.getNextElectionDate().getDateString());
                areaTr.addElement(cell);
            } else if (nextElectionDate.isDeadline()) {
                TD cell = new TD();
                cell.addElement(new Span(" ").clazz("no-later-than"));
                cell.addContent(" " + entry.getNextElectionDate().getDateString());
                areaTr.addElement(cell);
            } else {
                areaTr.addElement(new TD(entry.getNextElectionDate().getDateString()));
            }
            areaTr.addElement(new TD(" ").clazz("_area_" + entry.getAreaCode()));
            areaTr.addElement(new TD(" ").clazz(entry.getTypeAsClass()));
        }
        body.addElement(createFooter());
        return html;
    }
}
