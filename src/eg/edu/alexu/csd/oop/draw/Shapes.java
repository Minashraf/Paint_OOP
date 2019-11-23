package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.Map;

public abstract class Shapes implements Shape {
    private int x,y;
    private Color Color,FillColor;

    @Override
    public void setPosition(Point position) {
        this.x=position.x;
        this.y=position.y;
    }

    @Override
    public Point getPosition() {
        return new Point(this.x,this.y);
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
    }

    @Override
    public Map<String, Double> getProperties() {
        return null;
    }

    @Override
    public void setColor(Color color) {
        this.Color=color;
    }

    @Override
    public Color getColor() {
        return this.Color;
    }

    @Override
    public void setFillColor(Color color) {
        this.FillColor=color;
    }

    @Override
    public Color getFillColor() {
        return this.FillColor;
    }

    @Override
    public void draw(Graphics canvas) {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
