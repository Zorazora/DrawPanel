package util;

import com.thoughtworks.xstream.XStream;
import model.CanvasBoard;
import model.Coordinate;
import model.Graph;
import model.OneStroke;

import java.io.*;

public class XMLFile {
    public boolean writeInXML(CanvasBoard canvasBoard){
        XStream xStream = new XStream();
        xStream.alias("CanvasBoard",CanvasBoard.class);
        xStream.alias("Graph", Graph.class);
        xStream.alias("OneStroke", OneStroke.class);
        xStream.alias("Coordinate", Coordinate.class);
        String str = xStream.toXML(canvasBoard);

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/data/"+canvasBoard.getCanvasName()+".xml");
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CanvasBoard readOutXML(String canvasName){
        File file = new File("src/main/resources/data/"+canvasName+".xml");
        BufferedReader reader = null;
        CanvasBoard canvasBoard = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String xmlString = "";
            String tempString = null;
            while ((tempString = reader.readLine())!=null){
                xmlString = xmlString+tempString+"\n";
            }
            reader.close();
            XStream xStream = new XStream();
            xStream.alias("CanvasBoard",CanvasBoard.class);
            xStream.alias("Graph",Graph.class);
            xStream.alias("OneStroke",OneStroke.class);
            xStream.alias("Coordinate",Coordinate.class);
            canvasBoard = (CanvasBoard) xStream.fromXML(xmlString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return canvasBoard;
    }
}
