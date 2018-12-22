package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public static Main singleton;

    private QualificationGUI qualificationGUI;
    private EmployeeListGUI employeeListGUI;
    private LanguageListGUI languageListGUI;

    @Override
    public void start(Stage primaryStage) throws Exception{
        singleton = this;
        BorderPane root = new BorderPane();
        Insets insets = new Insets(10);
        root.setPadding(insets);
        ArrayList<Guide> guides = Guide.getGuides();
        employeeListGUI = new EmployeeListGUI(guides);
        languageListGUI = new LanguageListGUI(employeeListGUI.getSelectedGuide().getLangs());
        qualificationGUI = new QualificationGUI(employeeListGUI.getSelectedGuide().getExhibitions());
        primaryStage.setTitle("Konstmuseum");
        BorderPane.setAlignment(qualificationGUI.getLayout(), Pos.CENTER);
        root.setLeft(employeeListGUI.getLayout());
        root.setCenter(qualificationGUI.getLayout());
        root.setRight(languageListGUI.getLayout());
        BorderPane.setMargin(qualificationGUI.getLayout(), insets);
        BorderPane.setMargin(employeeListGUI.getLayout(), insets);
        BorderPane.setMargin(languageListGUI.getLayout(), insets);
        primaryStage.setScene(new Scene(root, 800, 512));
        primaryStage.show();
    }

    public void changeSelectedGuide(Guide newGuide){
        qualificationGUI.updateQualifications(newGuide.getExhibitions());
        languageListGUI.updateLanguages(newGuide.getLangs());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
