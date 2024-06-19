import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

// Device classes
abstract class Device {
    protected String macAddress;

    public Device() {
        // Generate random MAC address
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String hex = Integer.toHexString(random.nextInt(256));
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
            if (i < 5) {
                sb.append(':');
            }
        }
        macAddress = sb.toString().toUpperCase();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public abstract void display();
}

class Hub extends Device {
    private String name;
    private List<EndDevice> connectedDevices;

    public Hub(int ports, String name) {
        super();
        this.name = name;
        this.connectedDevices = new ArrayList<>();
    }

    public void addDevice(EndDevice device) {
        connectedDevices.add(device);
    }

    public void display() {
        System.out.println("HUB: " + name);
        System.out.println("\t\t MAC ADDRESS: " + getMacAddress());
        System.out.println("\t\t HUB with " + connectedDevices.size() + " devices");
    }

    public void sendData(EndDevice sender, EndDevice receiver, int data, Star star) {
        System.out.println("Broadcasting data from " + sender.getMacAddress() + " to all devices connected to Hub " +
                this.getMacAddress() + " via Star network: " + data);
        for (EndDevice device : connectedDevices) {
            // Broadcasting data to all connected devices except the sender
            if (!device.getMacAddress().equals(sender.getMacAddress())) {
                device.sendData(receiver, data, star);
            }
        }
    }

}

class Switch extends Device {
    private String name;
    private int ports;
    private HashMap<String, Device> addressTable;

    public Switch(int ports, String name) {
        super();
        this.ports = ports;
        this.name = name;
        this.addressTable = new HashMap<>();
    }

    public void display() {
        System.out.println("Switch: " + name);
        System.out.println("\t\t MAC ADDRESS: " + getMacAddress());
        System.out.println("\t\t Switch with " + ports + " ports");
    }

    public void learnAddress(Device device) {
        String macAddress = device.getMacAddress();
        if (!addressTable.containsKey(macAddress)) {
            addressTable.put(macAddress, device);
        }
    }

    public Device getDeviceByMacAddress(String macAddress) {
        return addressTable.get(macAddress);
    }

    public int getCollisionDomain() {
        return addressTable.size(); // Each device connected to a switch has its own collision domain
    }

}

class EndDevice extends Device {
    private String name;

    public EndDevice(String name) {
        super();
        this.name = name;
    }

    public void display() {
        System.out.println("EndDevice: " + name);
        System.out.println("\t\t MAC ADDRESS: " + getMacAddress());
    }

    // New method for sending data
    public void sendData(EndDevice receiver, int data, Star star) {
        System.out.println("Sending data from " + this.name + " to " + receiver.name + " via Star network.");
        System.out.println("Data: " + data);
    }
}

// Topology classes
abstract class Topology {
    protected List<Device> devices;

    public Topology() {
        devices = new ArrayList<>();
    }

    public abstract void addDevice(Device device);

    public abstract void connectDevices(Device device1, Device device2);

    public abstract void display();

    public abstract int getBroadcastDomain();

    public abstract int getCollisionDomain();
}

class Bus extends Topology {
    public void addDevice(Device device) {
        devices.add(device);
    }

    public void connectDevices(Device device1, Device device2) {
        // No connections needed in a bus topology
    }

    public void display() {
        System.out.println("Bus Topology:");
        for (Device device : devices) {
            device.display();
            System.out.println();
        }
    }

    public int getBroadcastDomain() {
        return devices.size();
    }

    public int getCollisionDomain() {
        return devices.size();
    }
}

class Star extends Topology {
    private Device centralDevice;

    public Star(Device centralDevice) {
        this.centralDevice = centralDevice;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void connectDevices(Device device1, Device device2) {
        // No connections needed in a star topology
    }

    public void display() {
        System.out.println("Star Topology:");
        centralDevice.display();
        System.out.println(" connected to:");
        for (Device device : devices) {
            device.display();
            System.out.println();
        }
    }

    public int getBroadcastDomain() {
        return 1;
    }

    public int getCollisionDomain() {
        return 1; // Each device is connected to a hub/switch, so each has its own collision
                  // domain
    }
}

class Bridge extends Device {
    class BridgeDevice {
        private List<Device> devices;

        public BridgeDevice() {
            devices = new ArrayList<>();
        }

        // Method to add devices to the bridge
        public void addDevice(Device device) {
            devices.add(device);
        }

        // Method to send data to all devices connected to the bridge
        public void sendDataToAll(String data) {
            System.out.println("Sending data to all devices connected to the bridge:");
            // for (Device device : devices) {
            // // device.sendData(data);
            // }
        }
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Unimplemented method 'display'");
    }

}

// Flow control classes
interface FlowControl {
    void controlFlow(Device sender, Device receiver);
}

class StopAndWait implements FlowControl {
    private int frameSize;
    private int transmissionDelay;

    public StopAndWait(int frameSize, int transmissionDelay) {
        this.frameSize = frameSize;
        this.transmissionDelay = transmissionDelay;
    }

    public void controlFlow(Device sender, Device receiver) {
        System.out.println("Stop and Wait flow control between " + sender.getClass().getSimpleName() +
                " and " + receiver.getClass().getSimpleName());

        int totalFrames = 10;

        Random random = new Random();

        for (int i = 0; i < totalFrames; ++i) {
            String frame = "Frame " + (i + 1) + " of " + totalFrames;
            // Add checksum to frame
            int checksum = 0;
            for (char c : frame.toCharArray()) {
                checksum += c;
            }
            frame += " " + checksum;

            System.out.println("Sending frame " + (i + 1) + " of " + totalFrames + " with frame size " + frameSize);

            try {
                Thread.sleep(transmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add random error to frame
            int randomNumber = random.nextInt(100) + 1; // 1-100
            if (randomNumber <= 10) { // 10% probability of error
                System.out.println("Error detected in frame " + (i + 1) + ". Retransmitting...");
                i--; // Retransmit the frame
            } else {
                System.out.println("Frame " + (i + 1) + " received.");
            }
        }
    }
}

class SlidingWindow implements FlowControl {
    private int windowSize;
    private int frameSize;
    private int transmissionDelay;

    public SlidingWindow(int windowSize, int frameSize, int transmissionDelay) {
        this.windowSize = windowSize;
        this.frameSize = frameSize;
        this.transmissionDelay = transmissionDelay;
    }

    public void controlFlow(Device sender, Device receiver) {
        System.out.println("Sliding Window flow control between " + sender.getClass().getSimpleName() +
                " and " + receiver.getClass().getSimpleName());

        int totalFrames = 10;
        int sentFrames = 0;
        int ackedFrames = 0;

        Random random = new Random();

        while (ackedFrames < totalFrames) {
            for (int i = sentFrames; i < Math.min(sentFrames + windowSize, totalFrames); ++i) {
                String frame = "Frame " + (i + 1) + " of " + totalFrames + " with frame size " + frameSize;
                // Add checksum to frame
                int checksum = 0;
                for (char c : frame.toCharArray()) {
                    checksum += c;
                }
                frame += " " + checksum;

                System.out.println("Sending frame " + (i + 1) + " of " + totalFrames + " with frame size " + frameSize);

                try {
                    Thread.sleep(transmissionDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Add random error to frame
                int randomNumber = random.nextInt(100) + 1; // 1-100
                if (randomNumber <= 10) { // 10% probability of error
                    System.out.println("Error detected in frame " + (i + 1) + ". Retransmitting...");
                    i--; // Retransmit the frame
                } else {
                    System.out.println("Frame " + (i + 1) + " received.");
                }
            }
            sentFrames += windowSize;
            for (int i = 0; i < windowSize && ackedFrames < totalFrames; ++i) {
                System.out.println("ACK for frame " + (++ackedFrames) + " received.");
            }
        }
    }
}

// Error control classes
interface ErrorControl {
    void detectErrors(Device sender, Device receiver);
}

class ParityCheck implements ErrorControl {
    private int errorProbability; // Probability of an error in percentage(0-100)

    public ParityCheck(int errorProbability) {
        this.errorProbability = errorProbability;
    }

    public void detectErrors(Device sender, Device receiver) {
        System.out.println("Parity Check error control between " + sender.getClass().getSimpleName() +
                " and " + receiver.getClass().getSimpleName());

        Random random = new Random();

        int frameCount = 10;
        for (int i = 0; i < frameCount; ++i) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int randomNumber = random.nextInt(100) + 1; // 1-100
            if (randomNumber <= errorProbability) {
                System.out.println("Error detected in frame " + (i + 1) + ". Retransmitting...");
            } else {
                System.out.println("Frame " + (i + 1) + " transmitted successfully.");
            }
        }
    }
}
// class ParityCheck implements ErrorControl {
// private int errorProbability; // Probability of an error in percentage
// (0-100)
// private ParityType parityType; // Type of parity check (EVEN or ODD)

// // Enum to represent parity types
// enum ParityType {
// EVEN, // Even parity
// ODD // Odd parity
// }

// public ParityCheck(int errorProbability, ParityType parityType) {
// this.errorProbability = errorProbability;
// this.parityType = parityType;
// }

// public void detectErrors(Device sender, Device receiver) {
// System.out.println("Parity Check (" + parityType + ") error control between "
// +
// sender.getClass().getSimpleName() + " and " +
// receiver.getClass().getSimpleName());

// Random random = new Random();
// int frameCount = 10;

// for (int i = 0; i < frameCount; ++i) {
// try {
// Thread.sleep(500);
// } catch (InterruptedException e) {
// e.printStackTrace();
// }

// // Simulate frame data transmission
// String frameData = "Frame " + (i + 1) + " data"; // Example frame data

// // Calculate parity bit based on the frame data
// char parityBit = calculateParityBit(frameData);

// // Introduce errors based on the specified error probability
// boolean errorDetected = random.nextInt(100) < errorProbability;

// if (errorDetected) {
// System.out.println("Error detected in frame " + (i + 1) + ".
// Retransmitting...");
// } else {
// System.out.println("Frame " + (i + 1) + " transmitted successfully.");
// }
// }
// }

// // Method to calculate parity bit based on the frame data
// private char calculateParityBit(String frameData) {
// int count = 0;
// for (char c : frameData.toCharArray()) {
// if (c == '1') {
// count++;
// }
// }
// if (parityType == ParityType.EVEN) {
// return (count % 2 == 0) ? '0' : '1'; // Even parity
// } else {
// return (count % 2 == 0) ? '1' : '0'; // Odd parity
// }
// }
// }

// Access control classes
interface AccessControl {
    void controlAccess(Device sender, Device receiver);
}

class CSMA_CD implements AccessControl {
    private int collisionProbability; // Probability of a collision in percentage (0-100)

    public CSMA_CD(int collisionProbability) {
        this.collisionProbability = collisionProbability;
    }

    public void controlAccess(Device sender, Device receiver) {
        System.out.println("CSMA/CD access control between " + sender.getClass().getSimpleName() +
                " and " + receiver.getClass().getSimpleName());

        Random random = new Random();

        int frameCount = 10;
        for (int i = 0; i < frameCount; ++i) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int randomNumber = random.nextInt(100) + 1; // 1-100
            if (randomNumber <= collisionProbability) {
                System.out.println("Collision detected for frame " + (i + 1) + ". Retransmitting...");
            } else {
                System.out.println("Frame " + (i + 1) + " transmitted without collision.");
            }
        }
    }
}

public class PhysicalandDatalinkLayer {
    public static void main(String[] args) {
        // Test Case 1: Two end devices with dedicated connection
        EndDevice device1 = new EndDevice("device 1");
        EndDevice device2 = new EndDevice("device 2");
        System.out.println("Test Case 1: Two end devices with dedicated connection");
        device1.display();
        device2.display();
        System.out.println("Data Transmission between device 1 and device 2:");
        device1.sendData(device2, 100, null); // Null Star parameter as it's not needed for direct connection
        System.out.println();

        // Test Case 2: Star topology with five end devices connected to a hub
        Hub hub = new Hub(5, "Hub");
        EndDevice device3 = new EndDevice("device 3");
        EndDevice device4 = new EndDevice("device 4");
        EndDevice device5 = new EndDevice("device 5"); // Creating the fifth device
        hub.addDevice(device1);
        hub.addDevice(device2);
        hub.addDevice(device3);
        hub.addDevice(device4);
        hub.addDevice(device5); // Adding the fifth device to the hub
        Star starTopology = new Star(hub); // Creating a Star object with the hub
        System.out.println("Test Case 2: Star topology with five end devices connected to a hub");
        hub.display(); // Display hub information
        System.out.println("End Devices connected to the hub:");
        device1.display();
        device2.display();
        device3.display();
        device4.display();
        device5.display();
        System.out.println("Communication within end devices in the star topology:");

        // Sending data from device 1 to device 2 via Star network.
        System.out.println("Sending data from " + device1.getMacAddress() + " to " + device2.getMacAddress()
                + " via Star network.");
        device1.sendData(device2, 100, starTopology); // Passing the Star object for communication within the star

        // Sending data from device 3 to device 4 via Star network.
        System.out.println("Sending data from " + device3.getMacAddress() + " to " + device4.getMacAddress()
                + " via Star network.");
        device3.sendData(device4, 200, starTopology); // Passing the Star object for communication within the star

        // Test Case 3: Switch with five end devices connected to it
        Switch switchDevice = new Switch(5, "Switch");
        EndDevice device6 = new EndDevice("device 6");
        EndDevice device7 = new EndDevice("device 7");
        EndDevice device8 = new EndDevice("device 8");
        EndDevice device9 = new EndDevice("device 9");
        EndDevice device10 = new EndDevice("device 10");
        switchDevice.learnAddress(device1);
        switchDevice.learnAddress(device2);
        switchDevice.learnAddress(device3);
        switchDevice.learnAddress(device4);
        switchDevice.learnAddress(device5);
        switchDevice.learnAddress(device6);
        switchDevice.learnAddress(device7);
        switchDevice.learnAddress(device8);
        switchDevice.learnAddress(device9);
        switchDevice.learnAddress(device10);
        System.out.println("Test Case 3: Switch with five end devices connected to it");
        switchDevice.display();
        System.out.println("Data Transmission between end devices with flow control and error control:");
        StopAndWait stopAndWait = new StopAndWait(10, 100);
        stopAndWait.controlFlow(device1, device2);
        ParityCheck parityCheck = new ParityCheck(20);
        parityCheck.detectErrors(device3, device4);
        CSMA_CD csma_cd = new CSMA_CD(30);
        csma_cd.controlAccess(device5, device6);
        System.out.println();
        /// bd -1(switch), cd -5(end devices.).

        // Test Case 4: Two star topologies with five end devices connected to hubs,
        // connected via a switch
        Hub hub1 = new Hub(5, "Hub 1");
        Hub hub2 = new Hub(5, "Hub 2");

        // Adding devices to Hub 1
        hub1.addDevice(device1);
        hub1.addDevice(device2);
        hub1.addDevice(device3);
        hub1.addDevice(device4);
        hub1.addDevice(device5);

        // Adding devices to Hub 2
        hub2.addDevice(device6);
        hub2.addDevice(device7);
        hub2.addDevice(device8);
        hub2.addDevice(device9);
        hub2.addDevice(device10);

        // Creating a switch
        Switch switchDevice2 = new Switch(10, "Switch");

        // Learning addresses in the switch
        switchDevice2.learnAddress(hub1);
        switchDevice2.learnAddress(hub2);
        switchDevice2.learnAddress(device1);
        switchDevice2.learnAddress(device2);
        switchDevice2.learnAddress(device3);
        switchDevice2.learnAddress(device4);
        switchDevice2.learnAddress(device5);
        switchDevice2.learnAddress(device6);
        switchDevice2.learnAddress(device7);
        switchDevice2.learnAddress(device8);
        switchDevice2.learnAddress(device9);
        switchDevice2.learnAddress(device10);

        System.out.println(
                "Test Case 4: Two star topologies with five end devices connected to hubs, connected via a switch");

        // Creating a star topology
        starTopology = new Star(hub1);
        starTopology.addDevice(hub2);
        starTopology.display();

        System.out.println("Communication between all end devices in the network:");
        device1.sendData(device6, 300, starTopology); // Using Star parameter for communication between hubs
        device2.sendData(device9, 400, starTopology); // Using Star parameter for communication between hubs

        System.out.println("Broadcast domain: " + starTopology.getBroadcastDomain());
        System.out.println("Collision domain: " + (switchDevice2.getCollisionDomain())); 
    }
}
