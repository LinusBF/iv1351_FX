package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

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
                if(newValue != null) {
                    selectNewQualification(newValue);
                }
            }
        });

        qualificationList.getSelectionModel().select(selectedQualification);
        qualificationList.getFocusModel().focus(qualifications.indexOf(selectedQualification));
    }

    private void addQualification() {
        /*ArrayList<String> exhibitionNames = new ArrayList<>();
        exhibitionNames.addAll(Exhibition.getAllExhibitions().stream().map(Exhibition::toString).collect(Collectors.toList()));*/
        ArrayList<Exhibition> exhibitions;
        try{
            exhibitions = Exhibition.getAllExhibitions();
        } catch (Exception e){
            System.out.println("Can't reach the database");
            exhibitions = new ArrayList<>();
        }

        if(exhibitions.size() > 0) {
            ChoiceDialog<Exhibition> dialog = new ChoiceDialog<>(exhibitions.get(0), exhibitions);
            dialog.setTitle("Add a qualification to a guide");
            dialog.setContentText("Choose an exhibition:");
            Optional<Exhibition> result = dialog.showAndWait();
            result.ifPresent(exhibition -> Main.singleton.addQualificationToGuide(exhibition));
        }
    }

    private void deleteQualification(){
        Main.singleton.removeQualificationFromGuide();
    }

    private void selectNewQualification(Exhibition newQualification){
        selectedQualification = newQualification;
        System.out.println("Selected qualification " + newQualification);
    }

    public Exhibition getSelectedQualification(){
        return selectedQualification;
    }

    public void updateQualifications(ArrayList<Exhibition> qualifications){
        this.qualifications = qualifications;
        qualificationList.getItems().setAll(qualifications);
        selectedQualification = qualifications.get(0);
    }

    public GridPane getLayout() {
        return layout;
    }
}
