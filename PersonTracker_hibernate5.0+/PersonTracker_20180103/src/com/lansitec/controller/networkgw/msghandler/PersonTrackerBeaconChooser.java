package com.lansitec.controller.networkgw.msghandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lansitec.dao.beans.Beacons;

public class PersonTrackerBeaconChooser {
	List<PersonTrackerBeanconNode> beanconList = null;
	List<PersonTrackerBeanconNode> chooseList  = null;
	
	public PersonTrackerBeaconChooser(){
		beanconList = new ArrayList<PersonTrackerBeanconNode>();
		chooseList = new ArrayList<PersonTrackerBeanconNode>();
	}
	
	public PersonTrackerBeaconChooser addBeacon(Beacons beacons,int rssi){
		beanconList.add(new PersonTrackerBeanconNode(beacons, rssi));
		return this;
	}
	
	@SuppressWarnings("unused")
	private void sortBeacon() {
		Collections.sort(beanconList, new Comparator<PersonTrackerBeanconNode>() {

			@Override
			public int compare(PersonTrackerBeanconNode a, PersonTrackerBeanconNode b) {
				int one = a.getRssi();
				int two = a.getRssi();
				//Ωµ–Ú≈≈–Ú
            	return new Integer(two).compareTo(new Integer(one));
            	//…˝–Ú≈≈–Ú
            	//return new Integer(one).compareTo(new Integer(two));
			}
		});
	}

}
