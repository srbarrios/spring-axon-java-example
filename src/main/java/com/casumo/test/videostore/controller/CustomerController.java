package com.casumo.test.videostore.controller;

import com.casumo.test.videostore.callback.LoggingCallback;
import com.casumo.test.videostore.controller.dto.CustomerDto;
import com.casumo.test.videostore.coreapi.CreateCustomerCommand;
import com.casumo.test.videostore.coreapi.RemoveCustomerCommand;
import com.casumo.test.videostore.entity.Customer;
import com.casumo.test.videostore.repository.CustomerRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    private CommandGateway commandGateway;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public @ResponseBody
    ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = repository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public @ResponseBody
    ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        Customer customer = repository.findOne(customerId);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customer/films/{customerId}")
    public @ResponseBody
    ResponseEntity<List<String>> getRentedFilms(@PathVariable String customerId) {
        Customer customer = repository.findOne(customerId);
        List<String> films = customer
                .getRentedFilmEntities()
                .stream()
                .map(rentedFilm -> rentedFilm.getFilm().getFilmId())
                .collect(Collectors.toList());
        return new ResponseEntity<>(films, HttpStatus.OK);
    }


    @PostMapping("/customer")
    public @ResponseBody
    ResponseEntity<String> createCustomer(@RequestBody CustomerDto customer) {
        String customerId = UUID.randomUUID().toString();
        commandGateway.send(
                new CreateCustomerCommand(
                        customerId,
                        customer.getFullName(),
                        customer.getPhoneNumber()
                ),
                LoggingCallback.INSTANCE);
        return new ResponseEntity<>(customerId, HttpStatus.CREATED);
    }

    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        commandGateway.send(new RemoveCustomerCommand(customerId), LoggingCallback.INSTANCE);
        return new ResponseEntity<>(customerId, HttpStatus.NO_CONTENT);
    }
}
