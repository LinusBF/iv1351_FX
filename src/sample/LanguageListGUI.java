package sample;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

/**
 * Created by Linus on 2018-12-21.
 */
public class LanguageListGUI {

    private GridPane layout;
    private ListView<Guide> guideList;
    private Scene scene;

    public LanguageListGUI(GridPane layout) throws Exception{
        layout = layout;
        guideList = new ListView<Guide>();
        initGUI();
        addComponents();
    }

    private void initGUI(){
        layout.setHgap(Utils.WINDOW_X/100);
        layout.setVgap(Utils.WINDOW_Y/100);
        scene = new Scene(layout, Utils.WINDOW_X, Utils.WINDOW_Y);
    }

    private void addComponents(){

    }

    public Scene getScene() {
        return scene;
    }

}
