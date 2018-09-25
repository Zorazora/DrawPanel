package util;

public class PassSaveData {
    private String picName;
    private SaveState saveState;

    public PassSaveData(String picName,SaveState saveState){
        this.picName = picName;
        this.saveState = saveState;
    }

    public String getPicName() {
        return picName;
    }

    public SaveState getSaveState() {
        return saveState;
    }
}
