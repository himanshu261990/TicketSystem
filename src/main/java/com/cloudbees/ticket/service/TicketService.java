package com.cloudbees.ticket.service;

import com.cloudbees.ticket.cache.TicketsCache;
import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import com.cloudbees.ticket.model.dto.UserSectionDTO;
import com.cloudbees.ticket.model.entity.Seat;
import com.cloudbees.ticket.util.TicketUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hpathak
 */
@Service
@AllArgsConstructor
public class TicketService {

    private SeatsService seatsService;

    /**
     * Creates the ticket for a user
     * Check if a seat is available in a train, if it's available gets the fare.
     * Does the payment and if the payment is success, allocate the seat
     * @param requestDTO
     * @return
     * @throws Exception
     */
    public TicketResponseDTO createTicket(TicketRequestDTO requestDTO) throws Exception{
        Seat seat = seatsService.getSeat(requestDTO.getUser().getEmailAddress())
                .orElseThrow(() -> new NoSuchElementException("No seat found"));
        int fare = seatsService.getFare(requestDTO.getFrom(), requestDTO.getTo());
        TicketResponseDTO ticketResponseDTO = TicketUtils.createTicket(requestDTO, seat, fare);
        // Do the payment for the ticket
        // If the payment succeeds, assign the ticketId and update the seat list
        ticketResponseDTO.setTicketId(TicketUtils.generateTicketId());
        seatsService.confirmSeat(seat.getId());
        TicketsCache.ticketsList.add(ticketResponseDTO);
        return ticketResponseDTO;
    }

    /**
     * Gets the ticket details for a ticketId
     * @param ticketId
     * @return
     * @throws Exception
     */
    public TicketResponseDTO getTicketDetails(String ticketId) throws Exception {
        TicketResponseDTO ticketResponseDTO =  TicketsCache.ticketsList.stream().filter(ticket -> ticket.getTicketId().equals(ticketId)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Ticket is not present"));
        return ticketResponseDTO;
    }

    /**
     * Gets the list of the users seating in the provided section
     * @param section
     * @return
     */
    public List<UserSectionDTO> getUsersListForASection(String section) {
        return TicketsCache.ticketsList.stream()
                .filter(ticket -> ticket.getSeat().getSection().equals(section))
                .collect(Collectors.toList())
                .stream()
                .map(ticketResponseDTO -> ticketDtoToUserSection(ticketResponseDTO))
                .collect(Collectors.toList());
    }

    /**
     * Removes the user from the train
     * Free up that seat once the user is removed from the train
     * @param ticketId
     * @return
     */
    public String removeUser(String ticketId) {
        Iterator iterator = TicketsCache.ticketsList.iterator();
        while(iterator.hasNext()) {
            TicketResponseDTO ticketResponseDTO = (TicketResponseDTO) iterator.next();
            if(ticketResponseDTO.getTicketId().equals(ticketId)) {
                iterator.remove();
                seatsService.updateSeatToVacant(ticketResponseDTO.getSeat());
                return ticketResponseDTO.getUser().getEmailAddress();
            }
        }
        throw new NoSuchElementException("User is not present in the train");
    }

    /**
     * Updated the users seat
     * If a seat is available, allocate that seat and free up the current seat
     * If the seat is unavailable, throws the exception that no seat is unavailable
     * @param ticketId
     * @return
     */
    public TicketResponseDTO updateUserSeat(String ticketId) {
        TicketResponseDTO ticketResponseDTO = TicketsCache.ticketsList.stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Ticket details not found"));
        Seat newSeat = seatsService.getSeat(ticketResponseDTO.getUser().getEmailAddress())
                .orElseThrow(() -> new NoSuchElementException("No other seats available"));
        seatsService.confirmSeat(newSeat.getId());
        seatsService.updateSeatToVacant(ticketResponseDTO.getSeat());
        ticketResponseDTO.setSeat(newSeat);
        ticketResponseDTO.setSeatNumber(newSeat.getSection() + "-" + newSeat.getSeatNumber());
        return ticketResponseDTO;
    }

    /**
     * Gets the avilable number of seats
     * @return
     */
    public long getAvailableSeats(){
        return seatsService.getAvailableSeats();
    }

    /**
     * Gets the available seats for a specific section
     * @param section
     * @return
     */
    public long getAvailableSeatsForASection(String section){
        return seatsService.getAvailableSeatsForASection(section);
    }

    private UserSectionDTO ticketDtoToUserSection(TicketResponseDTO ticketResponseDTO) {
        return UserSectionDTO.builder()
                .user(ticketResponseDTO.getUser())
                .ticketId(ticketResponseDTO.getTicketId())
                .seatNumber(ticketResponseDTO.getSeatNumber())
                .section(ticketResponseDTO.getSeat().getSection())
                .build();
    }



}
