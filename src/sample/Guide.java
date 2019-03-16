package sample;

import java.util.ArrayList;

public class Guide {
    private String name;
    private String personNr;
    private ArrayList<Language> langs;
    private ArrayList<Exhibition> exhibitions;

    private Guide(String name, String personNr, ArrayList<Language> langs, ArrayList<Exhibition> exhibitions) {
        this.name = name;
        this.personNr = personNr;
        this.langs = langs;
        this.exhibitions = exhibitions;
    }

    String getName() {
        return name;
    }

    void addLanguage(Language lang){
        this.langs.add(lang);
    }

    ArrayList<Language> getLangs() {
        return langs;
    }

    ArrayList<Exhibition> getExhibitions() {
        return exhibitions;
    }

    ArrayList<Exhibition> getQualifications() throws Exception {
        DbWrapper db = new DbWrapper();
        db.connect();
        return db.getExhibitionsEmployeeQualifiedFor(name);
    }

    static ArrayList<Guide> getGuides() throws Exception {
        ArrayList<Guide> guides = new ArrayList<>();

        DbWrapper db = new DbWrapper();
        db.connect();
        ArrayList<String[]> guideTuples = db.getAllGuidePersonNrAndName();

        for (String[] tuple : guideTuples){
            ArrayList<Language> guideLanguages = new ArrayList<>();
            for (String lang : db.getAllGuideLanguages(tuple[1])) {
                guideLanguages.add(new Language(lang));
            }
            ArrayList<Exhibition> guideExhibitions = db.getExhibitionsEmployeeQualifiedFor(tuple[1]);
            Guide guide = new Guide(tuple[1], tuple[0], guideLanguages, guideExhibitions);
            guides.add(guide);
        }

        return guides;
    }

    Boolean addLangToGuide(Language lang) throws Exception {
        if(langs.contains(lang)) return false;
        DbWrapper db = new DbWrapper();
        db.connect();
        return db.addEmployeeLanguage(name, lang.name);
    }

    Boolean removeLangFromGuide(Language lang) throws Exception {
        if(!langs.contains(lang)) return false;
        DbWrapper db = new DbWrapper();
        db.connect();
        return db.removeEmployeeLanguage(name, lang.name);
    }

    Boolean addQualificationToGuide(Exhibition qualification) throws Exception {
        if(exhibitions.contains(qualification)) return false;
        DbWrapper db = new DbWrapper();
        db.connect();
        return db.addEmployeeQualification(name, qualification.getID());
    }

    Boolean removeQualificationFromGuide(Exhibition qualification) throws Exception {
        if(!exhibitions.contains(qualification)) return false;
        DbWrapper db = new DbWrapper();
        db.connect();
        return db.removeEmployeeQualification(name, qualification.getID());
    }

    @Override
    public String toString() {
        return personNr + " (" + name + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Guide && ((Guide) obj).personNr.equals(this.personNr));
    }
}
