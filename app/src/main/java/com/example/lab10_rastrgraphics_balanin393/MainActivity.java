package com.example.lab10_rastrgraphics_balanin393;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Bitmap bmp1;
    Bitmap bmp2;
    int width, height;
    ImageView v1;
    ImageView v2;
    String name = ""; //picture name
    boolean result = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager asset  = getAssets();

        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        builder.setView(et);
        //All pictures in assets folder!!!!
        builder.setMessage("Write in picture name without extension (only cat picture exists):");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                name = et.getText().toString();
                result = true;
                dialog.cancel();
                if (!result) {
                    name = "cat";
                }
                name += ".png";

                InputStream stream = null;
                try{
                    stream = asset.open(name);

                }catch (IOException e) {
                    try {
                        stream = asset.open("cat.png");
                    }
                    catch (IOException d) {}
                }

                bmp1 = BitmapFactory.decodeStream(stream);

                try {
                    stream.close();
                }catch (IOException e) {}

                width = bmp1.getWidth();
                height = bmp1.getHeight();

                bmp2 = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

                v1 = findViewById(R.id.img1);
                v2 = findViewById(R.id.img2);

                v1.setImageBitmap(bmp1);
                v2.setImageBitmap(bmp2);
            }  });

        alert = builder.create();
        alert.show();


    }

    public void filter_copy(View v) {
        for (int i =0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int c =bmp1.getPixel(i,j);
                bmp2.setPixel(i,j,c);
            }
        }
    }

    public void filter_invert(View v) {
        for (int i =0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int c0 =bmp1.getPixel(i,j);
                int r = 255 - Color.red(c0);
                int g =255 - Color.green(c0);
                int b = 255 - Color.blue(c0);

                int c1 = Color.argb(255,r,g,b);
                bmp2.setPixel(i,j,c1);
            }
        }
    }

    public void filter_greyTones(View v) {
        for (int i =0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int c0 =bmp1.getPixel(i,j);
                int r =  Color.red(c0);
                int g =Color.green(c0);
                int b = Color.blue(c0);

                double res = (r+g+b) * 0.33;

                int c1 = Color.argb(255,(int)res,(int)res,(int)res);
                bmp2.setPixel(i,j,c1);
            }
        }
    }
    public void filter_BlackWhite(View v)  {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final SeekBar sb = new SeekBar(this);
        sb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sb.setMax(255);
        builder.setView(sb);
        builder.setMessage("Set black/white ratio (better with more than half)");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            BlackWhiteChange(sb.getProgress());
        }  });

        alert = builder.create();
        alert.show();
    }

        public void BlackWhiteChange(int p) {

            filter_greyTones(new View(this));
            for (int i =0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int c0 =bmp1.getPixel(i,j);
                    int r =  Color.red(c0);
                    int g =Color.green(c0);
                    int b = Color.blue(c0);

                    double res = (r+g+b) * 0.33;

                    int c1 = res <= p ? Color.BLACK : Color.WHITE;
                    bmp2.setPixel(i,j,c1);
                }
            }
        }

        public void filter_Brightness(View v) {
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final SeekBar sb = new SeekBar(this);
            sb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sb.setMax(255);
            builder.setView(sb);
            builder.setMessage("Set ratio of brightness (bigger - brighter)");
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    filter_BrightnessChange(255 - sb.getProgress());
                }  });

            alert = builder.create();
            alert.show();

        }

        public void filter_BrightnessChange(int p) {
            for (int i =0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int c0 = bmp1.getPixel(i, j);
                    int r = Color.red(c0);
                    int g = Color.green(c0);
                    int b = Color.blue(c0);
                    c0 = Color.argb(p, r, g, b);

                    bmp2.setPixel(i, j, c0);
                }
            }
        }

        public void filter_contrast(View v) {
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final SeekBar sb = new SeekBar(this);
            sb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sb.setMax(200);
            sb.setProgress(100);
            builder.setView(sb);
            builder.setMessage("Set ratio of contrast");
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    filter_ContrastChange(sb.getProgress()-100);
                }  });

            alert = builder.create();
            alert.show();

        }
        public void filter_ContrastChange(int lvl){
            double blue = 0;
            double green = 0;
            double red = 0;

            double c = Math.pow((100.0 + lvl) / 100.0, 2);

            for (int i =0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int c0 = bmp1.getPixel(i, j);
                    int r = Color.red(c0);
                    int g = Color.green(c0);
                    int b = Color.blue(c0);

                    double R = ((((r / 255.0) - 0.5) * c) + 0.5) * 255.0;
                    double G = ((((g / 255.0) - 0.5) * c) + 0.5) * 255.0;
                    double B = ((((b / 255.0) - 0.5) * c) + 0.5) * 255.0;

                    if (R > 255) {
                        R = 255;
                    } else if (R < 0) {
                        R = 0;
                    }
                    if (G > 255) {
                        G = 255;
                    } else if (G < 0) {
                        G = 0;
                    }
                    if (B > 255) {
                        B = 255;
                    } else if (B < 0) {
                        B = 0;
                    }
                    bmp2.setPixel(i, j, Color.argb(255, (int) R, (int) G, (int) B));
                }
            }
        }

        public void filter_VerticalFlip(View v) {
            for (int i =0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int c0 = bmp1.getPixel(i, j);
                    bmp2.setPixel(width - i-1, height - j-1, c0);
                }
            }
        }

        public void filter_HorizontalFlip(View v) {
            for (int i =1; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int c0 = bmp1.getPixel(i, j);
                    bmp2.setPixel(width - i -1 , j, c0);
                }
            }

        }

}