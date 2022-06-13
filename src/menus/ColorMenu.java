package menus;

import frames.DrawingPanel;
import global.Constants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.KeyStroke;

public class ColorMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    private DrawingPanel drawingPanel;

    //드로잉패널 연결
    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    public void initialize() {
    }
    protected DrawingPanel getDrawingPanel(){
        return this.drawingPanel;
    }

    public ColorMenu(String s) {
        super(s);

        ActionHandler actionHandler = new ActionHandler();
        for (Constants.EColorMenu eColorMenu : Constants.EColorMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eColorMenu.getLable());
            menuItem.setAccelerator(KeyStroke.getKeyStroke(eColorMenu.getC(), Event.SHIFT_MASK));
            menuItem.setToolTipText(eColorMenu.getTip());
            menuItem.setFont(new Font("맑은 고딕", Font.ITALIC,15));
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eColorMenu.name());
            this.add(menuItem);
        }
    }

    public void line() {
        Color color=JColorChooser.showDialog(this.getDrawingPanel(), "Line Color", this.getDrawingPanel().getForeground());
        this.getDrawingPanel().setLineColor(color);
    }

    public void fill() {
        Color color=JColorChooser.showDialog(this.getDrawingPanel(), "Fill Color", this.getDrawingPanel().getForeground());
        this.getDrawingPanel().setFillColor(color);
    }
/*
    public void initialize(DrawingPanel drawingPanel) {
        super.initialize(drawingPanel);
    }

 */
    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(Constants.EColorMenu.eLine.name())) {
                line(); //새로운 화면, 드로잉패널에서 해야할 일
            } else if(e.getActionCommand().equals(Constants.EColorMenu.eFill.name())) {
                fill();
            }
    }
}}
