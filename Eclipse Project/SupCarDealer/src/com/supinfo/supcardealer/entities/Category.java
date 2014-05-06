package com.supinfo.supcardealer.entities;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

	private static final long serialVersionUID = 4093006118817204420L;
	
	private long id;
	private String name;
	private List<Car> cars;
	
	public Category() {}
	
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
	public List<Car> getCars() {
		return cars;
	}
	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	
}
