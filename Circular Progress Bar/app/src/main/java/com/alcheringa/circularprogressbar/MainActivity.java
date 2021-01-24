package com.alcheringa.circularprogressbar;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.widget.Toast;

//import com.newsapp.aavaaz.app.secondpage.Homeis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressbar;
    File imagepath;
    File file=null;
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
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                {ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);}
//                Bitmap bitmap=takescreen();
//                saveBitmap(bitmap);
//                shareit();
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bmp=getScreenShot(rootView);
                store(bmp,"file.png");
                shareImage(file);
            }
        });


    }
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;

    }
    public void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT);
        }
    }
    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
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
        } catch(IOException ignored){
        }
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

