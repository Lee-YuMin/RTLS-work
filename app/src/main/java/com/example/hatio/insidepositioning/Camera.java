package com.example.hatio.insidepositioning;


import android.os.Environment;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Camera {

    static final String STRSAVEPATH = Environment.
            getExternalStorageDirectory()+"/odometer/";

    static final String SAVEFILEPATH = "Logger.txt";

    private String folder = null;
    private int seq;

    final static int MAX_FRAME = 151;

    public void init() {
        folder = STRSAVEPATH + "data/01/images";
        seq = 0;
    }

    double getFocal() {
        return 718.8560;
    }

    double getScale() {
        return 1;
    }

    Point getPrinciplePoint() {
        return new Point(607.1928, 185.2157);
    }

    Mat capture() {

        if(seq >= MAX_FRAME)
            return null;

        String filename = null;

        filename = folder + String.format("/%06d.png", seq++);

        Mat grayImage = imread(filename, CV_LOAD_IMAGE_GRAYSCALE);

//        cvtColor(colorImage, grayImage, COLOR_BGR2GRAY);

        return grayImage;
    }

}
