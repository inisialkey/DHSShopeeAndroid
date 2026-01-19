package com.fungsitama.dhsshopee.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fungsitama.dhsshopee.R;

public class PeringatanAkunActivity extends AppCompatActivity {

    private Button login;
    private TextView daftar,konfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_akun);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registrasi Berhasil Dilakukan");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        login = findViewById(R.id.login);
        daftar = findViewById(R.id.daftar);
        konfirmasi = findViewById(R.id.konfirmasi);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeringatanAkunActivity.this, LoginActivity.class);
                (PeringatanAkunActivity.this).finish();
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeringatanAkunActivity.this, RegisterActivity.class);
                (PeringatanAkunActivity.this).finish();
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeringatanAkunActivity.this, AktivasiAkunActivity.class);
                (PeringatanAkunActivity.this).finish();
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });


    }
}