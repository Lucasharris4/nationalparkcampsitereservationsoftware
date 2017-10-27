package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.view.DollarAmount;

import model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void getAvailableSites() {
		
	}

	@Override
	public List<Site> siteInformation(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List <Site> sites = new ArrayList<Site>();
		String sqlGetSiteInformation = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
				  					  "FROM site " +
				 					  "WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteInformation, campgroundId);
		while (results.next()) {
			Site theSite = new Site();
			theSite.setSite_id(results.getLong("site_id"));
			theSite.setSite_number(results.getLong("site_number"));
			theSite.setCampground_id(results.getLong("campground_id"));
			theSite.setMax_occupancy(results.getLong("max_occupancy"));
			theSite.setAccessible(results.getBoolean("accessible"));
			theSite.setRv_length(results.getLong("max_rv_length"));
			theSite.setUtilities(results.getBoolean("utilities"));
			sites.add(theSite);
		}
	
		return sites;
	}
	
	@Override
	 public void printSitesInCampground(List<Site> campSites, String dailyFee, int duration) {
		if (campSites.isEmpty()) {
			System.out.println("There are no available sites");
			System.out.println("Please choose a different park or a different date for your trip");
		} else {
			DollarAmount dollarAmount = new DollarAmount(0);
			dollarAmount = dollarAmount.stringToDollarAmount(dailyFee);
			int totalCost = dollarAmount.getTotalAmountInCents() * duration;
			DollarAmount totalCostInCents = new DollarAmount(totalCost);
			List <Site> sites = new ArrayList <Site>();
			sites = campSites;
			System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%s", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost"));
			for (Site site : sites) {
				System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%s", site.getSite_number(), site.getMax_occupancy(), site.isAccessible(), site.getRv_length(), site.isUtilities(), totalCostInCents.toString()));
			}
		}
	}

	@Override
	public Site getSite(long siteNumber, long campgroundId) {
		Site theSite = new Site();
		String sqlGetSite = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
							"FROM site " +
							"WHERE site_number = ? AND campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSite, siteNumber, campgroundId);
		
		results.next();
		theSite.setSite_id(results.getLong("site_id"));
		theSite.setSite_number(results.getLong("site_number"));
		theSite.setCampground_id(results.getLong("campground_id"));
		theSite.setMax_occupancy(results.getLong("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setRv_length(results.getLong("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		
		return theSite;
	}

}
