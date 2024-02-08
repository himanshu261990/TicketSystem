package com.cloudbees.ticket.controller;

import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import com.cloudbees.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hpathak
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<TicketResponseDTO> create(@RequestBody TicketRequestDTO ticketRequestDTO) throws Exception {
        return new ResponseEntity<>(ticketService.createTicket(ticketRequestDTO), HttpStatus.CREATED);
    }

}
