package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.lang.reflect.Method;

/**
 * Created by Linus on 2018-12-21.
 */
class Utils {
    static int WINDOW_X = 1280/2;
    static int WINDOW_Y = 720/2;


    static Button createButton(String text, EventHandler handler){
        Button theButton = new Button();
        theButton.setText(text);
        theButton.setOnAction(handler);
        return theButton;
    }
}
