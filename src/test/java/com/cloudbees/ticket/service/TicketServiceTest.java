package com.cloudbees.ticket.service;

import com.cloudbees.ticket.TestUtils;
import com.cloudbees.ticket.cache.TicketsCache;
import com.cloudbees.ticket.model.dto.TicketRequestDTO;
import com.cloudbees.ticket.model.dto.TicketResponseDTO;
import com.cloudbees.ticket.model.dto.UserSectionDTO;
import com.cloudbees.ticket.model.entity.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author hpathak
 */
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @InjectMocks
    private TicketService ticketService;

    @Mock
    private SeatsService seatsService;

    @InjectMocks
    private TicketsCache ticketsCache;

    @AfterEach
    public void cleanUp(){
        ticketsCache.ticketsList = new ArrayList<>();
    }

    @Test
    @DisplayName("Creates the ticket and return the details - throws exception")
    public void testCreateTicketThrowsException() {
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> {
                    ticketService.createTicket(request);
                });

    }
    @Test
    @DisplayName("Creates the ticket and return the details")
    public void testCreateTicket() throws Exception {
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        Seat seat = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat));

        TicketResponseDTO response = ticketService.createTicket(request);

        assertEquals("London", response.getFrom());
        assertEquals("France", response.getTo());
        assertEquals("Himanshu", response.getUser().getFirstName());
        assertEquals("Pathak", response.getUser().getLastName());
        assertEquals("him@pathak.com", response.getUser().getEmailAddress());
        assertNotNull(response.getTicketId());
        assertNotNull(response.getSeatNumber());
    }

    @Test
    @DisplayName("Get the ticket details from the ticketId - throws exception")
    public void testGetTicketDetailsThrowsException() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    ticketService.getTicketDetails("123");
                });
    }

    @Test
    @DisplayName("Get the ticket details from the ticketId")
    public void testGetTicketDetails() throws Exception {
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        Seat seat = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat));

        TicketResponseDTO response = ticketService.createTicket(request);

        TicketResponseDTO ticketDetails = ticketService.getTicketDetails(response.getTicketId());
        assertEquals(request.getUser().getEmailAddress(), ticketDetails.getUser().getEmailAddress());
    }

    @Test
    @DisplayName("Get the users for a specific section")
    public void testGetUsersForASectionInvalidSection() {
        List<UserSectionDTO> userSectionList = ticketService.getUsersListForASection("C");
        assertTrue(userSectionList.size() == 0);
    }

    @Test
    @DisplayName("Get the users for a specific section")
    public void testGetUsersForASection() throws Exception{
        TicketRequestDTO request1 = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");
        Seat seat1 = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();

        TicketRequestDTO request2 = TestUtils.createTicketRequest("Cloudbees", "Pathak", "him@cloudbees.com", "London", "France");
        Seat seat2 = Seat.builder()
                .section("B")
                .seatNumber(4)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat1));
        when(seatsService.getSeat("him@cloudbees.com")).thenReturn(Optional.of(seat2));

        TicketResponseDTO response1 = ticketService.createTicket(request1);
        TicketResponseDTO response2 = ticketService.createTicket(request2);

        List<UserSectionDTO> userSectionDTOList = ticketService.getUsersListForASection("A");
        assertEquals(userSectionDTOList.size(), 1);
        assertEquals(userSectionDTOList.get(0).getTicketId(), response1.getTicketId());
    }

    @Test
    @DisplayName("Removes the user from the train - throws exception")
    public void testRemoveUserThrowsException() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    ticketService.removeUser("123");
                });
    }

    @Test
    @DisplayName("Removes the user from the train")
    public void testRemoveUser() throws Exception{
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        Seat seat = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat));

        TicketResponseDTO response = ticketService.createTicket(request);

        String emailAddress = ticketService.removeUser(response.getTicketId());
        assertEquals(response.getUser().getEmailAddress(), emailAddress);
    }


    @Test
    @DisplayName("Updates the users seat - invalid ticket number - throws exception")
    public void testUpdateSeatForUserInvalidSeat() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    ticketService.updateUserSeat("123");
                });
    }

    @Test
    @DisplayName("Updates the users seat - no seat available - throws exception")
    public void testUpdateSeatForUserNoNewSeatAvailable() throws Exception {
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        Seat seat = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat));

        TicketResponseDTO response = ticketService.createTicket(request);

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> {
                    ticketService.updateUserSeat(response.getTicketId());
                });
    }

    @Test
    @DisplayName("Updates the users seat")
    public void testUpdateSeatForUser() throws Exception {
        TicketRequestDTO request = TestUtils.createTicketRequest("Himanshu", "Pathak", "him@pathak.com", "London", "France");

        Seat seat = Seat.builder()
                .section("A")
                .seatNumber(10)
                .build();
        Seat newSeat = Seat.builder()
                .section("B")
                .seatNumber(5)
                .build();

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(seat));

        TicketResponseDTO response = ticketService.createTicket(request);

        when(seatsService.getSeat("him@pathak.com")).thenReturn(Optional.of(newSeat));

        TicketResponseDTO updatedTicket = ticketService.updateUserSeat(response.getTicketId());
        assertEquals(newSeat.getSeatNumber(), updatedTicket.getSeat().getSeatNumber());
        assertEquals(newSeat.getSection()+"-"+newSeat.getSeatNumber(), updatedTicket.getSeatNumber());
    }

}
