package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Square extends Shapes {
    private Map<String,Double> Properties;
    public Square()
    {
        Properties=new HashMap<>();
        Properties.put("side",0.0);
        this.setColor(Color.black);
        this.setFillColor(Color.white);
    }
    public Square(int x1,int y1)
    {
        Properties=new HashMap<>();
        this.setPosition(new Point(x1,y1));
        Properties.put("side",0.0);
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
        canvas.fillRect((int)((double)this.getPosition().x-(Properties.get("side")/2)),(int)((double)this.getPosition().y-(Properties.get("side")/2)),
                (int)Math.round(Properties.get("side")),(int)Math.round(Properties.get("side")));
        ((Graphics2D)canvas).setStroke(new BasicStroke(2.0F));
        canvas.setColor(this.getColor());
        canvas.drawRect((int)((double)this.getPosition().x-(Properties.get("side")/2)),(int)((double)this.getPosition().y-(Properties.get("side")/2)),
                (int)Math.round(Properties.get("side")),(int)Math.round(Properties.get("side")));
    }
    public Object clone() throws CloneNotSupportedException {
        Shape square=new Square(this.getPosition().x+100,this.getPosition().y+100);
        square.setProperties(this.getProperties());
        square.setColor(this.getColor());
        square.setFillColor(this.getFillColor());
        return square;
    }
}
