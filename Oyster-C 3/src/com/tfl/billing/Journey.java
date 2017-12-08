package com.tfl.billing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * This class contains all the neccessary information of one journey. Like
 * origin stations card reader id, destination's card reader id, start time,
 * end time and duration of the journey.
 */
public class Journey {

    /////////////////////////////////////
    // FIELDS
    /////////////////////////////////////

    private final JourneyEvent start;
    private final JourneyEvent end;



    /////////////////////////////////////
    // CONSTRUCTOR
    /////////////////////////////////////

    public Journey(JourneyEvent start, JourneyEvent end) {
        this.start = start;
        this.end = end;
    }



    /////////////////////////////////////
    // METHODS
    /////////////////////////////////////

    public UUID originId() {
        return start.readerId();
    }

    public UUID destinationId() {
        return end.readerId();
    }

    public String formattedStartTime() {
        return format(start.time());
    }

    public String formattedEndTime() {
        return format(end.time());
    }

    public Date startTime() {
        return new Date(start.time());
    }

    public Date endTime() {
        return new Date(end.time());
    }

    public int durationSeconds() {
        return (int) ((end.time() - start.time()) / 1000);
    }

    public String durationMinutes() {
        return "" + durationSeconds() / 60 + ":" + durationSeconds() % 60;
    }

    private String format(long time) {
        return SimpleDateFormat.getInstance().format(new Date(time));
    }
}
