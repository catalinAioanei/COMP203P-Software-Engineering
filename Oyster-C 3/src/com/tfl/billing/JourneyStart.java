package com.tfl.billing;

import java.util.UUID;

public class JourneyStart extends com.tfl.billing.JourneyEvent {

    public JourneyStart(UUID cardId, UUID readerId) {

        super(cardId, readerId);
    }

    public JourneyStart(UUID cardId, UUID readerId, String time) {
        super(cardId, readerId, time);
    }
}