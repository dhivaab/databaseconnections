package com.blueocean.dataconnectioncode.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.blueocean.dataconnectioncode.model.CustomerJPA;

public interface CustomerRepository extends CrudRepository<CustomerJPA, Long> {

	List<CustomerJPA> findByLastName(String lastName);

	Optional<CustomerJPA> findById(Long id);
}
