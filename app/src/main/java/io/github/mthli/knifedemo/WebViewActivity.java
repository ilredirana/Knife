package io.github.mthli.knifedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mthli.knife.KnifeText;

public class WebViewActivity extends Activity {
    WebView viewer;
    private String CSS = "<style>" +
            "blockquote {" +
            "background: #f9f9f9;" +
            "  border-left: 2px solid #2196F3;" +
            "  margin: 1.5em 2px;" +
            "  padding: 0.5em ;" +
            "}" +
            "</style>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        viewer = (WebView) findViewById(R.id.webview);
        viewer.getSettings().setDefaultFontSize((int) (viewer.getSettings().getDefaultFontSize()*1.2));
        loadText(getIntent().getStringExtra("preview"));
    }

    private void loadText(String s){
        final String content = handleTags(s);
        new AlertDialog.Builder(this).setMessage("自定义标签处理前：\n"+s+"\n处理后：\n"+content).create().show();
        if (s!=null){
            viewer.loadDataWithBaseURL(null,content+CSS,"text/html","utf-8",null);
        }else {
            viewer.loadDataWithBaseURL(null,"内容为空","text/html","utf-8",null);
        }
    }

    private String handleTags(String originalContent){
        return handleAlignmentTag(handleBackgroundTag(originalContent));
    }

    private String handleBackgroundTag(String content){
        String tagOpen = "<bgcolor_(.{6})>";
        String tagClose = "</bgcolor_(.{6})>";

        Pattern patternStart = Pattern.compile(tagOpen);
        Matcher matcherStart = patternStart.matcher(content);

        StringBuffer stringBuffer = new StringBuffer();

        while (matcherStart.find()){
            String color = matcherStart.group(1);
            matcherStart.appendReplacement(stringBuffer,"<font style=\"background:#"+color+"\">");
        }
        matcherStart.appendTail(stringBuffer);
        return stringBuffer.toString().replaceAll(tagClose,"</font>");
    }

    private String handleAlignmentTag(String content){
        String tagOpen = "<align_(.{4,6})>";
        String tagClose = "</align_(.{4,6})>";

        Pattern patternStart = Pattern.compile(tagOpen);
        Matcher matcherStart = patternStart.matcher(content);

        StringBuffer stringBuffer = new StringBuffer();

        while (matcherStart.find()){
            String alignment = matcherStart.group(1);
            matcherStart.appendReplacement(stringBuffer,"<div style=\"text-align:"+alignment+"\">");
        }
        matcherStart.appendTail(stringBuffer);
        return stringBuffer.toString().replaceAll(tagClose,"</div>");
    }
}
