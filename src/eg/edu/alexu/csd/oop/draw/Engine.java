package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Engine implements DrawingEngine {
    private  List<Shape> shapes;
    private  List<Shape>[] History;
    private int end=0,current=-1,steps=0;
    public Engine()
    {
        shapes=new ArrayList<>();
        History=new List[20];
    }
    @Override
    public void refresh(Graphics canvas) {
        for (Shape s:shapes)
            s.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        steps=0;
        shapes=new ArrayList<>();
        if(current!=-1)
        {
            List <Shape> transition=History[(end-1)%20];
            for(Shape s: transition)
               shapes.add(s);
        }
        shapes.add(shape);
        History[end%20]= shapes;
        current=end;
        end++;
    }

    @Override
    public void removeShape(Shape shape) {
        steps=0;
        current=end;
        shapes=new ArrayList<>();
        List <Shape> transition=History[(end-1)%20];
        for(Shape s: transition)
            shapes.add(s);
        for(Shape s:shapes)
        {
            if(CheckExistence(shape,s))
            {
                shapes.remove(s);
                History[end%20]=shapes;
                end++;
                return;
            }
        }
    }
    private boolean CheckExistence(Shape shape, Shape InList)
    {
        return shape.getColor()==InList.getColor()
                &&shape.getFillColor()==InList.getFillColor()
                &&shape.getProperties()==InList.getProperties()
                &&shape.getClass()==InList.getClass()
                &&(shape.getPosition()==null&&InList.getPosition()==null)
                ||(shape.getPosition().x==InList.getPosition().x
                &&shape.getPosition().y==InList.getPosition().y);
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        steps=0;
        current=end;
        shapes=new ArrayList<>();
        List <Shape> transition=History[(end-1)%20];
        for(Shape s: transition)
            shapes.add(s);
        for(Shape s:shapes)
        {
            if(CheckExistence(oldShape,s))
            {
                shapes.remove(s);
                shapes.add(newShape);
                History[end%20]=shapes;
                end++;
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
        if(steps!=19&&current!=-1&&steps<end-1)
        {
            steps++;
            if(current==0)
                current=20;
            shapes=History[(current-1)%20];
            current=(current-1)%20;
        }
    }

    @Override
    public void redo() {
        if(steps!=0&&current!=-1)
        {
            steps--;
            shapes=History[(current+1)%20];
            current=(current+1)%20;
        }
    }

    @Override
    public void save(String path) {

    }

    @Override
    public void load(String path) {

    }
}
