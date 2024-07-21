package com.example.springboot;

public class Game {
    private String slug;
    private String name;
    private String description;

    public Game(){
        
    };
    
    public Game(String slug, String name, String description) {
        super();
        this.slug = slug;
        this.name = name;
        this.description = description;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
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
}
