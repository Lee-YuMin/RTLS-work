package com.example.hatio.insidepositioning;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.FeatureDetector;

import java.util.ArrayList;

import static org.opencv.calib3d.Calib3d.RANSAC;
import static org.opencv.calib3d.Calib3d.findEssentialMat;
import static org.opencv.calib3d.Calib3d.recoverPose;
import static org.opencv.video.Video.calcOpticalFlowPyrLK;

public class Odometer {

    final public int MIN_NUM_FEAT = 2000;

    double focal;
    Point pp = new Point();

    MatOfByte status = new MatOfByte();

    Mat R_f = new Mat();
    Mat t_f = new Mat(); //the final rotation and tranlation vectors containing the

    Mat E = new Mat();
    Mat R = new Mat();
    Mat t = new Mat();
    Mat mask = new Mat();

    double x, y, z;

    Mat prevImage = new Mat();
    MatOfPoint2f lastFeatures = new MatOfPoint2f();
    MatOfPoint2f prevFeatures = new MatOfPoint2f();
    MatOfPoint2f currFeatures = new MatOfPoint2f();


    Odometer(double focal, Point pp) {
        this.focal = focal;
        this.pp = pp;
    }

    MatOfPoint2f getLastFeatures () {
        return lastFeatures;
    }

    MatOfPoint2f getCurrFeatures() {
        return currFeatures;
    }

    void featureTracking(Mat prevImage, Mat currImage, MatOfPoint2f prevFeatures, MatOfPoint2f currFeatures, MatOfByte status) {

        // 트래킹에 실패한 포인트들은 버린다.
        MatOfFloat err = new MatOfFloat();
        Size winSize = new Size(21,21);
        TermCriteria termcrit = new TermCriteria(TermCriteria.COUNT+TermCriteria.EPS, 30, 0.01);

        calcOpticalFlowPyrLK(prevImage, currImage, prevFeatures, currFeatures, status, err, winSize, 3, termcrit, 0, 0.001);

        // KLT 트래킹에 실패하거나 프레임 바깥으로 벗어난 포인트들을 버린다.
//        int indexCorrection = 0;
//        byte[] statusArray = status.toArray();
//        Point[] currFeaturesArray = currFeatures.toArray();
//
//        for(int i = 0;i < statusArray.length;i++) {
//            Point pt = currFeaturesArray[i];
//
//            if((statusArray[i] == 0)||(pt.x < 0)||(pt.y < 0)) {
//
//                prevFeatures.toList().remove((i - indexCorrection));
//                currFeatures.toList().remove((i - indexCorrection));
//
//                indexCorrection++;
//            }
//        }
    }

    MatOfPoint2f featureDetection(Mat image)  {
        // uses FAST as of now, modify parameters as necessary

//        int fast_threshold = 20;
//        boolean nonmaxSuppression = true;
//
//        FAST(image, keypoints, fast_threshold, nonmaxSuppression);
//        KeyPoint.convert(keypoints, 1, vector<int>());

        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        FeatureDetector fast = FeatureDetector.create(FeatureDetector.FAST);
        fast.detect(image, keypoints);

        KeyPoint[] kps = keypoints.toArray();
        ArrayList<Point> arrayOfPoints = new ArrayList<Point>();

        for(int i = 0; i < kps.length; i++) {
            arrayOfPoints.add(kps[i].pt);
        }

        MatOfPoint2f matOfPoint = new MatOfPoint2f();
        matOfPoint.fromList(arrayOfPoints);

        return matOfPoint;
    }

    int estimate(Mat currImage, double scale) {

        if(prevImage.empty()) {
            prevImage = currImage;
            prevFeatures = featureDetection(prevImage);

            return 0;
        }

        featureTracking(prevImage, currImage, prevFeatures, currFeatures, status);

        E = findEssentialMat(currFeatures, prevFeatures, focal, pp, RANSAC, 0.999, 1.0, status);
        recoverPose(E, currFeatures, prevFeatures, R, t, focal, pp, status);

        if(R_f.empty()) {
            R_f = R.clone();
            t_f = t.clone();
        }

//        if(scale > 0.1 && (t.get(2, 0)[0] > t.get(0, 0)[0]) && (t.get(2, 0)[0] > t.get(1, 0)[0])) {
            Mat tmp = new Mat();
            Core.gemm(R_f, t, 1, new Mat(), 0, tmp);
            Core.add(t_f, tmp, t_f);
            Core.multiply(R, R_f, R_f);
//        } else {
//            System.out.printf("scale below 0.1, or incorrect translation");
//        }

        // 피쳐의 갯수가 작아지면, 다시 검출함.
        byte[] statusArray = status.toArray();
        int count = 0;
        for(int i = 0;i < statusArray.length;i++) {
            if(statusArray[i] == 0)
                count++;
        }
        if(count < MIN_NUM_FEAT) {
            prevFeatures = featureDetection(prevImage);
            featureTracking(prevImage, currImage, prevFeatures, currFeatures, status);
        }

        x = t_f.get(0, 0)[0];
        y = t_f.get(1, 0)[0];
        z = t_f.get(2, 0)[0];

        System.out.println("x, y, z : " + x + ", " + y + ", " + z);

        prevImage = currImage;
        lastFeatures = prevFeatures;
        prevFeatures = currFeatures;

        return 0;
    }
}