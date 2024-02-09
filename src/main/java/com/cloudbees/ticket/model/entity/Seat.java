package com.cloudbees.ticket.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author hpathak
 */
@Builder
@Data
public class Seat {

    private int id;
    private String section;
    private int seatNumber;

}
