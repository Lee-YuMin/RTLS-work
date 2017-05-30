package com.example.hatio.insidepositioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Pose {

    double x;
    double y;
    double z;

    public Pose(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Pose() {
        new Pose(0, 0, 0);
    }

    static int get(Pose pose, String path, int idx) {

        String line = null;
        int i = 0;
        double x = 0, y = 0, z = 0;

//        File fposes = new File(path);

        InputStream fis = null;
        File file = new File(path);

        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader fposes = new BufferedReader(isr);


//        파일이 열려있을 때 처리
//        if(!file.isopen) {
//            System.out.println("Unable to open file");
//            return -1;
//        }


        while(i++ <= idx)
            line = fposes.readLine();

        int in = Integer.parseInt(line);

        for(int j = 0;j < 12;j++) {
            z = in;
            if(j == 7)
                y = z;
            if(j == 3)
                x = z;
        }

        fposes.close();

        pose = new Pose(x, y, z);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
