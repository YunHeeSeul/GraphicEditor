package global;

import shapes.*;

import java.awt.Image;

import javax.swing.*;

public class Constants {

    public enum ETransformationStyle{
        e2PTransformation,
        eNPTransformation
    }
    //tool과 shape을 연결해주는 역할을 함
    public enum ETools{ // 서수 만듦. 0,1,2,3으로 읽힘 순서가 생김
        eSelection("선택", new TSelection(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\사각형.png"),"사각형 그리기", ETransformationStyle.e2PTransformation),	//enumeration 객체에다가 shape을 붙임
        eRectangle("사각형",new TRectangle(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\사각형.png"),"사각형 그리기", ETransformationStyle.e2PTransformation),	//enumeration 객체에다가 shape을 붙임
        eOval("동그라미",new TOval(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\타원.png"), "타원 그리기", ETransformationStyle.e2PTransformation),
        eLine("라인",new TLine(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\직선.png"),"직선 그리기", ETransformationStyle.e2PTransformation),
        ePolygon("폴리건",new TPolygon(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\다각형.png"),"폴리건 그리기", ETransformationStyle.eNPTransformation),
        eText("텍스트",new TText(),new ImageIcon ("C:\\Users\\윤희슬\\Desktop\\텍스트.png"),"텍스트 쓰기", ETransformationStyle.e2PTransformation);

        //서수를 객체로 만들면 클래스를 따로 만들어야함
        private String label;
        private TShape tool;
        private ImageIcon icon;
        private String tip;
        private ETransformationStyle eTransformationStyle;

        private ETools(String label, TShape tool, ImageIcon icon1, String tip, ETransformationStyle eTransformationStyle) { //TShape이 파라미터로 넘어옴
            this.label=label;
            this.tool=tool;
            ImageIcon icon = imageSetSize(icon1,25,25);
            this.icon=icon;
            this.tip = tip;
            this.eTransformationStyle=eTransformationStyle;
        }

        public String getLabel() {
            return this.label;	//화면에 나오는 글씨 붙임
        }
        public TShape newShape() {
            return this.tool.clone();	//ETools에 있는 객체를 그냥 가져오는 것이 아니라 newShape을 이용해서 shape을 만들어서 가져오는 것
        }
        public ImageIcon getIcon() {
            return this.icon;
        }
        public String getTip() {
            return this.tip;
        }

        public ETransformationStyle getTransformationStyle() {
            return this.eTransformationStyle;
        }

        ImageIcon imageSetSize(ImageIcon icon, int i, int j) {
            Image past = icon.getImage();	//imageicon을 image로 변환
            Image img = past.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH); //크기 변환
            ImageIcon now = new ImageIcon(img); //새로운 이미지를 다시  icon으로
            return now;
        }

    }

    public enum EFileMenu {
        eNew("새로만들기","새로운 그림판 만들기", 'N'),
        eOpen("열기","저장되어 있는 그림 불러오기",'O'),
        eSave("저장하기","저장하기",'S'),
        eSaveAs("다른이름으로","다른 이름으로 새롭게 저장하기",' '),
        ePrint("프린트","프린트하기",'P'),
        eQuit("종료","프로그램 종료하기",'X');

        private String label;
        private String tip;
        private char c;

        private EFileMenu(String label, String tip, char c) {
            this.label=label;	//화면에 나오는 글씨 붙임
            this.tip = tip;
            this.c=c;
        }

        public String getLabel() {
            return this.label;}	//화면에 나오는 글씨 붙임
        public String getTip() {
            return this.tip;
        }
        public char getC() {
            return this.c;
        }
    }

    public enum EColorMenu {
        eLine("LineColor", "LineColor",'L'),
        eFill("FillColor", "FillColor",'F');

        private String label;
        private String tip;
        private char c;

        private EColorMenu(String label, String tip, char c) {
            this.label = label;
            this.tip = tip;
            this.c=c;
        }
        public String getLable() {
            return this.label;
        }
        public String getTip() {
            return this.tip;
        }
        public char getC() {
            return this.c;
        }
    }

}
