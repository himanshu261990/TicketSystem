package com.cloudbees.ticket.model.dto;

import com.cloudbees.ticket.model.entity.Seat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hpathak
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    private String ticketId;
    private String from;
    private String to;
    private String seatNumber;
    private int fare;
    private UserDTO user;

    @JsonIgnore
    private Seat seat;

}
