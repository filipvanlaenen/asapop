package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.H2;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.LI;
import net.filipvanlaenen.txhtmlj.P;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.TBody;
import net.filipvanlaenen.txhtmlj.TH;
import net.filipvanlaenen.txhtmlj.THead;
import net.filipvanlaenen.txhtmlj.TR;
import net.filipvanlaenen.txhtmlj.Table;
import net.filipvanlaenen.txhtmlj.UL;

class AreaIndexPagesBuilder extends PageBuilder {
    /**
     * Class representing an entry in the electoral calendar.
     */
    private final class Entry {
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
         */
        private Entry(final ElectionConfiguration electionConfiguration) {
            this.electionConfiguration = electionConfiguration;
            this.expectedDate = ExpectedDate.parse(electionConfiguration.getNextElectionDate());
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

    AreaIndexPagesBuilder(final WebsiteConfiguration websiteConfiguration) {
        this.websiteConfiguration = websiteConfiguration;
    }

    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaCode != null) {
                result.put(Paths.get(areaCode, "index.html"), createAreaIndexPage(areaConfiguration));
            }
        }
        return result;
    }

    String createAreaIndexPage(final AreaConfiguration areaConfiguration) {
        Html html = new Html();
        html.addElement(createHead(1));
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader(1));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("_area_" + areaConfiguration.getAreaCode()));
        section.addElement(new H2(" ").clazz("upcoming-elections"));
        Set<ElectionConfiguration> electionConfigurations = areaConfiguration.getElectionConfigurations();
        if (electionConfigurations == null || electionConfigurations.isEmpty()) {
            P p = new P();
            section.addElement(p);
            p.addElement(new Span(" ").clazz("none"));
        } else {
            UL ul = new UL();
            section.addElement(ul);
            List<Entry> entries = new ArrayList<Entry>();
            for (ElectionConfiguration electionConfiguration : electionConfigurations) {
                if (electionConfiguration.getNextElectionDate() != null) {
                    entries.add(new Entry(electionConfiguration));
                }
            }
            entries.sort(new Comparator<Entry>() {
                @Override
                public int compare(final Entry e1, final Entry e2) {
                    int dateResult = e1.getNextElectionDate().compareTo(e2.getNextElectionDate());
                    if (dateResult != 0) {
                        return dateResult;
                    } else {
                        return e1.getTypeAsClass().compareTo(e2.getTypeAsClass());
                    }
                }
            });
            for (Entry entry : entries) {
                LI li = new LI();
                ul.addElement(li);
                ExpectedDate nextElectionDate = entry.getNextElectionDate();
                if (nextElectionDate.isApproximate()) {
                    li.addElement(new Span(" ").clazz("around"));
                    li.addContent(" ");
                } else if (nextElectionDate.isDeadline()) {
                    li.addElement(new Span(" ").clazz("no-later-than"));
                    li.addContent(" ");
                }
                li.addContent(nextElectionDate.getDateString());
                li.addContent(": ");
                li.addElement(new Span(" ").clazz(entry.getTypeAsClass()));
            }
        }
        section.addElement(new H2(" ").clazz("latest-opinion-polls"));
        Table table = new Table();
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("fieldwork-period"));
        tr.addElement(new TH(" ").clazz("polling-firm-commissioner"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        body.addElement(createFooter());
        return html.asString();
    }
}
