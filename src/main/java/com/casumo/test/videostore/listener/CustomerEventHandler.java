package com.casumo.test.videostore.listener;

import com.casumo.test.videostore.coreapi.CustomerCreatedEvent;
import com.casumo.test.videostore.coreapi.CustomerRemovedEvent;
import com.casumo.test.videostore.coreapi.FilmsRentedEvent;
import com.casumo.test.videostore.coreapi.FilmsReturnedEvent;
import com.casumo.test.videostore.entity.Customer;
import com.casumo.test.videostore.entity.Film;
import com.casumo.test.videostore.entity.RentedFilm;
import com.casumo.test.videostore.repository.CustomerRepository;
import com.casumo.test.videostore.repository.FilmRepository;
import com.casumo.test.videostore.repository.RentedFilmRepository;
import com.casumo.test.videostore.utils.TimeProvider;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class CustomerEventHandler {

    private final FilmRepository filmRepository;
    private final CustomerRepository customerRepository;
    private final RentedFilmRepository rentedFilmRepository;
    private final TimeProvider timeProvider;
    private final CommandGateway commandGateway;

    public CustomerEventHandler(final FilmRepository filmRepository,
                                final CustomerRepository customerRepository,
                                final RentedFilmRepository rentedFilmRepository,
                                final TimeProvider timeProvider,
                                final CommandGateway commandGateway) {
        this.filmRepository = filmRepository;
        this.customerRepository = customerRepository;
        this.rentedFilmRepository = rentedFilmRepository;
        this.timeProvider = timeProvider;
        this.commandGateway = commandGateway;
    }

    @EventHandler
    protected void on(CustomerCreatedEvent event) {
        customerRepository.save(new Customer(event.getCustomerId(), event.getFullName(), event.getPhoneNumber()));
    }

    @EventHandler
    protected void on(CustomerRemovedEvent event) {
        customerRepository.delete(event.getCustomerId());
    }

    @EventHandler
    protected void on(FilmsRentedEvent event) {
        addCustomerRentedFilms(event.getCustomerId(), event.getFilms(), event.getDays());
    }


    @EventHandler
    protected void on(FilmsReturnedEvent event) {
        removeCustomerRentedFilms(event.getCustomerId(), event.getFilms());
    }


    private void addCustomerRentedFilms(String customerId, List<String> filmsIds, int days) {
        Customer customer = customerRepository.findOne(customerId);
        List<Film> films = filmsIds
                .stream()
                .map(filmRepository::findOne)
                .collect(Collectors.toList());
        for (Film film : films) {
            Optional<RentedFilm> returnedFilm = customer.getRentedFilm(film);
            if (!returnedFilm.isPresent()) {
                Date now = timeProvider.getCurrentDate();
                Date expectedReturnDate = Date.from(now.toInstant().plus(days, DAYS));
                RentedFilm newRentedFilm = new RentedFilm(customer, film, now, expectedReturnDate);
                rentedFilmRepository.saveAndFlush(newRentedFilm);
                film.setRenter(customer);
                filmRepository.saveAndFlush(film);
                customer.addRentedFilm(newRentedFilm);
            }
        }
        customerRepository.saveAndFlush(customer);
    }

    private void removeCustomerRentedFilms(String customerId, List<String> filmsIds) {
        Customer customer = customerRepository.findOne(customerId);
        List<Film> films = filmsIds
                .stream()
                .map(filmRepository::findOne)
                .collect(Collectors.toList());
        for (Film film : films) {
            Optional<RentedFilm> returnedFilm = customer.getRentedFilm(film);
            if (returnedFilm.isPresent()) {
                Date now = timeProvider.getCurrentDate();
                returnedFilm.get().setReturnDate(now);
                rentedFilmRepository.saveAndFlush(returnedFilm.get());
                film.setRenter(null);
                filmRepository.saveAndFlush(film);
                customer.removeRentedFilm(returnedFilm.get());
            }
        }
        customerRepository.saveAndFlush(customer);
    }


}
