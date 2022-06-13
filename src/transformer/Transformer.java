package transformer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import shapes.TAnchors;
import shapes.TShape;

public abstract class Transformer {

    protected TShape selectedShape;
    protected AffineTransform affineTransform;	//matrix 만들것
    protected TAnchors anchors;

    protected int px, py;	//
    protected double cx, cy;	//
    protected Point2D before, center;


    public Transformer(TShape selectedShape) {
        this.selectedShape = selectedShape;
        this.affineTransform=this.selectedShape.getAffineTransform(); //변형하려면 어파인 트랜스폼을 가져와서 변형
        this.anchors=this.selectedShape.getAnchors(); //변형하려면 어파인 트랜스폼을 가져와서 변형
        this.center = new Point2D.Double();
        this.before = new Point2D.Double();
    }
    public abstract void prepare(int x, int y);
    public abstract void keepTransforming(int x, int y);
    public abstract void finalize(int x, int y);




}