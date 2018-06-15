package com.lans.controller.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lans.dao.BeaconsDAO;
import com.lans.dao.beans.Beacons;
import com.lans.infrastructure.controller.http.ErrorGen;
import com.lans.infrastructure.controller.http.RespGenerator;
import com.lans.infrastructure.controller.http.ResponseSender;

/**
 * Servlet implementation class BeaconController
 */
@WebServlet("/asset/beacon")
public class BeaconController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeaconController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oper = request.getParameter("oper");
		String usrname = request.getParameter("usrname");
		List<Beacons> list = null;

		if (oper != null) {
			doPost(request, response);
			return;
		}

		RespGenerator rsp = null;
		try {
			if (usrname != null) {
				list = BeaconsDAO.getBeaconByUsrname(usrname);				
			} 
			else
			{
				list = BeaconsDAO.getAllBeacons();
			}
			rsp = new RespGenerator(0, request);
			rsp.addResponse("list", list);
		} catch (Exception e) {
			rsp = new RespGenerator(e, request);
		}

		ResponseSender.send(response, rsp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oper = request.getParameter("oper");
		String sMajor = request.getParameter("major");
        String sMinor = request.getParameter("minor");
        String sFloor = request.getParameter("floor");
        String sType = request.getParameter("type");
        String sOwner = request.getParameter("owner");
        String sDesc = request.getParameter("description");
        String sLati = request.getParameter("lati");
        String sLongi = request.getParameter("longi");
        String sX = request.getParameter("x");
        String sY = request.getParameter("y");
        String sRssi1 = request.getParameter("rssi1");
        String sRssi2 = request.getParameter("rssi2");
        String sA = request.getParameter("a");
        String sN = request.getParameter("n");
		String id = request.getParameter("id");
        
        RespGenerator rsp = null;

		try {
			if (oper.equals("add") && (sMajor != null) && (sMinor != null)
					&& (sFloor != null) && (sType != null) && (sOwner != null) 
					&& (sDesc != null) && (sLati != null)
					&& (sLongi != null) && (sX != null) && (sY != null) && (sRssi1 != null) && (sRssi2 != null)) {
				
				Beacons newBeacon = new Beacons(Integer.parseInt(sMajor), Integer.parseInt(sMinor), 
						sFloor, sType, sOwner, sDesc, 
						Double.parseDouble(sLati), Double.parseDouble(sLongi), 
						Double.parseDouble(sX), Double.parseDouble(sY),
					    Byte.parseByte(sRssi1),Byte.parseByte(sRssi2),
						Double.parseDouble(sA), Double.parseDouble(sN));
				BeaconsDAO.create(newBeacon);
				
			} else if (oper.equals("del") && (sMajor != null) && (sMinor != null)) {
				List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(Integer.parseInt(sMajor), 
																			Integer.parseInt(sMinor));
				BeaconsDAO.delete(beaconList);
				
			} else if (oper.equals("edit") && (((sMajor != null) || (sMinor != null))
										|| (id != null))) {
				Beacons beacon = null;
				if (id != null) {
					beacon = BeaconsDAO.get(Integer.parseInt(id));
				} else {
					List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(Integer.parseInt(sMajor), 
																				Integer.parseInt(sMinor));
					if (beaconList.isEmpty() == false) {
						beacon = beaconList.get(0);
					}
				}
				if (beacon == null) {
					throw ErrorGen.gen(1);
				}

				if (sFloor != null) {
					beacon.setFlor(sFloor);
				}
				if (sType != null) {
					beacon.setPostype(sType);
				}
				if (sOwner != null) {
					beacon.setOwner(sOwner);
				}
				if (sDesc != null) {
					beacon.setDescription(sDesc);
				}
				if (sLati != null) {
					beacon.setLati(Double.parseDouble(sLati));
				}
				if (sLongi != null) {
					beacon.setLongi(Double.parseDouble(sLongi));
				}
				if (sA != null) {
					beacon.setA(Double.parseDouble(sA));
				}
				if (sN != null) {
					beacon.setN(Double.parseDouble(sN));
				}
				if (sX != null) {
					beacon.setX(Double.parseDouble(sX));
				}
				if (sY != null) {
					beacon.setY(Double.parseDouble(sY));
				}
				if (sRssi1 != null) {
					beacon.setRssi1(Byte.parseByte(sRssi1));
				}
				if (sRssi2 != null) {
					beacon.setRssi2(Byte.parseByte(sRssi2));
				}
				BeaconsDAO.update(beacon);
			} else {
				rsp = new RespGenerator(9, request);
			}

			if (rsp == null) {
				rsp = new RespGenerator(0, request);
			}
		} catch (Exception e) {
			rsp = new RespGenerator(e, request);
		}

		ResponseSender.send(response, rsp);
	}

}
