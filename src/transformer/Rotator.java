package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import shapes.TShape;

public class Rotator extends Transformer {

    protected Shape shape;
    private double cx,cy; //중심점
    public Rotator(TShape selectedShape) {
        super(selectedShape);
    }

    @Override
    public void prepare(int x, int y) {

        //중심점
        this.cx=this.selectedShape.getCenterX();
        this.cy=this.selectedShape.getCenterY();

        //전 점 기억하기
        this.px=x;
        this.py=y;

        /*
        //중심점
        this.center.setLocation(this.selectedShape.getCenterX(),this.selectedShape.getCenterY());
        //전 점
        this.before.setLocation(x,y);
         */

    }
/*
    private double calculateRotateAngle(Point2D center,Point2D before, int x, int y){
        //angle 구하기
     /*
        double startAngle=Math.toDegrees(Math.atan2(py-cy, px-cx));
        double endAngle=Math.toDegrees(Math.atan2(y-cy, x-cx));
        double angle = endAngle-startAngle;

      */
    /*
        double startAngle = Math.toDegrees(Math.atan2(center.getX()-before.getX(), center.getY() - before.getY()));
        double endAngle = Math.toDegrees(Math.atan2(center.getX()-x, center.getY() - y));
        double angle = endAngle-startAngle;
        if(angle < 0) angle += 360;
        return angle;
    }
    */
private double calculateRotateAngle(int x, int y){
    //angle 구하기
     /*
        double startAngle=Math.toDegrees(Math.atan2(py-cy, px-cx));
        double endAngle=Math.toDegrees(Math.atan2(y-cy, x-cx));
        double angle = endAngle-startAngle;

      */

        double startAngle = Math.toDegrees(Math.atan2(py-cy,px-cx));
        double endAngle = Math.toDegrees(Math.atan2(cy-y, cx-x));
        double angle = endAngle-startAngle;
        if(angle < 0) angle += 360;
        return angle;
    }
    @Override
    public void keepTransforming(int x, int y) {
      /*
        // 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
        //this.affineTransform.getShearX(); //우선 원점으로 땡겨놓기
        //this.affineTransform.getShearY(); //우선 원점으로 땡겨놓기
        this.affineTransform.translate(cx, cy); //우선 원점으로 땡겨놓기
        this.affineTransform.rotate(angle);
        this.affineTransform.translate(-cx, -cy); //우선 원점으로 땡겨놓기

        //그릴 때 실제로 좌표 계산
        this.px=x;
        this.py=y;
       */
        double rotationAngle = calculateRotateAngle(x,y);
        affineTransform.setToRotation(Math.toRadians(rotationAngle),this.cx,this.cy);
        this.affineTransform.rotate(rotationAngle);
       // this.before.setLocation(x,y);
    }

    @Override
    public void finalize(int x, int y) {
        //	this.shape = this.affineTransform.createTransformedShape(this.shape);
   //     this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
    }

}

