package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class TOval extends TShape{//shape의 서브클래스, shape의 한 종류, 같은 종류는 함수를 공유

    public TOval() {  //constants의 new는 그냥 메모리 확보용
        this.shape = new Ellipse2D.Double(); //정교함 차이
    }
    public TShape clone() {
        return new TOval(); //TOval을 만들어줌
    }
    public void prepareDrawing(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.shape;
        ellipse.setFrame(x, y, 0, 0); //원점을 x와 y로 잡음

    }
    public void keepDrawing(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.shape;
        ellipse.setFrame(ellipse.getX(), ellipse.getY(), x-ellipse.getX(), y-ellipse.getY());
    }


}