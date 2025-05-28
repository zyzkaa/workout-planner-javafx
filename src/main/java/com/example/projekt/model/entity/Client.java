package com.example.projekt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue
    private int id;
    @NonNull
    private String name;

    public Client(@NonNull String name) {
        this.name = name;
    }

}
