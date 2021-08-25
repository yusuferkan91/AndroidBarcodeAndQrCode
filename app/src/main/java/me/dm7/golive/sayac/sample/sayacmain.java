package me.dm7.golive.sayac.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zbar.sample.R;

public class sayacmain extends AppCompatActivity {
    Button button;
    EditText width;
    EditText heigth;
    EditText treshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_sayac);
        button = (Button) findViewById(R.id.buttonsayac);
        width = (EditText) findViewById(R.id.txt_maskWidth);
        heigth =(EditText) findViewById(R.id.txt_maskHeight);
        treshold = (EditText) findViewById(R.id.txt_treshold);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSayacClick();
            }
        });
    }
    private void btnSayacClick() {
        logSave.logAdd("Kamera başlatıldı.");
        final float scale = getResources().getDisplayMetrics().density;
        if(width.getText().toString().length() >0 || heigth.getText().toString().length() >0)
        {
            String value1= width.getText().toString();
            int finalValue1=Integer.parseInt(value1);
            String value2= heigth.getText().toString();
            int finalValue2=Integer.parseInt(value2);
            maskCamera.maskHeigth=(int) scale*finalValue2;
            maskCamera.maskWidth=(int) scale*finalValue1;
            System.out.println("sayacmain  if   "+width.getText().toString());
            modelRead.THRESHOLD = Double.parseDouble(treshold.getText().toString());
        }


        launchActivity(maskCamera.class);
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        PostInfo.Barcode = null;
        finish();
        launchActivity(MainActivity.class);
    }
}
