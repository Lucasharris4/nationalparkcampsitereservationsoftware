package com.techelevator;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		List <Campground> campgrounds = new ArrayList <Campground>();
		String sqlGetAllCampgrounds = "SELECT name, campground_id, park_id, open_from_mm, open_to_mm, daily_fee " +
										"FROM campground ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds);
		while (results.next()) {
			Campground theCampground = new Campground();
			
			theCampground.setName(results.getString("name"));
			theCampground.setParkName(results.getString("name"));
			theCampground.setPark_id(results.getLong("park_id"));
			theCampground.setCampground_id(results.getLong("campground_id"));
			theCampground.setOpen_from_mm(results.getString("open_from_mm"));
			theCampground.setOpen_to_mm(results.getString("open_to_mm"));
			theCampground.setDaily_fee(results.getDouble("daily_fee"));
			
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}

	@Override
	public Campground getCampgroundFromUser(long campgroundId) {
		Campground theCampground = new Campground();
		String sqlGetCampground = "SELECT name, campground_id, park_id, open_from_mm, open_to_mm, daily_fee " +
									"FROM campground " +
									"WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampground, campgroundId);
		results.next();
		
		theCampground.setName(results.getString("name"));
		theCampground.setPark_id(results.getLong("park_id"));
		theCampground.setCampground_id(results.getLong("campground_id"));
		theCampground.setOpen_from_mm(results.getString("open_from_mm"));
		theCampground.setOpen_to_mm(results.getString("open_to_mm"));
		theCampground.setDaily_fee(results.getDouble("daily_fee"));
		
		return theCampground;
	}

}
