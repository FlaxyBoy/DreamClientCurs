package ua.dream.chat.window.frames;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ua.dream.chat.utils.async.AsyncUtils;

public class LoadFrame {

    private static BorderPane root;
    private static String reason = "Load";
    private static Text text;
    private static int points;

    public static void setReason(String reason) {
        LoadFrame.reason = reason;
    }

    public static Parent getRoot() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #AD18A9FF");
        root.setCenter(text);
        return root;
    }

    public static void init() {
        System.out.println("Start async thread");
        AsyncUtils.runAsync(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    break;
                }
                update();
            }
        });
        text = new Text();
        text.setText("Load");
        text.setFont(Font.font("System" , 34 ));
        text.setStyle("-fx-background-color: #ffffff");
    }

    private static void update() {
        points++;
        if(points >= 5) {
            points = 0;
        }
        String field = reason;
        for(int i = 1 ; i <= points ; i++) field += ".";
        text.setText(field);
    }
}
