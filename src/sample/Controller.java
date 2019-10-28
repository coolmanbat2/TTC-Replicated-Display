package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/***
 * Second Class used for Thread that contains the loop needed
 * to cycle through TIME, and OP ID#.
 */
class Cycling extends Thread {
    private int currStop;
    private Text stops;
    private static boolean isAppAlive = true;
    private LinkedList<String> ttcStops;

    /**
     * Using this constructor to access information from the original controller
     * This is only used because I cannot access FXML information via this class.
     * @param stops fx:id FXML Text Function
     * @param ttcStops List of all stops in a Route.
     * @param currStop Provides the index of the 'ttcStops' list.
     */
    Cycling(Text stops, LinkedList<String> ttcStops, int currStop) {
        this.stops = stops;
        this.ttcStops = ttcStops;
        this.currStop = currStop;
    }

    /**
     * Provides a 24hr real-time clock.
     * @author Thanusun
     * @date October 23rd, 2019
     */
    private void clock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            String minute = Integer.toString(currentTime.getMinute());
            // the special case where its 12 AM and 'houConv' does not convert that time; this function will.
            String hour = currentTime.getHour() == 0 ? "12" : Integer.toString(currentTime.getHour());
            String AMPM = currentTime.getHour() > 12 ? "PM" : "AM";
            // changes the minute display time to add a zero only when the clock doesn't reach 10.
            String minConv = currentTime.getMinute() < 10 ?  "0" + minute : minute;
            // changes hour time to 12 hour format.
            String houConv = currentTime.getHour() > 12 ? Integer.toString(currentTime.getHour()-12) : hour;
            stops.setText(houConv + ":" + minConv + " " + AMPM);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(1);
        clock.play();
    }

    /**
     * Inverts the boolean flag isAppAlive, when user closes the program.
     * This measure is created so that the Thread can safely finish itself then close the program.
     * Note: This program is actually used in Main and not used in this class.
     */
    static void invertIsAppAlive() {
        isAppAlive = !isAppAlive;
    }

    /***
     * Changes Text with current Time along with some Random ID.
     * @author Thanusun
     * @date October 21st, 2019
     */
    @FXML
    public void run() {
        try {
            while (isAppAlive) {
                /*
                Does a check to see if the user clicked their primary mouse button,
                if so then move into the next stop and
                 */
                TimeUnit.SECONDS.sleep(2);
                clock();
                TimeUnit.SECONDS.sleep(2);
                stops.setText("OP ID# 4325324");
                TimeUnit.SECONDS.sleep(2);
                stops.setText(ttcStops.get(currStop));
                stops.setTextAlignment(TextAlignment.CENTER);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


public class Controller {
    @FXML
    private Text stops;
    private int currStop = 0;
    private LinkedList<String> ttcStops = new LinkedList<>();
    private static int lastElement;
    private Cycling cycle;

    /***
     * adds the elements into the list & initializes data that is not assigned.
     * @author Thanusun
     * @date October 18th, 2019
     */
    public Controller() {
        ttcStops.add("Yonge Street");
        ttcStops.add("Bay street");
        ttcStops.add("Elizabeth Street");
        ttcStops.add("University Avenue");
        ttcStops.add("McCaul Street");
        ttcStops.add("St. George Street");

        lastElement = ttcStops.size() - 1;
    }

    /**
     * Initailizer for second Thread, with Time and OP ID display.
     * @author Thanusun
     * @date October 23rd, 2019
     */
    @FXML
    private void initialize() {
        cycle = new Cycling(stops, ttcStops, currStop);
        cycle.start();
    }

    /***
     * Here function will change each stop & announce itself with every click.
     * @author Thanusun
     * @param mouseEvent takes in clicks initiated by the user.
     * @date October 14th, 2019
     */
    @FXML
    public void changeStop(MouseEvent mouseEvent) {
        if (cycle.isAlive()) {
            cycle.interrupt();
        }
        // stops the previous cycle that is currently running.
        // Go back to first element after reaching the last element.
        if (currStop == lastElement) {
            currStop = 0;
        } else {
            currStop++;
        }

        initialize();
        stops.setText(ttcStops.get(currStop));
        stops.setTextAlignment(TextAlignment.CENTER);
    }
}
