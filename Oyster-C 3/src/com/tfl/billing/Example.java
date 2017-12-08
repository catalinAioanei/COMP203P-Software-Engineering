package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.Station;

public class Example {
    public static void main(String[] args) throws Exception {

        ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();
        CardReadersManager cardReadersManager = new CardReadersManager();
        OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

        cardReadersManager.connect(paddingtonReader,bakerStreetReader);
        TravelTracker travelTracker = new TravelTracker(cardReadersManager);

        paddingtonReader.touch(myCard);
        //minutesPass(1);
        bakerStreetReader.touch(myCard);

        travelTracker.chargeAccounts();
    }

    private static void minutesPass(int n) throws InterruptedException {
        Thread.sleep(n * 60 * 1000);
    }
}

