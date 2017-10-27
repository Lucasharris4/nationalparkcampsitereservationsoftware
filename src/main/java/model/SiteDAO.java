package model;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.Site;

public interface SiteDAO {
	public List <Site> siteInformation(long campgroundId, LocalDate fromDate, LocalDate toDate);
	public void getAvailableSites();
	public void printSitesInCampground(List<Site> campSites, String dailyFee, int duration);
	public Object getSite(long siteId, long campgroundId);
}
