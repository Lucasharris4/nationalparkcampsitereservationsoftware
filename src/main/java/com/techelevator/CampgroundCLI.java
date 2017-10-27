package com.techelevator;




import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

import model.CampgroundDAO;
import model.ParkDAO;
import model.ReservationDAO;
import model.SiteDAO;

public class CampgroundCLI {	
	
	private static final String PARK_MENU_OPTION_ACADIA = "Acadia";
	private static final String PARK_MENU_ARCHES = "Arches";
	private static final String PARK_MENU_OPTION_CUYAHOGA_VALLEY = "Cuyahoga Valley";
	private static final String PARK_MENU_OPTION_QUIT = "Quit";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ACADIA, 
																	 PARK_MENU_ARCHES,
																	 PARK_MENU_OPTION_CUYAHOGA_VALLEY,
																	 PARK_MENU_OPTION_QUIT };
	
	private static final String CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS,
																		   CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION,
																		   CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	
	
	private static final String SELECT_A_COMMAND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search For Available Reservation";
	private static final String SELECT_A_COMMAND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Menu";
	private static final String SELECT_A_COMMAND_MENU_OPTION_ADVANCED_SEARCH = "Advanced Search";
	private static final String[] SELECT_A_COMMAND_MENU_OPTIONS = new String[] {SELECT_A_COMMAND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION,
																				SELECT_A_COMMAND_MENU_OPTION_ADVANCED_SEARCH,
																				SELECT_A_COMMAND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	
	private static final String YES_FINAL_MENU_OPTION = "Yes";
	private static final String NO_FINAL_MENU_OPTION = "No";
	private static final String[] FINAL_MENU_OPTIONS = new String[] {YES_FINAL_MENU_OPTION, 
																	NO_FINAL_MENU_OPTION };
	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		
		
	}
	
	public void run() {
		while(true) {
			printHeading("View Parks Interface");
			String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
			if (!choice.equals(PARK_MENU_OPTION_QUIT)) {
				handlePark(choice);
			} else {
				System.out.println("Goodbye");
				System.exit(0);
			}
		}
	}

	private void handlePark(String choice) {
		String parkName = parkDAO.displayParkInformation(choice);	
		choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			handleListAllCampgrounds(parkName);
		} else if (choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			System.out.println("Please enter your reservation ID");
			long userInput = menu.getLongFromUser();
			reservationDAO.searchForExistingReservation(userInput);
		}
		System.out.println();

	}

	private void handleListAllCampgrounds(String parkName) {
		System.out.println();
		printHeading("Park Campgrounds");
		parkDAO.getCampgrounds(parkName);
		System.out.println();
		String choice = (String)menu.getChoiceFromOptions(SELECT_A_COMMAND_MENU_OPTIONS);
		if (choice.equals(SELECT_A_COMMAND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			handleSearchForReservation();
		} else if (choice.equals(SELECT_A_COMMAND_MENU_OPTION_ADVANCED_SEARCH)) {
			handleAdvancedSearch();
		}
		System.out.println();
	}

	private void handleAdvancedSearch() {
		Campground campground = new Campground();
		campground = getCampground();
		try {
			System.out.println("How many people in your party?");
			long peopleInYourParty = (long) Integer.parseInt(menu.getStringFromUser());
			System.out.println("Do you need wheelchair accessibility?");
			boolean needsAccessible = menu.getStringFromUser().equalsIgnoreCase("y") ? true : false;
			System.out.println("Do you need a utility hookup?");
			boolean utilityHookup = menu.getStringFromUser().equalsIgnoreCase("y") ? true : false;
			System.out.println("If you have a RV, please enter the length in ft, if not please enter 0");
			long rvLengthNeeded = (long) Integer.parseInt(menu.getStringFromUser());
			if (campground.getName() != null) {
				LocalDate fromDate = getFromDate();
				LocalDate toDate = getToDate();
				List <Site> availableSites = getSitesAdvanced(campground.getCampground_id(), fromDate, toDate, peopleInYourParty, needsAccessible, utilityHookup, rvLengthNeeded);
				siteDAO.printSitesInCampground(availableSites, campground.getDaily_fee(), toDate.getDayOfYear() - fromDate.getDayOfYear());
				System.out.println();
				if (!availableSites.isEmpty()) {
					bookReservation(availableSites, fromDate, toDate, campground);
				}
			}
		} catch (Exception e) {
			System.out.println("What you have entrered is an invalid entry sir/madam");
		}	
	}


	private void handleSearchForReservation() {
		Campground campground = getCampground();
		if (campground.getName() != null) {
			LocalDate fromDate = getFromDate();
			LocalDate toDate = getToDate();
			List <Site> availableSites = reservationDAO.availableReservations(campground.getCampground_id(), fromDate, toDate);
			siteDAO.printSitesInCampground(availableSites, campground.getDaily_fee(), toDate.getDayOfYear() - fromDate.getDayOfYear());
			System.out.println();
			if (!availableSites.isEmpty()) {
				bookReservation(availableSites, fromDate, toDate, campground);
			}
		}
	}

	private void bookReservation(List availableSites, LocalDate fromDate, LocalDate toDate, Campground campground) {
		System.out.println("Which site should be reserved (enter 0 to cancel)?");
		long siteNumber = menu.getLongFromUser();
		boolean siteIsAvailable = checkToSeeIfSiteIsAvailable(siteNumber, campground.getCampground_id(), availableSites);
		if (siteIsAvailable) {
			System.out.println("What name should the reservation be made under?");
			String nameForRes = menu.getStringFromUser();
			System.out.println();
			Site theSite = new Site();
			theSite = (Site) siteDAO.getSite(siteNumber, campground.getCampground_id());
			Long reservationId = reservationDAO.makeReservation(theSite.getSite_id(), nameForRes, fromDate, toDate);
			printConfirmation(reservationId);
			handleFinalMenu(nameForRes);
		} else {
			;
		}
	}

	private void handleFinalMenu(String nameForRes) {
		String choice = (String) menu.getChoiceFromOptions(FINAL_MENU_OPTIONS);
		if (choice == NO_FINAL_MENU_OPTION) {
			System.out.println("Thank you and enjoy your stay " + nameForRes);
			System.exit(0);
		}
	}

	private boolean checkToSeeIfSiteIsAvailable(long siteNumber, long campgroundId, List availableSites) {
		if (siteNumber != 0 && availableSites.contains(siteDAO.getSite(siteNumber, campgroundId))) {
			return true;
		} else if (siteNumber == 0) {
			return false;
		}
		System.out.println("Sorry that site is currently unavailable");
		return false;
	}

	private void printConfirmation(Long reservationId) {
		System.out.println("The reservation has been made and the confirmation id is " + reservationId);
		System.out.println("Will there be anything else?");
	}


	private List<Site> getSitesAdvanced(long campgroundId, LocalDate fromDate, LocalDate toDate, long peopleInYourParty,
			boolean needsAccessible, boolean utilityHookup, long rvLengthNeeded) {
		List <Site> availableSites = new ArrayList<Site>();
		availableSites = reservationDAO.availableReservations(campgroundId, fromDate, toDate, peopleInYourParty, needsAccessible, utilityHookup, rvLengthNeeded);
		return availableSites;
	}
	
	private LocalDate getToDate() {
		System.out.println("What is the departure date? ___/___/___ ");
		LocalDate toDate = menu.getDateFromUser();
		return toDate;
	}

	private LocalDate getFromDate() {
		System.out.println("What is the arrival date?  ___/___/___ ");
		LocalDate fromDate = menu.getDateFromUser();
		return fromDate;
	}

	private Campground getCampground() {
		Campground campground = new Campground();
		long campGroundId = 0;
		System.out.println("Which campground (enter 0 to cancel)? ___");
		try {
			campGroundId = menu.getLongFromUser();
			if (campGroundId != 0) {
				campground = campgroundDAO.getCampgroundFromUser(campGroundId);
			}
		} catch (Exception e) {
			System.out.println("*** What you have entered is not a valid option sir/madam ***");
		}
		return campground;
	}

	private void printHeading(String headingText) {
		System.out.println(headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
