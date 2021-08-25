package me.dm7.golive.sayac.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import me.dm7.barcodescanner.zbar.sample.R;

public class noteactivity extends AppCompatActivity {
    ImageView imageV;
    ImageView imageQR;
    TextView sayac;
    TextView tarih;
    TextView saat;
    TextView isim;
    TextView soyisim;
    TextView ilce;
    TextView adres;
    TextView OrBmp;
    TextView ModelBmp;
    Button btngonder;
    EditText endeks;
    Button btngeri;
    TextView T1;
    TextView T2;
    TextView T3;
    TextView T4;
    boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        imageV = (ImageView) findViewById(R.id.preview);
        imageQR = (ImageView) findViewById(R.id.qrview);
        sayac = (TextView) findViewById(R.id.txt_sayac);
        tarih = (TextView) findViewById(R.id.txt_tarih);
        saat = (TextView) findViewById(R.id.txt_saat);
        isim = (TextView) findViewById(R.id.txt_ad);
        soyisim = (TextView) findViewById(R.id.txt_soyad);
        ilce = (TextView) findViewById(R.id.txt_ilce);
        adres = (TextView) findViewById(R.id.txt_adres);
        OrBmp = (TextView) findViewById(R.id.orBmpSize);
        ModelBmp = (TextView) findViewById(R.id.modelBmpSize);
        T1 = (TextView) findViewById(R.id.txt_T1);
        T2 = (TextView) findViewById(R.id.txt_T2);
        T3 = (TextView) findViewById(R.id.txt_T3);
        T4 = (TextView) findViewById(R.id.txt_T4);

        btngeri = (Button) findViewById(R.id.buttonGeri);
        btngonder = (Button) findViewById(R.id.buttonGonder);
        //endeks = (EditText) findViewById(R.id.txt_endeks);
        PostInfo.getData();
        sayac.setText(PostInfo.Barcode);
        //endeks.setText(PostInfo.endeks);
        tarih.setText(PostInfo.tarih);
        saat.setText(PostInfo.saat);
        isim.setText(PostInfo.isim);
        soyisim.setText(PostInfo.soyisim);
        ilce.setText(PostInfo.ilce);
        adres.setText(PostInfo.adres);
        OrBmp.setText(PostInfo.OrBmpSize);
        ModelBmp.setText(PostInfo.modelBmpSize);
        System.out.println("tserisi size "+PostInfo.strTSerisi.size());
        try {
            T1.setText(PostInfo.strTSerisi.get(0));

        } catch (Exception e) {
            T1.setText("bulunamadı");

            e.printStackTrace();
        }
        try {
            T2.setText(PostInfo.strTSerisi.get(1));

        } catch (Exception e) {
            T2.setText("bulunamadı");

            e.printStackTrace();
        }
        try {
            T3.setText(PostInfo.strTSerisi.get(2));

        } catch (Exception e) {
            T3.setText("bulunamadı");
            e.printStackTrace();
        }
        try {
            T4.setText(PostInfo.strTSerisi.get(3));

        } catch (Exception e) {
            T4.setText("bulunamadı");

            e.printStackTrace();
        }

        PostInfo.strTSerisi.clear();
        btngeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGonderClick();
            }
        });
        try {
//            Bitmap bmp = BitmapFactory.decodeFile(PostInfo.PhotoPath);
//            Bitmap returnBitmap=modifyOrientation(bmp,PostInfo.PhotoPath);
            //imageV.setImageBitmap(PostInfo.getMaskImage());
            imageV.setImageBitmap(PostInfo.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageQR.setImageBitmap(PostInfo.generateQR());
        imageQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageQR.setImageBitmap(PostInfo.generateQR());
                loadQr(imageQR,PostInfo.generateQR());
            }
        });
        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadPhoto(imageV,PostInfo.getFullimage(),PostInfo.getMaskImage());
                loadQr(imageV,PostInfo.image);
            }
        });
    }
    private void loadQr(final ImageView imageView,Bitmap img){
//        ImageView tempImageView = imageView;
//        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
//                (ViewGroup) findViewById(R.id.layout_root));
//        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
//        Drawable btmp = resize(tempImageView.getDrawable());
//
//        image.setImageDrawable(btmp);
//        imageDialog.setView(layout);
//        imageDialog.setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        imageDialog.create();
//        imageDialog.show();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.custom_fullimage_dialog, null);
        PhotoView photoView = mView.findViewById(R.id.fullimage);
        photoView.setImageBitmap(img);
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, b.getWidth()*3, b.getHeight()*3, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    private void loadPhoto(final ImageView imageView, Bitmap fullImage, Bitmap oldImage) {

        ImageView tempImageView = imageView;
        final Bitmap backimg = oldImage;
        tempImageView.setImageResource(0);
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        tempImageView.setImageBitmap(fullImage);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                imageView.setImageResource(0);
                imageV.setImageBitmap(backimg);
                dialog.dismiss();
            }
        });
        imageDialog.create();
        imageDialog.show();
    }

    private void btnGonderClick() {
        PostInfo.postClear();
        finish();
        //launchActivity(Select_model.class);
        launchActivity(userControll.class);
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        btngeri.callOnClick();
    }
}
