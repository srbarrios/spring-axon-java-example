package com.casumo.test.videostore.aggregate;

import com.casumo.test.videostore.coreapi.*;
import com.casumo.test.videostore.entity.RentedFilm;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;
    private String fullName;
    private Integer phoneNumber;
    private Integer bonusPoints;
    private List<RentedFilm> rentedFilmEntities;

    @SuppressWarnings("unused")
    public CustomerAggregate() {
    }

    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand cmd) {
        apply(new CustomerCreatedEvent(cmd.getCustomerId(), cmd.getFullName(), cmd.getPhoneNumber()));
    }

    @EventSourcingHandler
    protected void on(CustomerCreatedEvent event) {
        this.customerId = event.getCustomerId();
    }

    @CommandHandler
    public void handle(RemoveCustomerCommand cmd) {
        apply(new CustomerRemovedEvent(cmd.getCustomerId()));
    }

    @CommandHandler
    public void handle(RentFilmsCommand cmd) {
        apply(new FilmsRentedEvent(cmd.getCustomerId(), cmd.getFilms(), cmd.getDays()));
    }

    @CommandHandler
    public void handle(ReturnFilmsCommand cmd) {
        apply(new FilmsReturnedEvent(cmd.getCustomerId(), cmd.getFilms()));
    }

}
