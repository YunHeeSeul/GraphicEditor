package frames;

import global.Constants;
import shapes.TAnchors;
import shapes.TSelection;
import shapes.TShape;
import transformer.Mover;
import transformer.Drawer;
import transformer.Resizer;
import transformer.Rotator;
import transformer.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class DrawingPanel extends JPanel {
    //attribute; 속성; 이름 , 나이, 키 ,..; 전체를 나타내는 값
    private static final long serialVersionUID = 1L;

    //components; 부품
    private Vector<TShape> shapes; //그렸던 것을 어레이에 저장하기 위해 벡터 만듦
    private BufferedImage bufferedImage;    //타입이 BufferedImage
    private Graphics2D graphics2DBufferedImage; //bufferedImage의 그림 도구를 만들어줌

    //associated attribute
    private Constants.ETools selectedTool; //도형들을 일반화시킨 추상적 개념
    private TShape currentShape; //지금 현재 그려지는 그림. 툴에서 꺼내옴
    private TShape selectedShape;	//
    private Transformer transformer;

    private boolean updated;
    private enum EDrawingState{
        eIdle, //아무것도 안하고 있다
        e2PointDrawing,	//점이 2개짜리 그림을 그리고 있다
        eNPointDrawing,	//점이 n개짜리 그림을 그리고 있다
        eMoving,
        eRotating,
        eResizing
    }

    EDrawingState eDrawingState;

    public DrawingPanel() {
        //attributes
        this.setBackground(Color.WHITE);
        this.eDrawingState = EDrawingState.eIdle; //처음 아무것도 안하는 상태로 설정
        this.updated = false;
        //components
        this.shapes=new Vector<TShape>(); //shape을 저장할 어레이 만듦; 저장해놨다가 다시 그리기 위해

        MouseHandler mouseHandler = new MouseHandler();//마우스인풋리스너를 상속받아서 패널에 등록. 이 패널의 마우스 핸들러임을 등록하는 것
        //마우스리스너와 마우스모션리스너를 동시에 관리할 핸들러를 만들어줌
        this.addMouseListener(mouseHandler); //버튼 센서를 감지
        this.addMouseMotionListener(mouseHandler); //위치 센서를 감지
        this.addMouseWheelListener(mouseHandler);//wheel

    }

    public void initialize() {
        //드로잉패널을 깔아뭉개는 이미지를 드로잉패널 사이즈랑 똑같이 만들어 놓고 거기에 그림을 그릴 것
        //그리고 나서 이 이미지에 그릴 수 있는 graphics2D도구 하나를 생성
        //new가 아니라 드로잉패널에서 가져와야함. 드로잉패널이 이미지를 만드는 능력이 있음. 패널에 자기를 버퍼링하는 것이기 때문에
        //사이즈는 드로잉 패널만큼 가져와서 만드는 것
        this.bufferedImage = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());
        this.graphics2DBufferedImage = (Graphics2D) this.bufferedImage.getGraphics(); //this.graphics2DBufferedImage는 bufferedImage에서 가져옴. bufferedImage가 graphics를 가지고 다님

    }
    public void setLineColor(Color color) {
        Graphics2D g2D = (Graphics2D) this.getGraphics();
        g2D.setXORMode(this.getBackground());
        if (this.selectedShape != null) {
            this.selectedShape.setLineColor(color);
        }
        this.selectedShape.draw(g2D);
    }

    public void setFillColor(Color color) {
        if (this.selectedShape != null) {
            this.selectedShape.setFillColor(color);
        }
    }

    //boolean일 경우 get 대신 is 사용함
    public boolean isUpdated() {	//드로잉 패널의 수정 여부 함수
        return this.updated;
    }

    public void setUpdated(boolean updated) {this.updated = updated;}

    public Object getShapes() {return this.shapes;
    }
    //확인되지 않은 오퍼레이션과 관련된 경고를 억제
    @SuppressWarnings("unchecked")
    public void setShapes (Object shapes) { //타입을 왜 object로 했을까? => 파일을 읽고 있을 때는 어떤 타입인지 모름. serialize를 하는 오브젝트의 최상위 클래스의 이름은 object라 함. 자바의 최상위 클래스 이름이 오브젝트
        this.shapes = (Vector<TShape>) shapes;
        this.repaint();	//paint란 이벤트를 발생시킴. ; paint는 우리가 절대로 호출하면 안되는 함수
    }

    public void setSelectedTool(Constants.ETools selectedTool) {this.selectedTool = selectedTool;}	//버튼 누르면 우리가 만든 enum 객체가 옴

    //overriding
    public void paint(Graphics graphics) {
        super.paint(graphics); //드로잉패널 자체를 그리는 것

        //1. 버퍼 지우기;  bufferedImage를 clear 시킨다는 것은 아예 새로운 종이를 만드는 것
        //clearRect 라는 건 그 안의 내용을 다 지워버리는 것
        //2. 그 안에 전체 그림을 다시 그리기;   그리고 for문을 통해 다시 다 그리는 것
        this.graphics2DBufferedImage.clearRect(0,0,this.bufferedImage.getWidth(),this.bufferedImage.getHeight());
                for(TShape shape:this.shapes) { //저장된 그림을 다 그려야함
                  shape.draw(this.graphics2DBufferedImage);
         }
        //3.모니터에 다시 그리기
        graphics.drawImage(this.bufferedImage,0,0,this);
    }

    private void prepareTransforming(int x, int y) { //transformer을 만들어야함 mover, resizer, rotator, drawer /드로잉하기 시작하면 쉐입을 만듦
        if(selectedTool == Constants.ETools.eSelection) 	{	//selection이면 mover, resizer, rotater 생길 수 있음
            currentShape = onShape(x, y);	//밑에 도형이 있는지 물어봄.
            if(currentShape!=null) {//밑에 도형이 있으면 move, resize, rotate 중 하나, 앵커가 없으면 커서는 그냥 무브가 됨
                TAnchors.EAnchors eAnchor = currentShape.getSelectedAnchor();
                if (eAnchor== TAnchors.EAnchors.eMove) {
                    this.transformer=new Mover(this.currentShape); //transformer한테 대상을 줌 move할

                }else if(eAnchor == TAnchors.EAnchors.eRR) {
                    this.transformer=new Rotator(this.currentShape); //transformer한테 대상을 줌 rotate할
                }else {
                    this.transformer=new Resizer(this.currentShape); //transformer한테 대상을 줌 resize할
                }
            }
            else {
                this.currentShape=this.selectedTool.newShape(); //transformer한테 대상을 줌 move할
                this.transformer=new Drawer(this.currentShape); //transformer한테 대상을 줌 move할
            }
        }
        else {
            this.currentShape=this.selectedTool.newShape(); //transformer한테 대상을 줌 move할
            this.transformer=new Drawer(this.currentShape); //transformer한테 대상을 줌 move할

        }
        //우리가 만든 bufferedImage에다가 그리라고 준 것
        this.transformer.prepare(x, y);
        //keepTransforming에서는 무조건 xor 모드로, prepare 끝나자마자 xor 모드로 바꾸고
        this.graphics2DBufferedImage.setXORMode(this.getBackground());

    }

    private void keepTransforming(int x, int y) {
        //erase 지우고
        this.currentShape.draw(this.graphics2DBufferedImage);
        this.getGraphics().drawImage(this.bufferedImage, 0,0,this); //모니터에 지운 이미지 그리기

        //transform 좌표 바꾸고
        this.transformer.keepTransforming(x, y); // 좌표를 바꿔줌

        //draw  그리고
        this.currentShape.draw(this.graphics2DBufferedImage); // 그릴 땐 graphic 필요
        this.getGraphics().drawImage(this.bufferedImage, 0,0,this); //모니터에 새로 그린 이미지 그리기
    }

    private void continueTransforming(int x, int y) { //중간에 점을 추가하는 함수 , N point 일 때만 실행됨
        this.currentShape.addPoint(x,y);
    }

    private void finishTransforming(int x, int y) {
        //다 끝나면 paint 모드로 바꾸고
        this.graphics2DBufferedImage.setPaintMode();    //다 그리고 나면 xor 모드에서 엎어쓰는 모드, paint 모드로 바껴야 함
        this.transformer.finalize(x, y);

        if(this.selectedShape != null) {
            this.selectedShape.setSelected(false);
        }
        if(!(this.currentShape instanceof TSelection)) {	//currentShape의 타입이 TSelection이 아니면
            this.shapes.add(this.currentShape);	//그림이 끝날 때마다 저장
            this.selectedShape = this.currentShape;	//그림을 그리고 나면 무조건 선택된 상태가 되는 것
            this.selectedShape.setSelected(true);	//그림을 그릴 때마다 앵커가
        }
        //repaint하면 이제까지 그린 그림이 다 이미지가 카피돼서 그래픽카드로 가버림
        this.repaint();
        this.setUpdated(true);

    }
/*
    private void prepareMoving(int x, int y) {

        Graphics2D graphics2D = (Graphics2D) this.getGraphics(); //2D에는 exclusive or이 있음
        graphics2D.setXORMode(this.getBackground());

        this.mover.prepare(x, y);	//도형한테 움직일 준비해라
        this.currentShape.draw(graphics2D);
    }

    private void keepMoving(int x, int y) {
        Graphics2D graphics2D = (Graphics2D) this.getGraphics(); //2D에는 exclusive or이 있음
        graphics2D.setXORMode(this.getBackground());

        //erase 같은 좌표에 다시 그림으로써 지워짐
        this.currentShape.draw(graphics2D);

        //draw
        this.mover.keepTransforming(x, y);
        this.currentShape.draw(graphics2D); // 그릴 땐 graphic 필요

    }

    private void finishMoving(int x, int y) {
        Graphics2D graphics2D = (Graphics2D) this.getGraphics(); //2D에는 exclusive or이 있음
        graphics2D.setXORMode(this.getBackground());

        if(this.selectedShape != null) {
            this.selectedShape.setSelected(false);
        }
        if(!(this.currentShape instanceof TSelection)) {	//currentShape의 타입이 TSelection이 아니면

            //add하면 안됨. 그릴 때만 하는 것
            this.selectedShape = this.currentShape;	//그림을 그리고 나면 무조건 선택된 상태가 되는 것
            this.selectedShape.setSelected(true);	//그림을 그릴 때마다 앵커가
            this.mover.finalize(x, y);
            this.selectedShape.draw((Graphics2D) this.getGraphics());
        }
        this.repaint();
    }
*/
    private TShape onShape(int x, int y) {//벡터에 저장된 도형들 위에 커서가 있는지
        for(TShape shape: this.shapes) {
            if(shape.contains(x,y)) {	//그려진 도형한테 점이 도형 위에 있는지 확인하는 함수
                return shape;
            }
        }
        return null;
    }

    private void changeSelection(int x, int y) { //우선 커서가 도형 위에 있는지 없는지 확인, 있다면 기존에 있는 앵커를 지우고 새롭게 그려야 함
        if(this.selectedShape != null) {
            this.selectedShape.setSelected(false);
        }
        this.repaint();

        //draw anchors
        this.selectedShape = this.onShape(x, y);
        if(this.selectedShape != null) {
            this.selectedShape.setSelected(true);
            this.selectedShape.draw((Graphics2D) this.getGraphics());
        }
    }

    private void changeCursor(int x, int y) {
        Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);	//그림 그리는 커서
        if(this.selectedTool == Constants.ETools.eSelection) {
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);	//selection이면 default cursor로

            this.currentShape = this.onShape(x, y);
            if(this.currentShape!=null) {//밑에 도형이 있으면 move, resize, rotate 중 하나, 앵커가 없으면 커서는 그냥 무브가 됨
                cursor = new Cursor(Cursor.MOVE_CURSOR);
                if(this.currentShape.isSelected()) {	//앵커가 있는것, 어떤 앵커인지 봐야 함
                    TAnchors.EAnchors eAnchor = this.currentShape.getSelectedAnchor();
                    switch (eAnchor) {
                        case eRR: cursor = new Cursor(Cursor.HAND_CURSOR);      break;
                        case eNW: cursor = new Cursor(Cursor.NW_RESIZE_CURSOR); break;
                        case eWW: cursor = new Cursor(Cursor.W_RESIZE_CURSOR);  break;
                        case eSW: cursor = new Cursor(Cursor.SW_RESIZE_CURSOR); break;
                        case eSS: cursor = new Cursor(Cursor.S_RESIZE_CURSOR);  break;
                        case eSE: cursor = new Cursor(Cursor.SE_RESIZE_CURSOR); break;
                        case eEE: cursor = new Cursor(Cursor.E_RESIZE_CURSOR);  break;
                        case eNE: cursor = new Cursor(Cursor.NE_RESIZE_CURSOR); break;
                        case eNN: cursor = new Cursor(Cursor.N_RESIZE_CURSOR);  break;
                        default:                                                break;
                    }
                }
            }
        }
        this.setCursor(cursor); //드로잉 패널에 둘 중 바뀐 커서로 바꿔줌
    }

    private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        //이벤트 핸들러에선 해석만 해주는 것, 해석해서 패널에 일을 지시하는 역할. 프로그램은 패널에 짜야함
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) { //L버튼
                if (e.getClickCount() == 1) {
                    this.lButtonClick(e);
                }
                else if (e.getClickCount() == 2) {
                    this.lButtonDoubleClick(e);
                }
            }
        }

        private void lButtonClick(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                changeSelection(e.getX(),e.getY());
                if(selectedTool.getTransformationStyle() == Constants.ETransformationStyle.eNPTransformation) {
                    prepareTransforming(e.getX(),e.getY()); //이 점이 그림 그릴 원점이니 그릴 준비하라는 함수
                    eDrawingState = EDrawingState.eNPointDrawing;
                }
            } else if(eDrawingState == EDrawingState.eNPointDrawing) {
                continueTransforming(e.getX(),e.getY());
            }
        }

        private void lButtonDoubleClick(MouseEvent e) {
            if (eDrawingState == EDrawingState.eNPointDrawing) {
                finishTransforming(e.getX(),e.getY()); // 마무리 해라
                eDrawingState = EDrawingState.eIdle;
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) { //mousemoved 제약조건  1.상태가 idle 2.도형 위에 있는지
            if (eDrawingState == EDrawingState.eNPointDrawing) {
                keepTransforming(e.getX(),e.getY()); // 계속 지우고 그리고 해라
            } else if(eDrawingState == EDrawingState.eIdle) {
                changeCursor(e.getX(),e.getY()); //좌표에 따라 커서를 바꿔라
            }
        }
        @Override
        public void mousePressed(MouseEvent e) { //2point transformer만 하게 만들어 놓음
            if (eDrawingState == EDrawingState.eIdle) {
                if(selectedTool.getTransformationStyle()== Constants.ETransformationStyle.e2PTransformation) {	//2point transformation을 하는 애라면 /결론은 폴리건이 아니라는 뜻
                    prepareTransforming(e.getX(), e.getY()); //move, rotate, ...무언
                    eDrawingState = EDrawingState.e2PointDrawing;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PointDrawing) {
                //		keepDrawing(e.getX(),e.getY()); // 계속 지우고 그리고 해라
                keepTransforming(e.getX(),e.getY());

            }else if(eDrawingState == EDrawingState.eMoving){
                keepTransforming(e.getX(),e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PointDrawing) {
                //	finishDrawing(e.getX(),e.getY()); // 마무리 해라
                finishTransforming(e.getX(),e.getY());
                eDrawingState = EDrawingState.eIdle;
            }
            else if (eDrawingState == EDrawingState.eMoving) {
                finishTransforming(e.getX(),e.getY());
                eDrawingState = EDrawingState.eIdle;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {


        }
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

    }
}
