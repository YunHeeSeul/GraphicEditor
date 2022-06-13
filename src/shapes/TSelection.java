package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TSelection extends TShape {//shape의 서브클래스

    public TSelection() { //new는 그냥 메모리 확보용, new 할 때 파라미터 없이 하는 대신 enumeration 객체에서 우리가 직접 new 하지 않고
        //enumeration 객체에서 그냥 객체를 가져올 것임
        //그 다음 원점 잡는 setOrigin 함수를 만들어주고 preparedrawing에서 setOrigin 한 번 불러줘야함
        //그러려면 shape에서 부르니까 setOrigin이 TShape에도 하나 있어야 함
        this.shape=new Rectangle();	//내가 만든 것이 아닌 JDK에 있는 Rectangle() 쓰는 것. 안에 x, y, w, h 들어가 있음. 이미 안에 contains 있음
    }

    public TShape clone() {
        return new TSelection(); //TRec을 만들어줌
    }
    public void prepareDrawing(int x, int y) { //실제 원점 좌표 세팅하는 함수
        //down cast
        Rectangle rectangle = (Rectangle) this.shape; //shape은 rec, oval,.. 종류 별로 좌표가 다 다르므로 rectangle로 downcast 해서 사용
        rectangle.setFrame(x, y, 0, 0); //노출시켰던 좌표를 jdk에 있는 rectangle 안에 저장
    }

    public void keepDrawing(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setSize(x-rectangle.x, y-rectangle.y);

    }



}