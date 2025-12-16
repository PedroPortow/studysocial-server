package com.example.server.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record UpdateSocietyDto(

        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        @JsonProperty("name")
        String name,

        @Size(max = 255, message = "A descrição não pode exceder 255 caracteres")
        @JsonProperty("description")
        String description
) {}