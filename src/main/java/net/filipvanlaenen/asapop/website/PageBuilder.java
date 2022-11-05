package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.FlowContent;
import net.filipvanlaenen.txhtmlj.Footer;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Header;
import net.filipvanlaenen.txhtmlj.HttpEquivValue;
import net.filipvanlaenen.txhtmlj.JavaScriptMimeTypeValue;
import net.filipvanlaenen.txhtmlj.Link;
import net.filipvanlaenen.txhtmlj.LinkTypeValue;
import net.filipvanlaenen.txhtmlj.Meta;
import net.filipvanlaenen.txhtmlj.Option;
import net.filipvanlaenen.txhtmlj.Script;
import net.filipvanlaenen.txhtmlj.Select;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Abstract super class for classes building pages, providing some utility methods to them.
 */
abstract class PageBuilder {
    /**
     * Enumeration with the links in the headers.
     */
    enum HeaderLink {
        /**
         * Link to the CSV Files page.
         */
        CSV_FILES("csv-files", "csv.html"),
        /**
         * Link to the electoral calendar.
         */
        ELECTORAL_CALENDAR("electoral-calendar", "calendar.html"),
        /**
         * Link to the main page.
         */
        INDEX("main-page", "index.html");

        /**
         * The string value for the class attribute.
         */
        private final String clazz;
        /**
         * The href.
         */
        private final String href;

        /**
         * Constructor taking the class attribute and the href as its parameters.
         *
         * @param clazz The class attribute.
         * @param href  The href.
         */
        HeaderLink(final String clazz, final String href) {
            this.clazz = clazz;
            this.href = href;
        }

        /**
         * Creates the header element, either a span element if the current page is the same as the header link, or an a
         * element otherwise.
         *
         * @param currentPage The page for which to create a header link.
         * @return Either a span or an a element.
         */
        private FlowContent createHeaderElement(final HeaderLink currentPage) {
            if (currentPage.equals(this)) {
                return new Span(" ").clazz(clazz);
            } else {
                return new A(" ").clazz(clazz).href(href);
            }
        }
    }

    /**
     * Creates a footer element for a page.
     *
     * @return A footer element for a page.
     */
    protected Footer createFooter() {
        Footer footer = new Footer();
        footer.addElement(new Div(" ").clazz("privacy-statement"));
        return footer;
    }

    /**
     * Creates a head element for a page.
     *
     * @return A head element for a page.
     */
    protected Head createHead() {
        Head head = new Head();
        head.addElement(new Meta().httpEquiv(HttpEquivValue.CONTENT_TYPE).content("text/html; charset=UTF-8"));
        head.addElement(new Title("ASAPOP Website"));
        head.addElement(new Link().rel(LinkTypeValue.STYLESHEET).href("_css/base.css").type("text/css"));
        head.addElement(new Link().rel(LinkTypeValue.STYLESHEET).href("_css/skin.css").type("text/css"));
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"));
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("_js/internationalization.js"));
        return head;
    }

    /**
     * Creates a header element for a page.
     *
     * @param currentPage A header link representing the page for which the header element should be created.
     * @return A header element for a page.
     */
    protected Header createHeader(final HeaderLink currentPage) {
        Header header = new Header();
        Div left = new Div().clazz("header-left");
        left.addElement(HeaderLink.INDEX.createHeaderElement(currentPage));
        header.addElement(left);
        Div right = new Div().clazz("header-right");
        header.addElement(right);
        right.addElement(HeaderLink.ELECTORAL_CALENDAR.createHeaderElement(currentPage));
        right.addContent(" · ");
        right.addElement(HeaderLink.CSV_FILES.createHeaderElement(currentPage));
        right.addContent(" · ");
        right.addElement(new Span(" ").clazz("language"));
        right.addContent(": ");
        Select languageSelector = new Select().id("language-selector").onchange("loadLanguage();");
        languageSelector.addElement(new Option("Deutsch").value("de"));
        languageSelector.addElement(new Option("English").value("en"));
        languageSelector.addElement(new Option("Esperanto").value("eo"));
        languageSelector.addElement(new Option("français").value("fr"));
        languageSelector.addElement(new Option("Nederlands").value("nl"));
        languageSelector.addElement(new Option("norsk").value("no"));
        right.addElement(languageSelector);
        return header;
    }
}
