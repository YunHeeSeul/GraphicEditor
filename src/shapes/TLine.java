package shapes;


import java.awt.geom.Line2D;

public class TLine extends TShape{

    public TLine() {
        this.shape = new Line2D.Double();

    }
    public void prepareDrawing(int x, int y) {
        Line2D line = (Line2D)this.shape;
        line.setLine(x, y, x, y);
    }
    public TShape clone() {
        return new TLine(); //TLine을 만들어줌
    }
    public void keepDrawing(int x, int y) {
        Line2D line = (Line2D)this.shape;
        line.setLine(line.getX1(), line.getY1(), x, y);

    }

}