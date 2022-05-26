package eu.mulk.mulkcms2.common.markdown;

import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

public class MarkdownConverter {

  public enum Mode {
    POST,
    COMMENT,
  }

  private final Parser parser;
  private final HtmlRenderer renderer;
  private final Mode mode;

  public MarkdownConverter(Mode mode) {
    var options = new MutableDataSet();
    options.set(
        Parser.EXTENSIONS,
        Arrays.asList(
            TablesExtension.create(),
            StrikethroughExtension.create(),
            AbbreviationExtension.create(),
            AutolinkExtension.create(),
            DefinitionExtension.create(),
            TypographicExtension.create(),
            FootnoteExtension.create(),
            AbbreviationExtension.create()));
    options.set(Parser.FENCED_CODE_BLOCK_PARSER, true);
    options.set(HtmlRenderer.SOFT_BREAK, "\n");
    options.set(HtmlRenderer.HARD_BREAK, "<br />\n");
    options.set(TypographicExtension.ENABLE_SMARTS, true);
    options.set(TypographicExtension.ENABLE_QUOTES, true);
    options.set(FootnoteExtension.FOOTNOTE_BACK_REF_STRING, "");

    this.mode = mode;
    this.parser = Parser.builder(options).build();
    this.renderer = HtmlRenderer.builder(options).build();
  }

  public String htmlify(String markdown) {
    var parsedDocument = parser.parse(markdown);
    var uncleanHtml = renderer.render(parsedDocument);
    var cleaner = makeCleaner();
    var cleanedDocument = cleaner.clean(Jsoup.parseBodyFragment(uncleanHtml));
    cleanedDocument.select("table").addClass("pure-table").addClass("pure-table-horizontal");
    return cleanedDocument.body().html();
  }

  private Cleaner makeCleaner() {
    var safelist =
        switch (mode) {
          case POST -> Safelist.relaxed()
              .addTags("abbr", "acronym")
              .addAttributes("abbr", "title")
              .addAttributes("acronym", "title");
          case COMMENT -> Safelist.simpleText()
              .addTags(
                  "p",
                  "blockquote",
                  "cite",
                  "code",
                  "pre",
                  "dd",
                  "dl",
                  "dt",
                  "s",
                  "sub",
                  "sup",
                  "ol",
                  "ul",
                  "li",
                  "abbr",
                  "acronym",
                  "ins",
                  "del")
              .addAttributes("abbr", "title")
              .addAttributes("acronym", "title");
        };
    return new Cleaner(safelist);
  }
}
