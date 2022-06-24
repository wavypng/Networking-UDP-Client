/*
 * Server App upon UDP
 * Hung Pham, Rigoberto Hinojos
 */ 
 
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

public class UDPServer {

    private static final String[] ID = {"00001", "00002", "00003", "00004", "00005", "00006"};
    private static final String[] ITEMS = {"New Inspiron 15", "New Inspiron 17", "New Inspiron 15R",
            "New Inspiron 15z Ultrabook", "XPS 14 Ultrabook", "New XPS 12 UltrabookXPS"};
    private static final double[] PRICE = {379.99, 449.99, 549.99, 749.99, 999.99, 1199.99};
    private static final int[] INVENTORY = {157, 128, 202, 315, 261, 178};

    public static void main(String[] args) throws IOException {
         
        DatagramSocket udpServerSocket = new DatagramSocket(5260);
        // BufferedReader in = null;
        DatagramPacket udpPacket = null, udpPacket2 = null, udpPacket3 = null, udpPacket4 = null;
        String fromClient = null, toClientDesc = null;
        double toClientPrice;
        int toClientInventory;
        boolean morePackets = true;

        byte[] buf = new byte[256];
        
        while (morePackets) {
            try {

                // receive UDP packet from client
                udpPacket = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket);

                fromClient = new String(
                udpPacket.getData(), 0, udpPacket.getLength(), "UTF-8");

                // get the response
                int index = Arrays.asList(ID).indexOf(fromClient);
                toClientDesc = ITEMS[index];
                toClientPrice = PRICE[index];
                toClientInventory = INVENTORY[index];
                // System.out.println("get got" + toClientPrice + "yeeee" + toClientInventory);
                // toClient = fromClient.toUpperCase();
                                         
                // send the response to the client at "address" and "port"
                InetAddress address = udpPacket.getAddress();
                int port = udpPacket.getPort();
                byte[] buf2 = toClientDesc.getBytes("UTF-8");;
                udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                udpServerSocket.send(udpPacket2);
                byte[] buf3 = ByteBuffer.allocate(8).putDouble(toClientPrice).array();
                udpPacket3 = new DatagramPacket(buf3, buf3.length, address, port);
                udpServerSocket.send(udpPacket3);
                byte[] buf4 = ByteBuffer.allocate(4).putInt(toClientInventory).array();
                udpPacket4 = new DatagramPacket(buf4, buf4.length, address, port);
                udpServerSocket.send(udpPacket4);

            } catch (IOException e) {
                e.printStackTrace();
                morePackets = false;
            }
        }
  
        udpServerSocket.close();

    }
}
