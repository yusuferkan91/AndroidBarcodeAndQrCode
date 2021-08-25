package me.dm7.golive.sayac.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.dm7.barcodescanner.zbar.sample.R;

public class Select_model extends AppCompatActivity {
    Button digitalButon;
    Button mekanikButon;
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
        launchActivity(userControll.class);

    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        digitalButon = (Button) findViewById(R.id.digital);
        mekanikButon = (Button) findViewById(R.id.mekanik);
        digitalButon.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                modelRead.modelname="faster_rcnn_inception_v2_elektrik_1500_pro_35.pb";
                modelRead.modelpb="faster_rcnn_inception_v2_elektrik_1500_pro_35.pbtxt";
                modelRead.context=Select_model.this;
                modelRead.modelLoad();
                launchActivity(MainActivity.class);

            }
        });
        mekanikButon.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                modelRead.modelname="faster_rcnn_inception_v2_digit_pro35_br_cns_hue.pb";
//                modelRead.modelpb="faster_rcnn_inception_v2_digit_pro35_br_cns_hue.pbtxt";
//                modelRead.context=Select_model.this;
//                modelRead.modelLoad();
//                launchActivity(MainActivity.class);

            }
        });
    }
}
