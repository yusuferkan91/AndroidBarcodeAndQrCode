package me.dm7.golive.sayac.sample;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import net.glxn.qrgen.android.QRCode;

public class PostInfo {
    public static String Barcode;
    public static String PhotoPath;
    public static String endeks;
    public static List<String> strTSerisi = new ArrayList<String>();
    public static String isim;
    public static String soyisim;
    public static String ilce;
    public static String adres;
    public static String tarih;
    public static String saat;
    public static String paketVersion;
    public static Bitmap image;
    public static String modelName;
    public static String OrBmpSize;
    public static String modelBmpSize;
    public static void postClear(){
        Barcode = null;
        PhotoPath = null;
//        isim = null;
//        soyisim = null;
//        ilce = null;
//        adres = null;
        tarih =null;
        saat = null;

    }
    public static void getData(){
        tarih =  new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        saat =  new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//        isim = "Yusuf";
//        soyisim = "Erkan";
//        ilce = "Ataşehir";
//        adres = "Esatpaşa mah. No: 29";
    }
    public static Bitmap generateQR(){
        Bitmap myBitmap = QRCode.from("Sayaç numarası: " + Barcode + "\n"
                                       + "Endeks: " + endeks + "\n"
                                        + "İsim: " + isim + "\n"
                                        + "Soyisim: " + soyisim + "\n"
                                        + "İlçe: " + ilce + "\n"
                                        + "Adres: " + adres + "\n"
                                        + "Tarih: " + tarih + "\n"
                                        + "Saat: " + saat + "\n"
                                        + "Uygulama Version: " + paketVersion + "\n"
                                        + "Model ismi: " + modelName).withSize(1000,1000).withCharset("UTF-8").bitmap();

//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
//                myBitmap, 1000, 1000, false);
        return myBitmap;
    }
    public static Bitmap getFullimage(){

        if (image == null || generateQR() == null)
            return image;
        int height = image.getHeight();
        if (height < generateQR().getHeight())
            height = generateQR().getHeight();

        Bitmap bmOverlay = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(image, 0, 0, null);
        canvas.drawBitmap(generateQR(), 0, image.getHeight()-generateQR().getHeight(), null);
        return bmOverlay;
    }
    public static Bitmap getMaskImage(){
        Bitmap returnBitmap = null;
        try{
            Bitmap bmp = BitmapFactory.decodeFile(PhotoPath);
            returnBitmap=modifyOrientation(bmp,PhotoPath);
        }catch (Exception e) {
        e.printStackTrace();
        }
        return returnBitmap;
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
}
