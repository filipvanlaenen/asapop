package net.filipvanlaenen.asapop.website;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.OpinionPolls;
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
 * Class building the page with statistics.
 */
final class StatisticsPageBuilder extends PageBuilder {
    /**
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;

    /**
     * Constructor taking the website configuration and the map with the opinion polls as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param opinionPollsMap      The map with the opinion polls.
     */
    StatisticsPageBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> opinionPollsMap) {
        super(websiteConfiguration);
        this.opinionPollsMap = opinionPollsMap;
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
        body.addElement(createHeader(PageBuilder.HeaderLink.STATISTICS));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("statistics"));
        Table table = new Table().clazz("statistics-table");
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH(" ").clazz("country"));
        tr.addElement(new TH(" ").clazz("number-of-opinion-polls"));
        tr.addElement(new TH(" ").clazz("number-of-response-scenarios"));
        tr.addElement(new TH(" ").clazz("number-of-result-values"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        TR totalTr = new TR();
        tHead.addElement(totalTr);
        List<AreaConfiguration> sortedAreaConfigurations =
                getAreaConfigurations().stream().filter(ac -> ac.getAreaCode() != null).collect(Collectors.toList());
        sortedAreaConfigurations.sort(new Comparator<AreaConfiguration>() {
            @Override
            public int compare(final AreaConfiguration ac0, final AreaConfiguration ac1) {
                return ac0.getAreaCode().compareTo(ac1.getAreaCode());
            }
        });
        int totalNumberOfOpinionPolls = 0;
        int totalNumberOfResponseScenarios = 0;
        int totalNumberOfResultValues = 0;
        for (AreaConfiguration areaConfiguration : sortedAreaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            TR areaTr = new TR();
            tBody.addElement(areaTr);
            TD tdAreaName = new TD();
            areaTr.addElement(tdAreaName);
            tdAreaName.addElement(new A(" ").clazz("_area_" + areaCode).href(areaCode + "/index.html"));
            if (opinionPollsMap.containsKey(areaCode)) {
                OpinionPolls opinionPolls = opinionPollsMap.get(areaCode);
                int numberOfOpinionPolls = opinionPolls.getNumberOfOpinionPolls();
                int numberOfResponseScenarios = opinionPolls.getNumberOfResponseScenarios();
                int numberOfResultValues = opinionPolls.getNumberOfResultValues();
                totalNumberOfOpinionPolls += numberOfOpinionPolls;
                totalNumberOfResponseScenarios += numberOfResponseScenarios;
                totalNumberOfResultValues += numberOfResultValues;
                areaTr.addElement(new TD(Integer.toString(numberOfOpinionPolls)).clazz("statistics-value-td"));
                areaTr.addElement(new TD(Integer.toString(numberOfResponseScenarios)).clazz("statistics-value-td"));
                areaTr.addElement(new TD(Integer.toString(numberOfResultValues)).clazz("statistics-value-td"));
            } else {
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
            }
        }
        totalTr.addElement(new TD(" ").clazz("total"));
        totalTr.addElement(new TD(Integer.toString(totalNumberOfOpinionPolls)).clazz("statistics-total-td"));
        totalTr.addElement(new TD(Integer.toString(totalNumberOfResponseScenarios)).clazz("statistics-total-td"));
        totalTr.addElement(new TD(Integer.toString(totalNumberOfResultValues)).clazz("statistics-total-td"));
        body.addElement(createFooter());
        return html;
    }
}
