package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.lang.reflect.Method;

/**
 * Created by Linus on 2018-12-21.
 */
public class Utils {
    public static int WINDOW_X = 1280/2;
    public static int WINDOW_Y = 720/2;


    public static Button createButton(String text, EventHandler handler){
        Button theButton = new Button();
        theButton.setText(text);
        theButton.setOnAction(handler);
        return theButton;
    }

    public static Button createButton(String text, Method method) throws Exception {
        Button theButton = new Button();
        theButton.setText(text);

        theButton.setOnAction(e -> {
            try {method.invoke(null, null);}
            catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return theButton;
    }
}
