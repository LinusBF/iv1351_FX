package sample;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by Linus on 2018-12-21.
 */
class QualificationGUI {
    private GridPane layout;
    private Exhibition selectedQualification;
    private ListView<Exhibition> qualificationList;
    private ArrayList<Exhibition> qualifications;
    private Button deleteBtn;

    QualificationGUI(ArrayList<Exhibition> qualifications) throws Exception{
        this.qualifications = qualifications;
        selectedQualification = (qualifications.isEmpty() ? null : qualifications.get(0));
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
        Button addQualBtn = Utils.createButton("Add Qualification", event -> addQualification());
        Button deleteQualBtn = Utils.createButton("Remove Qualification", event -> deleteQualification());
        deleteBtn = deleteQualBtn;
        if(selectedQualification == null) deleteBtn.setDisable(true);
        btnBox.getChildren().addAll(addQualBtn, deleteQualBtn);
        box.getChildren().addAll(title, qualificationList, btnBox);
        layout.add(box, 0, 0);

        qualificationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                selectNewQualification(newValue);
            }
        });

        qualificationList.getSelectionModel().select(selectedQualification);
        qualificationList.getFocusModel().focus(qualifications.indexOf(selectedQualification));
    }

    private void addQualification() {
        ArrayList<Exhibition> exhibitions;
        try{
            exhibitions = Exhibition.getAllExhibitions();
            Collection<Exhibition> currentQualifications = Main.singleton.getSelectedGuide().getQualifications();
            exhibitions.removeAll(currentQualifications);
        } catch (Exception e){
            System.out.println("Can't reach the database");
            exhibitions = new ArrayList<>();
        }

        if(exhibitions.size() > 0) {
            Exhibition firstListElement = exhibitions.get(0);
            ChoiceDialog<Exhibition> dialog = new ChoiceDialog<>(firstListElement, exhibitions);
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
        deleteBtn.setDisable((selectedQualification == null));
        System.out.println("Selected qualification " + newQualification);
    }

    Exhibition getSelectedQualification(){
        return selectedQualification;
    }

    void updateQualifications(ArrayList<Exhibition> qualifications){
        this.qualifications = qualifications;
        qualificationList.getItems().setAll(qualifications);
        if(qualificationList.getItems().isEmpty()){
            selectNewQualification(null);
        } else {
            selectNewQualification(qualifications.get(0));
        }
    }

    GridPane getLayout() {
        return layout;
    }
}
