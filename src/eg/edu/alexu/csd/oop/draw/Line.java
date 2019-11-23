package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Line extends Shapes {
    private Map<String,Double> Properties;
    public Line()
    {
        Properties=new HashMap<>();
        Properties.put("x2",0.0);
        Properties.put("y2",0.0);
        this.setColor(Color.black);
    }
    public Line(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("x2",(double)x1);
        Properties.put("y2",(double)y1);
        this.setColor(Color.black);
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
        ((Graphics2D)canvas).setStroke(new BasicStroke(2.0F));
        canvas.setColor(this.getColor());
        canvas.drawLine(this.getPosition().x,this.getPosition().y,
                (int)Math.round(Properties.get("x2")),(int)Math.round(Properties.get("y2")));
    }
    public Object clone() throws CloneNotSupportedException {
        Shape line=new Line(this.getPosition().x+100,this.getPosition().y+100);
        Map<String ,Double>prop=new HashMap<>();
        prop.put("x2",Properties.get("x2")+100);
        prop.put("y2",Properties.get("y2")+100);
        line.setProperties(prop);
        line.setColor(this.getColor());
        return line;
    }
}
