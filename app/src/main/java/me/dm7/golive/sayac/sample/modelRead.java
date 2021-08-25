package me.dm7.golive.sayac.sample;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class modelRead {
    public static Net net;
    public static Net net1;

    public static Context context;
    public static Bitmap bmp;
    private static final String[] classNames = {"0","1","2","3","4","5","6","7","8","9"};
    public static Integer arr[];
    public static Map<Integer,String> mapL =  new HashMap<Integer,String>();
    public static Map<Integer,String> mapR =  new HashMap<Integer,String>();
    public static String modelname;
    public static String modelpb;
    public static int toppixel;
    public static int botpixel;
    private static double[] colorpixel = {};
    public static double THRESHOLD;

    public static void modelLoad(){
        if (!OpenCVLoader.initDebug()) {
            System.out.println("*************************if");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback);
        } else {
            System.out.println("*************************else");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        PostInfo.modelName = modelname;
        String proto = getPath(modelpb,context);
        String weights = getPath(PostInfo.modelName,context);
        String digits = getPath("digit_model.pb",context);
        net = Dnn.readNetFromTensorflow(weights,proto);
        net.setPreferableTarget(Dnn.DNN_TARGET_OPENCL);
    }
    private static String getPath(String file, Context context) {
        System.out.println("*************"+context.getAssets());
        AssetManager assetManager = context.getAssets();
        BufferedInputStream inputStream = null;
        try {
            // Read data from assets.
            inputStream = new BufferedInputStream(assetManager.open(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // Create copy file in storage.
            File outFile = new File(context.getFilesDir(), file);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {

        }
        return "";
    }
    public static Bitmap detect(){
        mapL.clear();
        mapR.clear();

        //final double THRESHOLD = 0.1;
        // Get a new frame
        Mat frame = new Mat();
        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmpnew = bmp.copy(Bitmap.Config.ARGB_8888, true);

        Utils.bitmapToMat(bmp32, frame);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        System.out.println("framerows:"+frame.rows());
        System.out.println("framecols:"+frame.cols());
        System.out.println("framechannels():"+frame.channels());

        Mat blob = Dnn.blobFromImage(frame, 1.0, new Size(500,100),new Scalar(1,1,1),true);

        net.setInput(blob);

        Timer.start();
        Mat detections = net.forward();
        Timer.stop();
        double seconds = Timer.getElapsedSeconds();
        System.out.println("Geçen süre " + seconds + " saniyedir" );
        int cols = frame.cols();
        int rows = frame.rows();
        System.out.println("detections.rows()"+detections.rows() );

        detections = detections.reshape(1, (int)detections.total() / 7);
        System.out.println("detections.rows()"+detections.rows() );

        for (int i = 0; i < detections.rows(); ++i) {

            double confidence = detections.get(i, 2)[0];

            if (confidence > THRESHOLD) {

                int classId = (int)detections.get(i, 1)[0];
                int left   = (int)(detections.get(i, 3)[0] * cols);
                int top    = (int)(detections.get(i, 4)[0] * rows);
                int right  = (int)(detections.get(i, 5)[0] * cols);
                int bottom = (int)(detections.get(i, 6)[0] * rows);
//                Mat frameProb = new Mat();
////                resultBmp = Bitmap.createBitmap(bmp32,left-25,top-25,(right-left)+25, (bottom-top)+25);
//
////                canvas.drawBitmap(resultBmp,left,0,null);
//                Utils.bitmapToMat(resultBmp, frameProb);
//                Imgproc.cvtColor(frameProb, frameProb, Imgproc.COLOR_RGBA2RGB);
//
//                Mat blob1 = Dnn.blobFromImage( frameProb, 1.0/255.0, new Size(28,28));
//
//                net1.setInput(blob1);
//                Mat probs = net1.forward();
//                double confidence1=0;
//                int digit=-1;
//                for (int j = 0; j < probs.cols(); ++j) {
//                    if (confidence1<probs.get(0, j)[0]){
//                        confidence1=probs.get(0, j)[0];
//                        digit=j;
//                    }
//                }
//                System.out.println("confidence1------------"+digit);
                //System.out.println("+++++++++++++"+classId+"*"+left+"*"+top);
                // Draw rectangle around detected object.
                Imgproc.rectangle(frame, new Point(left, top), new Point(right, bottom),
                        new Scalar(0, 255, 0),2);

                String label = classNames[classId];
                mapL.put(left,label);
                mapR.put(right,label);
                toppixel=top;
                botpixel=bottom;
                int[] baseLine = new int[1];
                Size labelSize = Imgproc.getTextSize(label, Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, 2, baseLine);
//                // Draw background for label.
                Imgproc.rectangle(frame, new Point(left, top - labelSize.height),
                        new Point(left + labelSize.width, top + baseLine[0]),
                        new Scalar(255, 255, 255), Core.FILLED);
                // Write class name and confidence.
                Imgproc.putText(frame, label, new Point(left, top),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0));



                ;//break;
            }
        }
        Map<Integer, String> sortedMapL = new TreeMap<Integer, String>(mapL);
        Map<Integer, String> sortedMapR = new TreeMap<Integer, String>(mapR);
//        String edx = "";
//        for(int i=0;i<sortedMapL.size();i++){
//            edx = edx + sortedMapL.values().toArray()[i];
//        }
//        if(edx != ""){
//            System.out.println("edx--------"+edx);
//            PostInfo.strTSerisi.add(edx);
//        }else{
//            PostInfo.strTSerisi.add("-----------");
//        }


        MissingNumbers mnumber= new MissingNumbers();
        try{
            frame=mnumber.missing(sortedMapL,sortedMapR,toppixel,botpixel,frame,colorpixel,bmpnew);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Imgproc.putText(frame,"Sure: " + seconds,new Point(0, 50) ,Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0),3);
        Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(frame, bm);
        PostInfo.modelBmpSize = String.valueOf(bm.getHeight()) + "*" + String.valueOf(bm.getWidth());
        return bm;
    }
    private static BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
};

