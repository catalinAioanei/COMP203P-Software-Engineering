package com.tfl.billing;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class is responsible for calculating the proper price of the journeys taken by a customer
 * on the current day. It also holds the neccesary parameters of the calculation rules
 * which were defined by TFL.
 */
public class Calculator {

    /////////////////////////////////////
    // FIELDS
    /////////////////////////////////////

    public static final BigDecimal PENALTY_FARE = new BigDecimal(9);
    private static final BigDecimal PEAK_LONG_JOURNEY_PRICE = new BigDecimal(3.80);
    private static final BigDecimal PEAK_SHORT_JOURNEY_PRICE = new BigDecimal(2.90);
    private static final BigDecimal OFF_PEAK_LONG_JOURNEY_PRICE = new BigDecimal(2.70);
    private static final BigDecimal OFF_PEAK_SHORT_JOURNEY_PRICE = new BigDecimal(1.60);
    private static final BigDecimal PEAK_CAP_PRICE = new BigDecimal(9.00);
    private static final BigDecimal OFF_PEAK_CAP_PRICE = new BigDecimal(7.00);
    private static final int LONG_JOURNEY_MINIMUM_DURATION_IN_MIN = 25;
    private static final int MORNING_PEAK_TIME_START_HOUR = 6;
    private static final int MORNING_PEAK_TIME_END_HOUR = 9;
    private static final int EVENING_PEAK_TIME_START_HOUR = 17;
    private static final int EVENING_PEAK_TIME_END_HOUR = 19;



    /////////////////////////////////////
    // CONSTRUCTOR
    /////////////////////////////////////

    private Calculator(){}



    /////////////////////////////////////
    // METHODS
    /////////////////////////////////////

    /**
     * Checks whether the journey took place in peak hours.
     * return boolean
     */
    public static boolean isPeak(Journey journey) {
        return peak(journey.startTime()) || peak(journey.endTime());
    }

    /**
     * Checks whether a specific time is in peak hours.
     * return boolean
     */
    private static boolean peak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= MORNING_PEAK_TIME_START_HOUR && hour <= MORNING_PEAK_TIME_END_HOUR) || (hour >= EVENING_PEAK_TIME_START_HOUR && hour <= EVENING_PEAK_TIME_END_HOUR);
    }

    /**
     * Checks whether the journey was long.
     * return boolean
     */
    public static boolean isLong(Journey journey){
        if(journey.durationSeconds() >= LONG_JOURNEY_MINIMUM_DURATION_IN_MIN * 60)
            return true;
        return false;
    }

    /**
     * Calculates the overall price of a journey.
     * return BigDecimal - price of the journey
     */
    public static BigDecimal calculatePriceOfJourney(Journey journey){
        BigDecimal journeyPrice;
        if(isPeak(journey)){
            journeyPrice = Calculator.getJourneyPrice(journey, PEAK_SHORT_JOURNEY_PRICE, PEAK_LONG_JOURNEY_PRICE);
        }else {
            journeyPrice = Calculator.getJourneyPrice(journey, OFF_PEAK_SHORT_JOURNEY_PRICE, OFF_PEAK_LONG_JOURNEY_PRICE);
        }

        return journeyPrice;
    }

    /**
     * Gets the price of a journey depending on the period and the length of the journey.
     * It is only used by the calculatePriceOfJourney method.
     * return BigDecimal - price of the journey
     */
    private static BigDecimal getJourneyPrice(Journey journey, BigDecimal shortPrice, BigDecimal longPrice) {
        if(Calculator.isLong(journey))
            return longPrice;
        return shortPrice;
    }

    /**
     * Calculates the total price of the journeys of the customer on the current day.
     * return BigDecimal - total price
     */
    public static BigDecimal calculateCustomerTotal(List<Journey> journeys){ // uses the calculator class;
        BigDecimal customerTotal = BigDecimal.ZERO;
        boolean traveledOnPeak=false;
        for (Journey journey : journeys){
            customerTotal = customerTotal.add(calculatePriceOfJourney(journey));
            if(isPeak(journey))
                traveledOnPeak = true;
        }
        if(traveledOnPeak && customerTotal.compareTo(PEAK_CAP_PRICE)==1)
            customerTotal = PEAK_CAP_PRICE;
        else if(!traveledOnPeak && customerTotal.compareTo(OFF_PEAK_CAP_PRICE)==1)
            customerTotal = OFF_PEAK_CAP_PRICE;
        return customerTotal;

    }

    /**
     * Rounds an amount to the nearest penny value.
     * return BigDecimal
     */
    public static BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
