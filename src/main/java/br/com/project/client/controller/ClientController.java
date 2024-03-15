package br.com.project.client.controller;

import br.com.project.client.dtos.ClientDTO;
import br.com.project.client.repository.ClientRepository;
import br.com.project.client.services.ClientService;
import br.com.project.client.services.DataBaseExeption;
import br.com.project.client.services.ResouceEntityNotFoundExeption;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable Long id) throws ResouceEntityNotFoundExeption {
            ClientDTO dto = clientService.findById(id);
            return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<Page<ClientDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "20") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy){

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        Page<ClientDTO> dto = clientService.findAll(pageRequest);
        return ResponseEntity.ok().body(dto);
    }

    //update/put
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id,@RequestBody ClientDTO clientDTO) throws ResouceEntityNotFoundExeption{
        ClientDTO dto = clientService.updateById(id, clientDTO);
        return ResponseEntity.ok(dto);
    }

    //Post
    @PostMapping
    public ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO clientDTO){
        ClientDTO dto = clientService.insert(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    //delete
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ResouceEntityNotFoundExeption, DataBaseExeption {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}