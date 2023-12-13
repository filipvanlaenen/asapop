package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.A;
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
     * Comparator class to sort the elections.
     */
    static final class ElectionComparator implements Comparator<Election> {
        /**
         * Today's day.
         */
        private final LocalDate now;

        /**
         * Constructor with the current day as its parameter.
         *
         * @param now Today's date.
         */
        ElectionComparator(final LocalDate now) {
            this.now = now;
        }

        @Override
        public int compare(final Election e1, final Election e2) {
            int dateResult = e1.getNextElectionDate(now).compareTo(e2.getNextElectionDate(now));
            if (dateResult != 0) {
                return dateResult;
            }
            int areaResult = e1.areaCode().compareTo(e2.areaCode());
            if (areaResult != 0) {
                return areaResult;
            } else {
                return e1.electionType().compareTo(e2.electionType());
            }
        }
    }

    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * Today's day.
     */
    private final LocalDate now;

    /**
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param elections            The elections.
     * @param now                  Today's day.
     */
    ElectoralCalendarPageBuilder(final WebsiteConfiguration websiteConfiguration, final Elections elections,
            final LocalDate now) {
        super(websiteConfiguration);
        this.elections = elections;
        this.now = now;
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
        List<Election> nextElections = new ArrayList<Election>(elections.getNextElections(now));
        nextElections.sort(new ElectionComparator(now));
        for (Election nextElection : nextElections) {
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            ElectionDate nextElectionDate = nextElection.getNextElectionDate(now);
            TD cell = new TD();
            areaTr.addElement(cell);
            String qualifierClass = nextElectionDate.getQualifierTermKey();
            if (qualifierClass != null) {
                cell.addElement(new Span(" ").clazz(qualifierClass));
                cell.addContent(" ");
            }
            cell.addContent(nextElectionDate.getDateString());
            TD td = new TD();
            areaTr.addElement(td);
            td.addElement(
                    new A(" ").clazz("_area_" + nextElection.areaCode()).href(nextElection.areaCode() + "/index.html"));
            areaTr.addElement(new TD(" ").clazz(nextElection.electionType().getTermKey()));
        }
        body.addElement(createFooter());
        return html;
    }
}
