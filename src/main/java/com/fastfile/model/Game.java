package com.fastfile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug;
    private String name;
    private String description;
    private String publisher;

    @Override
    public String toString() {
        return String.format("User[id=%d, slug='%s', name='%s', description='%s']", id, slug, name, description);
    }

}