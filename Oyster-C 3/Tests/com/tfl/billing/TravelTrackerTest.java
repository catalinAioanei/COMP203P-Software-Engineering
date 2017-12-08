package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.external.Customer;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;


import static org.junit.Assert.*;

public class TravelTrackerTest {

    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();
    CardReadersManager cardReadersManager = new CardReadersManager();
    UUID originID = OysterReaderLocator.atStation(Station.PADDINGTON).id();
    UUID destID = OysterReaderLocator.atStation(Station.ALDGATE).id();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private OysterCard refreshDatabaseAndAddCustomCustomer(){
        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.addCustomerToDatabase(new Customer("Catalin", oysterCard));
        return oysterCard;
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void getsCorrectPriceOf380ForOneLongPeakTravel() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";

        OysterCard oysterCard = refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
        cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("3.80"));
    }

    @Test
    public void getsCorrectPriceOf270ForOneLongOffPeakTravel() {
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:30:00";

        OysterCard oysterCard = refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
        cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("2.70"));
    }

    @Test
    public void getsCorrectPriceOf290ForOneShortPeakTravel() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:24:00";

        OysterCard oysterCard =refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
        cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("2.90"));
    }

    @Test
    public void getsCorrectPriceOf160ForOneShortOffPeakTravel() {
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:24:00";

        OysterCard oysterCard =refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
        cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("1.60"));
    }

    @Test
    public void getCorrectCapPriceForPeakTravel(){
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";

        OysterCard oysterCard =refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        for(int i=0;i<=3;i++){
            cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
            cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);
        } // it does the same trip, at the exact moment, 4 times, which couldn't be possible, but just for testing purposes;

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("9.00"));
    }

    @Test
    public void getCorrectCapPriceForOffPeakTravel(){
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:30:00";

        OysterCard oysterCard =refreshDatabaseAndAddCustomCustomer();

        TravelTracker travelTracker = new TravelTracker(cardReadersManager);
        for(int i=0;i<=3;i++){
            cardReadersManager.cardScanned(oysterCard.id(), originID, startTime);
            cardReadersManager.cardScanned(oysterCard.id(), destID, endTime);
        }

        travelTracker.chargeAccounts();
        assertTrue(outContent.toString().contains("7.00"));
    }

}
