package com.cloudbees.ticket.model.dto;

import lombok.Builder;

/**
 * @author hpathak
 */
@Builder
public class TicketRequestDTO {

    private String from;
    private String to;
    private UserDTO user;

}
