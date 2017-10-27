package model;

import java.util.List;

import com.techelevator.Campground;
import com.techelevator.Park;

public interface ParkDAO {
	public List <Park> getAllParks();
	public String displayParkInformation(String parkName);
	public List <Campground> getCampgrounds(String parkName);
	public void displayAvailableReservations();
}
