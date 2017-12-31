package com.casumo.test.videostore.controller;

import com.casumo.test.videostore.controller.dto.RentFilmsDto;
import com.casumo.test.videostore.controller.dto.ReturnFilmsDto;
import com.casumo.test.videostore.coreapi.RentFilmsCommand;
import com.casumo.test.videostore.coreapi.ReturnFilmsCommand;
import com.casumo.test.videostore.entity.Customer;
import com.casumo.test.videostore.entity.Film;
import com.casumo.test.videostore.entity.RentedFilm;
import com.casumo.test.videostore.repository.CustomerRepository;
import com.casumo.test.videostore.repository.FilmRepository;
import com.casumo.test.videostore.repository.RentedFilmRepository;
import com.casumo.test.videostore.utils.TimeProvider;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.casumo.test.videostore.constants.VideoStoreConstants.*;

@RestController
public class VideoStoreController {

    private Logger logger = LoggerFactory.getLogger(VideoStoreController.class);

    private final FilmRepository filmRepository;
    private final CustomerRepository customerRepository;
    private final RentedFilmRepository rentedFilmRepository;
    private final TimeProvider timeProvider;

    @Autowired
    private CommandGateway commandGateway;

    public VideoStoreController(final FilmRepository filmRepository,
                                final CustomerRepository customerRepository,
                                final RentedFilmRepository rentedFilmRepository,
                                final TimeProvider timeProvider) {
        this.filmRepository = filmRepository;
        this.customerRepository = customerRepository;
        this.rentedFilmRepository = rentedFilmRepository;
        this.timeProvider = timeProvider;
    }

    @GetMapping("/videostore/rentedfilms")
    public @ResponseBody
    ResponseEntity<List<RentedFilm>> rentedFilms() {
        List<RentedFilm> rentedFilms = rentedFilmRepository.findAll();
        return new ResponseEntity<>(rentedFilms, HttpStatus.OK);

    }

    @PostMapping("/videostore/rent")
    public @ResponseBody
    ResponseEntity<Long> rentFilms(@RequestBody RentFilmsDto rentFilmsDto) {
        commandGateway.send(new RentFilmsCommand(rentFilmsDto.getCustomerId(), rentFilmsDto.getFilmsIds(), rentFilmsDto.getDays()));
        long rentalPrice = calculateRentalPrice(rentFilmsDto.getFilmsIds(), rentFilmsDto.getDays());
        return new ResponseEntity<>(rentalPrice, HttpStatus.OK);
    }

    @PostMapping("/videostore/return")
    public @ResponseBody
    ResponseEntity<Long> returnFilms(@RequestBody ReturnFilmsDto returnFilmsDto) {
        commandGateway.send(new ReturnFilmsCommand(returnFilmsDto.getCustomerId(), returnFilmsDto.getFilmsIds()));
        long surcharge = calculateSurcharge(returnFilmsDto.getCustomerId(), returnFilmsDto.getFilmsIds());
        return new ResponseEntity<>(surcharge, HttpStatus.OK);
    }

    private long calculateSurcharge(String customerId, List<String> filmsIds) {
        Customer customer = customerRepository.findOne(customerId);
        List<Film> films = filmsIds
                .stream()
                .map(filmRepository::findOne)
                .collect(Collectors.toList());
        long surcharge = 0;
        for (Film film : films) {
            Optional<RentedFilm> returnedFilm = customer.getRentedFilm(film);
            if (returnedFilm.isPresent()) {
                long returnDate = timeProvider.getCurrentDate().getTime(); //Not the exact time stored but enough to compare days
                long expectedDate = returnedFilm.get().getExpectedReturnDate().getTime();
                long exceededDays = TimeUnit.MILLISECONDS.toDays(returnDate - expectedDate);
                if (exceededDays > 0) {
                    surcharge += exceededDays * PENALTY_PRICE;
                }
            }
        }
        return surcharge;
    }

    private long calculateRentalPrice(List<String> filmsIds, int days) {
        List<Film> films = filmsIds
                .stream()
                .map(filmRepository::findOne)
                .collect(Collectors.toList());
        long rentalPrice = 0;
        for (Film film : films) {
            switch (film.getType()) {
                case newFilm:
                    rentalPrice += days * PREMIUM_PRICE;
                    break;
                case regular:
                    rentalPrice += BASIC_PRICE + Math.max(0, days - 3) * BASIC_PRICE;
                    break;
                case old:
                    rentalPrice += BASIC_PRICE + Math.max(0, days - 5) * BASIC_PRICE;
                    break;
                default:
                    logger.warn("Film type " + film.getType().toString() + " don't have rental price");
                    break;
            }
        }
        return rentalPrice;
    }
}
