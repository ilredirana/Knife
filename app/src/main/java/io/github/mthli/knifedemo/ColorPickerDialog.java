package io.github.mthli.knifedemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ColorPickerDialog extends Dialog implements OnClickListener{

    private ColorPickerDialogListener listener;

    public interface ColorPickerDialogListener {
        public void onClick(View view);
    }

    public ColorPickerDialog(Context context, ColorPickerDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);
        initViews();
    }

    private void initViews(){
        ImageButton transparent = (ImageButton)findViewById(R.id.transparent);
        ImageButton blue_grey = (ImageButton)findViewById(R.id.blue_grey);
        ImageButton black = (ImageButton)findViewById(R.id.black);
        ImageButton light_green = (ImageButton)findViewById(R.id.light_green);
        ImageButton green = (ImageButton)findViewById(R.id.green);
        ImageButton teal = (ImageButton)findViewById(R.id.teal);
        ImageButton lime = (ImageButton)findViewById(R.id.lime);
        ImageButton amber = (ImageButton)findViewById(R.id.amber);
        ImageButton red = (ImageButton)findViewById(R.id.red);
        ImageButton blue = (ImageButton)findViewById(R.id.blue);
        ImageButton indigo = (ImageButton)findViewById(R.id.indigo);
        ImageButton purple = (ImageButton)findViewById(R.id.purple);

        Button button = (Button) findViewById(R.id.ok);

        transparent.setOnClickListener(this);
        blue_grey.setOnClickListener(this);
        black.setOnClickListener(this);
        light_green.setOnClickListener(this);
        green.setOnClickListener(this);
        teal.setOnClickListener(this);
        lime.setOnClickListener(this);
        amber.setOnClickListener(this);
        red.setOnClickListener(this);
        blue.setOnClickListener(this);
        indigo.setOnClickListener(this);
        purple.setOnClickListener(this);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ok){
            this.dismiss();
        }else {
            listener.onClick(v);
        }
    }
}
/*
switch (v.getId()){
        case R.id.transparent:{
        break;
        }
        case R.id.blue_grey:{
        break;
        }
        case R.id.black:{
        break;
        }
        case R.id.light_green:{
        break;
        }
        case R.id.green:{
        break;
        }
        case R.id.teal:{
        break;
        }
        case R.id.lime:{
        break;
        }
        case R.id.amber:{
        break;
        }
        case R.id.red:{
        break;
        }
        case R.id.blue:{
        break;
        }
        case R.id.indigo:{
        break;
        }
        case R.id.purple:{
        break;
        }
        }
 */