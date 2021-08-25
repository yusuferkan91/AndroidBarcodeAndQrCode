package me.dm7.golive.sayac.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zbar.sample.R;

public class new_user_account extends AppCompatActivity {
    EditText isim;
    EditText soyisim;
    EditText ilce;
    EditText adres;
    Button kaydet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);
        isim = (EditText) findViewById(R.id.txt_ad);
        soyisim = (EditText) findViewById(R.id.txt_soyad);
        ilce = (EditText) findViewById(R.id.txt_ilce);
        adres = (EditText) findViewById(R.id.txt_adres);
        kaydet = (Button) findViewById(R.id.btnKaydet);
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kaydetClick();
            }
        });
    }

    private void kaydetClick() {
        if(isim != null && soyisim != null && ilce != null && adres != null){
            userControll user = new userControll();
            user.isim = isim.getText().toString();
            user.soyisim = soyisim.getText().toString();
            user.ilce = ilce.getText().toString();
            user.adres = adres.getText().toString();
            user.saveUser(this);
            launchActivity(userControll.class);
        }
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(PostInfo.isim != null){
            finish();
            launchActivity(userControll.class);
        }else{
            finish();
        }
    }
}
