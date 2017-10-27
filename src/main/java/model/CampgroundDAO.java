package model;

import java.util.List;

import com.techelevator.Campground;

public interface CampgroundDAO {
	public List <Campground> getAllCampgrounds();
	public Campground getCampgroundFromUser(long campgroundId);
	
}
