/*
 * Copyright (C) 2015 Matthew Lee
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.mthli.knife;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

public class KnifeParser {
    public static Spanned fromHtml(String source, UrlImageGetter urlImageGetter) {
        return Html.fromHtml(source, urlImageGetter, new KnifeTagHandler());
    }

    public static String toHtml(Spanned text) {
        StringBuilder out = new StringBuilder();
        withinHtml(out, text);
        return tidy(out.toString());
    }

    private static void withinHtml(StringBuilder out, Spanned text) {
        int next;

        for (int i = 0; i < text.length(); i = next) {

            next = text.nextSpanTransition(i, text.length(), ParagraphStyle.class);

            ParagraphStyle[] styles = text.getSpans(i, next, ParagraphStyle.class);
            Log.e("STYLE_LENGTH",styles.length+"个");
            if (styles.length == 2) {
                if (styles[0] instanceof BulletSpan && styles[1] instanceof QuoteSpan) {
                    // Let a <br> follow the BulletSpan or QuoteSpan end, so next++
                    withinBulletThenQuote(out, text, i, next++);
                } else if (styles[0] instanceof QuoteSpan && styles[1] instanceof BulletSpan) {
                    withinQuoteThenBullet(out, text, i, next++);
                } else {
                    withinContent(out, text, i, next);
                }
            } else if (styles.length == 1) {
                if (styles[0] instanceof BulletSpan) {
                    withinBullet(out, text, i, next);
                } else if (styles[0] instanceof QuoteSpan) {
                    withinQuote(out, text, i, next);
                }else if (styles[0] instanceof AlignmentSpan) {
                    withinAlignment(out, text, i, next);
                }else {
                    withinContent(out, text, i, next);
                }
            } else {
                withinContent(out, text, i, next);
            }
        }
    }

    private static void withinBulletThenQuote(StringBuilder out, Spanned text, int start, int end) {
        out.append("<ul><li>");
        withinQuote(out, text, start, end);
        out.append("</li></ul>");
    }

    private static void withinQuoteThenBullet(StringBuilder out, Spanned text, int start, int end) {
        out.append("<blockquote>");
        withinBullet(out, text, start, end);
        out.append("</blockquote>");
    }

    private static void withinBullet(StringBuilder out, Spanned text, int start, int end) {
        out.append("<ul>");

        int next;

        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, BulletSpan.class);

            BulletSpan[] spans = text.getSpans(i, next, BulletSpan.class);
            for (BulletSpan span : spans) {
                out.append("<li>");
            }

            withinContent(out, text, i, next);
            for (BulletSpan span : spans) {
                out.append("</li>");
            }
        }

        out.append("</ul>");
    }

    private static void withinQuote(StringBuilder out, Spanned text, int start, int end) {
        int next;

        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, QuoteSpan.class);

            QuoteSpan[] quotes = text.getSpans(i, next, QuoteSpan.class);
            for (QuoteSpan quote : quotes) {
                out.append("<blockquote>");
            }

            withinContent(out, text, i, next);
            for (QuoteSpan quote : quotes) {
                out.append("</blockquote>");
            }
        }
    }

    private static void withinAlignment(StringBuilder out, Spanned text, int start, int end) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, AlignmentSpan.class);

            AlignmentSpan[] spans = text.getSpans(i, next, AlignmentSpan.class);
            for (AlignmentSpan span : spans) {
                out.append("<align_");
                if (span.getAlignment().equals(Layout.Alignment.ALIGN_CENTER))
                    out.append("center>");
                else if (span.getAlignment().equals(Layout.Alignment.ALIGN_NORMAL))
                    out.append("left>");
                else if (span.getAlignment().equals(Layout.Alignment.ALIGN_OPPOSITE)) {
                    out.append("right>");
                }
            }
            withinContent(out, text, i, next);
            for (AlignmentSpan span : spans) {
                out.append("</align_");
                if (span.getAlignment().equals(Layout.Alignment.ALIGN_CENTER))
                    out.append("center>");
                else if (span.getAlignment().equals(Layout.Alignment.ALIGN_NORMAL))
                    out.append("left>");
                else if (span.getAlignment().equals(Layout.Alignment.ALIGN_OPPOSITE))
                    out.append("right>");
            }
        }
    }

    private static void withinContent(StringBuilder out, Spanned text, int start, int end) {
        int next;

        for (int i = start; i < end; i = next) {
            next = TextUtils.indexOf(text, '\n', i, end);
            if (next < 0) {
                next = end;
            }

            int nl = 0;
            while (next < end && text.charAt(next) == '\n') {
                next++;
                nl++;
            }

            withinParagraph(out, text, i, next - nl, nl);
        }
    }

    // Copy from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/Html.java,
    // remove some tag because we don't need them in Knife.
    private static void withinParagraph(StringBuilder out, Spanned text, int start, int end, int nl) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, CharacterStyle.class);

            CharacterStyle[] spans = text.getSpans(i, next, CharacterStyle.class);
            for (int j = 0; j < spans.length; j++) {

                if (spans[j] instanceof StyleSpan) {
                    int style = ((StyleSpan) spans[j]).getStyle();

                    if ((style & Typeface.BOLD) != 0) {
                        out.append("<b>");
                    }

                    if ((style & Typeface.ITALIC) != 0) {
                        out.append("<i>");
                    }

                }

                if (spans[j] instanceof UnderlineSpan) {
                    out.append("<u>");
                }

                if (spans[j] instanceof AbsoluteSizeSpan){
                    System.out.println("ABS");
                }

                if (spans[j] instanceof RelativeSizeSpan){
                    if (((RelativeSizeSpan)(spans[j])).getSizeChange()==0.8f){
                        out.append("<small>");
                    }
                    if (((RelativeSizeSpan)(spans[j])).getSizeChange()==1.25f){
                        out.append("<big>");
                    }
                }
                // Use standard strikethrough tag <del> rather than <s> or <strike>
                if (spans[j] instanceof StrikethroughSpan) {
                    out.append("<del>");
                }

                if (spans[j] instanceof URLSpan) {
                    out.append("<a href=\"");
                    out.append(((URLSpan) spans[j]).getURL());
                    out.append("\">");
                }

                if (spans[j] instanceof UrlImageSpan) {
                    out.append("<img src=\"");
                    out.append(((UrlImageSpan) spans[j]).getUrl());
                    out.append("\" style=\"max-width:100%\" >");

                    // Don't output the dummy character underlying the image.
                    i = next;
                }else if (spans[j] instanceof ImageSpan) {
                    out.append("<img src=\"");
                    ImageSpan imageSpan = (ImageSpan)spans[j];
                    String source = imageSpan.getSource();
                    out.append(source);
                    out.append("\" style=\"max-width:100%\" >");
                    // Don't output the dummy character underlying the image.
                    i = next;
                }
                if (spans[j] instanceof ForegroundColorSpan) {
                    out.append("<font color =\"#");
                    String color = Integer.toHexString(((ForegroundColorSpan) spans[j]).getForegroundColor() + 0x01000000);

                    while (color.length() < 6) {
                        color = "0" + color;
                    }
                    out.append(color);
                    out.append("\">");
                }
                if (spans[j] instanceof BackgroundColorSpan) {
                    if (((BackgroundColorSpan) spans[j]).getBackgroundColor()!= Color.TRANSPARENT){
                        out.append("<bgcolor_");
                        String color = Integer.toHexString(((BackgroundColorSpan) spans[j]).getBackgroundColor() + 0x01000000);

                        while (color.length() < 6) {
                            color = "0" + color;
                        }
                        out.append(color);
                        out.append(">");
                    }
                }
            }

            withinStyle(out, text, i, next);
            for (int j = spans.length - 1; j >= 0; j--) {
                if (spans[j] instanceof URLSpan) {
                    out.append("</a>");
                }

                if (spans[j] instanceof StrikethroughSpan) {
                    out.append("</del>");
                }

                if (spans[j] instanceof UnderlineSpan) {
                    out.append("</u>");
                }

                if (spans[j] instanceof RelativeSizeSpan){
                    if (((RelativeSizeSpan)(spans[j])).getSizeChange()==0.8f){
                        out.append("</small>");
                    }
                    if (((RelativeSizeSpan)(spans[j])).getSizeChange()==1.25f){
                        out.append("</big>");
                    }
                }

                if (spans[j] instanceof StyleSpan) {
                    int style = ((StyleSpan) spans[j]).getStyle();

                    if ((style & Typeface.BOLD) != 0) {
                        out.append("</b>");
                    }

                    if ((style & Typeface.ITALIC) != 0) {
                        out.append("</i>");
                    }
                }
                if (spans[j] instanceof ForegroundColorSpan) {
                    out.append("</font>");
                }
                if (spans[j] instanceof BackgroundColorSpan) {
                    if (((BackgroundColorSpan) spans[j]).getBackgroundColor()!= Color.TRANSPARENT){
                        out.append("</bgcolor_");
                        String color = Integer.toHexString(((BackgroundColorSpan) spans[j]).getBackgroundColor() + 0x01000000);

                        while (color.length() < 6) {
                            color = "0" + color;
                        }
                        out.append(color);
                        out.append(">");
                    }
                }
            }
        }

        for (int i = 0; i < nl; i++) {
            out.append("<br>");
        }
    }

    private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            out.append(c);
        }
    }

    private static String tidy(String html) {
        return html.replaceAll("</ul>(<br>)?", "</ul>").replaceAll("</blockquote>(<br>)?", "</blockquote>");
    }
}
