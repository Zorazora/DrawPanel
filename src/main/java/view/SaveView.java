package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.PassSaveData;
import util.SaveState;

import java.io.IOException;

public class SaveView {
    private static SaveView saveView;

    private PassSaveData saveData;
    private Stage stage;

    private SaveView(){
        try {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../SaveView.fxml"));
            AnchorPane saveFrame = (AnchorPane) loader.load();
            Scene scene = new Scene(saveFrame);
            stage.setTitle("");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SaveView getInstance(){
        if(saveView!=null){
            return saveView;
        }else {
            saveView = new SaveView();
            return saveView;
        }
    }

    public PassSaveData passSaveData(){
        if(stage != null){
            stage.showAndWait();
        }
        return saveData;
    }

    public void setPassSaveData(PassSaveData saveData){
        this.saveData = saveData;
    }

    public void hide(){
        if(stage != null){
            stage.hide();
        }
    }
}
