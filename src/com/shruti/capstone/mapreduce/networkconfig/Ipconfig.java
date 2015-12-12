package com.shruti.capstone.mapreduce.networkconfig;
/*
 * Capstone project by Shruti Gorde
 * class for ip configuration and socket connection
 * 
 */
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import com.shruti.capstone.Memo;
import com.shruti.capstone.Memokind;

import android.util.Log;

public class Ipconfig {
//get the IP address of the server
	public static String getServerIP() {
		try {
			for (Enumeration<NetworkInterface> netin = NetworkInterface
					.getNetworkInterfaces(); netin.hasMoreElements();) {
				NetworkInterface intf = netin.nextElement();
				for (Enumeration<InetAddress> eIpAdd = intf
						.getInetAddresses(); eIpAdd.hasMoreElements();) {
					InetAddress addip = eIpAdd.nextElement();
					if (!addip.isLoopbackAddress()) {
						return addip.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(Ipconfig.class.getName(), ex.toString());
		}
		return null;
	}

	public static final String LaptopIP = System.getProperty("ipAddress",
			"192.168.0.4"); //change the ip address accordingly
	public static final int LaptopPort = Integer.parseInt(System.getProperty(
			"port", "4443")); 

	public static void doTaskRequest(Socket socket) throws IOException {

		Memo msg = new Memo();
		msg.setMemoKind(Memokind.TASK_SET);
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		oos.reset();
		oos.writeObject(msg);
		oos.flush();
	}
}
