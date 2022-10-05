package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Footer;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Header;
import net.filipvanlaenen.txhtmlj.JavaScriptMimeTypeValue;
import net.filipvanlaenen.txhtmlj.Option;
import net.filipvanlaenen.txhtmlj.Script;
import net.filipvanlaenen.txhtmlj.Select;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Abstract super class for classes building pages, providing some utility methods to them.
 */
abstract class PageBuilder {
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
        head.addElement(new Title("ASAPOP Website"));
        head.addElement(createStyle());
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"));
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("_js/internationalization.js"));
        return head;
    }

    /**
     * Creates a header element for a page.
     *
     * @param linkToMainPage True if a link to the main page should be added.
     * @return A header element for a page.
     */
    protected Header createHeader(final boolean linkToMainPage) {
        Header header = new Header();
        Div left = new Div().clazz("header-left");
        if (linkToMainPage) {
            left.addElement(new A(" ").clazz("main-page").href("index.html"));
        } else {
            left.addElement(new Span(" ").clazz("main-page"));
        }
        header.addElement(left);
        Div right = new Div().clazz("header-right");
        header.addElement(right);
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

    /**
     * Creates a style element for a page.
     *
     * @return A style element for a page.
     */
    private Style createStyle() {
        StringBuffer style = new StringBuffer();
        style.append("header { display: block; width: 100%; }\n");
        style.append(".header-left {\n");
        style.append("  display: inline-block; float: left; overflow: hidden; position: relative; text-align: left;\n");
        style.append("  width: 49%;\n");
        style.append("}\n");
        style.append(".header-right {\n");
        style.append("  display: inline-block; float: right; overflow: hidden; position: relative;\n");
        style.append("  text-align: right; width: 49%;\n");
        style.append("}\n");
        style.append(".privacy-statement { text-align: center; }\n");
        style.append(".svg-chart-container-left {\n");
        style.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: left;\n");
        style.append("}\n");
        style.append(".svg-chart-container-right {\n");
        style.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: right;\n");
        style.append("}\n");
        style.append(".two-svg-charts-container { display: block; }\n");
        style.append("@media screen and (max-width: 700px) {\n");
        style.append("  .svg-chart-container-left { width: 100%; }\n");
        style.append("  .svg-chart-container-right { float: none; width: 100%; }\n");
        style.append("  .two-svg-charts-container { }\n");
        style.append("}\n");
        return new Style(style.toString());
    }
}
