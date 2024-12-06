package com.example.cs_5520_final.model;

/**
 * Pet Model class that creates a pet object with its attributes
 */
public class PetModel {
    private String type;
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String color;
    private String furLength;
    private int vaccinated;
    private String state;
    private String description;

    /**
     * Constructor for the pet model
     * @param type pet type
     * @param name pet name
     * @param age pet age
     * @param breed pet breed
     * @param gender pet gender (M/F)
     * @param color pet color
     * @param furLength pet fur length
     * @param vaccinated vaccine status for pet
     * @param state location of pet
     * @param description description of the pet from adoption service
     */
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