package sample;

import javafx.scene.Scene;
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
public class LanguageListGUI {

    private GridPane layout;
    private ListView<Language> languageList;
    private ArrayList<Language> langs;

    public LanguageListGUI(ArrayList<Language> langs) throws Exception{
        langs = langs;
        layout = new GridPane();
        languageList = new ListView<>();
        languageList.getItems().setAll(langs);
        initGUI();
        addComponents();
    }

    private void initGUI(){
        layout.setHgap(Utils.WINDOW_X/100);
        layout.setVgap(Utils.WINDOW_Y/100);
    }

    private void addComponents(){
        VBox box = new VBox(10);
        Text title = new Text("Languages");
        title.setFont(new Font(24));
        HBox btnBox = new HBox(15);
        Button addLangBtn = Utils.createButton("Add Language", event -> addLanguage());
        Button deleteLangBtn = Utils.createButton("Remove Language", event -> deleteLanguage());
        btnBox.getChildren().addAll(addLangBtn, deleteLangBtn);
        box.getChildren().addAll(title, languageList, btnBox);
        layout.add(box, 0, 0);
    }

    private void addLanguage(){

    }

    private void deleteLanguage(){

    }

    public GridPane getLayout() {
        return layout;
    }
}
