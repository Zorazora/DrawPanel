package model;

import util.GraphType;

import java.util.ArrayList;

public class Graph {
    private ArrayList<OneStroke> strokes;
    private GraphType graphType;

    public Graph(){
        strokes = new ArrayList<OneStroke>();
        graphType = null;
    }

    public Graph(ArrayList<OneStroke> strokes){
        this.strokes = strokes;
        graphType = null;
    }

    public void setGraphType(GraphType graphType){
        this.graphType = graphType;
    }

    public int getSize(){
        return strokes.size();
    }

    public OneStroke getStrokeI(int i){
        if(i>=0&&i<strokes.size()){
            return strokes.get(i);
        }
        return null;
    }

    public void addStroke(OneStroke stroke){
        strokes.add(stroke);
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public Coordinate computeCenter(){
        Coordinate center = new Coordinate(0,0);
        double totalX = 0;
        double totalY = 0;
        int total = 0;
        for(OneStroke stroke:strokes){
            for(int i=0;i<stroke.getSize();i++){
                total++;
                totalX = totalX+stroke.getCoordinateI(i).getPosX();
                totalY = totalY+stroke.getCoordinateI(i).getPosY();
            }
        }
        center.setPosX(totalX/total);
        center.setPosY(totalY/total);
        return center;
    }
}
