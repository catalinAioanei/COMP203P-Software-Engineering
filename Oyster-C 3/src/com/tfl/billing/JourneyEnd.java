package com.tfl.billing;

import java.util.UUID;

public class JourneyEnd extends JourneyEvent {

    public JourneyEnd(UUID cardId, UUID readerId) {

        super(cardId, readerId);
    }
    public JourneyEnd(UUID cardId, UUID readerId, String time) {
        super(cardId, readerId, time);
    }
}
