package model;

public class Coordinate {
    private double posX;
    private double posY;

    public Coordinate(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }

    public double getPosX(){
        return posX;
    }

    public double getPosY(){
        return posY;
    }

    public void setPosX(double posX){
        this.posX = posX;
    }

    public void setPosY(double posY){
        this.posY = posY;
    }

    public void setCoordinate(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }
}
