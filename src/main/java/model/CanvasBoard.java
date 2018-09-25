package model;

import java.util.ArrayList;

public class CanvasBoard {
    private ArrayList<OneStroke> strokes;
    private ArrayList<Graph> graphs;
    private String canvasName;

    public CanvasBoard(ArrayList<OneStroke> strokes,ArrayList<Graph> graphs,String canvasName){
        this.strokes = strokes;
        this.graphs = graphs;
        this.canvasName = canvasName;
    }

    public ArrayList<OneStroke> getStrokes() {
        return strokes;
    }

    public ArrayList<Graph> getGraphs() {
        return graphs;
    }

    public String getCanvasName() {
        return canvasName;
    }

    public void setCanvasName(String canvasName) {
        this.canvasName = canvasName;
    }

    public void setStrokes(ArrayList<OneStroke> strokes) {
        this.strokes = strokes;
    }

    public void setGraphs(ArrayList<Graph> graphs) {
        this.graphs = graphs;
    }
}
