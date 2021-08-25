package me.dm7.golive.sayac.sample;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MissingNumbers extends AppCompatActivity {
    public Mat missing(Map<Integer, String> sortedMapL, Map<Integer, String> sortedMapR, int toppixel, int botpixel, Mat frame, double[] colorpixel, Bitmap bmpnew){
        Mat graybitmap = new Mat();
        int min=9999;
        toppixel=toppixel-20;
        int boxSize=0;
        int bulunamayan=0;
        int currentDistance = 0;
        int newL=0;
        int newR=0;

        System.out.println("sortedMapL.size():"+sortedMapL.size());

        for(int i=0;i<sortedMapL.size()-1;i++){

            if ((int)sortedMapL.keySet().toArray()[i+1]-(int)sortedMapR.keySet().toArray()[i]<min){
                if((int)sortedMapL.keySet().toArray()[i+1]<(int)sortedMapR.keySet().toArray()[i] ){
                    sortedMapL.remove(sortedMapL.keySet().toArray()[i]);
                    sortedMapR.remove(sortedMapR.keySet().toArray()[i]);
                }
                else{
                    min=(int)sortedMapL.keySet().toArray()[i+1]-(int)sortedMapR.keySet().toArray()[i];
                }
            }
        }

        for(int i=0;i<sortedMapL.size();i++) {
            boxSize = boxSize + ((int) sortedMapR.keySet().toArray()[i] - (int) sortedMapL.keySet().toArray()[i]);
        }
        min=min+20;

        boxSize=boxSize/sortedMapL.size();
        Iterator iterator = sortedMapL.entrySet().iterator();

        for(int i=0;i<sortedMapL.size()-1;i++){
            Map.Entry pair = (Map.Entry)iterator.next();
            if (pair.getValue()=="1"){
                currentDistance = ((int)sortedMapL.keySet().toArray()[i+1]-(int)sortedMapR.keySet().toArray()[i])-20;
                newL=(int)sortedMapL.keySet().toArray()[i+1]-min-boxSize-15;
                newR=(int)sortedMapL.keySet().toArray()[i+1]-min-30;
            }
            else{
                currentDistance = (int)sortedMapL.keySet().toArray()[i+1]-(int)sortedMapR.keySet().toArray()[i];
                newL=(int)sortedMapL.keySet().toArray()[i+1]-min-boxSize;
                newR=((int)sortedMapL.keySet().toArray()[i+1]-min)+20;
            }

            while(currentDistance>min+50){
                currentDistance=newL-(int)sortedMapR.keySet().toArray()[i];
                Imgproc.rectangle(frame, new Point(newL, toppixel), new Point(newR, botpixel),
                        new Scalar(255, 0, 0),2);
                newR=newL-min+10;
                newL=newL-min-boxSize;
                bulunamayan=bulunamayan+1;
            }
        }
        int size = sortedMapL.size()+bulunamayan;

//////////////////////////// ilk sayıyı bulup bulmadığının tespiti//////////////////////////////////
        if(size<9) {
            try {
                int xFirst = (int) sortedMapL.keySet().toArray()[0] - min - ((int) sortedMapR.keySet().toArray()[0] - (int) sortedMapL.keySet().toArray()[0]) + 20;
                int yFirst = toppixel;
                int wFirst = (int) sortedMapR.keySet().toArray()[0] - (int) sortedMapL.keySet().toArray()[0];
                int hFirst = botpixel - toppixel;
                int rFirst = (int) sortedMapL.keySet().toArray()[0] - min;
                double gry = 0;
                int count = 0;
                Bitmap correctBmp = Bitmap.createBitmap(bmpnew, xFirst, yFirst, wFirst, hFirst, null, true);
                Utils.bitmapToMat(correctBmp, graybitmap);

                for (int row = 0; row < graybitmap.rows(); row++) {
                    for (int col = 0; col < graybitmap.cols(); col++) {
                        count = count + 1;
                        colorpixel = graybitmap.get(row, col);
                        gry = gry + ((0.3 * colorpixel[0]) + (0.59 * colorpixel[1]) + (0.11 * colorpixel[2]));
                    }
                }
                gry = gry / count;
                System.out.println("gry1:" + gry);

                while (gry < 100) {
                    Bitmap correctBmp1 = Bitmap.createBitmap(bmpnew, xFirst, yFirst, wFirst, hFirst, null, true);
                    Utils.bitmapToMat(correctBmp1, graybitmap);

                    for (int row = 0; row < graybitmap.rows(); row++) {
                        for (int col = 0; col < graybitmap.cols(); col++) {
                            count = count + 1;
                            colorpixel = graybitmap.get(row, col);
                            gry = gry + ((0.3 * colorpixel[0]) + (0.59 * colorpixel[1]) + (0.11 * colorpixel[2]));
                        }
                    }
                    gry = gry / count;
                    System.out.println("gry2Last:" + gry);
                    Imgproc.rectangle(frame, new Point(xFirst, toppixel), new Point(rFirst + 50, botpixel),
                            new Scalar(255, 0, 0), 2);
                    bulunamayan += 1;
                    int temp = xFirst;
                    wFirst = rFirst - xFirst;
                    xFirst = xFirst - min - (rFirst - xFirst);
                    rFirst = temp - min;
                    size = size + 1;
                    if (size >= 8) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//////////////////////////////// son rakamların bulunup bulunmadığı ////////////////////////////////
        while(size<9){
            int lastNumber=sortedMapL.size()-1;
            try {

                int xLast = (int) sortedMapR.keySet().toArray()[lastNumber] + min-10 ;
                int yLast = toppixel;
                int wLast = (int) sortedMapR.keySet().toArray()[lastNumber] - (int) sortedMapL.keySet().toArray()[lastNumber];
                int hLast = botpixel - toppixel;
                int rLast = (int) sortedMapR.keySet().toArray()[lastNumber] + min + ((int) sortedMapR.keySet().toArray()[lastNumber] - (int) sortedMapL.keySet().toArray()[lastNumber]);

                double gryLast = 0;
                int countLast = 0;
                try {


                Bitmap correctBmp = Bitmap.createBitmap(bmpnew, xLast, yLast, wLast, hLast, null, true);
                Utils.bitmapToMat(correctBmp, graybitmap);

                for (int row = 0; row < graybitmap.rows(); row++) {
                    for (int col = 0; col < graybitmap.cols(); col++) {
                        countLast = countLast + 1;
                        colorpixel = graybitmap.get(row, col);
                        gryLast = gryLast + ((0.3 * colorpixel[0]) + (0.59 * colorpixel[1]) + (0.11 * colorpixel[2]));
                    }
                }
                gryLast = gryLast / countLast;
                System.out.println("gry1Last:" + gryLast);

                if (gryLast < 128) {
                    Bitmap correctBmp1 = Bitmap.createBitmap(bmpnew, xLast, yLast, wLast, hLast, null, true);
                    Utils.bitmapToMat(correctBmp1, graybitmap);

                    for (int row = 0; row < graybitmap.rows(); row++) {
                        for (int col = 0; col < graybitmap.cols(); col++) {
                            countLast = countLast + 1;
                            colorpixel = graybitmap.get(row, col);
                            gryLast = gryLast + ((0.3 * colorpixel[0]) + (0.59 * colorpixel[1]) + (0.11 * colorpixel[2]));
                        }
                    }
                    gryLast = gryLast / countLast;
                    System.out.println("gry2Last:" + gryLast);
                    Imgproc.rectangle(frame, new Point(xLast, toppixel), new Point(rLast , botpixel),
                            new Scalar(255, 0, 0), 2);
                    bulunamayan += 1;
                    int temp = xLast;
                    rLast = temp + min + (rLast - xLast);
                    xLast = rLast + min;
                    wLast = rLast - xLast;



                    if (size >= 8) {
                        break;
                    }
                }
                    size = size + 1;

                } catch (Exception e) {
                    size=size+1;
                e.printStackTrace();
                    continue;

                }
            }
            catch (Exception e) {
                e.printStackTrace();
                continue;

            }
        }
////////////////////////////////// Sonucların yazılması/////////////////////////////////////////////
        String endeksDigit="";
        Iterator iterator1 = sortedMapL.entrySet().iterator();

        int sayac=0;
        while (sayac<9){
            try{
                Map.Entry pair1 = (Map.Entry)iterator1.next();
                System.out.println("left : "+pair1.getKey()+" digit : "+pair1.getValue());

                endeksDigit=endeksDigit + pair1.getValue();
                sayac=sayac+1;
                System.out.println("sayac: "+sayac);

            } catch (Exception e) {
                sayac=sayac+1;
                System.out.println("sayac: "+sayac);
                e.printStackTrace();
            }

        }
        System.out.println("bulunamayan: "+bulunamayan);

        if (bulunamayan!=0){
            PostInfo.strTSerisi.add(endeksDigit+"-bulunamayan:"+bulunamayan);
            System.out.println("if-----------------------------------------------"+bulunamayan+"----endeksDigit---"+endeksDigit+"size:"+PostInfo.strTSerisi.size());

        }

        else{
            PostInfo.strTSerisi.add(endeksDigit);
            System.out.println("else-----------------------------------------------"+bulunamayan+"----endeksDigit---"+endeksDigit+"size:"+PostInfo.strTSerisi.size());

        }
        return frame;
    }

}
