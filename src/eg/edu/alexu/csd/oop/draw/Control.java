package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
public class Control {
    private Map<String,Double> Properties;
    public Shape Contain(Point current, Shape[] AllShapes){
        for(int i=0;i<AllShapes.length;i++){
            Properties=AllShapes[i].getProperties();
            if(AllShapes[i] instanceof Circle){
                int x= AllShapes[i].getPosition().x;
                int y= AllShapes[i].getPosition().y;
                int radius=(int)Math.round(Properties.get("radius"));
                if(Math.sqrt(Math.pow(x-current.x,2)+Math.pow(y-current.y,2))<=radius)
                    return AllShapes[i];

            }else if(AllShapes[i] instanceof Ellipse){
                int width=(int)Math.round(Properties.get("width"));
                int height=(int)Math.round(Properties.get("height"));
                int x= AllShapes[i].getPosition().x;
                int y= AllShapes[i].getPosition().y;
                if(((Math.pow((current.x-x),2)/Math.pow(width,2))+(Math.pow((current.y-y),2)/Math.pow(height,2)))<=1){
                    return AllShapes[i];
                }
            }else if(AllShapes[i] instanceof Line){
                double x1= AllShapes[i].getPosition().x;
                double y1= AllShapes[i].getPosition().y;
                double x2=Properties.get("x2");
                double y2=Properties.get("y2");
                double OriginalDist= Point2D.distance(x1,y1,x2,y2);
                double FirstDist=Point2D.distance(x1,y1,current.x,current.y);
                double SecondDist=Point2D.distance(x2,y2,current.x,current.y);
                if(Math.abs(OriginalDist-(FirstDist+SecondDist))<Math.pow(10,-2))
                    return AllShapes[i];

            }else if(AllShapes[i] instanceof Rectangle){
                double width=(int)Math.round(Properties.get("width"));
                double height=(int)Math.round(Properties.get("height"));
                double x1= AllShapes[i].getPosition().x+width/2;
                double y1= AllShapes[i].getPosition().y+height/2;
                double x2=AllShapes[i].getPosition().x-width/2;
                double y2=AllShapes[i].getPosition().y-height/2;

                if(current.x<x1&&current.x>x2&&current.y<y1&&current.y>y2)
                    return AllShapes[i];
            }else if(AllShapes[i] instanceof Square){
                double side=Math.round(Properties.get("side"));
                double x1= AllShapes[i].getPosition().x+side/2;
                double y1= AllShapes[i].getPosition().y+side/2;
                double x2=AllShapes[i].getPosition().x-side/2;
                double y2=AllShapes[i].getPosition().y-side/2;
                if(current.x<x1&&current.x>x2&&current.y<y1&&current.y>y2)
                    return AllShapes[i];

            }else if(AllShapes[i] instanceof Triangle){
                double x1= AllShapes[i].getPosition().x;
                double y1= AllShapes[i].getPosition().y;
                double x2=Properties.get("x2");
                double x3=Properties.get("x3");
                double y2=Properties.get("y2");
                double y3=Properties.get("y3");
                double area,area1,area2,area3;
                area=Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2);
                area1=Math.abs((current.x*(y2-y3) + x2*(y3-current.y) + x3*(current.y-y2))/2);
                area2=Math.abs((x1*(current.y-y3) + current.x*(y3-y1) + x3*(y1-current.y))/2);
                area3=Math.abs((x1*(y2-current.y) + x2*(current.y-y1) + current.x*(y1-y2))/2);
                if(Math.abs(area-(area1+area2+area3))<Math.pow(10,-7))
                    return AllShapes[i];

            }
        }

        return null;
    }
    public void CircleProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField Radius = new JTextField();
        Radius.setText(String.valueOf(exists.getProperties().get("radius")));
        final Color[] Out = {exists.getColor()};
        final Color[] In = {exists.getFillColor()};
        JButton Color=new JButton("Color");
        JButton FillColor=new JButton("Fill Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                FillColor,
                new JLabel("Radius"),
                Radius,
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        FillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                In[0] =JColorChooser.showDialog(panel1,"Select a color", In[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Circle(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);New.setFillColor(In[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("radius",Double.parseDouble(Radius.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
    public void EllipseProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField Width = new JTextField();
        Width.setText(String.valueOf(exists.getProperties().get("width")));
        JTextField Height = new JTextField();
        Height.setText(String.valueOf(exists.getProperties().get("height")));
        final Color[] Out = {exists.getColor()};
        final Color[] In = {exists.getFillColor()};
        JButton Color=new JButton("Color");
        JButton FillColor=new JButton("Fill Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                FillColor,
                new JLabel("Width"),
                Width,
                new JLabel(("Height")),
                Height
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        FillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                In[0] =JColorChooser.showDialog(panel1,"Select a color", In[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Ellipse(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);New.setFillColor(In[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("width",Double.parseDouble(Width.getText()));
        Prop.put("height",Double.parseDouble(Height.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
    public void LineProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField X2 = new JTextField();
        X2.setText(String.valueOf(exists.getProperties().get("x2")));
        JTextField Y2 = new JTextField();
        Y2.setText(String.valueOf(exists.getProperties().get("y2")));
        final Color[] Out = {exists.getColor()};
        JButton Color=new JButton("Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                new JLabel("X2"),
                X2,
                new JLabel(("Y2")),
                Y2
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Line(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("x2",Double.parseDouble(X2.getText()));
        Prop.put("y2",Double.parseDouble(Y2.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
    public void SquareProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField Side = new JTextField();
        Side.setText(String.valueOf(exists.getProperties().get("side")));
        final Color[] Out = {exists.getColor()};
        final Color[] In = {exists.getFillColor()};
        JButton Color=new JButton("Color");
        JButton FillColor=new JButton("Fill Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                FillColor,
                new JLabel("Side length"),
                Side,
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        FillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                In[0] =JColorChooser.showDialog(panel1,"Select a color", In[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Square(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);New.setFillColor(In[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("side",Double.parseDouble(Side.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
    public void RectangleProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField Width = new JTextField();
        Width.setText(String.valueOf(exists.getProperties().get("width")));
        JTextField Height = new JTextField();
        Height.setText(String.valueOf(exists.getProperties().get("height")));
        final Color[] Out = {exists.getColor()};
        final Color[] In = {exists.getFillColor()};
        JButton Color=new JButton("Color");
        JButton FillColor=new JButton("Fill Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                FillColor,
                new JLabel("Width"),
                Width,
                new JLabel(("Height")),
                Height
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        FillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                In[0] =JColorChooser.showDialog(panel1,"Select a color", In[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Rectangle(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);New.setFillColor(In[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("width",Double.parseDouble(Width.getText()));
        Prop.put("height",Double.parseDouble(Height.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
    public void TriangleProp(Shape exists, JPanel panel1,DrawingEngine engine)
    {
        JTextField X = new JTextField();
        X.setText(String.valueOf(exists.getPosition().x));
        JTextField Y = new JTextField();
        Y.setText(String.valueOf(exists.getPosition().y));
        JTextField X2 = new JTextField();
        X2.setText(String.valueOf(exists.getProperties().get("x2")));
        JTextField Y2 = new JTextField();
        Y2.setText(String.valueOf(exists.getProperties().get("y2")));
        JTextField X3 = new JTextField();
        X3.setText(String.valueOf(exists.getProperties().get("x3")));
        JTextField Y3 = new JTextField();
        Y3.setText(String.valueOf(exists.getProperties().get("y3")));
        final Color[] Out = {exists.getColor()};
        final Color[] In = {exists.getFillColor()};
        JButton Color=new JButton("Color");
        JButton FillColor=new JButton("Fill Color");
        Object [] options={"Change","Delete","Clone","Cancel"};
        final JComponent[] inputs = new JComponent[] {
                new JLabel("x"),
                X,
                new JLabel("y"),
                Y,
                Color,
                FillColor,
                new JLabel("X2"),
                X2,
                new JLabel(("Y2")),
                Y2,
                new JLabel("X3"),
                X3,
                new JLabel(("Y3")),
                Y3
        };
        Color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Out[0] =JColorChooser.showDialog(panel1,"Select a color", Out[0]);

            }
        });
        FillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                In[0] =JColorChooser.showDialog(panel1,"Select a color", In[0]);

            }
        });
        int result = JOptionPane.showOptionDialog(null, inputs, "Edit", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
        Shape New=new Triangle(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText()));
        New.setColor(Out[0]);New.setFillColor(In[0]);
        Map<String,Double>Prop=new HashMap<>();
        Prop.put("x2",Double.parseDouble(X2.getText()));
        Prop.put("y2",Double.parseDouble(Y2.getText()));
        Prop.put("x3",Double.parseDouble(X3.getText()));
        Prop.put("y3",Double.parseDouble(Y3.getText()));
        New.setProperties(Prop);
        if(result==0)
            engine.updateShape(exists,New);
        else if (result==1)
            engine.removeShape(exists);
        else if(result==2)
        {
            try{
                engine.addShape((Shape) exists.clone());
            }catch (Exception E)
            {
                E.printStackTrace();
            }
        }
    }
}