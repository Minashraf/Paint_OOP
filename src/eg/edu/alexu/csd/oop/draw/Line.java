package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Line extends Shapes {
    private Map<String,Double> Properties=new HashMap<>();
    private Line(int x1,int y1)
    {
        this.setPosition(new Point(x1,y1));
        Properties.put("x2",(double)x1);
        Properties.put("y2",(double)y1);
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
        canvas.drawLine(this.getPosition().x,this.getPosition().y,
                (int)Math.round(Properties.get("x2")),(int)Math.round(Properties.get("y2")));
    }
    public Object clone() throws CloneNotSupportedException {
        Shape line=new Line(this.getPosition().x+5,this.getPosition().y+5);
        line.setProperties(this.getProperties());
        line.setColor(this.getColor());
        line.setFillColor(this.getFillColor());
        return line;
    }
}
