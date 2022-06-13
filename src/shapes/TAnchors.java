package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import frames.DrawingPanel;
import shapes.TAnchors.EAnchors;

public class TAnchors {	//각각의 shape이 자신을 둘러싸고 있는 외접사각형을 주면 됨

    private final static int WIDTH = 15;
    private final static int HEIGHT = 15;

    public enum EAnchors {
        eNW,
        eWW,
        eSW,
        eSS,
        eSE,
        eEE,
        eNE,
        eNN,
        eRR,
        eMove
    }

    @SuppressWarnings("unused")
    private DrawingPanel drawingPanel;

    private Ellipse2D anchors[];
    //eRotateAnchor 추가
    private EAnchors eSelectedAnchor, eResizeAnchor, eRotateAnchor;	//selectedAnchor의 대각선에 있는 앵커임

    public EAnchors geteSelectedAnchor() {return eSelectedAnchor;}

    public void seteSelectedAnchor(EAnchors eSelectedAnchor) {this.eSelectedAnchor = eSelectedAnchor;}

    public EAnchors geteResizeAnchor() {return eResizeAnchor;}

    public void seteResizeAnchor(EAnchors eResizeAnchor) {this.eResizeAnchor = eResizeAnchor;}

    //추가
    public EAnchors geteRotateAnchor() {return eRotateAnchor;}

    public void seteRotateAnchor(EAnchors eRotateAnchor) {this.eRotateAnchor = eRotateAnchor;}

    public TAnchors() {
        this.anchors = new Ellipse2D.Double[EAnchors.values().length-1];	//동그라미의 주소를 담을 수 있는 그릇; emove는 빼려고 -1 해준 것

        for(int i=0; i<EAnchors.values().length-1; i++) {
            this.anchors[i] = new Ellipse2D.Double(); //어레이의 인덱스에 동그라미 붙이기
        }
    }

    public void draw(Graphics2D graphics2D, Rectangle BoundingRectangle) { //어디에 그릴지 알려줌, 사각형 줌

        for(int i=0; i<EAnchors.values().length-1; i++) {

            //원점, for문을 돌 때마다 좌표를 원점으로 초기화(좌표를 누적되어 계산되게 하지 않기 위해서)
            int x = BoundingRectangle.x-WIDTH/2;	//x,y 좌표
            int y = BoundingRectangle.y-HEIGHT/2;
            int w = BoundingRectangle.width;
            int h = BoundingRectangle.height;

            EAnchors eAnchor = EAnchors.values()[i];

            //좌표 계산
            switch (eAnchor) {
                case eNW:                           break;
                case eWW: y = y + h/2;              break;
                case eSW: y = y + h;                break;
                case eSS: x = x + w/2;  y = y + h;  break;
                case eSE: x = x + w;    y = y + h;  break;
                case eEE: x = x + w;    y = y + h/2;break;
                case eNE: x = x + w;                break;
                case eNN: x = x + w/2;              break;
                case eRR: x = x + w/2;  y = y - h/2;break;
                default:                            break;
            }
            this.anchors[eAnchor.ordinal()].setFrame(x, y, WIDTH, HEIGHT);
//			Color foreground = graphics2D.getColor(); //foreground 컬러 저장
//			//background 컬러로 앵커를 채우고
//			graphics2D.setColor(graphics2D.getBackground());
//			graphics2D.fill(this.anchors[eAnchor.ordinal()]);
//			//foreground 컬러로 앵커를 그림
//			graphics2D.setColor(foreground);
            graphics2D.draw(this.anchors[eAnchor.ordinal()]);

        }
    }

    public void associate(DrawingPanel drawingPanel) { //저장할 곳을 만들어줌
        this.drawingPanel = drawingPanel;

    }

    public boolean contains(int x,int y) { //저장할 곳을 만들어줌
        for(int i=0; i<EAnchors.values().length-1; i++) {
            if(this.anchors[i].contains(x, y)) {
                this.eSelectedAnchor=EAnchors.values()[i];	//저장했음
                return true;
            }
        }
        return false;
    }

    public Point2D getResizeAnchorPoint(int x, int y) {

        //대각선 앵커 좌표 계산
        switch (this.eSelectedAnchor) {
            case eNW:
                this.eResizeAnchor=EAnchors.eSE;
                break;
            case eWW:
                this.eResizeAnchor=EAnchors.eEE;
                break;
            case eSW:
                this.eResizeAnchor=EAnchors.eNE;
                break;
            case eSS:
                this.eResizeAnchor=EAnchors.eNN;
                break;
            case eSE:
                this.eResizeAnchor=EAnchors.eNW;
                break;
            case eEE:
                this.eResizeAnchor=EAnchors.eWW;
                break;
            case eNE:
                this.eResizeAnchor=EAnchors.eSW;
                break;
            case eNN:
                this.eResizeAnchor=EAnchors.eSS;
                break;

            default:
                break;
        }
        //대각선에 있는 앵커의 센터에 있는 x,y를 계산해준것 이게 resize의 원점임
        Point2D point = new Point2D.Double(
                anchors[eResizeAnchor.ordinal()].getCenterX(),
                anchors[eResizeAnchor.ordinal()].getCenterY());
        return point;
    }

    public Point2D getRotateAnchorPoint(int x, int y) {

        //대각선 앵커 좌표 계산
        switch (this.eRotateAnchor) {
            case eNW:
                this.eRotateAnchor=EAnchors.eSE;
                break;
            case eWW:
                this.eRotateAnchor=EAnchors.eEE;
                break;
            case eSW:
                this.eRotateAnchor=EAnchors.eNE;
                break;
            case eSS:
                this.eRotateAnchor=EAnchors.eNN;
                break;
            case eSE:
                this.eRotateAnchor=EAnchors.eNW;
                break;
            case eEE:
                this.eRotateAnchor=EAnchors.eWW;
                break;
            case eNE:
                this.eRotateAnchor=EAnchors.eSW;
                break;
            case eNN:
                this.eRotateAnchor=EAnchors.eSS;
                break;

            default:
                break;
        }
        //대각선에 있는 앵커의 센터에 있는 x,y를 계산해준것 이게 resize의 원점임
        Point2D point = new Point2D.Double(
                anchors[eRotateAnchor.ordinal()].getCenterX(),
                anchors[eRotateAnchor.ordinal()].getCenterY());
        return point;
    }
}
