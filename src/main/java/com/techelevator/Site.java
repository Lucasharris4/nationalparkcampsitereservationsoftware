package com.techelevator;

public class Site {
	private long site_id;
	private long campground_id;
	private long site_number;
	private long max_occupancy;
	private boolean accessible;
	private long rv_length;
	private boolean utilities;
	
	public long getSite_id() {
		return site_id;
	}
	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}
	public long getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(long campground_id) {
		this.campground_id = campground_id;
	}
	public long getSite_number() {
		return site_number;
	}
	public void setSite_number(long site_number) {
		this.site_number = site_number;
	}
	public long getMax_occupancy() {
		return max_occupancy;
	}
	public void setMax_occupancy(long max_occupancy) {
		this.max_occupancy = max_occupancy;
	}
	public String isAccessible() {
		if (accessible) {
			return "Yes";
		}
			return "No";
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public long getRv_length() {
		return rv_length;
	}
	public void setRv_length(long rv_length) {
		this.rv_length = rv_length;
	}
	public String isUtilities() {
		if (utilities) {
			return "Yes";
		}
			return "N/A";		
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof Site)) {
			return false;
		} else {
			Site otherSite = (Site)other;
			return site_id == otherSite.site_id;
		}
	}
}
