package kr.co.finote.backend.global.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class StringUtils {

    private static final int PREVIEW_TEXT_MAX_LENGTH = 200;

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    // 10자리 랜덤 해시 스트링 생성 함수
    public static String makeRandomString() {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int random = (int) (Math.random() * 62);
            if (random < 10) {
                randomString.append(random);
            } else if (random > 35) {
                randomString.append((char) (random + 61));
            } else {
                randomString.append((char) (random + 55));
            }
        }
        return randomString.toString();
    }

    public static String markdownToPreviewText(String originBody) {
        // markdown to html
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(originBody);
        String html = renderer.render(document);

        // html to text
        Document doc = Jsoup.parse(html);

        // preview text limit length 200
        if (doc.text().length() > PREVIEW_TEXT_MAX_LENGTH)
            return doc.text().substring(0, PREVIEW_TEXT_MAX_LENGTH);
        else return doc.text();
    }

    public static String makeRandom6Number() {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            randomString.append(random);
        }
        return randomString.toString();
    }
}
