package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends Shapes {
    private Map<String,Double> Properties;
    private Triangle(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("x2",(double)x1);
        Properties.put("x3",(double)x1);
        Properties.put("y2",(double)y1);
        Properties.put("y3",(double)y1);
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
        canvas.drawPolygon(new int []{this.getPosition().x,(int)Math.round(Properties.get("x2")),
                (int)Math.round(Properties.get("x3"))}
                ,new int[]{this.getPosition().y,(int)Math.round(Properties.get("y2")),
                        (int)Math.round(Properties.get("y3"))},3);
    }
    public Object clone() throws CloneNotSupportedException {
        Shape triangle=new Triangle(this.getPosition().x+5,this.getPosition().y+5);
        triangle.setProperties(this.getProperties());
        triangle.setColor(this.getColor());
        triangle.setFillColor(this.getFillColor());
        return triangle;
    }
}
