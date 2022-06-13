package menus;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import frames.DrawingPanel;
import global.Constants.EFileMenu;

public class FileMenu extends JMenu {

    private static final long serialVersionUID = 1L;
    private File file;	//File 타입 참조 변수 선언; file에 저장

    private DrawingPanel drawingPanel;

    @SuppressWarnings("deprecation")//권장되지 않는 기능과 관련된 경고를 억제
    public FileMenu(String title) {
        super(title);

        this.file = null;	//파일 상태를 초기화 해줌
        //버튼에 액션 핸들러 달려 있음. 원래는 버튼마다 달리지만 코드가 완전히 같아서 하나만 만듦
        //n개의 버튼에 전체 하나의 액션핸들러가 공유
        ActionHandler actionHandler = new ActionHandler();
        for(EFileMenu eMenuItem: EFileMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eMenuItem.getLabel());	//menuItem 객체 생성
            //단축키 설정; setAccelerator() 메소드 사용
            //대문자에 가속키 Ctrl
            menuItem.setAccelerator(KeyStroke.getKeyStroke(eMenuItem.getC(), Event.CTRL_MASK));
            //툴팁 설정
            menuItem.setToolTipText(eMenuItem.getTip());
            //폰트 설정
            menuItem.setFont(new Font("맑은 고딕", Font.ITALIC,15));
            //메뉴 아이템 배경 및 글씨 색상 설정
            menuItem.setBackground(Color.LIGHT_GRAY);
            menuItem.setForeground(Color.pink);
            //메뉴아이템 항목마다 actionHandler 추가
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eMenuItem.name());
            //파일 메뉴에 메뉴아이템 항목들 추가
            this.add(menuItem);
        }
    }

    //드로잉패널 연결
    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    //항상 association 밑에 달아놓기
    public void initialize() {
    }
    private boolean saveCheck() {
        boolean check = true;
        if(this.drawingPanel.isUpdated()) {
            int reply = JOptionPane.NO_OPTION;
            if(this.drawingPanel.isUpdated()) {
                reply = JOptionPane.showConfirmDialog(this.drawingPanel, "변경 내용을 저장 할까요?");
                if(reply!=JOptionPane.CANCEL_OPTION) {
                    if(reply == JOptionPane.OK_OPTION) {
                        check = this.save();
                    }
                }
                else
                    check = false;
            }

        }
        return check;
    }

    public void newPanel() {
        boolean check = this.saveCheck();
        if(!check) {

        }
    }

    public void open() {
        boolean check = this.saveCheck();
        if(check) {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(this.drawingPanel); //dialog가 drawingPanel을 부모로 설정해줘야 드로잉패널이 종료되면 dialog도 자동으로 종료
            if(returnVal == JFileChooser.APPROVE_OPTION) {	//approve_option == ok면
                this.file = fileChooser.getSelectedFile();
                this.load(this.file);// this.file을 주고서 load 함수 호출
            }
        }

    }

    public boolean save() {
        boolean check = true;

        if(this.drawingPanel.isUpdated()) {	//드로잉 패널에 변화가 있는지 없는지
            if(this.file==null) {
                check = this.saveas();
            } else {
                this.store(this.file);
            }
        }
        return check;
    }

    public boolean saveas() {
        boolean check = true;

        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this.drawingPanel); //dialog가 drawingPanel을 부모로 설정해줘야 드로잉패널이 종료되면 dialog도 자동으로 종료
        if(returnVal == JFileChooser.APPROVE_OPTION) {	//approve_option ==> ok면
            this.file = fileChooser.getSelectedFile();	//fileChooser한테 어떤 파일을 선택했는지를 가져와야함
            this.store(this.file);// this.file에 저장하라고
        } else {
            check = false;
        }
        return check;
    }

    public void print() {
        // TODO Auto-generated method stub

    }

    public void quit() {
        boolean check = this.saveCheck();
        int result=JOptionPane.showConfirmDialog(null, "종료하시겠습니까?","확인",JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION)
            if(check) {
                System.exit(0);
            } else
                System.exit(0);


    }

    private void store(File file) {	//dialog에서 정의한 파일을 받아서 정의
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);		//()안엔 파일 이름 쓰는 것
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream); // 파일아웃풋스트림과 연결; 파일아웃풋스트림이 있어야만 동작이 됨.
            //오브젝트아웃풋스트림은 오브젝트를 바이트스트림으로 변형만 시켜주는 것
            objectOutputStream.writeObject(this.drawingPanel.getShapes()); //드로잉패널에 있는 shape을 가져와서 연결
            objectOutputStream.close(); // 오브젝트가 파일을 연결해놨기 때문에 오브젝트를 클로즈하면 파일도 자동으로 클로즈
            JOptionPane.showMessageDialog(null, "저장하였습니다."); // 메시지 출력
            this.drawingPanel.setUpdated(false);	//저장되면 드로잉패널에 다시 false로 상태 바꿔줌

        }catch(FileNotFoundException e){
            e.printStackTrace();

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    private void load(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream); // 파일아웃풋스트림과 연결
            Object object = objectInputStream.readObject();	//오브젝트를 읽어오는
            this.drawingPanel.setShapes(object);	//읽어서 드로잉 패널에 주고 shapes에다 저장
            objectInputStream.close();
            JOptionPane.showMessageDialog(null, "파일을 불러왔습니다."); // 메시지 출력

        }catch(FileNotFoundException e){
            e.printStackTrace();

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    class ActionHandler implements ActionListener{	//
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(EFileMenu.eNew.name())) {
                newPanel(); //새로운 화면, 드로잉패널에서 해야할 일
            } else if(e.getActionCommand().equals(EFileMenu.eOpen.name())) {	//open을 누르면
                open();
            } else if(e.getActionCommand().equals(EFileMenu.eSave.name())) {
                save();
            } else if(e.getActionCommand().equals(EFileMenu.eSaveAs.name())) {
                saveas();
            } else if(e.getActionCommand().equals(EFileMenu.ePrint.name())) {
                print();
            } else if(e.getActionCommand().equals(EFileMenu.eQuit.name())) {
                quit();
            }
        }
    }

}