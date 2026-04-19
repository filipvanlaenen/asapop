package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.util.Comparator;

import net.filipvanlaenen.asapop.model.Area;
import net.filipvanlaenen.asapop.model.ElectedBody;
import net.filipvanlaenen.asapop.model.ElectedOffice;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.ModifiableSortedMap;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.LI;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.UL;

/**
 * Class building the index page.
 */
final class IndexPageBuilder extends PageBuilder {
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
     * @param now                  The today's day.
     */
    IndexPageBuilder(final WebsiteConfiguration websiteConfiguration, final LocalDate now) {
        super(websiteConfiguration);
        this.now = now;
    }

    /**
     * Builds the content of the index page.
     *
     * @return The content of the index page
     */
    Html build() {
        Html html = new Html();
        html.addElement(createHead());
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader(PageBuilder.HeaderLink.INDEX));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("upcoming-elections"));
        UL ul = new UL();
        section.addElement(ul);
        ModifiableSortedMap<ElectionEntry, LI> rowMap = ModifiableSortedMap.empty(new ElectionEntryComparator());
        for (Area area : Area.getAll()) {
            String areaId = area.getId();
            for (ElectedBody electedBody : area.getElectedBodies()) {
                ElectionDate nextElectionDate = electedBody.getNextElectionDate(now);
                if (nextElectionDate != null) {
                    rowMap.add(new ElectionEntry(nextElectionDate, areaId, electedBody.getId()),
                            createUpcomingElectionLi(electedBody, areaId));
                }
            }
            for (ElectedOffice electedOffice : area.getElectedOffices()) {
                ElectionDate nextElectionDate = electedOffice.getNextElectionDate(now);
                if (nextElectionDate != null) {
                    rowMap.add(new ElectionEntry(nextElectionDate, areaId, electedOffice.getId()),
                            createUpcomingElectionLi(electedOffice, areaId));
                }
            }
        }
        for (LI li : OrderedCollection.of(rowMap.getValues(), 0, 5)) {
            ul.addElement(li);
        }
        body.addElement(createFooter());
        return html;
    }

    private LI createUpcomingElectionLi(final ElectedBody electedBody, final String areaId) {
        LI li = new LI();
        li.addElement(new A(" ").clazz("_area_" + areaId).href(areaId + "/index.html"));
        li.addContent(" – ");
        li.addElement(new Span(" ").clazz("_electedBody_" + areaId + "_" + electedBody.getId()));
        li.addContent(" (");
        li.addContent(electedBody.getAllProperNamesConcatenated());
        li.addContent("): ");
        ElectionDate nextElectionDate = electedBody.getNextElectionDate(now);
        if (nextElectionDate == null) {
            li.addElement(new Span(" ").clazz("none"));
        } else {
            String qualifierClass = nextElectionDate.getQualifierTermKey();
            if (qualifierClass != null) {
                li.addElement(new Span(" ").clazz(qualifierClass));
                li.addContent(" ");
            }
            li.addContent(nextElectionDate.getDateString());
        }
        return li;
    }

    private LI createUpcomingElectionLi(final ElectedOffice electedOffice, final String areaId) {
        LI li = new LI();
        li.addElement(new A(" ").clazz("_area_" + areaId).href(areaId + "/index.html"));
        li.addContent(" – ");
        li.addElement(new Span(" ").clazz("_electedOffice_" + areaId + "_" + electedOffice.getId()));
        li.addContent(" (");
        li.addContent(electedOffice.getAllProperNamesConcatenated());
        li.addContent("): ");
        ElectionDate nextElectionDate = electedOffice.getNextElectionDate(now);
        if (nextElectionDate == null) {
            li.addElement(new Span(" ").clazz("none"));
        } else {
            String qualifierClass = nextElectionDate.getQualifierTermKey();
            if (qualifierClass != null) {
                li.addElement(new Span(" ").clazz(qualifierClass));
                li.addContent(" ");
            }
            li.addContent(nextElectionDate.getDateString());
        }
        return li;
    }
}
