package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class is responsible for adapting the methods of the external library.
 * The main purpose of this class is if the external library is changed then
 * only these methods are needed to be changed to remain compatible.
 */
public class ExternalJarAdapter implements ExternalJar {

    /////////////////////////////////////
    // METHODS
    /////////////////////////////////////

    @Override
    public OysterCard getOysterCard(String id) {
        return new OysterCard(id);
    }

    @Override
    public OysterCardReader getCardReader(Station station) {

        OysterCardReader ourReader = OysterReaderLocator.atStation(station);
        return ourReader;
    }

    @Override
    public List<Customer> getCustomers() {

        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = customerDatabase.getCustomers();
        return customers;
    }

    @Override
    public boolean addCustomerToDatabase(Customer customer) {
        List<Customer> customers = this.getCustomers();
        boolean added = customers.add(customer);
        while (customers.add(customer) == false) {
            added = customers.add(customer);
        }
        return added;
    }

    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal cost) {
        PaymentsSystem.getInstance().charge(customer, journeys, cost);
    }
}
