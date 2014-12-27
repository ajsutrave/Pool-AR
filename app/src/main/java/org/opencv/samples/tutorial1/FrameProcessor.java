package org.opencv.samples.tutorial1;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by AJ on 12/25/2014.
 */
public class FrameProcessor {



    //    private void proccessSobel(){
//        int scale = 1;
//        int delta = 0;
//        int ddepth = -1;
//        Imgproc.GaussianBlur(src_gray, src_gray, new Size(9, 9), 2, 2);
//
//        /// Generate grad_x and grad_y
//        Mat grad_x = new Mat();
//        Mat grad_y = new Mat();
//        Mat abs_grad_x = new Mat();
//        Mat abs_grad_y = new Mat();
//        Mat grad = new Mat();
//
//        /// Gradient X
//        //Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, BORDER_DEFAULT );
//        Imgproc.Sobel(src_gray, grad_x, ddepth, 1, 0, 3, scale, delta, Imgproc.BORDER_DEFAULT);
//        Core.convertScaleAbs(grad_x, abs_grad_x);
//
//        /// Gradient Y
//        //Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, BORDER_DEFAULT );
//        Imgproc.Sobel(src_gray, grad_y, ddepth, 0, 1, 3, scale, delta, Imgproc.BORDER_DEFAULT);
//        Core.convertScaleAbs(grad_y, abs_grad_y);
//
//        /// Total Gradient (approximate)
//        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);
//
//        grad_x.release();
//        grad_y.release();
//        abs_grad_x.release();
//        abs_grad_y.release();
//    }


}
