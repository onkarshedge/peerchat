package chat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.Set;



public class Ptwo {
	Thread t1=new Thread(){
		public void run(){
			byte [] buf=new byte[1024];
			DatagramPacket pk_recv=new DatagramPacket(buf,buf.length);
			while(true){
				try {
					soc.receive(pk_recv);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String msg=new String(pk_recv.getData(),0,pk_recv.getLength());
				System.out.println(pk_recv.getSocketAddress().toString()+" "+msg);
			}
		}
	};
	Thread t2=new Thread(){
		public void run(){
			InetAddress adrs=null;
			try {
				adrs=InetAddress.getByName(Multicast_ip_address);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(true){
				String msg=Username;
				DatagramPacket pck=new DatagramPacket(msg.getBytes(), msg.length(),adrs,MPORT);
				try {
					soc.send(pck);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(3500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Thread t3=new Thread(){
		 public void run(){
			byte[] msg=new byte[1024];
			MulticastSocket recv_broadcast = null;
			try {
				recv_broadcast = new MulticastSocket(MPORT);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				recv_broadcast.joinGroup(InetAddress.getByName(Multicast_ip_address));
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(true){
			try {
				DatagramPacket msgpackt=new DatagramPacket(msg, msg.length);
				recv_broadcast.receive(msgpackt);
				String frnd_name=new String(msgpackt.getData(),0,msgpackt.getLength());
				SocketAddress frnd_adrs=msgpackt.getSocketAddress();
				User e=new User(frnd_name,(InetSocketAddress)frnd_adrs);
				friends.add(e);
				for(User p:friends){
					System.out.println(p.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
				//recv_broadcast.leaveGroup(InetAddress.getByName(inet_adr));
				recv_broadcast.close();
			}
		  }
		}
	};
	
	
	DatagramSocket soc;
	final int MPORT=12002;
	final String Multicast_ip_address="228.0.0.3";
	final String Username="suresh";
	Set<User> friends=new HashSet<User>();
	public Ptwo() {
		try {
			soc=new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
	public void send(String messg){
		DatagramPacket pk_send=new DatagramPacket(messg.getBytes(),messg.getBytes().length);
		try {
			soc.send(pk_send);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void init() {
		t3.start();
		t1.start();
		t2.start();
	}
	
	
	public static void main(String[] args) {
		Pone obj =new Pone();
		obj.init();
		
		while(true){
		try {
			obj.send(new BufferedReader(new InputStreamReader(System.in)).readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}

}
