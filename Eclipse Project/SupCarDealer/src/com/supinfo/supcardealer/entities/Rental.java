package com.supinfo.supcardealer.entities;

import java.io.Serializable;
import java.util.Date;

public class Rental implements Serializable {
	
	private static final long serialVersionUID = -5546981130301989187L;
	
	private long id;
	private long rentedCarId;
	private long renterId;
	private String startDate;
	private String endDate;
	private float price;
	
	public Rental() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRentedCarId() {
		return rentedCarId;
	}
	public void setRentedCarId(long rentedCarId) {
		this.rentedCarId = rentedCarId;
	}
	public long getRenterId() {
		return renterId;
	}
	public void setRenterId(long renterId) {
		this.renterId = renterId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
