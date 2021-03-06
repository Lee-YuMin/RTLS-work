package com.example.hatio.insidepositioning;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraView extends Activity {
    String TAG = "CAMERA";
    private Context mContext = this;
    private Camera mCamera;
    private CameraPreview mPreview;
    // 초기에 객체를 생성해주지 않으면 isAlive()에서 애러가남
    getImageThread thread = new getImageThread();
    private boolean runThread = false;
    public static Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.view_camera_preview);

        mToast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);  // 토스트 초기화

        mContext = this;
        // 카메라 인스턴스 생성
        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance();

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);


            Button captureButton = (Button) findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // JPEG 콜백 메소드로 이미지를 가져옵니다
                    if (!thread.isAlive()) {
                        runThread = true;
                        thread = new getImageThread();
                        thread.start();
                    } else {
                        runThread = false;
                        thread.interrupt();
                        mToast.setText("쓰레드 정지");
                        mToast.show();
                    }
                }
            });
        } else {
            Toast.makeText(mContext, "no camera on this device!", Toast.LENGTH_SHORT).show();
        }

    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // JPEG 이미지가 byte[] 형태로 들어옵니다

            mToast.setText("찰칵");
            mToast.show();

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Toast.makeText(mContext, "Error saving!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                //Thread.sleep(500);
                mCamera.startPreview();

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            } /*catch (InterruptedException e) {
                e.printStackTrace();
               }*/
        }
    };

    /**
     * 카메라 하드웨어 지원 여부 확인
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 카메라가 최소한 한개 있는 경우 처리
            Log.i(TAG, "Number of available camera : " + Camera.getNumberOfCameras());
            return true;
        } else {
            // 카메라가 전혀 없는 경우
            Toast.makeText(mContext, "No camera found!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 카메라 인스턴스를 안전하게 획득합니다
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            // 사용중이거나 사용 불가능 한 경우
        }
        return c;
    }

    /**
     * 이미지를 저장할 파일 객체를 생성합니다
     */
    private static File getOutputMediaFile() {
        // SD카드가 마운트 되어있는지 먼저 확인해야합니다
        // Environment.getExternalStorageState() 로 마운트 상태 확인 가능합니다

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // 굳이 이 경로로 하지 않아도 되지만 가장 안전한 경로이므로 추천함.

        // 없는 경로라면 따로 생성한다.
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }

        // 파일명을 적당히 생성. 여기선 시간으로 파일명 중복을 피한다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        Log.i("MyCamera", "Saved at" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        return mediaFile;
    }

    @Override
    public void onPause() {
        super.onPause();
        // 보통 안쓰는 객체는 onDestroy에서 해제 되지만 카메라는 확실히 제거해주는게 안전하다.

    }


    private class getImageThread extends Thread {
        public getImageThread() {
            // 초기화 작업
        }

        public void run() {

            int second = 0;

            while (runThread) {
                second++;
                try {
                    mCamera.takePicture(null, null, mPicture);
                    Thread.sleep(1000); // 1초간 Thread를 잠재운다
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("경과된 시간 : ", Integer.toString(second));
            }
        }
    }

}
