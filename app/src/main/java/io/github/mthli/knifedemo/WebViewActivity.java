package io.github.mthli.knifedemo;

import android.app.Activity;
import android.os.Bundle;

import io.github.mthli.knife.KnifeText;

public class WebViewActivity extends Activity {
    KnifeText viewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        viewer = (KnifeText) findViewById(R.id.viewer);
        viewer.setFocusable(false);
        loadText(getIntent().getStringExtra("preview"));
    }

    private void loadText(final String s){
        if (s!=null){
            viewer.fromHtml(s);
        }else {
            viewer.fromHtml("内容为空");
        }
    }
}
