package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Engine implements DrawingEngine {
    private List<Shape> shapes;
    public Engine()
    {
        shapes=new ArrayList<>();
    }
    @Override
    public void refresh(Graphics canvas) {
        for (Shape s:shapes)
            s.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void removeShape(Shape shape) {
        for(Shape s:shapes)
        {
            if(shape.getColor()==s.getColor()
               &&shape.getFillColor()==s.getFillColor()
               &&shape.getProperties()==s.getProperties()
               &&shape.getClass()==s.getClass()
               &&(shape.getPosition()==null&&s.getPosition()==null)
               ||(shape.getPosition().x==s.getPosition().x
               &&shape.getPosition().y==s.getPosition().y))
            {
                shapes.remove(s);
                return;
            }
        }
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        for(Shape s:shapes)
        {
            if(oldShape.getColor()==s.getColor()
                    &&oldShape.getFillColor()==s.getFillColor()
                    &&oldShape.getProperties()==s.getProperties()
                    &&oldShape.getClass()==s.getClass()
                    &&(oldShape.getPosition()==null&&s.getPosition()==null)
                    ||(oldShape.getPosition().x==s.getPosition().x
                    &&oldShape.getPosition().y==s.getPosition().y))
            {
                shapes.remove(s);
                shapes.add(newShape);
                return;
            }
        }
    }

    @Override
    public Shape[] getShapes() {
        Shape[] AllShapes=new Shape[shapes.size()];
        for(int i=0;i<shapes.size();i++)
            AllShapes[i]=shapes.get(i);
        return AllShapes;
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return null;
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void save(String path) {

    }

    @Override
    public void load(String path) {

    }
}
