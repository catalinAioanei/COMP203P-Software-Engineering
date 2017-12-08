package com.tfl.billing;

import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.*;

/**
 * Gets all the journey events of each customer and calculates the fares
 * individually using the Calculator class.
 */
public class TravelTracker  {

    /////////////////////////////////////
    // FIELDS
    /////////////////////////////////////

    private final ExternalJarAdapter externalJarAdapter;
    public final CardReadersManager cardReadersManager;



    /////////////////////////////////////
    // CONSTRUCTOR
    /////////////////////////////////////

    public TravelTracker(CardReadersManager cardReadersManager) {
        externalJarAdapter = new ExternalJarAdapter();
        this.cardReadersManager = cardReadersManager;
    }



    /////////////////////////////////////
    // METHODS
    /////////////////////////////////////

    public void chargeAccounts() {
        List<Customer> Customers = externalJarAdapter.getCustomers();
        for (Customer customer : Customers) {
            totalJourneysFor(customer);
        }
    }

    private void totalJourneysFor(Customer customer) {
        List<JourneyEvent> customerJourneyEvents = createCustomerJourneyEvents(customer);
        List<Journey> journeyListForCustomer = new ArrayList<>();
        boolean penaltyFare = false;
        try{
            journeyListForCustomer = createJourneyListForCustomer(customerJourneyEvents);
        } catch(Exception e){
            penaltyFare = true;
        }

        BigDecimal customerTotal = penaltyFare ? Calculator.PENALTY_FARE : Calculator.calculateCustomerTotal(journeyListForCustomer);
        externalJarAdapter.charge(customer, journeyListForCustomer, Calculator.roundToNearestPenny(customerTotal));
    }

    private List<JourneyEvent> createCustomerJourneyEvents(Customer customer) {
        List<JourneyEvent> customerJourneyEvents = new ArrayList<>();
        List<JourneyEvent> eventLog = cardReadersManager.getEventLog();

        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
        return customerJourneyEvents;
    }

    private List<Journey> createJourneyListForCustomer(List<JourneyEvent> customerJourneyEvents) throws Exception {
        List<Journey> journeys = new ArrayList<>();
        JourneyEvent start = null;

        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                if (start != null)
                    throw new Exception("Customer started a journey without finishing the previous one");
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }
        if (start != null)
            throw new Exception("Customer started a journey without finishing the previous one");
        return journeys;
    }

 }
