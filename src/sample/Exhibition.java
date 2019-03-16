package sample;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Linus on 2018-12-21.
 */
public class Exhibition {
    private Integer ID;
    private String title;
    private Date start;
    private Date end;
    private Integer space;
    private Double cost;

    Exhibition(Integer ID, String title, Date start, Date end, Integer space, Double cost) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.end = end;
        this.space = space;
        this.cost = cost;
    }

    static ArrayList<Exhibition> getAllExhibitions() throws Exception {
        ArrayList<Exhibition> exhibitions;
        DbWrapper db = new DbWrapper();
        db.connect();

        exhibitions = db.getAllExhibitions();

        return exhibitions;
    }

    Integer getID(){
        return ID;
    }

    @Override
    public String toString() {
        return title + " (" + start.toString() + " : " + end.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Exhibition && this.getID().equals(((Exhibition) obj).getID());
    }
}
