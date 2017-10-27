package com.techelevator;

import java.time.LocalDate;

import com.techelevator.view.DollarAmount;

public class Campground {
	private long campground_id;
	private long park_id;
	private String name;
	private String open_from_mm;
	private String open_to_mm;
	private DollarAmount daily_fee;
	private String parkName;
	
	public long getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(long campground_id) {
		this.campground_id = campground_id;
	}
	public long getPark_id() {
		return park_id;
	}
	public void setPark_id(long park_id) {
		this.park_id = park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen_from_mm() {
		int monthNumber = Integer.parseInt(open_from_mm);
		String month = null;
		
		switch (monthNumber) {
		case 1 : month = "January";
		break;
		case 2 : month = "February";
		break;
		case 3: month = "March";
		break;
		case 4: month = "April";
		break;
		case 5: month = "May";
		break;
		case 6: month = "June";
		break;
		case 7: month = "July";
		break;
		case 8: month = "August";
		break;
		case 9: month = "September";
		break;
		case 10: month = "October";
		break;
		case 11: month = "November";
		break;
		case 12: month = "December";
		break;
		
		}
		return month;
	}
	public void setOpen_from_mm(String open_from_mm) {
		this.open_from_mm = open_from_mm;
	}
	public String getOpen_to_mm() {
		int monthNumber = Integer.parseInt(open_to_mm);
		String month = null;
		
		switch (monthNumber) {
		case 1 : month = "January";
		break;
		case 2 : month = "February";
		break;
		case 3: month = "March";
		break;
		case 4: month = "April";
		break;
		case 5: month = "May";
		break;
		case 6: month = "June";
		break;
		case 7: month = "July";
		break;
		case 8: month = "August";
		break;
		case 9: month = "September";
		break;
		case 10: month = "October";
		break;
		case 11: month = "November";
		break;
		case 12: month = "December";
		break;
		
		}
		return month;
	}
	public void setOpen_to_mm(String open_to_mm) {
		this.open_to_mm = open_from_mm;
	}
	public String getDaily_fee() {
		return daily_fee.toString();
	}
	public void setDaily_fee(double daily_fee) {
		int dollars = (int) daily_fee;
		int cents = (int) ((daily_fee - dollars) * 100);
		DollarAmount dollarAmount = new DollarAmount((int)((daily_fee * 100) + cents));
		this.daily_fee = dollarAmount;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String toString() {
		return this.name;
	}
	
	
}
