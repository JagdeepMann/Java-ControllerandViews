package com.suraj.loginregistration.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suraj.loginregistration.models.Client;

@Repository
public interface ClientRepo extends CrudRepository<Client, Long>{
	Client findByEmail(String email);
	List<Client> findAll();
	void deleteById(Long id);
	Client save(Client c);
	Optional<Client> findById(Long id);

}
