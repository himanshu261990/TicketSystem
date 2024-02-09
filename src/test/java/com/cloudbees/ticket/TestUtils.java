package com.cloudbees.ticket;

import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.UserDTO;

/**
 * @author hpathak
 */
public class TestUtils {

    public static TicketRequestDTO createTicketRequest(String firstName, String lastName, String email, String from, String to) {
        return TicketRequestDTO.builder()
                .from(from)
                .to(to)
                .user(UserDTO.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .emailAddress(email)
                        .build())
                .build();
    }
}
