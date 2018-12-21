package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        ArrayList<Guide> guides = Guide.getGuides();
        ArrayList<Exhibition> exhibitions = Exhibition.getAllExhibitions();
        QualificationGUI qualifications = new QualificationGUI(exhibitions);
        EmployeeListGUI employees = new EmployeeListGUI(guides);
        LanguageListGUI languages = new LanguageListGUI(employees.getSelectedGuide().getLangs());
        primaryStage.setTitle("Konstmuseum");
        root.setLeft(qualifications.getLayout());
        root.setCenter(employees.getLayout());
        root.setRight(languages.getLayout());
        BorderPane.setAlignment(employees.getLayout(), Pos.CENTER_RIGHT);
        primaryStage.setScene(new Scene(root, 1024, 512));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
