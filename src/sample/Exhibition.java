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

    public Exhibition(Integer ID, String title, Date start, Date end, Integer space, Double cost) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.end = end;
        this.space = space;
        this.cost = cost;
    }

    private static ArrayList<Exhibition> getMockExhibitions(){
        ArrayList<Exhibition> exhibitions = new ArrayList<>();
        Exhibition ex1 = new Exhibition(1, "Picasso", new Date(2018,12,26), new Date(2018,12,28), 400, 50000.0);
        Exhibition ex2 = new Exhibition(2, "Da Vinci", new Date(2018,12,26), new Date(2018,12,28), 400, 50000.0);
        Exhibition ex3 = new Exhibition(3, "Van Gouh", new Date(2018,12,26), new Date(2018,12,28), 400, 50000.0);
        Exhibition ex4 = new Exhibition(4, "Freak Show", new Date(2018,12,26), new Date(2018,12,28), 400, 50000.0);
        exhibitions.add(ex1);
        exhibitions.add(ex2);
        exhibitions.add(ex3);
        exhibitions.add(ex4);

        return exhibitions;
    }

    public static ArrayList<Exhibition> getAllExhibitions(){
        return getMockExhibitions();
    }

    @Override
    public String toString() {
        return title;
    }
}
