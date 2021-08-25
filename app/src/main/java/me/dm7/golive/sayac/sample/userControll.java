package me.dm7.golive.sayac.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;

import me.dm7.barcodescanner.zbar.sample.R;

public class userControll extends Activity {
    public String isim;
    public String soyisim;
    public String ilce;
    public String adres;
    private SharedPreferences myPrefs;
    Button yeni_kayit;
    Button kayitli_giris;
    ViewGroup linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_giris);
        //modelRead.context = this;
        //modelRead.modelLoad();
        modelRead.modelname="faster_rcnn_inception_v2_elektrik_7000_pro_aug3_2m.pb";
        modelRead.modelpb="faster_rcnn_inception_v2_elektrik_7000_pro_aug3_2m.pbtxt";
        modelRead.context=userControll.this;
        modelRead.modelLoad();

        yeni_kayit = (Button) findViewById(R.id.yeni_kayit);
        kayitli_giris = (Button) findViewById(R.id.kayitli_giriş);
        linearLayout = (ViewGroup) findViewById(R.id.users);
        yeni_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(new_user_account.class);
            }
        });
        kayitli_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchActivity(Select_model.class);
                launchActivity(MainActivity.class);
            }
        });
        String users = backUser();
        System.out.println(users);
        if(users != null){
            String[] details = users.split("@");
            PostInfo.isim = details[0];
            PostInfo.soyisim = details[1];
            PostInfo.ilce = details[2];
            PostInfo.adres = details[3];
            kayitli_giris.setText(PostInfo.isim+ " " + PostInfo.soyisim + " olarak devam et");
        }
        else{
            yeni_kayit.callOnClick();
        }
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
        finish();
    }
    public String dataConcat(){
        return isim + "@" + soyisim + "@" + ilce + "@" + adres;
    }

    void saveUser(Context context){
        String strUser = dataConcat();
        System.out.println(strUser);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPrefs.edit();

        editor.putString("message",strUser);
        editor.apply();
    }
    public String backUser(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);;
        if(prefs.contains("message")){
            String message = prefs.getString("message", "not found");
            System.out.println("*********"+message);
            return message;
        }
        else{
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
