package com.cloudbees.ticket.cache;

import com.cloudbees.ticket.config.TicketConfig;
import com.cloudbees.ticket.model.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author hpathak
 */
@Component
//@AllArgsConstructor
public class SeatsCache {

    public static Map<String, Integer> farePrice = new HashMap<>();
    public static List<Seat> seatsList = new LinkedList<>();

    /**
     * Adds the fare data into farePrice map
     * Adds data into the seat list considering the configuration for the total number of sections and seats per section
     * @param ticketConfig
     */
    public SeatsCache(TicketConfig ticketConfig) {
        farePrice.put("LONDON-FRANCE", 20);
        int id = 1;
        for(int i=0; i<ticketConfig.getTotalNumberOfSections(); i++){
            String section = i == 0 ? "A" : "B";
            for(int j=1; j<=ticketConfig.getTotalNumberOfSeatsPerSection(); j++) {
                seatsList.add(Seat.builder()
                        .id(id++)
                        .section(section)
                        .seatNumber(j)
                        .build());
            }
        }
    }
}
