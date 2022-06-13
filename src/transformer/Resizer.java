package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import shapes.TShape;
import shapes.TAnchors;
import shapes.TAnchors.EAnchors;

public class Resizer extends Transformer {
    protected Shape shape;
    protected double xScale, yScale;	//scale, 배율

    public Resizer(TShape selectedShape) {
        super(selectedShape);
    }

    @Override
    public void prepare(int x, int y) {//
        Point2D resizeAnchorPoint = this.anchors.getResizeAnchorPoint(x, y);	//resize 앵커의 포지션을 여기서 계산, 이렇게 되면 셀렉티드 된 앵커가 앵커에 있어야함 근데 우리는 티쉐입에 있음 원래는 셀렉티드 앵커를
        this.px=x;
        this.py=y;
        this.cx = resizeAnchorPoint.getX();
        this.cy = resizeAnchorPoint.getY();
    }

    @Override
    public void keepTransforming(int x, int y) {//전의 점을 계속 저장해놔야
        this.getResizeScale(x,y);
        this.affineTransform.translate(cx, cy); //우선 원점으로 땡겨놓기
        // 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
        this.affineTransform.scale(this.xScale, this.yScale);	//계산
        this.affineTransform.translate(-cx, -cy); //다시 원점으로 되돌려 놓기

        //그릴 때 실제로 좌표 계산
        this.px=x;
        this.py=y;
    }

    @Override
    public void finalize(int x, int y) {
        //좌표 아예 바꿔버림
        //	this.selectedShape.finalizeResizing(x, y);
        //	this.shape = this.affineTransform.createTransformedShape(this.shape);
        //	this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
    }

    private void getResizeScale(int x, int y) { //sx, sy 여기서 계산
        EAnchors eResizeAnchor = this.anchors.geteResizeAnchor();
        //px는 전 점 cx는 현재 움직인 점
        double w1 = px-cx;
        double w2 = x-cx;

        double h1 = py-cy;
        double h2 = y-cy;

        //배율계산
        switch (eResizeAnchor) {
            case eNW: xScale=w2/w1;   yScale=h2/h1; break;
            case eWW: xScale=w2/w1;   yScale=1;     break;
            case eSW: xScale=w2/w1;   yScale=h2/h1; break;
            case eSS: xScale=1;       yScale=h2/h1; break;
            case eSE: xScale=w2/w1;   yScale=h2/h1; break;
            case eEE: xScale=w2/w1;   yScale=1;     break;
            case eNE: xScale=w2/w1;   yScale=h2/h1; break;
            case eNN: xScale=1;       yScale=h2/h1; break;
            default:                                break;
        }
 //       this.xScale=xScale;
 //       this.yScale=yScale;
    }
}
