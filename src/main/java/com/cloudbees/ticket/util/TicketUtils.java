package com.cloudbees.ticket.util;

import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import com.cloudbees.ticket.model.dto.UserDTO;
import com.cloudbees.ticket.model.entity.Seat;

import java.util.Random;

/**
 * @author hpathak
 */
public class TicketUtils {

    public static TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO, Seat seat, int fair){
        return TicketResponseDTO.builder()
                .from(ticketRequestDTO.getFrom())
                .to(ticketRequestDTO.getTo())
                .seatNumber(seat.getSection() + "-" + seat.getSeatNumber())
                .seat(seat)
                .fare(fair)
                .user(UserDTO.builder()
                        .firstName(ticketRequestDTO.getUser().getFirstName())
                        .lastName(ticketRequestDTO.getUser().getLastName())
                        .emailAddress(ticketRequestDTO.getUser().getEmailAddress())
                        .build())
                .build();
    }

    public static String generateTicketId() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(100000));
    }

}
