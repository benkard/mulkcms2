package eu.mulk.mulkcms2.benki.wiki;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

@Entity
@Table(name = "wiki_page_revisions", schema = "benki")
public class WikiPageRevision extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  @CheckForNull
  public OffsetDateTime date;

  @Column(name = "title", nullable = true, length = -1)
  @CheckForNull
  public String title;

  @Column(name = "content", nullable = true, length = -1)
  @CheckForNull
  public String content;

  @Column(name = "format", nullable = true, length = -1)
  @CheckForNull
  public String format;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page", referencedColumnName = "id", nullable = false)
  public WikiPage page;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User author;

  public WikiPageRevision() {}

  public WikiPageRevision(
      OffsetDateTime date,
      String title,
      String content,
      String format,
      WikiPage page,
      User author) {
    this.date = date;
    this.title = title;
    this.content = unhrefify(unwikilinkify(Jsoup.parse(content))).select("body").html();
    this.format = format;
    this.page = page;
    this.author = author;
  }

  @CheckForNull
  public String enrichedContent() {
    if (content == null) {
      return null;
    }
    return wikilinkify(hrefify(Jsoup.parse(content))).select("body").html();
  }

  private static Document tagsoupMapText(Document soup, Function<String, String> fn) {
    for (var subnode :
        soup.select(":not(a):not(a *)").stream()
            .flatMap(node -> node.childNodes().stream())
            .collect(Collectors.toUnmodifiableList())) {
      if (subnode instanceof TextNode) {
        var newNode = new Element(Tag.valueOf("span"), "");
        newNode.html(fn.apply(((TextNode) subnode).text()));
        subnode.replaceWith(newNode);
        newNode.unwrap();
      }
    }
    return soup;
  }

  private static final Pattern WIKIWORD_REGEX =
      Pattern.compile(
          "\\p{javaUpperCase}+\\p{javaLowerCase}+\\p{javaUpperCase}+\\p{javaLowerCase}+\\w+");
  private static final Pattern URL_REGEX =
      Pattern.compile("\\(?\\bhttps?://[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]");

  private static Document hrefify(Document soup) {
    return tagsoupMapText(
        soup,
        x ->
            URL_REGEX
                .matcher(x)
                .replaceAll(
                    match -> {
                      var s = match.group();
                      var leftParen = s.startsWith("(");
                      var rightParen = s.endsWith(")");
                      var url =
                          s.substring(leftParen ? 1 : 0, rightParen ? s.length() - 1 : s.length());
                      return String.format(
                          "%s<a href=\"%s\" class=\"benkiautohref\">%s</a>%s",
                          leftParen ? "(" : "", url, url, rightParen ? ")" : "");
                    }));
  }

  private static Document unhrefify(Document soup) {
    soup.select(".benkiautohref").unwrap();
    return soup;
  }

  private static Document wikilinkify(Document soup) {
    return tagsoupMapText(
        soup,
        x ->
            WIKIWORD_REGEX
                .matcher(x)
                .replaceAll(
                    match ->
                        String.format(
                            "<a href=\"/wiki/%s\" class=\"benkilink\">%s</a>",
                            match.group(), match.group())));
  }

  private static Document unwikilinkify(Document soup) {
    soup.select(".benkilink").unwrap();
    return soup;
  }
}
