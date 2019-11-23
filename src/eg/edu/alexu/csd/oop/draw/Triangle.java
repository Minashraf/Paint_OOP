package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends Shapes {
    private Map<String,Double> Properties;
    public Triangle()
    {
        Properties=new HashMap<>();
        Properties.put("x2",0.0);
        Properties.put("x3",0.0);
        Properties.put("y2",0.0);
        Properties.put("y3",0.0);
        this.setColor(Color.black);
        this.setFillColor(Color.white);
    }
    public Triangle(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("x2",(double)x1);
        Properties.put("x3",(double)x1);
        Properties.put("y2",(double)y1);
        Properties.put("y3",(double)y1);
        this.setColor(Color.black);
        this.setFillColor(Color.white);
    }
    @Override
    public void setProperties(Map<String, Double> properties) {
        this.Properties=properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return this.Properties;
    }
    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(this.getFillColor());
        canvas.fillPolygon(new int []{this.getPosition().x,(int)Math.round(Properties.get("x2")),
                        (int)Math.round(Properties.get("x3"))}
                ,new int[]{this.getPosition().y,(int)Math.round(Properties.get("y2")),
                        (int)Math.round(Properties.get("y3"))},3);
        ((Graphics2D)canvas).setStroke(new BasicStroke(2.0F));
        canvas.setColor(this.getColor());
        canvas.drawPolygon(new int []{this.getPosition().x,(int)Math.round(Properties.get("x2")),
                (int)Math.round(Properties.get("x3"))}
                ,new int[]{this.getPosition().y,(int)Math.round(Properties.get("y2")),
                        (int)Math.round(Properties.get("y3"))},3);
    }
    public Object clone() throws CloneNotSupportedException {
        Shape triangle=new Triangle(this.getPosition().x+100,this.getPosition().y+100);
        Map<String,Double>prop=new HashMap<>();
        prop.put("x2",Properties.get("x2")+100);
        prop.put("y2",Properties.get("y2")+100);
        prop.put("x3",Properties.get("x3")+100);
        prop.put("y3",Properties.get("y3")+100);
        triangle.setProperties(prop);
        triangle.setColor(this.getColor());
        triangle.setFillColor(this.getFillColor());
        return triangle;
    }
}
