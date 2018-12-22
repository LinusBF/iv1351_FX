package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by Linus on 2018-12-21.
 */
public class QualificationGUI {
    private GridPane layout;
    private Exhibition selectedQualification;
    private ListView<Exhibition> qualificationList;
    private ArrayList<Exhibition> qualifications;

    public QualificationGUI(ArrayList<Exhibition> qualifications) throws Exception{
        this.qualifications = qualifications;
        selectedQualification = qualifications.get(0);
        layout = new GridPane();
        qualificationList = new ListView<>();
        qualificationList.getItems().setAll(qualifications);
        initGUI();
        addComponents();
    }

    private void initGUI(){
        layout.setHgap(Utils.WINDOW_X/100);
        layout.setVgap(Utils.WINDOW_Y/100);
    }

    private void addComponents(){
        VBox box = new VBox(10);
        Text title = new Text("Qualifications");
        title.setFont(new Font(24));
        HBox btnBox = new HBox(15);
        Button addLangBtn = Utils.createButton("Add Qualification", event -> addQualification());
        Button deleteLangBtn = Utils.createButton("Remove Qualification", event -> deleteQualification());
        btnBox.getChildren().addAll(addLangBtn, deleteLangBtn);
        box.getChildren().addAll(title, qualificationList, btnBox);
        layout.add(box, 0, 0);

        qualificationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Exhibition>() {
            @Override
            public void changed(ObservableValue<? extends Exhibition> observable, Exhibition oldValue, Exhibition newValue) {
                selectNewQualification(newValue);
            }
        });

        qualificationList.getSelectionModel().select(selectedQualification);
        qualificationList.getFocusModel().focus(qualifications.indexOf(selectedQualification));
    }

    private void addQualification(){

    }

    private void deleteQualification(){

    }

    private void selectNewQualification(Exhibition newQualification){
        selectedQualification = newQualification;
        System.out.println("Selected qualification " + newQualification);
    }

    public void updateQualifications(ArrayList<Exhibition> qualifications){
        this.qualifications = qualifications;
        qualificationList.getItems().setAll(qualifications);
    }

    public GridPane getLayout() {
        return layout;
    }
}
