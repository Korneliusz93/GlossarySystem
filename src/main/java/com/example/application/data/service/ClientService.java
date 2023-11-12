package com.example.application.data.service;

import com.example.application.data.entity.Client;
import com.example.application.data.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public List<Client> findAllClients(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return clientRepository.findAll();
        } else {
            return clientRepository.search(stringFilter);
        }
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public long countClients() {
        return clientRepository.count();
    }

    public void deleteClient(Client client) {
        clientRepository.delete(client);
    }

    public void saveClient(Client client) {
        if (client == null) {
            System.err.println("Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        clientRepository.save(client);
    }
}
