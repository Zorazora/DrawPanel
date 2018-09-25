package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CanvasListView {
    private static CanvasListView canvasListView;

    private Stage stage;
    private String chosenString;

    private CanvasListView(){
        try{
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../CanvasListView.fxml"));
            AnchorPane listFrame = (AnchorPane) loader.load();
            Scene scene = new Scene(listFrame);
            stage.setTitle("");
            stage.setScene(scene);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static CanvasListView getInstance(){
        if(canvasListView !=null){
            return canvasListView;
        }else{
            canvasListView = new CanvasListView();
            return canvasListView;
        }
    }

    public void hide(){
        if(stage != null){
            stage.hide();
        }
    }

    public String passListData(){
        if(stage != null){
            stage.showAndWait();
        }
        return chosenString;
    }

    public void setChosenString(String chosenString){
        this.chosenString = chosenString;
    }
}
