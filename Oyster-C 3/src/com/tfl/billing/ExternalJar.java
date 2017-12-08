package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ExternalJar {
    OysterCard getOysterCard(String id);

    OysterCardReader getCardReader(Station station);

    List<Customer> getCustomers();

    boolean addCustomerToDatabase(Customer customer);

    void charge(Customer customer, List<Journey> journeys, BigDecimal cost);

    static boolean isRegisteredId(UUID cardId){
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        return customerDatabase.isRegisteredId(cardId);
    }

}
