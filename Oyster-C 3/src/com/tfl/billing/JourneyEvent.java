package com.tfl.billing;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class JourneyEvent {

    private final UUID cardId;
    private final UUID readerId;
    private final long time;

    public JourneyEvent(UUID cardId, UUID readerId) {
        this.cardId = cardId;
        this.readerId = readerId;
        this.time = System.currentTimeMillis();
    }

    public JourneyEvent(UUID cardId, UUID readerId, String time) { // used in the overloaded() cardScanned method.
        this.cardId = cardId;
        this.readerId = readerId;
        long timeMs = 0;
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
            timeMs = date.getTime();
        } catch (ParseException e) {
            System.out.println("Unable to convert string to date");
            e.printStackTrace();
        }
        this.time = timeMs;
    }



    public UUID cardId() {
        return cardId;
    }

    public UUID readerId() {
        return readerId;
    }

    public long time() {
        return time;
    }
}
