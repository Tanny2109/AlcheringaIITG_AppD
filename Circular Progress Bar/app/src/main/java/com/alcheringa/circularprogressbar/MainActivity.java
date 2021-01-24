package com.alcheringa.circularprogressbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.newsapp.aavaaz.app.secondpage.Homeis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressbar;
    File imagepath;
    Button share;
    private TextView progresstext;
    int steps = 0;
    int perc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = findViewById(R.id.progress_bar);
        progresstext = findViewById(R.id.progress_text);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Button btn = findViewById(R.id.animate);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fadein);

                img.setVisibility(View.VISIBLE);
                img.startAnimation(animation);

            }
        });

        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap=takescreen();
                saveBitmap(bitmap);
                shareit();
            }
        });
    }

    private void updateProgressbar(){
        perc = steps/10000;
        progressbar.setProgress(perc);
        progresstext.setText("" + perc + "%");
    }

    public void onClick(View view) {
        if(steps<=990000){
            steps+=10000;
            try{
                updateProgressbar();
            }catch (Exception e){

            }

        }
    }


    public void saveBitmap(Bitmap bitmap){
        imagepath=new File(Environment.getExternalStorageDirectory() +"/Pictures/"+"screenshot.jpg");
        FileOutputStream fos;
        String path;
        //File file=new File(path);
        try{
            fos=new FileOutputStream(imagepath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        }catch(FileNotFoundException e){
        }
        catch(IOException e){}
    }
    public void shareit(){
        Uri path= FileProvider.getUriForFile(getBaseContext(),"com.alcheringa.circularprogressbar",imagepath);
        Intent share=new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = Uri.fromFile(imagepath);
        share.putExtra(Intent.EXTRA_STREAM, uri);
//        share.putExtra(Intent.EXTRA_STREAM,path);
        share.setType("image/*");
        startActivity(Intent.createChooser(share,"Share..."));
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(imagepath);
//        intent.setDataAndType(uri,"image/jpeg");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);

    }

    private Bitmap takescreen(){
        View root=findViewById(android.R.id.content).getRootView();
        root.setDrawingCacheEnabled(true);
        return root.getDrawingCache();

    }
}

