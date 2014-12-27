package org.opencv.samples.tutorial1;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private final int CIRCLE_RECALC_FREQ = 20; //TODO tweak for best performance
    private final int HOUGH_CIRCLES_LINE_THICKNESS = 1;

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    private CvCameraViewFrame    mInputFrame;

    //TODO the frame processing should be in a separate class
    private Mat mCircles;
    private Mat mLines;
    private int frameCounter;
    private Mat mSrcColor;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + "MainActivity"); //this.getClass());
        mCircles = null;
        frameCounter = 0;
        mSrcColor = null;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial1_surface_view);

        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMessage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;

            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
                toastMessage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
                toastMessage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mInputFrame = inputFrame;
        return processFrame();
    }


    public Mat processFrame() {
        frameCounter++;
        if(frameCounter % CIRCLE_RECALC_FREQ == 0) {
            calculateHoughCircles();
        }
        mSrcColor = mInputFrame.rgba();
        drawHoughCircles(mSrcColor);
        return mSrcColor;
    }

    private void drawHoughCircles(Mat destination) {
        if(mCircles == null)
            return;

        if (mCircles.cols() > 0)
            for (int x = 0; x < mCircles.cols(); x++)
            {
                double vCircle[] = mCircles.get(0,x);

                if (vCircle == null)
                    break;

                Point pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                int radius = (int)Math.round(vCircle[2]);

                // draw the found circle
                Core.circle(destination, pt, radius, new Scalar(0, 255, 0), HOUGH_CIRCLES_LINE_THICKNESS);
                Core.circle(destination, pt, 3, new Scalar(0,0,255), HOUGH_CIRCLES_LINE_THICKNESS);
            }
    }

    public void calculateHoughCircles() {
        if (mCircles == null)
            mCircles = new Mat();

        //TODO tweak parameters for better accuracy
        int param1 = 100;
        int param2 = 30;
        int iMinRadius = 0;
        int iMaxRadius = 0;

        Mat srcGray = mInputFrame.gray();
        Imgproc.GaussianBlur(srcGray, srcGray, new Size(9, 9), 2, 2);
        Imgproc.HoughCircles(srcGray, mCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 10, param1, param2,
                iMinRadius, iMaxRadius);

        srcGray.release();
    }


}
