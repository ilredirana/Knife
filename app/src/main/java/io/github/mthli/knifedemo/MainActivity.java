package io.github.mthli.knifedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.github.mthli.knife.KnifeParser;
import io.github.mthli.knife.KnifeText;

public class MainActivity extends Activity {
    private static final String H1 = "<big>hhhfffffffhhhhhh</big><br><br>";
    private static final String H5 = "<small>hhhhhhhhh</small><br><br>";
    private static final String BOLD = "<b>Bold</b><br><br>";
    private static final String ITALIT = "<i>Italic</i><br><br>";
    private static final String UNDERLINE = "<u>Underline</u><br><br>";
    private static final String STRIKETHROUGH = "<s>Strikethroug</s><br><br>"; // <s> or <strike> or <del>
    private static final String BULLET = "<ul><li>asdfg</li></ul>";
    private static final String QUOTE = "<blockquote>Quote</blockquote>";
    private static final String LINK = "<a href=\"https://github.com/mthli/Knife\">Link</a><br><br>";
    private static final String IMAGE = "<img src=\"http://www.zgjm.org/uploads/allimg/140118/1334551250-0.jpg\"><br><br>";
    private static final String EXAMPLE = H1 + H5 + BOLD + ITALIT  + IMAGE +  UNDERLINE + STRIKETHROUGH + BULLET + QUOTE + LINK;

//    String e = "<p><u style=\"color: rgb(255, 0, 0);\">aa<strong>\u200Baaa</strong>aaa</u><br></p>";

    private KnifeText knife;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        knife = (KnifeText) findViewById(R.id.knife);
        // ImageGetter coming soon...
        knife.fromHtml(EXAMPLE);
        knife.setSelection(knife.getEditableText().length());

        initImageLoader(this);
        setTextSize();
        setupKeyboard();
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupAlign();
        setupClear();
        setupInsertImage();
    }

    private void setTextSize(){
        ImageButton set = (ImageButton) findViewById(R.id.bigger_size);
        ImageButton clear = (ImageButton) findViewById(R.id.smaller_size);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.textSize(1.25f);
            }
        });

        set.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "变大1/4", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.textSize(0.8f);
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "变小1/4", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupKeyboard() {
        final ImageButton keyboard = (ImageButton) findViewById(R.id.show_or_hide_keyboard);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.hideSoftInput();
            }
        });

        keyboard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "隐藏键盘", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupAlign() {
        ImageButton center = (ImageButton) findViewById(R.id.align_center);
        ImageButton left = (ImageButton) findViewById(R.id.align_left);

        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.alignCenter(true);
            }
        });

        center.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.alignCenter(false);
            }
        });

        left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        ImageButton clear = (ImageButton) findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.append("\n");
                knife.clearFormats();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupInsertImage() {
        ImageButton insertImage = (ImageButton) findViewById(R.id.inert_image);

        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                knife.clearFormats();
                gallery();
            }
        });

        insertImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "insert ic_insert_image", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showLinkDialog() {
        final int start = knife.getSelectionStart();
        final int end = knife.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_knowledge_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.undo:
                knife.undo();
                break;
            case R.id.redo:
                knife.redo();
                break;
            case R.id.view_html:
                String preview = KnifeParser.toHtml(knife.getText());
                Toast.makeText(this, preview,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("preview",preview);
                startActivity(intent);
                break;
            case R.id.view_html2:
                Toast.makeText(this, Html.toHtml(knife.getText()),Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 工具栏添加图片的逻辑
     */
    public void gallery() {
        // 调用系统图库
        // Intent getImg = new Intent(Intent.ACTION_GET_CONTENT);
        // getImg.addCategory(Intent.CATEGORY_OPENABLE);
        // getImg.setType("ic_insert_image/*");
        Intent getImg = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImg, 1001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001: {
                    // 添加图片
                    Bitmap originalBitmap;
                    Uri originalUri = data.getData();
                    try {
                        Cursor cursor = getContentResolver().query(originalUri,
                                null, null, null, null);
                        cursor.moveToFirst();
                        long fileSize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                        String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        cursor.close();
//                        long fileSize = file.length();
                        System.out.println(fileSize);
                        if (fileSize > 5 * 1024 * 1024 ) {
                            Toast.makeText(this, "图片不能大于5MB", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (fileSize <= 0) {
                            Toast.makeText(this, "图片错误", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        originalBitmap = ImageLoader.getInstance().loadImageSync(
                                originalUri.toString());

                        if (originalBitmap!= null) {
                            knife.addImage(originalBitmap,fileName,"返回来的url");
                        } else {
                            Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                    }


                    break;
                }
            }
        }
    }

    /**
     * 获取指定uri的本地绝对路径
     *
     * @param uri
     * @return
     */
    protected String getAbsoluteImagePath(Uri uri) {
        // can post ic_insert_image
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }
}
