package net.filipvanlaenen.asapop.website;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection;
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
 * Class building the page with an overview and links to the CSV files.
 */
final class CsvFilesPageBuilder extends PageBuilder {
    /**
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     */
    CsvFilesPageBuilder(final WebsiteConfiguration websiteConfiguration) {
        super(websiteConfiguration);
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
        List<AreaConfiguration> sortedAreaConfigurations =
                getAreaConfigurations().stream().filter(ac -> hasCsvFiles(ac)).collect(Collectors.toList());
        sortedAreaConfigurations.sort(new Comparator<AreaConfiguration>() {
            @Override
            public int compare(final AreaConfiguration ac0, final AreaConfiguration ac1) {
                return ac0.getAreaCode().compareTo(ac1.getAreaCode());
            }
        });
        for (AreaConfiguration areaConfiguration : sortedAreaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaConfiguration.getCsvConfiguration() != null) {
                TR areaTr = new TR();
                tBody.addElement(areaTr);
                TD tdCsvLink = new TD();
                areaTr.addElement(tdCsvLink);
                tdCsvLink.addElement(new A(areaCode + ".csv").href("_csv/" + areaCode + ".csv"));
                tdCsvLink.addContent(" (");
                tdCsvLink.addElement(new A("v1").href("_csv/" + areaCode + ".v1.csv"));
                tdCsvLink.addContent(")");
                TD tdAreaName = new TD();
                areaTr.addElement(tdAreaName);
                tdAreaName.addElement(new A(" ").clazz("_area_" + areaCode).href(areaCode + "/index.html"));
            }
            AreaSubdivisionConfiguration[] subdivisions = areaConfiguration.getSubdivsions();
            if (subdivisions != null) {
                OrderedCollection<AreaSubdivisionConfiguration> sortedSubdivisions =
                        new SortedTreeCollection<AreaSubdivisionConfiguration>(
                                new Comparator<AreaSubdivisionConfiguration>() {
                                    @Override
                                    public int compare(final AreaSubdivisionConfiguration asc0,
                                            final AreaSubdivisionConfiguration asc1) {
                                        return asc0.getAreaCode().compareTo(asc1.getAreaCode());
                                    }
                                }, Collection.of(subdivisions));
                for (AreaSubdivisionConfiguration subdivision : sortedSubdivisions) {
                    if (subdivision.getAreaCode() != null && subdivision.getCsvConfiguration() != null) {
                        String subdivisionCode = areaCode + "-" + subdivision.getAreaCode();
                        TR subdivisionTr = new TR();
                        tBody.addElement(subdivisionTr);
                        TD tdSubdivisionCsvLink = new TD();
                        subdivisionTr.addElement(tdSubdivisionCsvLink);
                        tdSubdivisionCsvLink
                                .addElement(new A(subdivisionCode + ".csv").href("_csv/" + subdivisionCode + ".csv"));
                        tdSubdivisionCsvLink.addContent(" (");
                        tdSubdivisionCsvLink.addElement(new A("v1").href("_csv/" + subdivisionCode + ".v1.csv"));
                        tdSubdivisionCsvLink.addContent(")");
                        TD tdSubdivisionName = new TD();
                        tdSubdivisionName
                                .addElement(new A(" ").clazz("_area_" + areaCode).href(areaCode + "/index.html"));
                        tdSubdivisionName.addContent(": ");
                        tdSubdivisionName.addElement(new Span(" ").clazz("_area_" + subdivisionCode));
                        subdivisionTr.addElement(tdSubdivisionName);
                    }

                }
            }
        }
        body.addElement(createFooter());
        return html;
    }

    /**
     * Returns whether an area configuration has a configuration present to export CSV files, either directly or though
     * a subdivision.
     *
     * @param areaConfiguration The area configuration.
     * @return True if the area configuration has a configuration present to export CSV files.
     */
    private boolean hasCsvFiles(final AreaConfiguration areaConfiguration) {
        if (areaConfiguration.getAreaCode() == null) {
            return false;
        }
        if (areaConfiguration.getCsvConfiguration() != null) {
            return true;
        }
        AreaSubdivisionConfiguration[] subdivisions = areaConfiguration.getSubdivsions();
        if (subdivisions != null) {
            for (AreaSubdivisionConfiguration subdivision : subdivisions) {
                if (subdivision.getAreaCode() != null && subdivision.getCsvConfiguration() != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
