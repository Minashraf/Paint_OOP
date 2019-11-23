package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Circle extends Shapes {
    private Map<String,Double> Properties;
    public Circle()
    {
        Properties=new HashMap<>();
        Properties.put("radius",0.0);
        this.setColor(Color.black);
        this.setFillColor(Color.white);
    }
    public Circle(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("radius",0.0);
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
        canvas.setColor(getFillColor());
        canvas.fillOval(this.getPosition().x-(int)Math.round(Properties.get("radius")),this.getPosition().y-(int)Math.round(Properties.get("radius")),
                (int)Math.round(Properties.get("radius"))*2,(int)Math.round(Properties.get("radius"))*2);
        ((Graphics2D)canvas).setStroke(new BasicStroke(2.0F));
        canvas.setColor(getColor());
        canvas.drawOval(this.getPosition().x-(int)Math.round(Properties.get("radius")),this.getPosition().y-(int)Math.round(Properties.get("radius")),
                (int)Math.round(Properties.get("radius"))*2,(int)Math.round(Properties.get("radius"))*2);
    }
    public Object clone() throws CloneNotSupportedException {
        Shape circle=new Circle(this.getPosition().x+100,this.getPosition().y+100);
        circle.setProperties(this.getProperties());
        circle.setColor(this.getColor());
        circle.setFillColor(this.getFillColor());
        return circle;
    }
}
