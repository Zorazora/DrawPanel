package model;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import util.GraphType;

import java.util.ArrayList;
import java.util.List;

public class ShapeDetection {
    public GraphType shapeDetect(){
        GraphType graphType = GraphType.NOTYPE;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("src/main/resources/screenshot/shot.png");
        Mat dst = src.clone();
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.adaptiveThreshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, 3, 3);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dst,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));

        if(contours.size() == 1){
            MatOfPoint2f newMatOfPoint2f = new MatOfPoint2f( contours.get(0).toArray() );
            graphType = detect(contours.get(0),newMatOfPoint2f);
        }
        return graphType;
    }

    public GraphType detect(MatOfPoint mp, MatOfPoint2f mp2f){
        double peri;
        peri = Imgproc.arcLength(mp2f,true);
        MatOfPoint2f polyShape = new MatOfPoint2f();
        Imgproc.approxPolyDP(mp2f,polyShape,0.04*peri,true);
        int shapeLen = polyShape.toArray().length;

        switch (shapeLen){
            case 3:
                return GraphType.TRIANGLE;
            case 4:
                Rect rect = Imgproc.boundingRect(mp);
                float width = rect.width;
                float height = rect.height;
                float ar = width/height;

                //计算宽高比，判断是矩形还是正方形
                if (ar>=0.95 && ar <=1.05) {
                    return GraphType.SQUARE;
                }else {
                    return GraphType.RECTANGLE;
                }
            default:
                return GraphType.CIRCLE;
        }
    }
}
