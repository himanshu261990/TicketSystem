package com.cloudbees.ticket.controller;

import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import com.cloudbees.ticket.model.dto.UserSectionDTO;
import com.cloudbees.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hpathak
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private TicketService ticketService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<TicketResponseDTO> create(@RequestBody TicketRequestDTO ticketRequestDTO) throws Exception {
        return new ResponseEntity<>(ticketService.createTicket(ticketRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(value="/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the ticket details from a ticketId")
    public ResponseEntity<TicketResponseDTO> get(@PathVariable String ticketId) throws Exception {
        return new ResponseEntity<>(ticketService.getTicketDetails(ticketId), HttpStatus.CREATED);
    }

    @GetMapping(value="/users/{section}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the ticket details from a ticketId")
    public ResponseEntity<List<UserSectionDTO>> getUsersForASection(@PathVariable String section) throws Exception {
        return new ResponseEntity<>(ticketService.getUsersListForASection(section), HttpStatus.CREATED);
    }

    @DeleteMapping(value="users/remove/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<Void> removeUser(@PathVariable String ticketId) throws Exception {
        ticketService.removeUser(ticketId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="users/update-ticket/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<TicketResponseDTO> updateUserTicket(@PathVariable String ticketId) throws Exception {
        return new ResponseEntity<>(ticketService.updateUserSeat(ticketId), HttpStatus.OK);
    }

    @GetMapping(value="/available-seats")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<Long> getAvailableSeats() throws Exception {
        return new ResponseEntity<>(ticketService.getAvailableSeats(), HttpStatus.OK);
    }

    @GetMapping(value="/available-seats/{sectionId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket for a user with from and to details")
    public ResponseEntity<Long> getAvailableSeatsForASection(@PathVariable String sectionId) throws Exception {
        return new ResponseEntity<>(ticketService.getAvailableSeatsForASection(sectionId), HttpStatus.OK);
    }


}
