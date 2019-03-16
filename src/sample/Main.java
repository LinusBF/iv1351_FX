package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    static Main singleton;

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

    void changeSelectedGuide(Guide newGuide){
        qualificationGUI.updateQualifications(newGuide.getExhibitions());
        languageListGUI.updateLanguages(newGuide.getLangs());
    }

    private void refresh(){
        ArrayList<Guide> guides;
        try{
            guides = Guide.getGuides();
        } catch (Exception e){
            System.out.println("Could not reach database");
            guides = new ArrayList<>();
        }

        if(guides.size() > 0){
            employeeListGUI.updateGuides(guides);
            languageListGUI.updateLanguages(employeeListGUI.getSelectedGuide().getLangs());
            qualificationGUI.updateQualifications(employeeListGUI.getSelectedGuide().getExhibitions());
        }
    }

    void addLanguageToGuide(Language lang){
        try{
            if(employeeListGUI.getSelectedGuide().addLangToGuide(lang)){
                refresh();
            }
        } catch (Exception e) {
            System.out.println("Can't talk to the database!\nStacktrace:");
            e.printStackTrace();
        }
    }

    void removeLanguageFromGuide(){
        Language langToRemove = languageListGUI.getSelectedLanguage();
        try{
            if(employeeListGUI.getSelectedGuide().removeLangFromGuide(langToRemove)){
                refresh();
            }
        } catch (Exception e){
            System.out.println("Can't talk to the database!\nStacktrace:");
            e.printStackTrace();
        }
    }

    void addQualificationToGuide(Exhibition exhibition){
        try{
            if(employeeListGUI.getSelectedGuide().addQualificationToGuide(exhibition)){
                refresh();
            }
        } catch (Exception e){
            System.out.println("Can't talk to the database!\nStacktrace:");
            e.printStackTrace();
        }
    }

    void removeQualificationFromGuide(){
        Exhibition qualificationToRemove = qualificationGUI.getSelectedQualification();
        try{
            if(employeeListGUI.getSelectedGuide().removeQualificationFromGuide(qualificationToRemove)){
                refresh();
            } else {
                employeeListGUI.showOccupiedGuideWarning();
            }
        } catch (Exception e){
            System.out.println("Can't talk to the database!\nStacktrace:");
            e.printStackTrace();
        }
    }

    Guide getSelectedGuide() {
        return employeeListGUI.getSelectedGuide();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
