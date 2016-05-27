package io.github.mthli.knifedemo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import io.github.mthli.knife.KnifeText;

public class WebViewActivity extends Activity {
    WebView viewer;
    private String CSS = "<style>" +
            "img {" +
            "  max-width:100%;" +
            "}" +
            "a:link {" +
            " color: #2196F3; " +
            "text-decoration: none" +
            "}" +
            "blockquote {" +
            "background: #f9f9f9;" +
            "  border-left: 10px solid #90CAF9;" +
            "  margin: 1.5em 2px;" +
            "  padding: 0.5em 2px;" +
//            "  quotes: \"\\201C\"\"\\201D\"\"\\2018\"\"\\2019\";" +
            "}" +
            "blockquote:before {" +
            "  color: #90CAF9;" +
            "  content: open-quote;" +
            "  font-size: 4em;" +
            "  line-height: 0.1em;" +
            "  margin-right: 0em;" +
            "  vertical-align: -0.4em;" +
            "}\n" +
            "blockquote:after {" +
            "  color: #90CAF9;" +
            "  content: open-quote open-quote;" +
            "  font-size: 4em;" +
            "  line-height: 0em;" +
            "  margin-right: 0em;" +
            "  vertical-align: -0.4em;" +
            "}\n" +
            "blockquote p {\n" +
            "  display: inline;\n" +
            "}</style>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        viewer = (WebView) findViewById(R.id.webview);
        loadText(getIntent().getStringExtra("preview"));
    }

    private void loadText(final String s){
        if (s!=null){
            viewer.loadData(s+CSS,"text/html", "utf-8");
        }else {
            viewer.loadData("内容为空","text/html", "utf-8");
        }
    }
}
