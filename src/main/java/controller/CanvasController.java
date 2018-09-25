package controller;

import com.thoughtworks.xstream.XStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import util.GraphType;
import util.PassSaveData;
import util.XMLFile;
import view.CanvasListView;
import view.Main;
import view.SaveView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

public class CanvasController implements Initializable{
    private Main main;
    @FXML private Canvas canvas;
    @FXML private Canvas background;

    @FXML private Button open;
    @FXML private Button save;
    @FXML private Button pencil;
    @FXML private Button recognize;
    @FXML private Button clear;
    @FXML private Button export;

    @FXML private Slider pencilWidth;
    @FXML private ColorPicker pickColor;

    private Coordinate initCoordinate;
    private Coordinate endCoordinate;

    private GraphicsContext graphicsContext;
    private GraphicsContext bgGraphicsContext;

    //用来暂存当前画板上的笔画与图形
    private ArrayList<OneStroke> strokes;
    private OneStroke tempStroke;
    private ArrayList<Graph> graphs;

    /**
     * 初始化各参数
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
        bgGraphicsContext = background.getGraphicsContext2D();
        pencil.setDisable(false);
        recognize.setDisable(false);
        initCoordinate = new Coordinate(0,0);
        endCoordinate = new Coordinate(0,0);
        strokes = new ArrayList<OneStroke>();
        graphs = new ArrayList<Graph>();
        pickColor.setValue(Color.BLACK);
        pencilWidth.setMin(1);
        pencilWidth.setMax(8);
    }

    /**
     * 两个画板出现鼠标按下的处理事件
     * @param event
     */
    @FXML
    public void canvasMousePressed(MouseEvent event){
        //System.out.println("Pressed");
        if(pencil.isDisabled()){
            //初始化画图形的笔触颜色、粗细与初始化一个笔画
            graphicsContext.setLineWidth(pencilWidth.getValue());
            graphicsContext.setStroke(pickColor.getValue());
            initCoordinate.setCoordinate(event.getX(),event.getY());
            tempStroke = new OneStroke(pickColor.getValue(),pencilWidth.getValue());
            tempStroke.addCoordinate(new Coordinate(event.getX(),event.getY()));
        }
        if(recognize.isDisabled()){
            //设置框选的框的外形与初始坐标
            bgGraphicsContext.setStroke(Color.GREY);
            initCoordinate.setCoordinate(event.getX(),event.getY());
        }
    }

    /**
     * 两个画板出现鼠标拖拽的处理事件
     * @param event
     */
    @FXML
    public void canvasMouseDragged(MouseEvent event){
        //System.out.println("Dragged");
        if(pencil.isDisabled()){
            graphicsContext.setStroke(pickColor.getValue());
            graphicsContext.strokeLine(initCoordinate.getPosX(),initCoordinate.getPosY(),event.getX(),event.getY());
            initCoordinate.setCoordinate(event.getX(),event.getY());
            tempStroke.addCoordinate(new Coordinate(event.getX(),event.getY()));
        }

        if(recognize.isDisabled()){
            bgGraphicsContext.clearRect(0,0,background.getWidth(),background.getHeight());
            endCoordinate.setCoordinate(event.getX(),event.getY());
            bgGraphicsContext.strokeRect(Math.min(initCoordinate.getPosX(),endCoordinate.getPosX()),
                    Math.min(initCoordinate.getPosY(),endCoordinate.getPosY()),
                    Math.abs(initCoordinate.getPosX()-endCoordinate.getPosX()),
                    Math.abs(initCoordinate.getPosY()-endCoordinate.getPosY()));
        }
    }

    /**
     * 两个画板出现鼠标释放的处理事件
     * @param event
     */
    @FXML
    public void canvasMouseReleased(MouseEvent event){
        //System.out.println("Released");
        if(pencil.isDisabled()){
            strokes.add(tempStroke);
            tempStroke = null;
        }
        if(recognize.isDisabled()){
            ArrayList<OneStroke> tempStore = new ArrayList<OneStroke>();
            tempStore.addAll(strokes);

            Graph oneGraph = new Graph();

            //将能构成图形的笔画从笔画集合中删除，加入一个图形中
            for(OneStroke stroke:strokes){
                if(stroke.isIntheFrame(Math.min(initCoordinate.getPosX(),endCoordinate.getPosX()),
                        Math.min(initCoordinate.getPosY(),endCoordinate.getPosY()),
                        Math.max(endCoordinate.getPosX(),initCoordinate.getPosX()),
                        Math.max(endCoordinate.getPosY(),initCoordinate.getPosY()))){
                    oneGraph.addStroke(stroke);
                    tempStore.remove(stroke);
                }
            }
            strokes = new ArrayList<OneStroke>();
            strokes.addAll(tempStore);

            bgGraphicsContext.clearRect(0,0,background.getWidth(),background.getHeight());

            //截取canvas上的所框选部分的图片以供识别
            WritableImage image = canvas.snapshot(new SnapshotParameters(),null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null).getSubimage(
                    (int)Math.min(initCoordinate.getPosX(),endCoordinate.getPosX()),
                    (int)Math.min(initCoordinate.getPosY(),endCoordinate.getPosY()),
                    (int)Math.abs(initCoordinate.getPosX()-endCoordinate.getPosX()),
                    (int)Math.abs(initCoordinate.getPosY()-endCoordinate.getPosY()));
            try {
                ImageIO.write(bufferedImage,"png", new File("src/main/resources/screenshot/shot.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ShapeDetection shapeDetection = new ShapeDetection();
            GraphType type = shapeDetection.shapeDetect();
            oneGraph.setGraphType(type);
            graphFillText(oneGraph);
            graphs.add(oneGraph);

            recognize.setDisable(false);
        }
    }

    /**
     * 对识别出的图形添加标注
     * @param oneGraph
     */
    public void graphFillText(Graph oneGraph){
        switch (oneGraph.getGraphType()){
            case TRIANGLE:
                System.out.println("triangle");
                graphicsContext.fillText("triangle",oneGraph.computeCenter().getPosX()-15,oneGraph.computeCenter().getPosY());
                break;
            case SQUARE:
                System.out.println("square");
                graphicsContext.fillText("square",oneGraph.computeCenter().getPosX()-15,oneGraph.computeCenter().getPosY());
                break;
            case RECTANGLE:
                System.out.println("rectangle");
                graphicsContext.fillText("rectangle",oneGraph.computeCenter().getPosX()-15,oneGraph.computeCenter().getPosY());
                break;
            case CIRCLE:
                System.out.println("circle");
                graphicsContext.fillText("circle",oneGraph.computeCenter().getPosX()-15,oneGraph.computeCenter().getPosY());
                break;
            case NOTYPE:
                System.out.println("noType");
                graphicsContext.fillText("NoType",oneGraph.computeCenter().getPosX()-15,oneGraph.computeCenter().getPosY());
                break;
        }
    }

    /**
     * 保存当前操作的画板
     * @param event
     */
    public void saveCanvas(ActionEvent event) {
        pencil.setDisable(false);
        recognize.setDisable(false);

        SaveView saveView = SaveView.getInstance();
        PassSaveData passSaveData = saveView.passSaveData();
        switch (passSaveData.getSaveState()){
            case NOTSAVE:
                graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                break;
            case CANCEL:
                break;
            case CONFIRM:
                graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                CanvasBoard canvasBoard = new CanvasBoard(strokes,graphs,passSaveData.getPicName());
                XMLFile xmlFile = new XMLFile();
                xmlFile.writeInXML(canvasBoard);
                break;
        }
        strokes = new ArrayList<OneStroke>();
        graphs = new ArrayList<Graph>();
    }

    /**
     * 打开已经保存过的画板
     * @param event
     */
    public void openCanvas(ActionEvent event) {
        pencil.setDisable(false);
        recognize.setDisable(false);

        CanvasListView canvasListView = CanvasListView.getInstance();
        String chosenString = canvasListView.passListData();
        System.out.println(chosenString);
        if(chosenString != null){
            XMLFile xmlFile = new XMLFile();
            CanvasBoard canvasBoard = xmlFile.readOutXML(chosenString);
            repaint(canvasBoard);
        }
    }

    /**
     * 打开保存画板后根据存储的画板信息进行重绘
     * @param canvasBoard
     */
    public void repaint(CanvasBoard canvasBoard){
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        strokes = new ArrayList<OneStroke>();
        strokes.addAll(canvasBoard.getStrokes());
        graphs = new ArrayList<Graph>();
        graphs.addAll(canvasBoard.getGraphs());
        //绘制未识别的笔画
        for(int i=0;i<canvasBoard.getStrokes().size();i++){
            paintOneStroke(canvasBoard.getStrokes().get(i));
        }
        //绘制图形
        for(int i=0;i<canvasBoard.getGraphs().size();i++){
            paintGraph(canvasBoard.getGraphs().get(i));
        }
    }

    /**
     * 重绘一个图形
     * @param graph
     */
    public void paintGraph(Graph graph){
        for(int i=0;i<graph.getSize();i++){
            if(graph.getStrokeI(i) != null){
                paintOneStroke(graph.getStrokeI(i));
            }
        }
        graphFillText(graph);
    }

    /**
     * 重绘一个笔画
     * @param stroke
     */
    public void paintOneStroke(OneStroke stroke){
        graphicsContext.setStroke(stroke.getColor());
        graphicsContext.setLineWidth(stroke.getPencilWidth());
        graphicsContext.beginPath();
        graphicsContext.moveTo(stroke.getFirst().getPosX(),stroke.getFirst().getPosY());
        int i=1;
        while (stroke.getCoordinateI(i)!=null){
            graphicsContext.lineTo(stroke.getCoordinateI(i).getPosX(),stroke.getCoordinateI(i).getPosY());
            i++;
        }
        graphicsContext.stroke();
    }

    public void pencilPressed(ActionEvent event) {
        recognize.setDisable(false);

        if(pencil.isDisabled()){
            pencil.setDisable(false);
        }else {
            pencil.setDisable(true);
        }
    }

    public void recognizePressed(ActionEvent event) {
        pencil.setDisable(false);

        if(recognize.isDisabled()){
            recognize.setDisable(false);
        }else {
            recognize.setDisable(true);
        }
    }

    public void clearPressed(ActionEvent event) {
        pencil.setDisable(false);
        recognize.setDisable(false);

        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        strokes = new ArrayList<OneStroke>();
        graphs = new ArrayList<Graph>();
    }

    public void exportAsPic(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null){
            if (!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            WritableImage image = canvas.snapshot(new SnapshotParameters(),null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null);
            try {
                ImageIO.write(bufferedImage,"png",file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
