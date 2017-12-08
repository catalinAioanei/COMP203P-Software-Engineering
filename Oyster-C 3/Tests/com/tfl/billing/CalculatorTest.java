package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CalculatorTest {

    UUID originID = OysterReaderLocator.atStation(Station.PADDINGTON).id();
    UUID destID = OysterReaderLocator.atStation(Station.ALDGATE).id();
    OysterCard oysterCard = new OysterCard();

    private Journey createJourneyAtSpecifiedTime(String startTime, String endTime){
        JourneyStart journeyStart = new JourneyStart(oysterCard.id(), originID, startTime);
        JourneyEnd journeyEnd = new JourneyEnd(oysterCard.id(), destID, endTime);
        Journey myJourney = new Journey(journeyStart, journeyEnd);

        return myJourney;
    }

    @Test
    public void costOfShortOffPeakJourneyIsCorrect() {
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:20:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertThat(Calculator.calculatePriceOfJourney( myJourney).doubleValue(), is(1.60));

    }


    @Test
    public void costOfLongOffPeakJourneyIsCorrect() {
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:30:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertThat(Calculator.calculatePriceOfJourney( myJourney).doubleValue(), is(2.70));

    }

    @Test
    public void costOfShortPeakJourneyIsCorrect() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:20:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertThat(Calculator.calculatePriceOfJourney(myJourney).doubleValue(), is(2.90));

    }

    @Test
    public void costOfLongPeakJourneyIsCorrect() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertThat(Calculator.calculatePriceOfJourney(myJourney).doubleValue(), is(3.80));

    }

    @Test
    public void peakJourneyIsPeak() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertTrue(Calculator.isPeak(myJourney));

    }

    @Test
    public void offPeakJourneyIsOffPeak() {
        String startTime = "2017/01/01 12:00:00";
        String endTime = "2017/01/01 12:30:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertFalse(Calculator.isPeak(myJourney));

    }

    @Test
    public void longJourneyIsLong() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:30:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertTrue(Calculator.isLong(myJourney));

    }

    @Test
    public void shortJourneyIsShort() {
        String startTime = "2017/01/01 07:00:00";
        String endTime = "2017/01/01 07:20:00";
        Journey myJourney = createJourneyAtSpecifiedTime(startTime,endTime);

        assertFalse(Calculator.isLong(myJourney));

    }

}