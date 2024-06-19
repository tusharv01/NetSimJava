import java.util.*;

// Transport Layer with Go-Back-N protocol
class TransportLayer {
    private static final int WINDOW_SIZE = 4;
    private Queue<String> senderBuffer;
    private Queue<String> receiverBuffer;
    private int sequenceNumber;

    public TransportLayer() {
        senderBuffer = new ArrayDeque<>();
        receiverBuffer = new ArrayDeque<>();
        sequenceNumber = 0;
    }

    public void sendData(EndDevice sender, EndDevice receiver, String data) {
        System.out.println("Sending data from " + sender.getMacAddress() + ":" + sender.getPort() + " to " + receiver.getMacAddress() + ":" + receiver.getPort() + " via Transport Layer (Go-Back-N)");

        // Split data into segments
        int totalSegments = (int) Math.ceil((double) data.length() / WINDOW_SIZE);
        for (int i = 0; i < totalSegments; i++) {
            String segment = data.substring(i * WINDOW_SIZE, Math.min((i + 1) * WINDOW_SIZE, data.length()));
            senderBuffer.offer(segment);
        }

        // Simulating sending with Go-Back-N
        while (!senderBuffer.isEmpty()) {
            if (sequenceNumber < senderBuffer.size()) {
                System.out.println("Sending segment " + sequenceNumber + ": " + senderBuffer.peek());
                receiverBuffer.offer(senderBuffer.poll());
                sequenceNumber++;
            } else {
                // If receiver confirms, remove from buffer
                System.out.println("Receiver confirmed segment " + (sequenceNumber - 1));
                senderBuffer.poll();
            }
        }
    }

    public void receiveData(EndDevice receiver, String data) {
        System.out.println("Receiving data at " + receiver.getMacAddress() + " via Transport Layer");
        receiverBuffer.offer(data);
    }
}

// Application Layer services
class ApplicationLayer {
    public void sendHTTPRequest(EndDevice sender, EndDevice receiver, String request) {
        System.out.println("Sending HTTP request from " + sender.getMacAddress() + ":" + sender.getPort() + " to " + receiver.getMacAddress() + ":" + receiver.getPort() + ": " + request);
    }

    public void sendFTPRequest(EndDevice sender, EndDevice receiver, String request) {
        System.out.println("Sending FTP request from " + sender.getMacAddress() + ":" + sender.getPort() + " to " + receiver.getMacAddress() + ":" + receiver.getPort() + ": " + request);
    }
}

// Device class (for MAC address)
abstract class Device {
    protected String macAddress;

    public Device() {
        // Generating a random MAC address
        this.macAddress = generateRandomMACAddress();
    }

    private String generateRandomMACAddress() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(String.format("%02X", (int) (Math.random() * 256)));
            if (i < 5) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

    public String getMacAddress() {
        return macAddress;
    }
}

// EndDevice class
class EndDevice extends Device {
    private String name;
    private int port;

    public EndDevice(String name, int port) {
        super();
        this.name = name;
        this.port = port;
    }

    public void display() {
        System.out.println("EndDevice: " + name);
        System.out.println("MAC Address: " + getMacAddress());
        System.out.println("Port Number: " + port);
        System.out.println();
    }

    public int getPort() {
        return port;
    }
}

// Main class to demonstrate the layers
public class TransportAndApplicationLayer {
    public static void main(String[] args) {
        // Create devices with specified port numbers
        EndDevice device1 = new EndDevice("Device1", 1024);
        EndDevice device2 = new EndDevice("Device2", 2048);

        // Display devices
        System.out.println("Devices:");
        device1.display();
        device2.display();

        // Create layers
        TransportLayer transportLayer = new TransportLayer();
        ApplicationLayer applicationLayer = new ApplicationLayer();

        // Test Application Layer services
        System.out.println("Testing Application Layer:");
        applicationLayer.sendHTTPRequest(device1, device2, "GET /index.html");
        applicationLayer.sendFTPRequest(device1, device2, "GET /file.txt");
        System.out.println();

        // Test Transport Layer (Go-Back-N)
        System.out.println("Testing Transport Layer (Go-Back-N):");
        transportLayer.sendData(device1, device2, "This is a large message that needs to be segmented.");
    }
}
