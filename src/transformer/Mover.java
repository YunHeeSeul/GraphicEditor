package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;

import shapes.TShape;
import shapes.TAnchors;

public class Mover extends Transformer {
    protected Shape shape;

    public Mover(TShape selectedShape) {
        super(selectedShape);
    }

    @Override
    public void prepare(int x, int y) {
        this.px=x;
        this.py=y;
    }

    @Override
    public void keepTransforming(int x, int y) {
        // 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
        this.affineTransform.translate(x-this.px, y-this.py);
        //그릴 때 실제로 좌표 계산
        this.px=x;
        this.py=y;
    }

    @Override
    public void finalize(int x, int y) {
        //좌표 아예 바꿔버림
        //	this.selectedShape.finalizeMoving(x, y);
        //	this.shape = this.affineTransform.createTransformedShape(this.shape);
        //	this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
    }

}

