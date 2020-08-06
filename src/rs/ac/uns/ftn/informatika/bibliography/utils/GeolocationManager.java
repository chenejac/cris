package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.io.File;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

public class GeolocationManager {

	  
	public static File data;
	
//	public static void main(String[] args) {
//		  GeolocationManager obj = new GeolocationManager();
//		ClientLocation location = obj.getLocation("10.1.211.135, 147.91.175.235");
//		System.out.println(location);
//	  }
	

	  public static ClientLocation getLocation(String ipAddress) {
		if(data == null){
			data = new File("/opt/cris/GeoLiteCity.dat");
		}
		if((ipAddress != null) && (ipAddress.contains(",")))
			ipAddress = ipAddress.substring(ipAddress.indexOf(",") + 1).trim();
		return getLocation(ipAddress, data);

	  }

	  private static ClientLocation getLocation(String ipAddress, File file) {

		ClientLocation clientLocation = null;

		try {
		clientLocation = new ClientLocation();

		LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
		Location locationServices = lookup.getLocation(ipAddress);

		clientLocation.setCountryCode(locationServices.countryCode);
		clientLocation.setCountryName(locationServices.countryName);
		clientLocation.setRegion(locationServices.region);
		clientLocation.setRegionName(regionName.regionNameByCode(
	             locationServices.countryCode, locationServices.region));
		clientLocation.setCity(locationServices.city);
		clientLocation.setPostalCode(locationServices.postalCode);
		clientLocation.setLatitude(String.valueOf(locationServices.latitude));
		clientLocation.setLongitude(String.valueOf(locationServices.longitude));

		} catch (Throwable e) {
//			System.out.println(e.getMessage());
		}

		return clientLocation;

	  }
	
}
