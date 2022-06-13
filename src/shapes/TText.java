package shapes;

import javax.swing.*;
import java.awt.*;

public class TText extends TShape{
    JTextArea jta;

    public TText(int x, int y, Color c, String s, Font f){
        super(x, y, c, s, f);
    }
    public TText(){
        this.shape = (Shape) new JTextArea();
        JScrollPane jsp = new JScrollPane(jta);
    }
    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(c);
        graphics2D.setFont(f);
        graphics2D.drawString(s, x, y);
    }
    @Override
    public void prepareDrawing(int x, int y) {

    }

    @Override
    public void keepDrawing(int x, int y) {

    }

    @Override
    public TShape clone() {
        return null;
    }
}
