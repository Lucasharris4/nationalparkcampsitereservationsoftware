package com.techelevator;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		List <Park> parks = new ArrayList <Park>();
		
		String sqlAllParks = "SELECT name " +
							"FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllParks);
		while (results.next()) {
			Park thePark = new Park();
			thePark.setName(results.getString("name"));
			parks.add(thePark);
		}
		return parks;
	}

	@Override
	public String displayParkInformation(String parkName) {
		
		String sqlParkInformation = "SELECT name, park_id, location, establish_date, area, visitors, description " +
									"FROM park " +
									"WHERE name = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlParkInformation, parkName);
		results.next();
		System.out.println();
		System.out.println(results.getString("name") + " National Park\n");
		System.out.println(String.format("%s%19s", "Location: ", results.getString("location")));
		System.out.println(String.format("%s%22s", "Established: ", results.getDate("establish_date")));
		System.out.println(String.format("%s%30s", "Area: ", results.getLong("area") +" sq km"));
		System.out.println(String.format("%s%15s", "Annual Visitors: ", results.getLong("visitors")));
		System.out.println();
		
		System.out.println(String.format("%s", results.getString("description")).replaceAll(".{35}(?= )", "$0\n"));
	
		return results.getString("name");
	}


	@Override
	public List <Campground> getCampgrounds(String parkName) {
		List <Campground> campgrounds = new ArrayList <Campground>();
		
		String sqlDiplayCampgrounds = "SELECT c.name as campground_name, c.campground_id, p.name, p.park_id, c.open_from_mm, c.open_to_mm, c.daily_fee " +
									"FROM park p "  +
									"JOIN campground c ON c.park_id = p.park_id " +
									"WHERE p.name = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlDiplayCampgrounds, parkName);
		while(results.next()) {
			Campground theCampground = new Campground();
			theCampground.setName(results.getString("campground_name"));
			theCampground.setParkName(results.getString("name"));
			theCampground.setPark_id(results.getLong("park_id"));
			theCampground.setCampground_id(results.getLong("campground_id"));
			theCampground.setOpen_from_mm(results.getString("open_from_mm"));
			theCampground.setOpen_to_mm(results.getString("open_to_mm"));
			theCampground.setDaily_fee(results.getDouble("daily_fee"));
			
			campgrounds.add(theCampground);
			
		}
		printCampgrounds(campgrounds);
		return campgrounds;
	}

	private void printCampgrounds(List<Campground> camps) {
		
		System.out.println(camps.get(0).getParkName());
		System.out.println();
		System.out.println(String.format("%11s%24s%19s%22s", "Name", "Open", "Close", "Daily Fee"));
		for (Campground campground : camps) {
			if (campground.getName().length() > 30) {
				String firstLine = campground.getName().substring(0, 12);
				String secondLine = campground.getName().substring(12);
				System.out.println(String.format("%s%-6s%-24s%-18s%-18s%s", "#", campground.getCampground_id(), firstLine, campground.getOpen_from_mm(), campground.getOpen_to_mm(), campground.getDaily_fee()));	
				System.out.println("       " + secondLine);
			} else {
			System.out.println(String.format("%s%-6s%-24s%-18s%-18s%s", "#", campground.getCampground_id(), campground.getName(), campground.getOpen_from_mm(), campground.getOpen_to_mm(), campground.getDaily_fee()));
			}
//			+ id + campground.getName() + campground.getOpen_from_mm() + campground.getOpen_to_mm() + campground.getDaily_fee())
		}
	}

	@Override
	public void displayAvailableReservations() {
		// TODO Auto-generated method stub
		
	}

}
