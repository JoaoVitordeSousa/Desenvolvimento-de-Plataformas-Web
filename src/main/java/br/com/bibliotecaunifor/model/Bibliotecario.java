package br.com.bibliotecaunifor.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BIBLIOTECARIO")
public class Bibliotecario extends Usuario {
    
}
