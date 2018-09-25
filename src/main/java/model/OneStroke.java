package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class OneStroke {
    private ArrayList<Coordinate> coordinates;
    private Color color;
    private double pencilWidth;

    public OneStroke(Color color,double pencilWidth){
        this.color = color;
        this.pencilWidth = pencilWidth;
        coordinates = new ArrayList<Coordinate>();
    }

    public OneStroke(ArrayList<Coordinate> coordinates){
        this.coordinates = coordinates;
    }

    public void addCoordinate(Coordinate coordinate){
        this.coordinates.add(coordinate);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getPencilWidth(){
        return pencilWidth;
    }

    public Coordinate getFirst(){
        if (coordinates!=null && coordinates.size()!=0){
            return coordinates.get(0);
        }else {
            return null;
        }
    }

    public Coordinate getLast(){
        if(coordinates!=null && coordinates.size()!=0){
            return coordinates.get(coordinates.size()-1);
        }else {
            return null;
        }
    }

    public Coordinate getCoordinateI(int i){
        if(i>=0&&i<coordinates.size()){
            return coordinates.get(i);
        }
        return null;
    }

    public int getSize(){
        return coordinates.size();
    }

    public boolean isIntheFrame(double leftTopX,double leftTopY,double rightBottomX,double rightBottomY){
        Coordinate temp;
        for(int i=0;i<coordinates.size();i++){
            temp = coordinates.get(i);
            if(!((temp.getPosX()>=leftTopX&&temp.getPosX()<=rightBottomX)
                    &&(temp.getPosY()>=leftTopY&&temp.getPosY()<=rightBottomY))){
                return false;
            }
        }
        if(coordinates.size() <= 0){
            return false;
        }
        return true;
    }
}
