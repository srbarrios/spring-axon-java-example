package com.casumo.test.videostore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class RentedFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rentedFilmId;

    @JsonIgnoreProperties({ "rentedFilmEntities" })
    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = Customer.class)
    private Customer customer;

    @JsonIgnoreProperties({ "renter"})
    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = Film.class)
    private Film film;


    private Date rentedDate;

    private Date expectedReturnDate;

    private Date returnDate;

    public RentedFilm() {
    }

    public RentedFilm(Customer customer, Film film, Date rentedDate, Date expectedReturnDate) {
        this.customer = customer;
        this.film = film;
        this.rentedDate = rentedDate;
        this.expectedReturnDate = expectedReturnDate;
    }

}
