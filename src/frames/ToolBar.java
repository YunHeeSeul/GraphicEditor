package frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import global.Constants.ETools;

public class ToolBar extends JToolBar {
    //attributes
    private static final long serialVersionUID = 1L;



    //associations (부모(메인프레임)가 만들어줌)
    private DrawingPanel drawingPanel;


    public ToolBar() {
        this.setBackground(Color.LIGHT_GRAY);
        //components
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionHandler actionHandler = new ActionHandler(); //액션핸들러 달아줌


        for(ETools eTool: ETools.values()) {
            JRadioButton toolButton = new JRadioButton(eTool.getLabel(),eTool.getIcon());
            toolButton.setBackground(Color.PINK);
            toolButton.setForeground(Color.BLUE);
            toolButton.setFont(new Font("맑은 고딕", Font.BOLD,15));
            toolButton.setToolTipText(eTool.getTip());
            toolButton.setActionCommand(eTool.name()); //string name을 주는 것
            toolButton.addActionListener(actionHandler);
            this.add(toolButton);
            buttonGroup.add(toolButton);
        }


    }

    public void associate(DrawingPanel drawingPanel) { //저장할 곳을 만들어줌
        this.drawingPanel = drawingPanel;

        //down casting; 원래 shape으로 쓰는데 구체적으로 eRectangle, eOval.. 등 구체적으로 하위클래스의 형태로 쓸 때가 있음 그럴 때 type cast 사용
        JRadioButton defaultButton = (JRadioButton) this.getComponent(ETools.eSelection.ordinal()); // 컴포넌트에서 꺼내옴. ETools eRectangle을 선택, 근데 eRectangle은 숫자가 아닌 객체이므로 ordinal()[enumeration object를 숫자로 바꿔주는 함수]를 사용하여 eRectangle의 순서가 몇번째인지 물어보는것
        //연결이 되야 에러(NULL)이 안 생김
        defaultButton.doClick(); //미리 버튼을 눌러놓음
    }
    //항상 association 밑에 달아놓기
    public void initialize() {
    }

    private class ActionHandler implements ActionListener{ //버튼에 액션핸들러 달아줌. 버튼이 눌릴 때 밑의 함수가 호출됨
        @Override
        public void actionPerformed(ActionEvent e) {
            //액션커맨드에서 나온 이름(string) 가지고 실제로 enumeration 객체를 찾아서 줌
            drawingPanel.setSelectedTool(ETools.valueOf(e.getActionCommand()));
        }
    }
}
