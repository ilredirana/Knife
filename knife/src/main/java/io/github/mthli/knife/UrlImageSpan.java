package io.github.mthli.knife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by rana on 2016/5/6.
 */
public class UrlImageSpan extends android.text.style.ImageSpan {
    private String mUrl;

    public UrlImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
