package com.example.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String slug;
    private String name;
    private String description;

    protected Game() {
    }

    public Game(String slug, String name, String description) {
        this.slug = slug;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, slug='%s', name='%s', description='%s']", id, slug, name, description);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() { return slug; }
}