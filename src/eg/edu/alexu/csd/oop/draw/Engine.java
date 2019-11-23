package eg.edu.alexu.csd.oop.draw;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.*;


public class Engine implements DrawingEngine {
    private  List<Shape> shapes;
    private  List<Shape>[] History;
    private static List<Class<? extends Shape>> SupportedShapes;
    private int start=0,end=0,EndChecker=0;
    private boolean UndoPermission=false,RedoPermission=false,FirstTime=true;
    public Engine()
    {
        SupportedShapes=new ArrayList<>();
        try
        {
            Class DynamicCircle=Class.forName("eg.edu.alexu.csd.oop.draw.Circle");
            if(Shapes.class.isAssignableFrom(DynamicCircle))
                SupportedShapes.add(DynamicCircle);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        SupportedShapes.add(Ellipse.class);
        SupportedShapes.add(Line.class);
        SupportedShapes.add(Rectangle.class);
        SupportedShapes.add(Square.class);
        SupportedShapes.add(Triangle.class);
        /*try
        {
            this.installPluginShape("C:\\Users\\MMMI\\IdeaProjects\\22_66_Paint\\src\\eg\\edu\\alexu\\csd\\oop\\draw\\RoundRectangle.jar");
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
        shapes=new ArrayList<>();
        History=new List[21];
        History[0]=new ArrayList<>();
    }
    @Override
    public void refresh(Graphics canvas) {
        for (Shape s:shapes)
            s.draw(canvas);
    }

    @Override
    public void addShape(Shape shape) {
        UndoPermission=false;
        RedoPermission=false;
        shapes=new ArrayList<>();
        if(!FirstTime)
        {
            if(end==0)
                end=21;
            List <Shape> transition=History[end%21];
            for(Shape s: transition)
               shapes.add(s);
        }
        FirstTime=false;
        shapes.add(shape);
        if(!SupportedShapes.contains(shape.getClass()))
            SupportedShapes.add(shape.getClass());
        History[(end+1)%21]= shapes;
        end=(end+1)%21;
        EndChecker=end;
        if(start==end)
            start++;
    }

    @Override
    public void removeShape(Shape shape) {
        UndoPermission=false;
        RedoPermission=false;
        shapes=new ArrayList<>();
        if(end==0)
            end=21;
        List <Shape> transition=History[end%21];
        for(Shape s: transition)
            shapes.add(s);
        for(Shape s:shapes)
        {
            if(FindShape(shape,s))
            {
                shapes.remove(s);
                History[(end+1)%21]=shapes;
                end=(end+1)%21;
                EndChecker=end;
                return;
            }
        }
    }

    private boolean FindShape(Shape shape, Shape InList)
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
        UndoPermission=false;
        RedoPermission=false;
        if(end==0)
            end=21;
        shapes=new ArrayList<>();
        List <Shape> transition=History[end%21];
        for(Shape s: transition)
            shapes.add(s);
        for(Shape s:shapes)
        {
            if(FindShape(oldShape,s))
            {
                shapes.remove(oldShape);
                shapes.add(newShape);
                History[(end+1)%21]=shapes;
                end=(end+1)%21;
                EndChecker=end;
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
        return SupportedShapes;
    }

    @Override
    public void installPluginShape(String jarPath)
    {
        try
        {
            File jar=new File(jarPath);
            URL fileURL=jar.toURI().toURL();
            String jarURL="jar:"+fileURL+"!/";
            URL[] urls={new URL(jarURL)};
            URLClassLoader ucl=new URLClassLoader(urls);
            String NameOfClass=jarPath.substring(jarPath.lastIndexOf('\\')+1,jarPath.indexOf(".jar"));
            Class<?> c=Class.forName("eg.edu.alexu.csd.oop.draw."+NameOfClass,true,ucl);
            SupportedShapes.add((Class<? extends Shape>) c);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("No file exists");
        }
    }

    @Override
    public void undo() {
        if(start!=end||UndoPermission)
        {
            UndoPermission=false;
            if(end==0)
                end=21;
            shapes=History[(end-1)%21];
            end=(end-1)%21;
            if(end==start)
                RedoPermission=true;
        }
        else
            throw new RuntimeException("No previous undo options");
    }

    @Override
    public void redo() {
        if(end!=EndChecker||RedoPermission)
        {
            RedoPermission=false;
            shapes=History[(end+1)%21];
            end=(end+1)%21;
            if(end==start)
                UndoPermission=true;
        }
        else
            throw new RuntimeException("No previous redo options");
    }

    @Override
    public void save(String path) {
        path=path.toLowerCase();
        if(path.substring(path.length()-3).contentEquals("xml"))
            SaveXml(path);
        else if(path.substring(path.length()-4).contentEquals("json"))
            saveJSON(path);
    }

    private void SaveXml(String path)
    {
        Document dom;
        Element e=null ;


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            dom = db.newDocument();


            Element rootEle = dom.createElement("Shapes");


            for(int i=0;i<shapes.size();i++)
            {
                Shape current=shapes.get(i);
                e = dom.createElement(current.getClass().getName());
                String id=current.getClass().getName();
                id=id.substring(id.indexOf("draw.")+5);
                e.setAttribute("id",id+i);
                if(current.getPosition()!=null)
                {
                    Element point=dom.createElement("Point");
                    Element X=dom.createElement("X");
                    X.appendChild(dom.createTextNode(Integer.toString(current.getPosition().x)));
                    Element Y=dom.createElement("Y");
                    Y.appendChild(dom.createTextNode(Integer.toString(current.getPosition().y)));
                    point.appendChild(X);point.appendChild(Y);
                    e.appendChild(point);
                }
                if(current.getColor()!=null)
                {
                    Element Color=dom.createElement("Color");
                    Color.appendChild(dom.createTextNode(current.getColor().toString()));
                    e.appendChild(Color);
                }
                if(current.getFillColor()!=null)
                {
                    Element FillColor=dom.createElement("FillColor");
                    FillColor.appendChild(dom.createTextNode(current.getFillColor().toString()));
                    e.appendChild(FillColor);
                }
                if(current.getProperties()!=null)
                {
                    Element Properties=dom.createElement("Properties");
                    Map<String,Double> prop=current.getProperties();
                    Iterator propIterator = prop.entrySet().iterator();
                    while (propIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry)propIterator.next();
                        Element Key=dom.createElement(mapElement.getKey().toString());
                        Key.appendChild(dom.createTextNode(mapElement.getValue().toString()));
                        Properties.appendChild(Key);
                    }
                    e.appendChild(Properties);
                }
                rootEle.appendChild(e);
            }
            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");


                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(path)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private void saveJSON(String path)
    {
        try
        {
            PrintWriter pr = new PrintWriter(path);
            pr.println('{');
            boolean First=true;
            for(Shape s:shapes)
            {
                if(!First)
                    pr.println(',');
                First=false;
                pr.println("  \""+s.getClass()+"\" :{");
                if(s.getPosition()!=null)
                {
                    pr.println("      \"point X\":"+s.getPosition().x+",");
                    pr.println("      \"point Y\":"+s.getPosition().y+",");
                }
                if(s.getColor()!=null)
                    pr.println("      \"Color\":\""+s.getColor()+"\",");
                if(s.getFillColor()!=null)
                    pr.println("      \"Fill color\":\":"+s.getFillColor()+"\",");
                Map<String,Double> prop=s.getProperties();
                if(prop!=null)
                {
                    pr.println("        \" Properties\":{");
                    Iterator propIterator = prop.entrySet().iterator();
                    int x=0;
                    while (propIterator.hasNext()) {
                        x++;
                        Map.Entry mapElement = (Map.Entry)propIterator.next();
                        pr.print("          \""+mapElement.getKey()+"\":"+mapElement.getValue());
                        if(x==prop.size())
                            pr.println("}");
                        else
                            pr.println(',');
                    }
                }
                pr.print("    }");
            }
            pr.println("}");
            pr.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("No such file exists.");
        }
    }

    @Override
    public void load(String path) {
        path=path.toLowerCase();
        if(path.substring(path.length()-3).contentEquals("xml"))
            LoadXml(path);
        else if(path.substring(path.length()-4).contentEquals("json"))
            LoadJSON(path);
        else
            throw new RuntimeException("Wrong file format");
    }

    private void LoadXml(String path)
    {

        try {

            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            shapes=new ArrayList<>();
            Element root = doc.getDocumentElement();
            for(Class<? extends Shape> shapeClass:this.getSupportedShapes())
            {
                NodeList nList = doc.getElementsByTagName(shapeClass.getName());
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);
                    Shape current=shapeClass.newInstance();
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;

                        if(eElement.getElementsByTagName("Point").getLength()>0)
                        {
                            String pointX=eElement.getElementsByTagName("X").item(0).getTextContent();
                            String pointY=eElement.getElementsByTagName("Y").item(0).getTextContent();
                            current.setPosition(new Point(Integer.parseInt(pointX),Integer.parseInt(pointY)));
                        }
                        if(eElement.getElementsByTagName("Color").getLength()>0)
                        {
                            String color=eElement.getElementsByTagName("Color").item(0).getTextContent();
                            int red=RedGetter(color);
                            int green=GreenGetter(color);
                            int blue=BlueGetter(color);
                            current.setColor(new Color(red,green,blue));
                        }
                        if(eElement.getElementsByTagName("FillColor").getLength()>0)
                        {
                            String color=eElement.getElementsByTagName("FillColor").item(0).getTextContent();
                            int red=RedGetter(color);
                            int green=GreenGetter(color);
                            int blue=BlueGetter(color);
                            current.setFillColor(new Color(red,green,blue));
                        }
                        if(eElement.getElementsByTagName("Properties").getLength()>0)
                        {
                            Map<String,Double>prop=current.getProperties();
                            Iterator propIterator = prop.entrySet().iterator();
                            while (propIterator.hasNext()) {
                                Map.Entry mapElement = (Map.Entry) propIterator.next();
                                String key= (String) mapElement.getKey();
                                Double value=Double.parseDouble(eElement.getElementsByTagName(key).item(0).getTextContent());
                                prop.put(key,value);
                            }
                            current.setProperties(prop);
                        }
                        shapes.add(current);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        History[0]=shapes;
        start=0;
        end=0;
        EndChecker=0;
        FirstTime=false;
    }

    private void LoadJSON(String path)
    {
        Scanner readCodes;
        shapes=new ArrayList<>();
        try {
            readCodes = new Scanner(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("No previous saved file exists!");
        }
        readCodes.nextLine();
        while(readCodes.hasNextLine())
        {
            String current = readCodes.nextLine();
            Shape CurrentShape= null;
            for(Class<? extends Shape> shapeClass : this.getSupportedShapes())
            {
                if(current.contains(shapeClass.toString()))
                {
                    try
                    {
                        CurrentShape=shapeClass.newInstance();
                        break;
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            current=readCodes.nextLine();
            int x,y;
            if(current.contains("X"))
            {
                x=Integer.parseInt(current.substring(current.indexOf(":")+1,current.length()-1));
                current=readCodes.nextLine();
                y=Integer.parseInt(current.substring(current.indexOf(":")+1,current.length()-1));
                CurrentShape.setPosition(new Point(x,y));
                current=readCodes.nextLine();
            }
            if(current.contains("Color")&&!current.contains("Fill"))
            {
                int red,blue,green;
                red=RedGetter(current);
                green=GreenGetter(current);
                blue=BlueGetter(current);
                CurrentShape.setColor(new Color(red,green,blue));
                current=readCodes.nextLine();
            }
            if(current.contains("Color")&&current.contains("Fill"))
            {
                int red,blue,green;
                red=RedGetter(current);
                green=GreenGetter(current);
                blue=BlueGetter(current);
                CurrentShape.setFillColor(new Color(red,green,blue));
                current=readCodes.nextLine();
            }
            if(current.contains("Properties"))
            {
                Map<String,Double>mp=new HashMap<>();
                while(true)
                {
                    current=readCodes.nextLine();
                    String key=current.substring(current.indexOf("\"")+1,current.indexOf(":")-1);
                    double value=Double.parseDouble(current.substring(current.indexOf(":")+1,current.length()-1));
                    mp.put(key,value);
                    if(current.contains("}"))
                        break;
                }
                CurrentShape.setProperties(mp);
            }
            shapes.add(CurrentShape);
            if(readCodes.hasNextLine())
                readCodes.nextLine();
        }
        History[0]=shapes;
        start=0;
        end=0;
        EndChecker=0;
        FirstTime=false;
        readCodes.close();
    }

    private int RedGetter(String current)
    {
        return Integer.parseInt(current.substring(current.indexOf("r=")+2,current.indexOf("g=")-1));
    }

    private int GreenGetter(String current)
    {
        return Integer.parseInt(current.substring(current.indexOf("g=")+2,current.indexOf("b=")-1));
    }

    private int BlueGetter(String current)
    {
        return Integer.parseInt(current.substring(current.indexOf("b=")+2,current.indexOf("]")));
    }
}
