package com.suraj.loginregistration.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.suraj.loginregistration.models.Client;
import com.suraj.loginregistration.repositories.ClientRepo;

@Service
public class ClientService {
	//connect our repo and constuct our Service
	private final ClientRepo clientRepo;
	// Construction of ClientService with ClientRepo variety and calling it clientRepo
	// within scope of this file
	public ClientService(ClientRepo clientRepo) {
		this.clientRepo = clientRepo;
	}

	// register/save client and hash their password
	public Client registerClient(Client client) {
		String hashed = BCrypt.hashpw(client.getPassword(), BCrypt.gensalt());
		client.setPassword(hashed);
		return clientRepo.save(client);
	}

	// find client by email
	public Client findByEmail(String email) {
		return clientRepo.findByEmail(email);
	}

	// find client by id
	public Client findClientById(Long id) {
		Optional<Client> u = clientRepo.findById(id);

		if(u.isPresent()) {
			return u.get();
		} else {
			return null;
		}
	}

	// authenticate client (login specific)
	public boolean authenticateClient(String email, String password) {
		// first find the client by email
		Client client = clientRepo.findByEmail(email);
		// if we can't find it by email, return false
		if(client == null) {
			return false;
		} else {
			// if the passwords match, return true, else, return false
			if(BCrypt.checkpw(password, client.getPassword())) {
				return true;
			} else {
				return false;
			}
		}
	}
}
