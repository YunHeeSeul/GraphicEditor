package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import shapes.TAnchors.EAnchors;

abstract public class TShape implements Serializable{
    //attributes
    private static final long serialVersionUID = 1L;

    private boolean bSelected;
    private Color lineColor, fillColor;
    private AffineTransform affineTransform;	//matrix 만들것


    //shape을 담을 수 있는, polymorphism을 쓸 수 있는 super object 만들기
    protected Shape shape;
    private TAnchors anchors;

    int x, y;
    String s;
    Color c;
    Font f;

    //constructors
    public TShape() {
        this.lineColor=Color.BLACK;
        this.fillColor=null;
        this.bSelected = false;
        this.anchors = new TAnchors();
        this.affineTransform = new AffineTransform();	//상태를 집어넣을 것
        this.affineTransform.setToIdentity();  //항등원으로 초기화시킨 것
        //항등원:더하기의 항등원은 0, 곱하기의 항등원은 1
        //: 어떤 operate가 존재할 때 그 operate에 대해 operation을 실행하더라도 항상 같은 operate가 나오게 하는 것
    }
    //
    public TShape(int x, int y, Color c, String s, Font f){
        this.x=x;
        this.y=y;
        this.c=c;
        this.s=s;
        this.f=f;
    }

    public double getCenterX() {
        return this.shape.getBounds2D().getCenterX();
    }

    public double getCenterY() {
        return this.shape.getBounds2D().getCenterY();
    }

    //공통의 그릇은 setOrigin, resize, draw, addPoint를 할 수 있어야 함
    //setOrigin, resize, draw, addPoint를 할 수 있는게 shape인데 얘는 그냥 대표성을 가진 개념이다
    //추상화된 수퍼클래스가 됨

    public void setLineColor(Color color) {
        this.lineColor = color;
    }
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public abstract void prepareDrawing(int x, int y);
    public abstract void keepDrawing(int x, int y);
    public void addPoint(int x, int y) {}
    //	public abstract void initDrawing(int x, int y);


    abstract public TShape clone(); //TShape을 리턴시켜주는 clone.
    //원래 java에 있는 clone은 새로운 걸 만들어 주는 것이 아니라 자기한테 있는 내용까지 그대로 메모리 복사해주는 의미다
    //여기서는 그냥 새로운 것을 만들어 준다는 의미로 사용한 것

    //getters and setters
    public boolean isSelected() {
        return this.bSelected;
    }

    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }

    public EAnchors getSelectedAnchor() {
        return this.anchors.geteSelectedAnchor();
    }

//		public void setSelectedAnchor(EAnchors eSelectedAnchor) {
//			this.anchors.seteSelectedAnchor(eSelectedAnchor);
//		}

    public AffineTransform getAffineTransform() {
        return this.affineTransform;
    }

    public TAnchors getAnchors() {
        return this.anchors;
    }

    public boolean contains(int x, int y) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        if(this.bSelected) {
            if(this.anchors.contains(x,y)) {
                return true;
            }
        }
        if(transformedShape.contains(x, y)) {
        //    if(this.shape.contains(x, y)) {
            this.anchors.seteSelectedAnchor(EAnchors.eMove);	//도형 밑에 있으면 emove가 됨
            return true;
        }
        return false;
    }

    public void draw(Graphics2D graphics2D) {
        //	graphics2D.transform
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        graphics2D.draw(transformedShape);
        if(this.bSelected) {
            this.anchors.draw(graphics2D, transformedShape.getBounds());
        }
        if(this.fillColor != null){
            graphics2D.setColor(fillColor);
            graphics2D.fill(this.shape);
        }else if(this.lineColor!=null){
            graphics2D.setColor(this.lineColor);
            graphics2D.draw(this.shape);
        }


    }
/*
		public void prepareRotating(int x, int y) {
			this.px=x;
			this.py=y;

		}

		public void keepRotating(int x, int y) {
			// 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
			this.affineTransform.rotate(px, py);
			//그릴 때 실제로 좌표 계산
			this.px=x;
			this.py=y;


		}

		public void finalizeRotating(int x, int y) {
			this.shape = this.affineTransform.createTransformedShape(this.shape);
			this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
		}

		public void prepareMoving(int x, int y) {
			this.px=x;
			this.py=y;

		}

		public void keepMoving(int x, int y) {
			// 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
			this.affineTransform.translate(x-this.px, y-this.py);
			//그릴 때 실제로 좌표 계산
			this.px=x;
			this.py=y;
		}

		public void finalizeMoving(int x, int y) {
			this.shape = this.affineTransform.createTransformedShape(this.shape);
			this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
		}

		public void prepareResizing(int x, int y) {	//기준 앵커의 점을 구하는 것
			this.px=x;
			this.py=y;
			Point2D resizeAnchorPoint = this.anchors.getResizeAnchorPoint(x, y);	//resize 앵커의 포지션을 여기서 계산, 이렇게 되면 셀렉티드 된 앵커가 앵커에 있어야함 근데 우리는 티쉐입에 있음 원래는 셀렉티드 앵커를
			this.cx = resizeAnchorPoint.getX();
			this.cy = resizeAnchorPoint.getY();


		}

		public void keepResizing(int x, int y) {//전의 점을 계속 저장해놔야
			this.getResizeScale(x,y);
			this.affineTransform.translate(cx, cy); //우선 원점으로 땡겨놓기
			// 실제로 값을 계산하는 것이 아닌 matrix 하나 만들어 놓은 것
			this.affineTransform.scale(this.xScale, this.yScale);	//계산
			this.affineTransform.translate(-cx, -cy); //다시 원점으로 되돌려 놓기

			//그릴 때 실제로 좌표 계산
			this.px=x;
			this.py=y;

		}

		public void finalizeResizing(int x, int y) {
			this.shape = this.affineTransform.createTransformedShape(this.shape);
			this.affineTransform.setToIdentity(); //매트릭스를 초기화 시켜버림
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
			case eNW:
				xScale=w2/w1;
				yScale=h2/h1;
				break;
			case eWW:
				xScale=w2/w1;
				yScale=1;
				break;
			case eSW:
				xScale=w2/w1;
				yScale=h2/h1;
				break;
			case eSS:
				xScale=1;
				yScale=h2/h1;
				break;
			case eSE:
				xScale=w2/w1;
				yScale=h2/h1;
				break;
			case eEE:
				xScale=w2/w1;
				yScale=1;
				break;
			case eNE:
				xScale=w2/w1;
				yScale=h2/h1;
				break;
			case eNN:
				xScale=1;
				yScale=h2/h1;
				break;

			default:
				break;
			}
			this.xScale=xScale;
			this.yScale=yScale;
		}

		*/


}

