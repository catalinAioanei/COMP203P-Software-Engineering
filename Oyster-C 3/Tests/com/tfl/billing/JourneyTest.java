package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.Station;
import org.junit.Assert;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JourneyTest {
    private UUID cardExampleID = UUID.randomUUID();
    private UUID readerOriginID = UUID.randomUUID();
    private UUID readerDestinationID = UUID.randomUUID();
    ExternalJar externalJarAdapter = new ExternalJarAdapter();

    OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

    JourneyStart journeyStart = new JourneyStart(myCard.id(),paddingtonReader.id());
    JourneyEnd journeyEnd = new JourneyEnd(myCard.id(),bakerStreetReader.id());
    Journey journey = new Journey(journeyStart,journeyEnd);

    private void createTestJourney(int secondsToSleep) throws InterruptedException
    {
        journeyStart = new JourneyStart(cardExampleID, readerOriginID);
        Thread.sleep(secondsToSleep * 1000);
        journeyEnd = new JourneyEnd(cardExampleID, readerDestinationID);
        journey = new Journey(journeyStart, journeyEnd);
    }

    @Test
    public void journeyStartStationIdEqualsOriginId() throws InterruptedException {
        createTestJourney(1);
        Assert.assertEquals(journey.originId(), journeyStart.readerId());
    }

    @Test
    public void journeyEndStationIdEqualsDestinationId() throws InterruptedException {
        createTestJourney(2);
        Assert.assertEquals(journey.destinationId(),journeyEnd.readerId());
    }

    @Test
    public void journeyStartTimeIsBeforeJourneyEndTime() throws InterruptedException {
        createTestJourney(1);
        assertTrue(journey.startTime().before(journey.endTime()));
    }


    @Test
    public void assertReturnOfCardID() throws InterruptedException {
        assertThat(journeyStart.cardId(), is(myCard.id()));
        assertThat(journeyEnd.cardId(), is(myCard.id()));
    }

    @Test
    public void assertReturnOfReaderID() throws InterruptedException {
        assertThat(journeyStart.readerId(), is(paddingtonReader.id()));
        assertThat(journeyEnd.readerId(), is(bakerStreetReader.id()));
    }

    @Test
    public void checkFormattedStartTimeTest() throws InterruptedException {
        assertThat(journey.formattedStartTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyStart.time()))));
    }

    @Test
    public void checkFormattedEndTimeTest() throws InterruptedException {
        assertThat(journey.formattedEndTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyEnd.time()))));
    }

    @Test
    public void assertStartTimeTest() throws InterruptedException {
        assertThat(journey.startTime(), is(new Date(journeyStart.time())));
    }

    @Test
    public void assertEndTimeTest() throws InterruptedException {
        assertThat(journey.endTime(), is(new Date(journeyEnd.time())));
    }

    @Test
    public void durationSeconds() throws InterruptedException {
        assertEquals(journey.durationSeconds(), (int) ((journeyEnd.time() - journeyStart.time()) / 1000));
    }

}