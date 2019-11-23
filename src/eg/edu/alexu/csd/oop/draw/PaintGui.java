package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PaintGui extends JPanel{
    private DrawingEngine engine=new Engine();
    private Control check=new Control();
    private JPanel panel1;
    private JButton button1=new JButton();
    private JButton button2=new JButton();
    private JButton button3=new JButton();
    private JButton button4=new JButton();
    private int index=0;
    private JComboBox comboBox1;
    private Shape current=null;
    private Point point2=new Point();
    private int x,y;
    private Map<String,Double> prop;
    private Shape exists;
    public PaintGui() {
        for(Class<? extends Shape> s:engine.getSupportedShapes())
            comboBox1.addItem(s.toString().substring(32));
        comboBox1.addItem("Select shape to edit");
        button1.setIcon(new ImageIcon("Icons/Save.png"));
        panel1.add(button1);
        button2.setIcon(new ImageIcon("Icons/Load.png"));
        panel1.add(button2);
        button4.setIcon(new ImageIcon("Icons/Undo.png"));
        panel1.add(button4);
        button3.setIcon(new ImageIcon("Icons/Redo.png"));
        panel1.add(button3);
        add(panel1);

        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Graphics canvas=panel1.getGraphics();
                x=e.getX();
                y=e.getY();
                exists=check.Contain(new Point(x,y),engine.getShapes());
                if(current==null&&exists!=null&&index==comboBox1.getItemCount()-1)
                {
                    if(exists instanceof Circle)
                        check.CircleProp(exists,panel1,engine);
                    else if (exists instanceof Ellipse)
                        check.EllipseProp(exists,panel1,engine);
                    else if (exists instanceof Line)
                        check.LineProp(exists,panel1,engine);
                    else if (exists instanceof Rectangle)
                        check.RectangleProp(exists,panel1,engine);
                    else if(exists instanceof Square)
                        check.SquareProp(exists,panel1,engine);
                    else if(exists instanceof Triangle)
                        check.TriangleProp(exists,panel1,engine);
                    panel1.update(canvas);
                    engine.refresh(canvas);
                    exists=null;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                x=e.getX();
                y=e.getY();
                if(current==null&&exists==null){
                    if(index==0)
                        current=new Circle(x,y);
                    else if(index==1)
                        current=new Ellipse(x,y);
                    else if(index==2)
                        current=new Line(x,y);
                    else if(index==3)
                        current=new Rectangle(x,y);
                    else if(index==4)
                        current=new Square(x,y);
                    else if(index==5)
                        current=new Triangle(x,y);
                    else if(index==6&&index!=comboBox1.getItemCount()-1)
                    {
                       Class<? extends Shape> W=engine.getSupportedShapes().get(6);
                       try{
                           current=W.newInstance();
                           current.setPosition(new Point(x,y));
                           current.setColor(Color.BLACK);
                           current.setFillColor(Color.WHITE);
                       }catch (Exception E)
                       {
                           E.printStackTrace();
                       }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Graphics canvas=panel1.getGraphics();
                if(current!=null){
                    engine.addShape(current);
                    panel1.update(canvas);
                    engine.refresh(canvas);
                    current=null;
                }
                super.mouseReleased(e);
            }
        });

        panel1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Graphics canvas=panel1.getGraphics();
                point2.x=e.getX();
                point2.y=e.getY();
                if(current!=null)
                {
                    if(index==0){
                        prop=new HashMap<>();
                        prop.put("radius", Point2D.distance(x,y,point2.x,point2.y));
                        current.setProperties(prop);
                    }
                    else if(index==1){
                        prop=new HashMap<>();
                        prop.put("width", (double) Math.abs(x-point2.x));
                        prop.put("height", (double) Math.abs(y-point2.y));
                        current.setProperties(prop);
                    }
                    else if(index==2){
                        prop=new HashMap<>();
                        prop.put("x2",(double)point2.x);
                        prop.put("y2",(double)point2.y);
                        current.setProperties(prop);
                    }
                    else if(index==3){
                        prop=new HashMap<>();
                        prop.put("width",(double) Math.abs(x-point2.x));
                        prop.put("height",(double) Math.abs(y-point2.y));
                        current.setProperties(prop);
                    }
                    else if(index==4){
                        prop=new HashMap<>();
                        if((double) Math.abs(x-point2.x)>(double) Math.abs(y-point2.y)){
                            prop.put("side",(double) Math.abs(x-point2.x));
                        }else if((double) Math.abs(x-point2.x)<=(double) Math.abs(y-point2.y)){
                            prop.put("side",(double) Math.abs(y-point2.y));
                        }
                        current.setProperties(prop);
                    }
                    else if(index==5){
                        prop=new HashMap<>();
                        if(x<point2.x){
                            prop.put("x2",(double)point2.x);
                            prop.put("x3",((double)(x+(point2.x-x)/2)));
                            prop.put("y2",(double)y);
                            prop.put("y3",(double)point2.y);
                        }else if(x>point2.x){
                            prop.put("x2",(double)point2.x);
                            prop.put("x3",((double)(x-(x-point2.x)/2)));
                            prop.put("y2",(double)y);
                            prop.put("y3",(double)point2.y);
                        }
                        current.setProperties(prop);
                    }
                    else if(index==6&&index!=comboBox1.getItemCount()-1)
                    {
                        prop=new HashMap<>();
                        prop.put("Width",(double) Math.abs(x-point2.x));
                        prop.put("Length",(double) Math.abs(y-point2.y));
                        prop.put("ArcWidth", (double)50);
                        prop.put("ArcLength", (double)50);
                        current.setProperties(prop);
                    }
                    panel1.update(canvas);
                    current.draw(canvas);
                    engine.refresh(canvas);
                }

                super.mouseDragged(e);
            }
        });

        button1.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose a directory to save your file");
            jfc.setSelectedFile(new File("My Drawing"));
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.setFileFilter(new FileNameExtensionFilter(".XML","xml"));
            jfc.setFileFilter(new FileNameExtensionFilter(".JSON","JSON"));

            int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                String ext=jfc.getFileFilter().toString();
                if (ext.contains("XML")&&!filename .endsWith(".xml"))
                    filename += ".xml";
                else if (ext.contains("JSON")&&!filename .endsWith(".json"))
                    filename += ".json";
                engine.save(filename);
            }

        });
        button2.addActionListener(e -> {
            Graphics canvas=panel1.getGraphics();
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose the drawing you want to load");
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.setFileFilter(new FileNameExtensionFilter(".XML","xml"));
            jfc.setFileFilter(new FileNameExtensionFilter(".JSON","JSON"));
            jfc.setFileFilter(new FileNameExtensionFilter(".Jar","jar"));

            int returnValue = jfc.showOpenDialog(null);
            boolean Jar=false;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                String ext=jfc.getFileFilter().toString();
                if (ext.contains(".XML")&&!filename.endsWith(".xml"))
                    filename += ".xml";
                else if (ext.contains(".JSON")&&!filename.endsWith(".json"))
                    filename += ".json";
                else if(ext.contains(".Jar"))
                {
                    Jar=true;
                    if(!filename.endsWith(".jar"))
                        filename+=".jar";
                }
                try
                {
                    if(!Jar)
                        engine.load(filename);
                    else
                    {
                        engine.installPluginShape(filename);
                        comboBox1.removeAllItems();
                        for(Class<? extends Shape> s:engine.getSupportedShapes())
                            comboBox1.addItem(s.toString().substring(32));
                        comboBox1.addItem("Select shape to edit");
                    }
                }catch (Exception E)
                {
                    JOptionPane.showMessageDialog(panel1,
                            "No such file exists",
                            "Loading error",
                            JOptionPane.ERROR_MESSAGE);
                }
                panel1.update(canvas);
                engine.refresh(canvas);
            }
        });
        button3.addActionListener(e -> {
            Graphics canvas=panel1.getGraphics();
            try{
                engine.redo();
            }catch (Exception E)
            {
                JOptionPane.showMessageDialog(panel1,
                        "You reached the last step",
                        "Redo error",
                        JOptionPane.ERROR_MESSAGE);
            }
            panel1.update(canvas);
            engine.refresh(canvas);
        });
        button4.addActionListener(e -> {
            Graphics canvas=panel1.getGraphics();
            try{
                engine.undo();
            }catch (Exception E)
            {
                JOptionPane.showMessageDialog(panel1,
                        "No previously saved steps",
                        "Undo error",
                        JOptionPane.ERROR_MESSAGE);
            }
            panel1.update(canvas);
            engine.refresh(canvas);
        });

        comboBox1.addItemListener(arg0 -> index=comboBox1.getSelectedIndex());
    }
    public static void main(String[] args) {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception E)
        {
            E.printStackTrace();
        }
        PaintGui gui=new PaintGui();
        JFrame jf=new JFrame();
        gui.panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(500, 500);
        jf.getContentPane().add(gui.panel1);
        jf.setLocation(450,50);
        jf.setUndecorated(true);
        jf.setVisible(true);
    }
}