package transformer;

import java.awt.Graphics2D;

import shapes.TShape;

//resizer,mover 처럼 여기서 계산하게 정리하기; prepareDrawing, keepDrawing
public class Drawer extends Transformer {
    private Mover mover;
    public Drawer(TShape selectedShape) {
        super(selectedShape);
    }

    @Override
    public void prepare(int x, int y) {
        this.selectedShape.prepareDrawing(x, y);
    }

    @Override
    public void keepTransforming(int x, int y) {
        this.selectedShape.keepDrawing(x, y);
    }

    @Override
    public void finalize(int x, int y) {
        //	this.selectedShape.finishDrawing(x, y);
    }

}
