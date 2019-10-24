package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Circle extends Shapes {
    private Map<String,Double> Properties;
    private Circle(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("radius",0.0);
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
                (int)Math.round(Properties.get("radius"))*2,(int)Math.round(Properties.get("radius"))*2);
    }
    public Object clone() throws CloneNotSupportedException {
        Shape circle=new Circle(this.getPosition().x+5,this.getPosition().y+5);
        circle.setProperties(this.getProperties());
        circle.setColor(this.getColor());
        circle.setFillColor(this.getFillColor());
        return circle;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Shape test=new Circle(0,0);
        Map<String,Double> pro=new HashMap<>();
        pro.put("radius",98.5);
        test.setColor(Color.cyan);
        test.setProperties(pro);
        Shape cl= (Circle) test.clone();
    }
}
