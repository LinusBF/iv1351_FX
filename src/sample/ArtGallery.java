package sample;

import java.util.ArrayList;
import java.util.Scanner;

public class ArtGallery {
    private ArrayList<Guide> guides;

    private ArtGallery(){
        super();
        try{
            guides = Guide.getGuides();
        } catch (Exception e){
            guides = new ArrayList<>();
        }
    }

    void printMenu(){
        System.out.println("\nChoose an operation:");
        System.out.println("1. Show all guides");
        System.out.println("2. Show languages a guide speaks");
        System.out.println("3. Add a language to a guide's knowledge");
        System.out.println("(0 to exit)");
    }

    private Guide getGuideByName(String name){
        Guide chosenGuide = null;
        for(Guide g : guides){
            if(g.getName().toLowerCase().equals(name.toLowerCase())){
                chosenGuide = g;
            }
        }
        return chosenGuide;
    }

    private Language getLangByName(String name) throws Exception {
        Language chosenLang = null;
        for(Language l : Language.getLangs()){
            if(l.name.toLowerCase().equals(name.toLowerCase())){
                chosenLang = l;
            }
        }
        return chosenLang;
    }

    private void printGuideLang(String name){
        Guide chosenGuide = this.getGuideByName(name);
        if(chosenGuide == null){
            System.out.println("\n\nCould not find a guide with that name!");
        } else{
            System.out.println(chosenGuide);
            System.out.println("Languages:");
            for (Language lang : chosenGuide.getLangs()){
                System.out.println(lang);
            }
        }
    }

    private void addLangToGuide(String name, String lang) throws Exception {
        Guide chosenGuide = this.getGuideByName(name);
        Language chosenLang = this.getLangByName(lang);
        if(chosenGuide == null){
            System.out.println("\n\nCould not find a guide with that name!");
        } else{
            if(chosenLang == null){
                chosenLang = new Language(lang);
            }

            chosenGuide.addLanguage(chosenLang);
            System.out.println(chosenGuide.getName() + " can now speak " + chosenLang.name);
        }
    }

    public static void main(String[] args) {
        ArtGallery gallery = new ArtGallery();

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the Art Museum!");
        gallery.printMenu();
        int choice = scan.nextInt();
        while(choice != 0){
            switch (choice){
                case 1:
                    System.out.println("\n\nGuides in our system:");
                    for(Guide g : gallery.guides){
                        System.out.println(g);
                    }
                    break;
                case 2:
                    System.out.println("\n\nEnter the guide's name:");
                    String name = scan.next();
                    gallery.printGuideLang(name);
                    break;
                case 3:
                    System.out.println("Who do you want to add a language to?");
                    String addName = scan.next();
                    System.out.println("What language do you want to add to this guide?");
                    String addLang = scan.next();
                    try {
                        gallery.addLangToGuide(addName, addLang);
                    } catch (Exception e) {
                        System.out.println("Can't access the database!");
                    }
                    break;
            }
            gallery.printMenu();
            choice = scan.nextInt();
        }
    }
}
