package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
//exploring and understanding
public class Pone {
	Thread t1=new Thread(){
		public void run(){
			while(true){
				try {
					soc.receive(pk_recv);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String msg=new String(pk_recv.getData(),0,pk_recv.getLength());
				System.out.println(msg);
			}
		}
	};
	
	byte[] buf;
	DatagramSocket soc;
	DatagramPacket pk_recv,pk_send;
	public Pone() {
		byte[] buf=new byte[1024];
		try {
			soc=new DatagramSocket(55210);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		pk_send=new DatagramPacket(buf,buf.length,new InetSocketAddress(30000));
		pk_recv=new DatagramPacket(buf, buf.length);
	}
	public void send(String messg){
		pk_send.setData(messg.getBytes());
		try {
			soc.send(pk_send);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void recv() {
		t1.start();
	}
	
	
	public static void main(String[] args) {
		Pone obj =new Pone();
		obj.recv();
		
		while(true){
		try {
			obj.send(new BufferedReader(new InputStreamReader(System.in)).readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}

}
