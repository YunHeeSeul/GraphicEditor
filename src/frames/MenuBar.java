package frames;

import javax.swing.JMenuBar;

import menus.ColorMenu;
import menus.EditMenu;
import menus.FileMenu;

public class MenuBar extends JMenuBar {
    //attributes
    private static final long serialVersionUID = 1L;

    //components
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private ColorMenu colorMenu;

    private DrawingPanel drawingPanel;

    public MenuBar() {

        //components
        this.fileMenu = new FileMenu("FILE");
        this.add(this.fileMenu);

        this.editMenu = new EditMenu("EDIT");
        this.add(this.editMenu);

        this.colorMenu = new ColorMenu("COLOR");
        this.add(this.colorMenu);
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.fileMenu.associate(this.drawingPanel); //자식한테 또 associate 해주는 것
        this.editMenu.associate(this.drawingPanel);
        this.colorMenu.associate(this.drawingPanel);
    }

    public void initialize() {
        this.fileMenu.initialize();
        this.editMenu.initialize();
        this.colorMenu.initialize();
    }
}
