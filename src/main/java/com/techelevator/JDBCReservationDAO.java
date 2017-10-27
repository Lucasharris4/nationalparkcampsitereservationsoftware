package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.text.Utilities;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Long makeReservation(Long siteId, String name, LocalDate fromDate, LocalDate toDate) {
		long reservationId = getNextReservationId();
		String sqlMakeReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, reservation_id) " +
									"VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlMakeReservation, siteId, name, fromDate, toDate, reservationId);
		
		return reservationId;
	}

	@Override
	public List<Site> availableReservations(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List <Site> availableSites = new ArrayList<Site>();
		
		boolean validDate = checkForInvalidDate(fromDate, toDate);
		boolean isOpen = checkForOffSeason(campgroundId, fromDate, toDate);
			
			if (isOpen && validDate) {
				String sqlGetAvailableSites = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
						 "FROM site s " +
						 "WHERE campground_id = ? AND s.site_id NOT IN (SELECT r.site_id " +
						 								 "FROM reservation r " +
						 								 "JOIN site s ON r.site_id = s.site_id " +
						 								 "WHERE from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
						 								 "LIMIT 5";

				SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, fromDate, toDate,
						fromDate, toDate);

				while (results.next()) {
					Site theSite = new Site();
					theSite.setSite_id(results.getLong("site_id"));
					theSite.setCampground_id(results.getLong("campground_id"));
					theSite.setSite_number(results.getLong("site_number"));
					theSite.setMax_occupancy(results.getLong("max_occupancy"));
					theSite.setAccessible(results.getBoolean("accessible"));
					theSite.setRv_length(results.getLong("max_rv_length"));
					theSite.setUtilities(results.getBoolean("utilities"));

					availableSites.add(theSite);
				}
			}
			return availableSites;
	}
	
	@Override
	public List<Site> availableReservations(long campgroundId, LocalDate fromDate, LocalDate toDate,
			long peopleInYourParty, boolean needsAccessible, boolean utilityHookup, long rvLengthNeeded) {
		List <Site> availableSites = new ArrayList<Site>();
		
		boolean validDate = checkForInvalidDate(fromDate, toDate);
		boolean isOpen = checkForOffSeason(campgroundId, fromDate, toDate);
		String sqlGetAvailableSites = null;
		SqlRowSet results = null;
		
			
			if (isOpen && validDate && utilityHookup && needsAccessible) {
				sqlGetAvailableSites = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
						 					  "FROM site s " +
						 					  "WHERE campground_id = ? AND max_occupancy >= ? AND accessible = ? AND utilities = ? AND max_rv_length >= ? AND s.site_id NOT IN " +
						 								 "(SELECT r.site_id " +
						 								 "FROM reservation r " +
						 								 "JOIN site s ON r.site_id = s.site_id " +
						 								 "WHERE from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
						 								 "LIMIT 5";
				 results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, peopleInYourParty, needsAccessible, utilityHookup, rvLengthNeeded, fromDate, toDate,
						fromDate, toDate);
			} else if (isOpen && validDate && utilityHookup) {
				sqlGetAvailableSites = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
						 					  "FROM site s " +
						 					  "WHERE campground_id = ? AND max_occupancy >= ? AND utilities = ? AND max_rv_length >= ? AND s.site_id NOT IN " +
						 								 "(SELECT r.site_id " +
						 								 "FROM reservation r " +
						 								 "JOIN site s ON r.site_id = s.site_id " +
						 								 "WHERE from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
						 								 "LIMIT 5";
				results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, peopleInYourParty, utilityHookup, rvLengthNeeded, fromDate, toDate,
						fromDate, toDate);
			} else if (isOpen && validDate && needsAccessible) {
				sqlGetAvailableSites = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
						 					  "FROM site s " +
						 					  "WHERE campground_id = ? AND max_occupancy >= ? AND accessible = ? AND max_rv_length >= ? AND s.site_id NOT IN " +
						 								 "(SELECT r.site_id " +
						 								 "FROM reservation r " +
						 								 "JOIN site s ON r.site_id = s.site_id " +
						 								 "WHERE from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
						 								 "LIMIT 5";
				results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, peopleInYourParty, needsAccessible, rvLengthNeeded, fromDate, toDate,
						fromDate, toDate);
			} else {
				sqlGetAvailableSites = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
						 					  "FROM site s " +
						 					  "WHERE campground_id = ? AND max_occupancy >= ? AND max_rv_length >= ? AND s.site_id NOT IN " +
						 								 "(SELECT r.site_id " +
						 								 "FROM reservation r " +
						 								 "JOIN site s ON r.site_id = s.site_id " +
						 								 "WHERE from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
						 								 "LIMIT 5";
				results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, peopleInYourParty, rvLengthNeeded, fromDate, toDate,
						 fromDate, toDate);
			}
			
			if (sqlGetAvailableSites != null) {

				while (results.next()) {
					Site theSite = new Site();
					theSite.setSite_id(results.getLong("site_id"));
					theSite.setCampground_id(results.getLong("campground_id"));
					theSite.setSite_number(results.getLong("site_number"));
					theSite.setMax_occupancy(results.getLong("max_occupancy"));
					theSite.setAccessible(results.getBoolean("accessible"));
					theSite.setRv_length(results.getLong("max_rv_length"));
					theSite.setUtilities(results.getBoolean("utilities"));

					availableSites.add(theSite);
				}
			}		
			
			return availableSites;
	}
	
	private boolean checkForInvalidDate(LocalDate fromDate, LocalDate toDate) {
		if (fromDate.isAfter(toDate)) {
			System.out.println("Look pal, if you're not going to take this thing seriously, maybe you just shouldn't go camping...");
			return false;
		} else if (fromDate.isBefore(LocalDate.now())){
			System.out.println("Nice try Marty McFly!");
			System.out.println();
			return false;
		} else {
			return true;
		}
	}


	private boolean checkForOffSeason(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		String sqlGetOffSeasonMonths = "SELECT open_from_mm, open_to_mm " +
										"FROM campground " +
										"WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetOffSeasonMonths, campgroundId);
		results.next();
		int open_from = Integer.parseInt(results.getString("open_from_mm"));
		int open_to = Integer.parseInt(results.getString("open_to_mm"));
		
		if (fromDate.getMonth().getValue() < open_from || fromDate.getMonth().getValue() > open_to || toDate.getMonth().getValue() < open_from || toDate.getMonth().getValue() > open_to) {
			return false;
		} else {
			return true;
		}									
	}

	@Override
	public void searchForExistingReservation(Long reservationId) {
		Reservation theReservation = new Reservation();
		String sqlSearchForExistingReservation = "SELECT site_id, name, from_date, to_date " +
												"FROM reservation " +
												"WHERE reservation_id = ? ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForExistingReservation, reservationId);
		
		while (results.next()) {
			theReservation.setName(results.getString("name"));
			theReservation.setSite_id(results.getLong("site_id"));
			theReservation.setFrom_date(results.getDate("from_date").toLocalDate());
			theReservation.setTo_date(results.getDate("to_date").toLocalDate());
			theReservation.setReservation_id(reservationId);
			
		}
		
		printReservationInfo(theReservation);
	}

	private void printReservationInfo(Reservation theReservation) {
		if (theReservation.getName() == null) {
			System.out.println("Sorry that reservation ID is invalid!");
		} else {
			System.out.println("Reservation ID: " + theReservation.getReservation_id());
			System.out.println("Booked under name : " + theReservation.getName());
			System.out.println("From : " + theReservation.getFrom_date() + " Until : " + theReservation.getTo_date());
			System.out.println("At site ID: " + theReservation.getSite_id());
		}
		
	}
	private Long getNextReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new reservation");
		}
	}
}
