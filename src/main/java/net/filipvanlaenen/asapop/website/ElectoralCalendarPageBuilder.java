package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.util.Comparator;

import net.filipvanlaenen.asapop.model.Area;
import net.filipvanlaenen.asapop.model.ElectedBody;
import net.filipvanlaenen.asapop.model.ElectedOffice;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.ModifiableSortedMap;
import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.P;
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
    private record ElectionEntry(ElectionDate electionDate, String areaId, String electionId) {
    }

    private static final class ElectionEntryComparator implements Comparator<ElectionEntry> {
        @Override
        public int compare(ElectionEntry ee1, ElectionEntry ee2) {
            int dateResult = ee1.electionDate().compareTo(ee2.electionDate());
            if (dateResult != 0) {
                return dateResult;
            }
            int areaResult = ee1.areaId().compareTo(ee2.areaId());
            if (areaResult != 0) {
                return areaResult;
            } else {
                return ee1.electionId().compareTo(ee2.electionId());
            }
        }
    }

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
    ElectoralCalendarPageBuilder(final WebsiteConfiguration websiteConfiguration, final LocalDate now) {
        super(websiteConfiguration);
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
        P p = new P();
        p.addElement(new Span(" ").clazz("import-this-calendar-as-an"));
        p.addContent(" ");
        p.addElement(new A(" ").clazz("icalendar-file").href("calendar.ical"));
        p.addContent(".");
        section.addElement(p);
        Table table = new Table();
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("date"));
        tr.addElement(new TH(" ").clazz("country"));
        tr.addElement(new TH(" ").clazz("election"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        ModifiableSortedMap<ElectionEntry, TR> rowMap = ModifiableSortedMap.empty(new ElectionEntryComparator());
        for (Area area : Area.getAll()) {
            for (ElectedBody electedBody : area.getElectedBodies()) {
                ElectionDate nextElectionDate = electedBody.getNextElectionDate(now);
                if (nextElectionDate != null) {
                    TR electionRow = new TR();
                    TD dateCell = new TD();
                    electionRow.addElement(dateCell);
                    String qualifierClass = nextElectionDate.getQualifierTermKey();
                    if (qualifierClass != null) {
                        dateCell.addElement(new Span(" ").clazz(qualifierClass));
                        dateCell.addContent(" ");
                    }
                    dateCell.addContent(nextElectionDate.getDateString());
                    TD areaCell = new TD();
                    electionRow.addElement(areaCell);
                    String areaId = area.getId();
                    areaCell.addElement(new A(" ").clazz("_area_" + areaId).href(areaId + "/index.html"));
                    TD electionTypeCell = new TD();
                    electionRow.addElement(electionTypeCell);
                    electionTypeCell
                            .addElement(new Span(" ").clazz("_electedBody_" + areaId + "_" + electedBody.getId()));
                    electionTypeCell.addContent(" (");
                    electionTypeCell.addContent(electedBody.getAllProperNamesConcatenated());
                    electionTypeCell.addContent(")");
                    rowMap.add(new ElectionEntry(nextElectionDate, areaId, electedBody.getId()), electionRow);
                }
            }
            for (ElectedOffice electedOffice : area.getElectedOffices()) {
                ElectionDate nextElectionDate = electedOffice.getNextElectionDate(now);
                if (nextElectionDate != null) {
                    TR electionRow = new TR();
                    TD dateCell = new TD();
                    electionRow.addElement(dateCell);
                    String qualifierClass = nextElectionDate.getQualifierTermKey();
                    if (qualifierClass != null) {
                        dateCell.addElement(new Span(" ").clazz(qualifierClass));
                        dateCell.addContent(" ");
                    }
                    dateCell.addContent(nextElectionDate.getDateString());
                    TD areaCell = new TD();
                    electionRow.addElement(areaCell);
                    String areaId = area.getId();
                    areaCell.addElement(new A(" ").clazz("_area_" + areaId).href(areaId + "/index.html"));
                    TD electionTypeCell = new TD();
                    electionRow.addElement(electionTypeCell);
                    electionTypeCell
                            .addElement(new Span(" ").clazz("_electedOffice_" + areaId + "_" + electedOffice.getId()));
                    electionTypeCell.addContent(" (");
                    electionTypeCell.addContent(electedOffice.getAllProperNamesConcatenated());
                    electionTypeCell.addContent(")");
                    rowMap.add(new ElectionEntry(nextElectionDate, areaId, electedOffice.getId()), electionRow);
                }
            }
        }
        for (TR rowTR : rowMap.getValues()) {
            tBody.addElement(rowTR);
        }
        body.addElement(createFooter());
        return html;
    }
}
