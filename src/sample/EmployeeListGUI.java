package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by Linus on 2018-12-21.
 */
class EmployeeListGUI {

    private GridPane layout;
    private ListView<Guide> guideList;
    private ArrayList<Guide> guides;
    private Guide selectedGuide;

    EmployeeListGUI(ArrayList<Guide> guides) {
        this.guides = guides;
        selectedGuide = guides.get(0);
        layout = new GridPane();
        guideList = new ListView<>();
        guideList.getItems().setAll(guides);
        initGUI();
        addComponents();
    }

    private void initGUI(){
        layout.setHgap(Utils.WINDOW_X/100);
        layout.setVgap(Utils.WINDOW_Y/100);
    }

    private void addComponents(){
        VBox box = new VBox(10);
        Text title = new Text("Guides");
        title.setFont(new Font(24));
        box.getChildren().addAll(title, guideList);
        layout.add(box, 0, 0);

        guideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                selectNewGuide(newValue);
            }
        });

        guideList.getSelectionModel().select(selectedGuide);
        guideList.getFocusModel().focus(guides.indexOf(selectedGuide));
    }

    void updateGuides(ArrayList<Guide> guides){
        this.guides = guides;
        selectedGuide = (guides.contains(selectedGuide) ? guides.get(guides.indexOf(selectedGuide)) : guides.get(0));
        guideList.getItems().setAll(guides);
        guideList.getSelectionModel().select(selectedGuide);
        guideList.getFocusModel().focus(guides.indexOf(selectedGuide));
    }

    private void selectNewGuide(Guide newGuide){
        if (newGuide.equals(selectedGuide)) return;
        selectedGuide = newGuide;
        System.out.println("Selected guide " + newGuide.getName());
        Main.singleton.changeSelectedGuide(newGuide);
    }

    void showOccupiedGuideWarning(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Could not delete qualification!");
        alert.setHeaderText("This guide is occupied!");
        alert.setContentText("This guide is scheduled to host a guided tour using this qualification.\nTherefore is could not be removed!");
        alert.showAndWait();
    }

    Guide getSelectedGuide(){
        return selectedGuide;
    }

    GridPane getLayout() {
        return layout;
    }
}
