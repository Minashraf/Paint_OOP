package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends Shapes {
    private Map<String,Double> Properties;
    private Rectangle(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("width",0.0);
        Properties.put("height",0.0);
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
        canvas.drawRect(this.getPosition().x,this.getPosition().y,
                (int)Math.round(Properties.get("width")),(int)Math.round(Properties.get("height")));
    }
    public Object clone() throws CloneNotSupportedException {
        Shape rectangle=new Rectangle(this.getPosition().x+5,this.getPosition().y+5);
        rectangle.setProperties(this.getProperties());
        rectangle.setColor(this.getColor());
        rectangle.setFillColor(this.getFillColor());
        return rectangle;
    }
}
