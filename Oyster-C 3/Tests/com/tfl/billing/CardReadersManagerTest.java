package com.tfl.billing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import com.tfl.underground.Station;
import org.junit.Test;


public class CardReadersManagerTest {

    private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();
    CardReadersManager cardReadersData = new CardReadersManager();
    OysterCardReader mockOysterCardReader = context.mock(OysterCardReader.class);

    @Test
    public void connectingCardReadersCallsRegisterMethodOnCardReader() {
        context.checking(new Expectations() {{
            exactly(1).of(mockOysterCardReader).register(cardReadersData);
        }});
        cardReadersData.connect(mockOysterCardReader);
    }

    @Test
    public void assertCardScanned() throws Exception {
        OysterCard myCard = externalJarAdapter.getOysterCard("3f1b3b55-f266-4426-ba1b-bcc506541866");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
        CardReadersManager cardReadersData = new CardReadersManager();

        cardReadersData.cardScanned(myCard.id(),paddingtonReader.id());
        assertThat(cardReadersData.getEventLog().size(),is(1));
        assertThat(cardReadersData.getCurrentlyTravelling().size(),is(1));
        cardReadersData.cardScanned(myCard.id(),bakerStreetReader.id());
        assertThat(cardReadersData.getEventLog().size(),is(2));
        assertThat(cardReadersData.getCurrentlyTravelling().size(),is(0));

    } // still don't know why the fuk this doesn't work

    @Test (expected = UnknownOysterCardException.class)
    public void assertExceptionIsThrownForUnknownCustomer()
    {
        OysterCard fakeCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef000");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        CardReadersManager cardReadersData = new CardReadersManager();

        cardReadersData.cardScanned(fakeCard.id(),paddingtonReader.id());
    }

    @Test
    public void assertConnectMethod() throws Exception
    {
        OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
        CardReadersManager cardReadersData = new CardReadersManager();

        OysterCard oysterCard = externalJarAdapter.getOysterCard("3f1b3b55-f266-4426-ba1b-bcc506541866");


        cardReadersData.connect(paddingtonReader,bakerStreetReader);
        cardReadersData.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 8:00:00");
        cardReadersData.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 9:20:00");
        cardReadersData.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 11:30:00");
        cardReadersData.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 12:30:00");
        cardReadersData.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 21:00:00");
        cardReadersData.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 22:20:00");

        assertEquals(cardReadersData.getEventLog().size(),6);
    }

}
