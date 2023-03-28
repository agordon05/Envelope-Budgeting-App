package dataAccess;

import java.util.ArrayList;

import dataObjects.Vendor;

public class VendorAccess {

	private static ArrayList<Vendor> vendors;
	
	public static void Initialize() {
		vendors = new ArrayList<Vendor>();
	}
	
	public static ArrayList<Vendor> getVendors(){
		return vendors;
	}
	
	public static Vendor getVendorByName(String name) {
		for(int index = 0; index < vendors.size(); index++) {
			if(vendors.get(index).getName().equals(name)) return vendors.get(index);
		}
		return null;
	}
	
	public static Vendor addVendor(Vendor vendor) {
		Vendor temp = new Vendor(vendor.getName(), vendor.getPrefName(), vendor.getPrefEnvelope());
		vendors.add(temp);
		return temp;
	}
	
	
}
