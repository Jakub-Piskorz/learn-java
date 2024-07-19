package com.example.db;

public class User {

  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;
  private String quote;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public void setFirstName(String name) {
    this.firstName = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}