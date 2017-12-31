package com.casumo.test.videostore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Long getRentedFilmId() {
        return rentedFilmId;
    }

    public Date getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(Date rentedDate) {
        this.rentedDate = rentedDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RentedFilm that = (RentedFilm) o;

        if (rentedFilmId != null ? !rentedFilmId.equals(that.rentedFilmId) : that.rentedFilmId != null) return false;
        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (film != null ? !film.equals(that.film) : that.film != null) return false;
        if (rentedDate != null ? !rentedDate.equals(that.rentedDate) : that.rentedDate != null) return false;
        if (expectedReturnDate != null ? !expectedReturnDate.equals(that.expectedReturnDate) : that.expectedReturnDate != null)
            return false;
        return returnDate != null ? returnDate.equals(that.returnDate) : that.returnDate == null;
    }

    @Override
    public int hashCode() {
        int result = rentedFilmId != null ? rentedFilmId.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (film != null ? film.hashCode() : 0);
        result = 31 * result + (rentedDate != null ? rentedDate.hashCode() : 0);
        result = 31 * result + (expectedReturnDate != null ? expectedReturnDate.hashCode() : 0);
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        return result;
    }
}
