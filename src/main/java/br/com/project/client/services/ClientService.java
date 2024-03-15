package br.com.project.client.services;

import br.com.project.client.dtos.ClientDTO;
import br.com.project.client.entities.Client;
import br.com.project.client.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientService() {
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) throws ResouceEntityNotFoundExeption{
        Optional<Client> obj = clientRepository.findById(id);
        Client client = obj.orElseThrow(() -> new ResouceEntityNotFoundExeption("entity not flound"));;
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(PageRequest pageRequest) {
        Page<Client> clientList = clientRepository.findAll(pageRequest);
        return clientList.map(categories -> new ClientDTO(categories));
    }

    @Transactional
    public ClientDTO updateById(Long id, ClientDTO clientDTO) throws ResouceEntityNotFoundExeption{
        try {
            Client client = clientRepository.findById(id).get();
            client.setName(clientDTO.getName());
            client.setCpf(clientDTO.getCpf());
            client.setChildren(clientDTO.getChildren());
            client.setIncome(clientDTO.getIncome());
            client.setBirthDate(clientDTO.getBirthDate());
            client = clientRepository.save(client);
            return new ClientDTO(client);
        }catch (EntityNotFoundException e){
            throw new ResouceEntityNotFoundExeption("Id not flound " + id);
        }
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setChildren(clientDTO.getChildren());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    public void deleteById(Long id) throws ResouceEntityNotFoundExeption, DataBaseExeption {
        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResouceEntityNotFoundExeption("Id not flound " + id);

        } catch (DataIntegrityViolationException e) {
            throw new DataBaseExeption("Integrity violation");
        }
    }
}
