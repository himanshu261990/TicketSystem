package com.cloudbees.ticket.service;

import com.cloudbees.ticket.cache.SeatsCache;
import com.cloudbees.ticket.model.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author hpathak
 */
@Service
public class SeatsService {

    /**
     * Gets the fare based on from and to
     * @param from
     * @param to
     * @return
     */
    public int getFare(String from, String to) {
        String journeyCode = from.toUpperCase().concat("-").concat(to.toUpperCase());
        if(SeatsCache.farePrice.get(journeyCode) != null) {
            return SeatsCache.farePrice.get(journeyCode);
        }
        return 0;
    }

    /**
     * Gets the seat based on email address
     * Added a logic to determine the section number based on the email length
     * If all the seats are already occupied in one section, check for seats in another section
     * If no seats are available, return empty
     * @param emailAddress
     * @return
     */
    public Optional<Seat> getSeat(String emailAddress) {
        if(SeatsCache.seatsList.size() > 0) {
            String section = emailAddress.length() % 2 == 0 ? "A" : "B";
            Optional<Seat> seat = SeatsCache.seatsList.parallelStream()
                    .filter(seats -> seats.getSection().equals(section)).findFirst();
            // Let's say if the section A or B seats get over,
            // we need to check if there is any seat left for in other section
            if (seat.isPresent()) {
                return seat;
            }
            String otherSection = section.equals("A") ? "B" : "A";
            return SeatsCache.seatsList.parallelStream()
                    .filter(seats -> seats.getSection().equals(otherSection)).findFirst();
        }
        return Optional.empty();

    }

    /**
     * Confirm the seat by removing it from the seat lis
     * @param seatId
     * @return
     */
    public boolean confirmSeat(int seatId){
        Iterator iterator = SeatsCache.seatsList.iterator();
        while (iterator.hasNext()){
            Seat seat = (Seat) iterator.next();
            if (seat.getId() == seatId) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Puts back a seat if it got free
     * @param seat
     */
    public void updateSeatToVacant(Seat seat){
        SeatsCache.seatsList.add(seat);
    }

    /**
     * Get the available number of seats
     * @return
     */
    public long getAvailableSeats() {
        return SeatsCache.seatsList.size();
    }

    /**
     * Gets the available number of seats for a section
     * @param section
     * @return
     */
    public long getAvailableSeatsForASection(String section) {
        return SeatsCache.seatsList.stream().filter(seat -> seat.getSection().equals(section)).count();
    }
}
