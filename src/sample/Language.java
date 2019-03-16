package sample;

import java.util.ArrayList;
import java.util.Objects;

public class Language {
    String name;

    Language(String name) {
        this.name = name;
    }

    static ArrayList<Language> getLangs() throws Exception {
        ArrayList<Language> langs = new ArrayList<>();
        DbWrapper db = new DbWrapper();
        db.connect();

        for (String lang : db.getAllLanguages()) {
            langs.add(new Language(lang));
        }

        return langs;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(name, language.name);
    }
}
