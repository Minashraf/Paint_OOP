package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Ellipse extends Shapes {
    private Map<String,Double> Properties=new HashMap<>();
    private Ellipse(int x1,int y1)
    {
        this.setPosition(new Point(x1,y1));
        Properties.put("width",(double)x1);
        Properties.put("height",(double)y1);
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
        canvas.drawOval(this.getPosition().x,this.getPosition().y,
                (int)Math.round(Properties.get("width")),(int)Math.round(Properties.get("height")));
    }
    public Object clone() throws CloneNotSupportedException {
        return new Ellipse(this.getPosition().x+5,this.getPosition().y+5);
    }
}
