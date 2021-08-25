package me.dm7.golive.sayac.sample;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.sourceforge.zbar.ImageScanner;

import java.io.File;
import java.io.IOException;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zbar.sample.R;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PERMISSION_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;
    private ZBarScannerView mScannerView;
    ImageScanner scanner;
    Bitmap barcodeGaleri;
    Button btnUpload, btnMulUpload, btnPickImage, btnPickVideo;
    String mediaPath, mediaPath1;
    ImageView imgView;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    ProgressDialog progressDialog;
    TextView str1, str2;
    File photoFile;
    ImageView imageView;
    Uri image;
    String mCameraFileName;
    String outPath;
    Button btnBarcode;
    Button btnAtla;
    public boolean isbarcode=false;
    public boolean isSayac=false;

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Endeks işleniyor...");
        //logSave.textview = (TextView) findViewById(R.id.logAdd);
        logSave.logAdd("Main activity onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnBarcode = (Button) findViewById(R.id.buttonBarkod);
        btnAtla = (Button) findViewById(R.id.btnatla);
        btnAtla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAtlaClick();
            }
        });
        //btnSayac = (Button) findViewById(R.id.btnSayac);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            PostInfo.paketVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //PostInfo.paketVersion = getApplicationContext().getPackageName();
        //PostInfo.paketVersion =BuildConfig.APPLICATION_ID;

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBarkodClick();
            }
        });
//        btnSayac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnSayacClick();
//            }
//        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        btnUpload.setEnabled(false);
        requestPermissions();
        //str1 = (TextView) findViewById(R.id.filename1);
//        if (PostInfo.Barcode == null) {
//            str1.setText("SAYAC NO : 0");
//        }
//        else {
//            str1.setText("SAYAC NO : " + PostInfo.Barcode);
//        }

        try {
            Bitmap bmp = BitmapFactory.decodeFile(PostInfo.PhotoPath);
            Bitmap returnBitmap=modifyOrientation(bmp,PostInfo.PhotoPath);
            //imgView.setImageBitmap(returnBitmap);
        } catch (Exception e) {
            //imgView.setImageBitmap(bmp);
            e.printStackTrace();
        }        //setupToolbar();
        //btnVisible();
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadFile();
//            }
//        });
    }

    private void btnAtlaClick() {

        PostInfo.Barcode=null;
        launchActivity(sayacmain.class);
    }

    void btnBarkodClick(){
        logSave.logAdd("btnBarkodClick ");
        launchActivity(SimpleScannerActivity.class);
        if(PostInfo.Barcode != null)
        {
            isbarcode = true;
        }
        else{
            isbarcode = false;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
//        logSave.logAdd("Back Click");
//        if(PostInfo.PhotoPath ==null && PostInfo.Barcode==null)
//        {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("Uygulamadan Çıkılsın mı?");
//            builder.setNeutralButton("Hayır", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int id) {
//                    logSave.logAdd("Çıkıştan vazgeçildi.");
//                }
//            });
//            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    System.exit(0);
//                }
//            });
//            builder.show();
//        }
//        PostInfo.PhotoPath = null;
//        PostInfo.Barcode = null;
//        str1.setText("SAYAC NO : 0");
//        imgView.setImageDrawable(null);
//        str2.setText(null);
        //btnVisible();
    }
//    void btnSayacClick(){
//        logSave.logAdd("btnSayacClick");
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Görüntü Kaynağı Seçin");
//        builder.setNeutralButton("Galeri", new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int id) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, 0);
//                logSave.logAdd("Galeriye girildi.");
//            }
//        });
//        builder.setPositiveButton("Kamera", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                logSave.logAdd("Kamera başlatıldı.");
//                launchActivity(maskCamera.class);
//
//            }
//        });
//        if(PostInfo.PhotoPath != null){
//            isSayac = true;
//
//        }else{
//            isSayac = false;
//        }
//        builder.show();
//    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, 0);
            }
        }
    }
//    public void launchSimpleActivity(View v) {
//        launchActivity(SimpleScannerActivity.class);
//    }


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        logSave.logAdd("onActivityResult: requestCode= " + requestCode + " resultCode= " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    image = data.getData();
                    imgView.setImageURI(image);
                    //imgView.setVisibility(View.VISIBLE);
                }
                if (image == null && mCameraFileName != null) {
                    image = Uri.fromFile(new File(mCameraFileName));
                    imgView.setImageURI(image);
                }
                File file = new File(mCameraFileName);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            String finalFile = outPath;
            mediaPath = finalFile;

            //btnVisible();
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                Toast.makeText(this, "You picked image", Toast.LENGTH_LONG).show();

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                PostInfo.PhotoPath = cursor.getString(columnIndex);

                // Set the Image in ImageView for Previewing the Media
                //imgView.setImageBitmap(BitmapFactory.decodeFile(PostInfo.PhotoPath));
                Bitmap bmp = BitmapFactory.decodeFile(PostInfo.PhotoPath);
                Bitmap returnBitmap=modifyOrientation(bmp,PostInfo.PhotoPath);
                logSave.logAdd("modifyOrientation çıkış: Bitmap height: " + returnBitmap.getHeight() + " Bitmap width: " + returnBitmap.getWidth());

                // Set the Image in ImageView for Previewing the Media
                imgView.setImageBitmap(returnBitmap);
                cursor.close();
            } // When an Video is picked

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        //btnVisible();
    }
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        logSave.logAdd("modifyOrientation giriş: Bitmap height: " + bitmap.getHeight() + " Bitmap width: " + bitmap.getWidth() + " Path: " + image_absolute_path);

        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
//    private void uploadFile() {
//        logSave.logAdd("uploadFile void");
//
//        progressDialog.show();
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
////                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        // Map is used to multipart the file using okhttp3.RequestBody
//        File file = new File(PostInfo.PhotoPath);
//        // Parsing any Media type file
//        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("key", file.getName(), requestBody);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//        final ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
//        call.enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
//                System.out.println(response.body());
//                ServerResponse serverResponse = response.body();
//                if (serverResponse != null) {
//                    str1.clearComposingText();
//                    str2.clearComposingText();
//                    str2.setText(serverResponse.getMessage());
//                    btnUpload.setEnabled(false);
//                } else {
//                    assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
//                    logSave.logAdd("Response: " + serverResponse.toString());
//                }
//                progressDialog.dismiss();
//            }
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                System.out.println(t.getMessage());
//                logSave.logAdd("onFailure: " + t.getMessage());
//            }
//        });
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        logSave.logAdd("onRequestPermissionsResult: " + "requestCode: " + requestCode);
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {

                    //Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}