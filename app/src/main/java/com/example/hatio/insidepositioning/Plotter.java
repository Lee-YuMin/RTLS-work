package com.example.hatio.insidepositioning;

import org.opencv.core.Mat;

/**
 * Created by hatio on 2017. 4. 14..
 */

public class Plotter {

    Mat trajectory;


//    Plotter() {
//        namedWindow( "Trajectory", WINDOW_AUTOSIZE );
//        trajectory = zeros(600, 600, CV_8UC3);
//    }
//
//    int plot(double dx, double dy, double dz, Pose truth) {
//
//        String text = null;
//        Point textOrg = new Point(10, 50);
//        Point textOrg1 = new Point(10, 70);
//        int fontFace = FONT_HERSHEY_PLAIN;
//        double fontScale = 1;
//        int thickness = 1;
//
//        int x = (int)dx + 300;
//        int y = (int)dz + 100;
//
//        int xx = (int)truth.x + 300;
//        int yy = (int)truth.z + 100;
//
//        circle(trajectory, Point(x, y) ,1, CV_RGB(255, 0, 0), 1);
//        circle(trajectory, Point(xx, yy) ,1, CV_RGB(0, 255, 0), 1);
//
//        rectangle(trajectory, Point(10, 30), Point(550, 70), CV_RGB(0,0,0), CV_FILLED);
//        sprintf(text, "Coordinates: x = %02fm y = %02fm z = %02fm", dx, dy, dz);
//        putText(trajectory, text, textOrg, fontFace, fontScale, Scalar::all(255), thickness, 8);
//        sprintf(text, "Coordinates: x = %02fm y = %02fm z = %02fm", truth.x, truth.y, truth.z);
//        putText(trajectory, text, textOrg1, fontFace, fontScale, Scalar::all(255), thickness, 8);
//
//        imshow( "Trajectory", trajectory );
//
//        return 0;
//    }

}
