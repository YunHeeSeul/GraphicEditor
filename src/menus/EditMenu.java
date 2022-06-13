package menus;

import frames.DrawingPanel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class EditMenu extends JMenu {

    private static final long serialVersionUID = 1L;

    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem groupItem;
    private JMenuItem ungroupItem;
    private DrawingPanel drawingPanel;

    //드로잉패널 연결
    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    public void initialize() {
    }

    public EditMenu(String s) {
        super(s);

        this.undoItem = new JMenuItem("UNDO");
        this.add(this.undoItem);

        this.redoItem = new JMenuItem("REDO");
        this.add(this.redoItem);

        this.cutItem = new JMenuItem("CUT");
        this.add(this.cutItem);

        this.copyItem = new JMenuItem("COPY");
        this.add(this.copyItem);

        this.pasteItem = new JMenuItem("PASTE");
        this.add(this.pasteItem);

        this.groupItem = new JMenuItem("GROUP");
        this.add(this.groupItem);

        this.ungroupItem = new JMenuItem("UNGROUP");
        this.add(this.ungroupItem);
    }
}