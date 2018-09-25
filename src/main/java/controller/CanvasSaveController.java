package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import util.PassSaveData;
import util.SaveState;
import view.SaveView;

import java.net.URL;
import java.util.ResourceBundle;

public class CanvasSaveController implements Initializable{
    @FXML
    private TextField name;
    @FXML
    private Button notSave;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleNotSave(ActionEvent event) {
        SaveView saveView = SaveView.getInstance();
        saveView.setPassSaveData(new PassSaveData("",SaveState.NOTSAVE));
        saveView.hide();
    }

    public void handleCancel(ActionEvent event) {
        SaveView saveView = SaveView.getInstance();
        saveView.setPassSaveData(new PassSaveData("",SaveState.CANCEL));
        saveView.hide();
    }

    public void handleConfirm(ActionEvent event) {
        SaveView saveView = SaveView.getInstance();
        saveView.setPassSaveData(new PassSaveData(name.getText(),SaveState.CONFIRM));
        saveView.hide();
    }
}
