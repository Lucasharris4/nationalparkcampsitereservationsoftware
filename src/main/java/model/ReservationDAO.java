package model;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.Site;

public interface ReservationDAO {
	public Long makeReservation(Long siteId, String name, LocalDate fromDate, LocalDate toDate);
	public List availableReservations(long campgroundId, LocalDate fromDate, LocalDate toDate);
	public void searchForExistingReservation(Long reservationId);
	public List<Site> availableReservations(long campgroundId, LocalDate fromDate, LocalDate toDate,
			long peopleInYourParty, boolean needsAccessible, boolean utilityHookup, long rvLengthNeeded);
}
