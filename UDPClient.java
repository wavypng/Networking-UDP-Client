/*
 * Client App upon UDP
 * Hung Pham, Rigoberto Hinojos
 */

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

public class UDPClient {

    private static final String[] ID = {"00001", "00002", "00003", "00004", "00005", "00006"};
    // private static final int[] ID = {1, 2, 3, 4, 5, 6};
    private static final String[] ITEMS = {"New Inspiron 15", "New Inspiron 17", "New Inspiron 15R",
            "New Inspiron 15z Ultrabook", "XPS 14 Ultrabook", "New XPS 12 UltrabookXPS"};
    
    public static void main(String[] args) throws IOException {

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

        //handles server address
        System.out.print("Enter IP address or hostname to connect (default 147.153.10.87): ");
        String addresString = "147.153.10.87";
        InetAddress address = InetAddress.getByName(addresString);
        String addressInput = sysIn.readLine();
        // if the user entered non-whitespace characters then actually parse their input
        if(!"".equals(addressInput.trim())) {
            try {
                address = InetAddress.getByName(addressInput);
            }
            catch(java.net.UnknownHostException e)
            {
                System.out.println("Wrong! " + e);
            }
        }
        
        //handles port input parsing
        System.out.println("Will connect to " + address);
        System.out.print("Enter port number (default 5260): ");
        int port = 5260;
        String portInput = sysIn.readLine();
        // if the user entered non-whitespace characters then actually parse their input
        if(!"".equals(portInput.trim())) {
            try {
                port = Integer.parseInt(portInput);
            }
            catch(NumberFormatException nfe) {
                System.out.println("Wrong! " + nfe);
            }
        }
        System.out.println("Using port: " + port + "\n");

        // if (args.length != 1) {
        //      System.out.println("Usage: java UDPClient <hostname>");
        //      return;
        // }

        while (true) {
            System.out.printf("%-10s%-30s%n%n", "Item ID", "Item Description");
            for (int i=0; i < ITEMS.length; i++){
                System.out.format("%-10s%-30s%n", ID[i], ITEMS[i]);
            }
            String idInput;
            do {
                System.out.print("\nEnter an Item ID to view: ");
                idInput = sysIn.readLine();
            } while (Arrays.stream(ID).anyMatch(idInput::equals) == false);

            // create a UDP socket
            DatagramSocket udpSocket = new DatagramSocket();
            
            double price;
            int inv;
            String desc;
            // String fromUser;
            
            // while ((fromUser = sysIn.readLine()) != null) {
            
            //display user input
            System.out.println("From Client: " + idInput); //TODO: remove in submission
            
            // send request / record time sent
            byte[] buf = idInput.getBytes();
            DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, port);
            long timeSend = System.currentTimeMillis();
            udpSocket.send(udpPacket);
            
            // get response
            byte[] buf2 = new byte[256];
            DatagramPacket udpPacket2 = new DatagramPacket(buf2, buf2.length);
            udpSocket.receive(udpPacket2);
            byte[] buf3 = new byte[256];
            DatagramPacket udpPacket3 = new DatagramPacket(buf3, buf3.length);
            udpSocket.receive(udpPacket3);
            byte[] buf4 = new byte[256];
            DatagramPacket udpPacket4 = new DatagramPacket(buf4, buf4.length);
            udpSocket.receive(udpPacket4);
            long rtt = System.currentTimeMillis() - timeSend; // get rtt
            
            // display response
            desc = new String(udpPacket2.getData(), 0, udpPacket2.getLength());
            // test3 = new String(udpPacket3.getData(), 0, udpPacket3.getLength());

            price = ByteBuffer.wrap(udpPacket3.getData()).getDouble();
            inv = ByteBuffer.wrap(udpPacket4.getData()).getInt();

            // System.out.println("yeah" + price + "stonks" + inv);

            System.out.printf("%-10s%-30s%-16s%-13s%-20s%n%n", "Item ID", "Item Description", 
            "Unit Price", "Inventory", "RTT of Query");

            System.out.format("%-10s%-30s$%1.2f%12d%13dms%n", idInput, desc, price, inv, rtt);
            
            //   if (fromUser.equals("Bye."))
            //       break;
            // }
            
            System.out.print("\nPress any key to continue or type quit to exit.\n> ");
            String option = sysIn.readLine();
            if (option.equals("quit")) {
                System.out.println("See ya!");
                break;
            }
            udpSocket.close();
        }
    }
}
