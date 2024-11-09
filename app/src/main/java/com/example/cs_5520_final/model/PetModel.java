package com.example.cs_5520_final.model;

public class PetModel {
    private String type;          // Type of the pet (e.g., Dog, Cat)
    private String name;          // Pet's name
    private int age;              // Pet's age
    private String breed;         // Pet's breed
    private String gender;        // Pet's gender
    private String color;         // Pet's color
    private String furLength;     // Pet's fur length
    private int vaccinated;   // Whether the pet is vaccinated (true/false)
    private String state;         // Location state
    private String description;   // Description of the pet

    // Constructor
    public PetModel(String type, String name, int age, String breed, String gender,
                    String color, String furLength, int vaccinated,
                    String state, String description) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.gender = gender;
        this.color = color;
        this.furLength = furLength;
        this.vaccinated = vaccinated;
        this.state = state;
        this.description = description;
    }

    public String getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public String getBreed() {
        return breed;
    }


    public String getGender() {
        return gender;
    }


    public String getColor() {
        return color;
    }

    public String getFurLength() {
        return furLength;
    }


    public int getVaccinated() {
        return vaccinated;
    }


    public String getState() {
        return state;
    }


    public String getDescription() {
        return description;
    }
}