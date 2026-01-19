package com.fungsitama.dhsshopee.activity.pdf;

import android.content.Intent;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fungsitama.dhsshopee.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class PDFViewActivity extends AppCompatActivity implements
        OnPageErrorListener{
    String namePdf;
    Integer pageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view);
        Intent intent = getIntent();
        namePdf = intent.getStringExtra("namepdf");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" Detail "+namePdf);
        getSupportActionBar().setIcon(R.drawable.ic_action_sigap);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //PDF View
        PDFView pdfView = findViewById(R.id.pdfView);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/sigap/" +namePdf);
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)

        .load();


    }


    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            onBackPressed(); //replaced
        }
        return true;
    }
    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("TAG", "Cannot load page " + page);
    }
}