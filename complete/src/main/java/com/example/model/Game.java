package com.example.model;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug;
    private String name;
    private String description;
    private String publisher;

    protected Game() {
    }

    public Game(String slug, String name, String description, String publisher) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.publisher = publisher;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}