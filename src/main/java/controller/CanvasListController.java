package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import view.CanvasListView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CanvasListController implements Initializable{
    @FXML
    private ListView<String> list;
    private ObservableList<String> nameData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameData = FXCollections.observableArrayList();
        addAllCanvasName();
        list.setItems(nameData);

        list.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable,String oldValue,String newValue)->chooseOpen(newValue)
        );
    }

    public void addAllCanvasName(){
        File file = new File("src/main/resources/data");
        File[] fileList = file.listFiles();
        for(int i=0;i<fileList.length;i++){
            nameData.add(fileList[i].getName().split(".xml")[0]);
        }
    }

    public void chooseOpen(String name){
        CanvasListView canvasListView = CanvasListView.getInstance();
        canvasListView.setChosenString(name);
        canvasListView.hide();
    }
}
