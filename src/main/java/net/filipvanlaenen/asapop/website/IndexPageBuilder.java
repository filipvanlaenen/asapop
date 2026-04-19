package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.util.Comparator;

import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Section;

/**
 * Class building the index page.
 */
final class IndexPageBuilder extends PageBuilder {
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
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param elections            The elections.
     * @param now                  The today's day.
     */
    IndexPageBuilder(final WebsiteConfiguration websiteConfiguration) {
        super(websiteConfiguration);
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
        // TODO: Five next elections
        body.addElement(createFooter());
        return html;
    }
}
