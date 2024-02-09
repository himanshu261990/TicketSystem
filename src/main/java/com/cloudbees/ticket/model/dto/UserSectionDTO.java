package com.cloudbees.ticket.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hpathak
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSectionDTO {

    private String section;
    private String ticketId;
    private String seatNumber;
    private UserDTO user;

}
