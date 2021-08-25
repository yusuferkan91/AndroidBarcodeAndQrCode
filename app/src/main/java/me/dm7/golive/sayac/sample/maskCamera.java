package me.dm7.golive.sayac.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.dnn.Dnn;
//import org.opencv.dnn.Net;
//import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.dm7.barcodescanner.zbar.sample.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class maskCamera extends AppCompatActivity implements SurfaceHolder.Callback {
    Camera camera;
    FrameLayout frameLayout;
    ImageButton button;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ImageButton flash;
    View viewImg;
    TextView str2;
    ProgressDialog progressDialog;
    ArrayList<Bitmap> bitmapArray;
    Drawable drawable;
    static int maskWidth;
    static int maskHeigth;
    int Tserisi;
    Camera.Parameters params;
    public static boolean previewing = false;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        logSave.logAdd("maskCamera onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.border_layout);
        //progressDialog = new ProgressDialog(this);
        bitmapArray = new ArrayList<Bitmap>();
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Görüntü yükleniyor...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        button = (ImageButton) findViewById(R.id.button);
        flash = (ImageButton) findViewById(R.id.flash);
        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashoff, getApplicationContext().getTheme()));;
        viewImg = (View) findViewById(R.id.border_camera);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_fra);
        FrameLayout.LayoutParams paramslayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT,Gravity.CENTER);
        str2 = new TextView(this);
        Tserisi = 1;
        str2.setText("T" + Tserisi +" numaralı ekranı okutun");
        //str2.setText("Sayaç Numaralarını Okutun");
        str2.setTextColor(Color.GREEN);
        str2.setTextSize(20);
        contentFrame.addView(str2,paramslayout);
        surfaceView = new SurfaceView(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        frameLayout.addView(surfaceView);
        if(maskWidth > 0 && maskHeigth >0)
        {
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(maskWidth,maskHeigth);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            viewImg.setLayoutParams(params);
            System.out.println("maskWidth  if   "+maskWidth);
            //viewImg.setMinimumHeight(maskHeigth);
        }
        else{
            //viewImg.setLayoutParams(new ViewGroup.LayoutParams(350, 70));
            final float scale = getResources().getDisplayMetrics().density;
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)scale*350,(int)scale*120);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            viewImg.setLayoutParams(params);
            //viewImg.setMinimumHeight(70);
            System.out.println("maskWidth   else  "+maskWidth);
        }
        if(!previewing){
            camera = Camera.open();
            if (camera != null){
                try {
                    camera.setDisplayOrientation(0);
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                    previewing = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
            if(camera != null)
            {   Toast.makeText(maskCamera.this, PostInfo.modelName, Toast.LENGTH_SHORT).show();

                logSave.logAdd("Fotoğraf çekildi.");
                progressDialog.show();
                camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
            }
            }
        });
        flash.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                drawable = flash.getDrawable();

                // TODO Auto-generated method stub
                if(camera != null)
                {

                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashon).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashoff, getApplicationContext().getTheme()));
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(params);



                    }
                    else if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashoff).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashauto, getApplicationContext().getTheme()));;
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        camera.setParameters(params);

                    }
                    else if(drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashauto).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashon, getApplicationContext().getTheme()));;
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(params);

                    }
                }
                logSave.logAdd("Fotoğraf çekildi.");

            }
        });
    }
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){

        public void onShutter() {
            // TODO Auto-generated method stub
            logSave.logAdd("maskCamera myShutterCallback");
        }};
    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            logSave.logAdd("maskCamera myPictureCallback_RAW");
        }};
    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            camera.stopPreview();
            logSave.logAdd("maskCamera myPictureCallback_JPG");
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            PostInfo.OrBmpSize = String.valueOf(bitmapPicture.getHeight()) + "*" + String.valueOf(bitmapPicture.getWidth());
            //Bitmap modelBitmap = model(bitmapPicture);
//            modelRead.bmp = bitmapPicture;
//            Bitmap modelBitmap = modelRead.detect();
//            PostInfo.image = modelBitmap;
//            //Normalde resmin gönderildiği yer
//            PostInfo.PhotoPath=saveToInternalStorage(modelBitmap);
            //PostInfo.image = bitmapPicture;
            //PostInfo.PhotoPath = saveToInternalStorage(PostInfo.getFullimage());
            logSave.logAdd("İlk Görüntü = Bitmap Height: " + bitmapPicture.getHeight() + " BitmapWidth: " + bitmapPicture.getWidth());
            try {
                if (bitmapPicture.getWidth()>bitmapPicture.getHeight()){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmapPicture = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);
                    logSave.logAdd("Bitmap 90 derece döndürme başarılı");
                }
            }catch (Exception e){
                logSave.logAdd("Bitmap 90 derece döndürme başarısız Hata: " + e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            float x=(viewImg.getX()*bitmapPicture.getWidth())/frameLayout.getWidth();
            float y=(viewImg.getY()*bitmapPicture.getHeight())/frameLayout.getHeight();
            float w=(viewImg.getWidth()*bitmapPicture.getWidth())/frameLayout.getWidth();
            float h=(viewImg.getHeight()*bitmapPicture.getHeight())/frameLayout.getHeight();
            Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture,  (int) x, (int) y, (int) w , (int) h, null, true);
            logSave.logAdd("Maske boyutları= x: " + x + " y: " + y + " w: " + w + " h: " + h );

            modelRead.bmp = correctBmp;
            Bitmap modelBitmap = modelRead.detect();
            bitmapArray.add(modelBitmap);
            Tserisi = Tserisi + 1;
            str2.setText("T" + Tserisi +" numaralı ekranı okutun");
            if(bitmapArray.size() == 4)
            {
                Bitmap topBmp = bitmapArray.get(0);
                for(int j = 1; j< bitmapArray.size();j++)
                {
                    topBmp = combineImages(topBmp,bitmapArray.get(j));
                }
                bitmapArray.clear();
                //PostInfo.image = modelBitmap;
                PostInfo.image = topBmp;
                //Normalde resmin gönderildiği yer
                //PostInfo.PhotoPath=saveToInternalStorage(modelBitmap);
                PostInfo.PhotoPath=saveToInternalStorage(topBmp);

                logSave.logAdd("Maskelenmiş fotoğraf path: " + PostInfo.PhotoPath);
                System.out.println("saveToInternalStorage(correctBmp)     "+saveToInternalStorage(correctBmp));
                System.out.println("PostInfo.PhotoPath       "+PostInfo.PhotoPath);
                logSave.logAdd("Fotoğraf yüklendi. MaskCamera kapatıldı.");
                //uploadFile();
                progressDialog.dismiss();
                finish();
                launchActivity(noteactivity.class);
            }
            camera.startPreview();
            progressDialog.dismiss();
        }};
    @Override
    public void onBackPressed() {
        logSave.logAdd("Back Click");
        finish();
        launchActivity(sayacmain.class);
    }
    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getHeight() > s.getHeight()) {
            height = c.getHeight() + s.getHeight();
            width = c.getWidth();
        } else {
            height = s.getHeight() + s.getHeight();
            width = c.getWidth();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);
        return cs;
    }
//    private void uploadFile() {
//        logSave.logAdd("uploadFile void");
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
//                System.out.println("mesaj:  " + serverResponse.getMessage());
//                if (serverResponse != null) {
//                    PostInfo.endeks = serverResponse.getMessage();
//                } else {
//                    assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
//                    logSave.logAdd("Response: " + serverResponse.toString());
//                }
//                progressDialog.dismiss();
//                finish();
//                launchActivity(noteactivity.class);
//            }
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                System.out.println(t.getMessage());
//                logSave.logAdd("onFailure: " + t.getMessage());
//            }
//        });
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=getOutputMediaFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            logSave.logAdd("Fotoğraf Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e)
            {
                logSave.logAdd("Fotoğraf IOException: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }
        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
                logSave.logAdd("SurfaceChanged Başarılı");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logSave.logAdd("SurfaceChanged hata: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        params = camera.getParameters();

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //params.setSceneMode(Camera.Parameters.SC);
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSizes = sizes.get(0);
        for(int i=0;i<sizes.size();i++)
        {
            if(sizes.get(i).width > mSizes.width)
                mSizes = sizes.get(i);
        }
        params.setPictureSize(mSizes.width, mSizes.height);
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        camera.setParameters(params);
        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            logSave.logAdd("SurfaceCreated Başarılı");
        }
        catch (IOException e)
        {
            logSave.logAdd("surfaceCreated hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        logSave.logAdd("SurfaceDestroyed Başarılı");
        launchActivity(MainActivity.class);
        }
    };
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                logSave.logAdd("getOutputMediaFile Return File: Null");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        logSave.logAdd("getOutputMediaFile Return File: " + mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
//    public Bitmap model(Bitmap bmp){
//        PostInfo.modelName = "Faster_RCNN_Endeks.pb";
//        System.out.println("bmp.getHeight(): "+bmp.getHeight());
//        String proto = getPath("Faster_RCNN_Endeks.pbtxt",this);
//        String weights = getPath(PostInfo.modelName,this);
//        System.out.println("*******************"+proto+"******"+weights);
//        net = Dnn.readNetFromTensorflow(weights,proto);
//        final double THRESHOLD = 0.5;
//        // Get a new frame
//        Mat frame = new Mat();
//        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, frame);
//        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
//        System.out.println("framerows:"+frame.rows());
//        System.out.println("framecols:"+frame.cols());
//        System.out.println("framechannels():"+frame.channels());
//        if (frame.rows()>800)
//        {
//            float a = (float) 800/frame.rows();
//            System.out.println("a:  "+a);
//            Size sz = new Size(frame.cols()*a,frame.rows()*a);
//            Imgproc.resize(frame,frame,sz);
//        }
//        // Forward image through network.
//        Mat blob = Dnn.blobFromImage(frame, 1.0, new Size(frame.cols(), frame.rows()),new Scalar(1,1,1),true);
//        net.setInput(blob);
//        System.out.println("blob:"+blob);
//        System.out.println("blobsize:"+blob.size());
//        Mat detections = net.forward();
//        int cols = frame.cols();
//        int rows = frame.rows();
//        detections = detections.reshape(1, (int)detections.total() / 7);
//        for (int i = 0; i < detections.rows(); ++i) {
//            double confidence = detections.get(i, 2)[0];
//            if (confidence > THRESHOLD) {
//                int classId = (int)detections.get(i, 1)[0];
//                int left   = (int)(detections.get(i, 3)[0] * cols);
//                int top    = (int)(detections.get(i, 4)[0] * rows);
//                int right  = (int)(detections.get(i, 5)[0] * cols);
//                int bottom = (int)(detections.get(i, 6)[0] * rows);
//                System.out.println("+++++++++++++"+classId+"*"+left+"*"+top);
//                // Draw rectangle around detected object.
//                Imgproc.rectangle(frame, new Point(left, top), new Point(right, bottom),
//                        new Scalar(0, 255, 0));
//                String label = classNames[classId] + ": " + confidence;
//                int[] baseLine = new int[1];
//                Size labelSize = Imgproc.getTextSize(label, Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
//                // Draw background for label.
//                Imgproc.rectangle(frame, new Point(left, top - labelSize.height),
//                        new Point(left + labelSize.width, top + baseLine[0]),
//                        new Scalar(255, 255, 255), Core.FILLED);
//                // Write class name and confidence.
//                Imgproc.putText(frame, label, new Point(left, top),
//                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0));
//            }
//        }
//        Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(),Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(frame, bm);
//        return bm;
//    }
//    private String getPath(String file, Context context) {
//        System.out.println("*************"+context.getAssets());
//        AssetManager assetManager = context.getAssets();
//        BufferedInputStream inputStream = null;
//        try {
//            // Read data from assets.
//            inputStream = new BufferedInputStream(assetManager.open(file));
//            byte[] data = new byte[inputStream.available()];
//            inputStream.read(data);
//            inputStream.close();
//            // Create copy file in storage.
//            File outFile = new File(context.getFilesDir(), file);
//            FileOutputStream os = new FileOutputStream(outFile);
//            os.write(data);
//            os.close();
//            // Return a path to file which may be read in common way.
//            return outFile.getAbsolutePath();
//        } catch (IOException ex) {
//
//        }
//        return "";
//    }
//    private final String[] classNames = {"endeks"};
//    private Net net;
//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:
//                {
//
//                } break;
//                default:
//                {
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        if (!OpenCVLoader.initDebug()) {
//
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
//        } else {
//
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
//    }
}
