package com.supinfo.supcardealer.entities;

import java.io.Serializable;

public class Car implements Serializable {

	private static final long serialVersionUID = 636366443370096539L;
	
	private long id;
	private String name;
	private int year;
	private Category category;
	private User owner;
	private int seats;
	private String baggage;
	private int doors;
	private String gearbox;
	private boolean conditionalAir;
	private float kilometers;
	private float pricePerDay;
	private String imageUrl;

	public Car() { }
	
	public Car(String name, int year, Category category, User owner, int seats, String baggage, int doors, 
				String gearbox, boolean conditionnalAir, float kilometers, float pricePerDay, String imageUrl ) { 
		this.name = name;
		this.year = year;
		this.category = category;
		this.owner = owner;
		this.seats = seats;
		this.baggage = baggage;
		this.doors = doors;
		this.gearbox = gearbox;
		this.conditionalAir = conditionnalAir;
		this.kilometers = kilometers;
		this.pricePerDay = pricePerDay;
		this.imageUrl = imageUrl;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public String getBaggage() {
		return baggage;
	}

	public void setBaggage(String baggage) {
		this.baggage = baggage;
	}

	public int getDoors() {
		return doors;
	}

	public void setDoors(int doors) {
		this.doors = doors;
	}

	public String getGearbox() {
		return gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}

	public boolean isConditionalAir() {
		return conditionalAir;
	}

	public void setConditionalAir(boolean conditionalAir) {
		this.conditionalAir = conditionalAir;
	}

	public float getKilometers() {
		return kilometers;
	}

	public void setKilometers(float kilometers) {
		this.kilometers = kilometers;
	}

	public float getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(float pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getImageUrl() {
		if(this.imageUrl == null) return "default.jpg";
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
