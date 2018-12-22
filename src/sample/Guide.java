package sample;

import java.util.ArrayList;

public class Guide {
    private String name;
    private String personNr;
    private ArrayList<Language> langs;
    private ArrayList<Exhibition> exhibitions;
    private Boolean changed;

    private Guide(String name, String personNr) {
        this.name = name;
        this.personNr = personNr;
        this.langs = new ArrayList<>();
        this.exhibitions = new ArrayList<>();
        this.changed = false;
    }

    public Guide(String name, String personNr, ArrayList<Language> langs, ArrayList<Exhibition> exhibitions) {
        this.name = name;
        this.personNr = personNr;
        this.langs = langs;
        this.exhibitions = exhibitions;
        this.changed = false;
    }

    public String getName() {
        return name;
    }

    public String getPersonNr() {
        return personNr;
    }

    public void addLanguage(Language lang){
        this.langs.add(lang);
        this.changed = true;
    }

    public ArrayList<Language> getLangs() {
        return langs;
    }

    public ArrayList<Exhibition> getExhibitions() {
        return exhibitions;
    }

    private static Guide[] getMockGuides(){
        ArrayList<Language> langs = Language.getLangs();

        Guide linus = new Guide("Linus", "9709157416");
        Guide josef = new Guide("Josef", "9701032345");
        Guide bader = new Guide("Bader", "8103871082");

        linus.addLanguage(langs.get(0));
        linus.addLanguage(langs.get(1));
        linus.addLanguage(langs.get(2));

        josef.addLanguage(langs.get(0));
        josef.addLanguage(langs.get(1));
        josef.addLanguage(langs.get(3));

        bader.addLanguage(langs.get(0));
        bader.addLanguage(langs.get(1));
        bader.addLanguage(langs.get(3));
        bader.addLanguage(langs.get(4));

        return new Guide[]{linus, josef, bader};
    }

    public static ArrayList<Guide> getGuides () throws Exception {
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

    public void save(){

    }

    @Override
    public String toString() {
        return name + " (" + personNr + ")";
    }
}
