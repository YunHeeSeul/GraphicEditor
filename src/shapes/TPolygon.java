package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;

public class TPolygon extends TShape{

    private static final long serialVersionUID = 1L;

//shape의 서브클래스, shape의 한 종류, 같은 종류는 함수를 공유


    public TPolygon() {  //constants의 new는 그냥 메모리 확보용

        this.shape = new Polygon();

    }
    public TShape clone() {
        return new TPolygon(); //TPol을 만들어줌
    }

    public void prepareDrawing(int x, int y) {


        this.addPoint(x, y);
        this.addPoint(x, y);
    }

    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon)this.shape;
        polygon.addPoint(x, y);

    }

    @Override
    public void keepDrawing(int x, int y) {
        Polygon polygon = (Polygon)this.shape;
        polygon.xpoints[polygon.npoints-1] = x;
        polygon.ypoints[polygon.npoints-1] = y;

    }



}