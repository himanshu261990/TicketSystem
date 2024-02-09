package com.cloudbees.ticket.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author hpathak
 */
@Builder
@Data
public class TicketRequestDTO {

    private String from;
    private String to;
    private UserDTO user;

}
