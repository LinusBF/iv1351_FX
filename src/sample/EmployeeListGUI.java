package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by Linus on 2018-12-21.
 */
public class EmployeeListGUI {

    private GridPane layout;
    private ListView<Guide> guideList;
    private ArrayList<Guide> guides;
    private Guide selectedGuide;

    public EmployeeListGUI(ArrayList<Guide> guides) {
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

        guideList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Guide>() {
            @Override
            public void changed(ObservableValue<? extends Guide> observable, Guide oldValue, Guide newValue) {
                selectNewGuide(newValue);
            }
        });

        guideList.getSelectionModel().select(selectedGuide);
        guideList.getFocusModel().focus(guides.indexOf(selectedGuide));
    }

    public void selectNewGuide(Guide newGuide){
        if (newGuide.equals(selectedGuide)) return;
        selectedGuide = newGuide;
        System.out.println("Selected guide " + newGuide.getName());
        Main.singleton.changeSelectedGuide(newGuide);
    }

    public Guide getSelectedGuide(){
        return selectedGuide;
    }

    public GridPane getLayout() {
        return layout;
    }
}
