package com.casumo.test.videostore.acceptance;

import com.casumo.test.videostore.aggregate.CustomerAggregate;
import com.casumo.test.videostore.coreapi.CreateCustomerCommand;
import com.casumo.test.videostore.coreapi.CustomerCreatedEvent;
import com.casumo.test.videostore.coreapi.CustomerRemovedEvent;
import com.casumo.test.videostore.coreapi.RemoveCustomerCommand;
import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {

    private FixtureConfiguration<CustomerAggregate> fixture;


    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(CustomerAggregate.class);
    }

    @Test
    public void testCustomerCreated() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateCustomerCommand("customer1", "name1", 0))
                .expectEvents(new CustomerCreatedEvent("customer1", "name1", 0));
    }

    @Test
    public void testSameCustomerIdCantBeCreatedTwice() throws Exception {
        fixture.given(new CustomerCreatedEvent("customer1", "name1", 0))
                .when(new CreateCustomerCommand("customer1", "name1", 0))
                .expectException(EventStoreException.class);
    }

    @Test
    public void testCustomerRemoved() throws Exception {
        fixture.givenCommands(new CreateCustomerCommand("customer1", "name1", 0))
                .when(new RemoveCustomerCommand(
                        "customer1"))
                .expectEvents(new CustomerRemovedEvent(
                        "customer1"));
    }


}
