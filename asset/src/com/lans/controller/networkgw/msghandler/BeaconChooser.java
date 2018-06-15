package com.lans.controller.networkgw.msghandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.lans.dao.beans.Beacons;


public class BeaconChooser {
	List<BeaconNode> beaconList = null;
	List<BeaconNode> chooseList = null;
	
	public BeaconChooser() {
		beaconList = new ArrayList<BeaconNode>();
		chooseList = new ArrayList<BeaconNode>();
	}
	
	public BeaconChooser addBeacon(Beacons beacon, int rssi) {
		beaconList.add(new BeaconNode(beacon, rssi));
		return this;
	}
	
	private void sortBeacon() {
		Collections.sort(beaconList, new Comparator<BeaconNode>() {
            public int compare(BeaconNode a, BeaconNode b) {
            	int one = a.getRssi();
            	int two = b.getRssi();
            	return new Integer(two).compareTo(new Integer(one));
            }
        });
	}
	
	public List<BeaconNode> find3NodesOfFloor(String floor) {
		for (BeaconNode node : beaconList) {
			if (node.getBeacon().getFlor().equals(floor)) {
				chooseList.add(node);
				
				if (chooseList.size() == 5) {
					break;
				}
			}
		}
		
		return chooseList;
	}
	
	public List<BeaconNode> choose() {
		chooseList.clear();

		//sort beacon based on RSSI
		sortBeacon();
		
		//if any of outdoor beacon scanned, use it only
		for (BeaconNode node : beaconList) {
			if (node.getBeacon().getPostype().equals(" “Õ‚")) {
				chooseList.add(node);
				return chooseList;
			}
		}
		
		//then all beacons must be indoor type
		//if less than 3 beacons just use all
		if (beaconList.size() <= 3) {
			chooseList.addAll(beaconList);
			return chooseList;
		}
		
		//if any middle floor beacon, use it only
		
		for (int idx = 0; idx < 3; idx++) {
			BeaconNode node = beaconList.get(idx);
			if (node.isMidFloor()) {
				chooseList.add(node);
				return chooseList;
			}
		}
		
		//select 3 indoor beacon of the same floor
		String node0Floor = beaconList.get(0).getBeacon().getFlor();
		String node1Floor = beaconList.get(1).getBeacon().getFlor();
		String node2Floor = beaconList.get(2).getBeacon().getFlor();
		if (node0Floor.equals(node1Floor) || node0Floor.equals(node2Floor)) {
			return find3NodesOfFloor(node0Floor);
		} else if (node1Floor.equals(node2Floor)) {
			return find3NodesOfFloor(node1Floor);
		}
		
		//if all beacons are not the same floor, use the one with highest power
		chooseList.add(beaconList.get(0));
		return chooseList;
	}
}
