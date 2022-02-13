package com.devsuperior.dsclient.tests;

import com.devsuperior.dsclient.model.Client;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Factory {

    public static Client createClient() {
        Client client =
                new Client(
                        1l,"Lebron","50128908722",
                        new BigDecimal(99000), LocalDate.of(1960,5,2),
                        3,LocalDate.now(), null);
        return client;
    }

}
