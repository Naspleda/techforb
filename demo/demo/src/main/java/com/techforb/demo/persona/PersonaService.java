package com.techforb.demo.persona;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PersonaService {
    public List<Persona> getPersonas(){
        return List.of(
                new Persona(
                        1000L,
                        "chris",
                        "123412341234",
                        700000,
                        "1234"
                )

        );
    }
}
