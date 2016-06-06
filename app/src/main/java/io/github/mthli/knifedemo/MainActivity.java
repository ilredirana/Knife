package io.github.mthli.knifedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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

String EXAMPLE = "left<br><align_center>center</align_center><br><align_right>right</align_right><br><b>粗体</b><br><i>斜体</i><br><u>下划线</u><br><del>删除线</del><br><ul><li>列表1</li></ul><ul><li>列表2</li></ul><blockquote>引用段啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊</blockquote><big><big>大大</big></big>大<br>小<small><small>小小</small></small><br><font color =\"#3f51b5\">颜</font><font color =\"#f44336\">色</font><br><bgcolor_f44336><font color =\"#ffffff\">背</font></bgcolor_f44336><font color =\"#ffffff\"><bgcolor_009688>景</bgcolor_009688></font><br><br>";
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
        setupTextColor();
        setupBackgroundColor();
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
        ImageButton right = (ImageButton) findViewById(R.id.align_right);

        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.setAlignment(1);
            }
        });

        center.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "居中", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.setAlignment(0);
            }
        });

        left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "居左", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.setAlignment(2);
            }
        });

        right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "居右", Toast.LENGTH_SHORT).show();
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

    private void setupTextColor() {
        ImageButton textColor = (ImageButton) findViewById(R.id.text_color);

        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (knife.getSelectionStart()<knife.getSelectionEnd()) {
                    showColorPickerDialog(true);
                }else {
                    Toast.makeText(MainActivity.this,"请先选择文字",Toast.LENGTH_SHORT).show();
                }
            }
        });

        textColor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "设置文字颜色", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBackgroundColor() {
        ImageButton backgroundColor = (ImageButton) findViewById(R.id.background_color);

        backgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (knife.getSelectionStart()<knife.getSelectionEnd()) {
                    showColorPickerDialog(false);
                }else {
                    Toast.makeText(MainActivity.this,"请先选择文字",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backgroundColor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "设置背景颜色", Toast.LENGTH_SHORT).show();
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
                gallery();
            }
        });

        insertImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "insert image", Toast.LENGTH_SHORT).show();
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

    /**
     * 显示颜色选择对话框
     * @param isFore true：文字；false：背景
     */
    private void showColorPickerDialog(final boolean isFore) {

        ColorPickerDialog.ColorPickerDialogListener listener = new ColorPickerDialog.ColorPickerDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.transparent:{
                        if (isFore){
                            knife.textColor(Color.WHITE);
                        }else {
                            knife.backgroundColor(Color.TRANSPARENT);
                        }
                        break;
                    }
                    case R.id.blue_grey:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.blue_grey));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.blue_grey));
                        }
                        break;
                    }
                    case R.id.black:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.black));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.black));
                        }
                        break;
                    }
                    case R.id.light_green:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.light_green));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.light_green));
                        }
                        break;
                    }
                    case R.id.green:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.green));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.green));
                        }
                        break;
                    }
                    case R.id.teal:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.teal));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.teal));
                        }
                        break;
                    }
                    case R.id.lime:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.lime));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.lime));
                        }
                        break;
                    }
                    case R.id.amber:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.amber));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.amber));
                        }
                        break;
                    }
                    case R.id.red:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.red));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.red));
                        }
                        break;
                    }
                    case R.id.blue:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.blue));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.blue));
                        }
                        break;
                    }
                    case R.id.indigo:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.indigo));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.indigo));
                        }
                        break;
                    }
                    case R.id.purple:{
                        if (isFore){
                            knife.textColor(getResources().getColor(R.color.purple));
                        }else {
                            knife.backgroundColor(getResources().getColor(R.color.purple));
                        }
                        break;
                    }
                }
            }
        };

        ColorPickerDialog dialog = new ColorPickerDialog(this, listener);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.dialog_color_picker, null, false);
        dialog.setContentView(view);
        //TODO
        if (isFore) {
            dialog.setTitle("选择文字颜色");
        }else {
            dialog.setTitle("选择背景颜色");
        }
        dialog.show();
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
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("preview",preview);
                startActivity(intent);
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
                            //TODO 这里可以做上传操作
                            knife.addImage(originalBitmap,fileName,"上传服务器返回来的url");
                        } else {
                            Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
