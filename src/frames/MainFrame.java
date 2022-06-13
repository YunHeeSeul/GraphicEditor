package frames;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    //attributes
    private static final long serialVersionUID = 1L;

    //components
    private MenuBar menuBar;
    private ToolBar toolBar;
    private DrawingPanel drawingPanel;

    public MainFrame() {
        //attributes
        this.setSize(700, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //메인프레임에서 close 버튼을 누르면 프로그램이 끝남.

        //components; 자식을 만들어서 본인한테 등록시키는 부분
        BorderLayout layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        this.menuBar = new MenuBar();
        this.setJMenuBar(this.menuBar);

        this.toolBar = new ToolBar();
        this.add(this.toolBar, BorderLayout.NORTH);

        this.drawingPanel = new DrawingPanel();
        this.add(this.drawingPanel, BorderLayout.CENTER);

        //association(본인과 자식을 연결)
        this.menuBar.associate(this.drawingPanel); //툴바한테 드로잉패널을 연결하라함.(association)
        this.toolBar.associate(this.drawingPanel); //툴바한테 드로잉패널을 연결하라함.(association)


    }

    public void initialize() {
        this.menuBar.initialize();
        this.toolBar.initialize();
        this.drawingPanel.initialize();

    }
}