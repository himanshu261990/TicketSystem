package com.cloudbees.ticket.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hpathak
 */
@Configuration
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketConfig {

    @Value("${ticket.totalNumberOfSections}")
    private int totalNumberOfSections;

    @Value("${ticket.totalNumberOfSeatsPerSection}")
    private int totalNumberOfSeatsPerSection;

}
