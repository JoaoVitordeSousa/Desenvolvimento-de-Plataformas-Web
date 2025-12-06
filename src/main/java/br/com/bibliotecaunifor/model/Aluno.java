package br.com.bibliotecaunifor.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario {
    
}
