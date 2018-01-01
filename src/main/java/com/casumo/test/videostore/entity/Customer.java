package com.casumo.test.videostore.entity;

import com.casumo.test.videostore.controller.dto.CustomerDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.casumo.test.videostore.constants.VideoStoreConstants.EXTRA_BONUS_POINTS;
import static com.casumo.test.videostore.constants.VideoStoreConstants.REGULAR_BONUS_POINTS;

@Entity
@Data
public class Customer extends CustomerDto {

    @Id
    String customerId;

    private Integer bonusPoints;

    @Column
    @ElementCollection(targetClass = RentedFilm.class)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "customer")
    private List<RentedFilm> rentedFilmEntities;

    public Customer() {
    }

    public Customer(String customerId, String fullName, Integer phoneNumber) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.bonusPoints = 0;
        this.rentedFilmEntities = Collections.emptyList();
    }

    public void addRentedFilm(RentedFilm newRentedFilm) {
        this.rentedFilmEntities.add(newRentedFilm);
        switch (newRentedFilm.getFilm().getType()) {
            case newFilm:
                this.bonusPoints += EXTRA_BONUS_POINTS;
                break;
            default:
                this.bonusPoints += REGULAR_BONUS_POINTS;
                break;
        }
    }

    public void removeRentedFilm(RentedFilm newRentedFilm) {
        this.rentedFilmEntities.remove(newRentedFilm);
    }

    public Optional<RentedFilm> getRentedFilm(Film film) {
        return this.rentedFilmEntities
                .stream()
                .filter(rentedFilm -> rentedFilm.getFilm().equals(film))
                .findFirst();
    }


}
