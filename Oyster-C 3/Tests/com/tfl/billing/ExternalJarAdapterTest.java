package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ExternalJarAdapterTest {
    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();

    private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void getCustomersMethodReturnsTheCustomersList(){
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        assertThat(externalJarAdapter.getCustomers(), is(customerDatabase.getCustomers()));
    }


    @Test
    public void getOysterCardReturnsANewOysterWithTheGivenStringAsId(){
        String id = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
        assertTrue(externalJarAdapter.getOysterCard(id) instanceof OysterCard); // checks for the correct return type
        assertThat(externalJarAdapter.getOysterCard(id).id().toString(), is(id)); // checks that the ids match.
    }

    @Test
    public void getCardReaderReturnsTheCorrectReader(){
        assertThat(externalJarAdapter.getCardReader(Station.PADDINGTON), is(OysterReaderLocator.atStation(Station.PADDINGTON)));
    }

    @Test
    public void chargeMethodCallsTheChargeMethodOnThePaymentSystem(){
        context.checking(new Expectations(){{

        }}); //TODO:!!!!!!

    }

}