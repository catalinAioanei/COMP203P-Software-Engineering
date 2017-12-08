package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.oyster.ScanListener;

import java.util.*;

/**
 * This class is responsible for registering a reasonable event
 * if a card was scanned on a specific station.
 */
public class CardReadersManager implements ScanListener{

    /////////////////////////////////////
    // FIELDS
    /////////////////////////////////////

    Set<UUID> currentlyTravelling = new HashSet<>();
    List<JourneyEvent> eventLog = new ArrayList<>();



    /////////////////////////////////////
    // METHODS
    /////////////////////////////////////

    /**
     * Connects the stations where the user has scanned him/her oyster card.
     * void
     */
    public void connect(OysterCardReader...oysterCardReaders) {
        for (OysterCardReader cardReader : oysterCardReaders) {
            cardReader.register(this);
        }
    }

    /**
     * Detects whether a customer's journey ended or started by scanning his/her card
     * and registers the event.
     * void
     */
    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (ExternalJar.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    /**
     * This method was created for testing purposes.
     * void
     */
    public void cardScanned(UUID cardId, UUID readerId, String time) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId, time));
            currentlyTravelling.remove(cardId);
        } else {
            if (ExternalJar.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId, time));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    /**
     * This method returns the list of journey events of a particular user.
     * return List<JourneyEvent>
     */
    public List<JourneyEvent> getEventLog() {return eventLog;}

    /**
     * This method returns the card ids of the customers who are currently travelling.
     * return Set<UUID>
     */
    public Set<UUID> getCurrentlyTravelling(){
        return currentlyTravelling;
    }
}
