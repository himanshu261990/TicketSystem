package com.cloudbees.ticket.service;

import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import org.springframework.stereotype.Service;

/**
 * @author hpathak
 */
@Service
public class TicketService {

    public TicketResponseDTO createTicket(TicketRequestDTO requestDTO) {
        return TicketResponseDTO.builder().build();
    }

}
