import java.util.*;

// Interface class to represent network interfaces
class Interface {
    private String name;
    private String ipAddress;
    private String subnetMask;
    private String macAddress;
    private boolean status;

    public Interface(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
        this.status = false; // default to shutdown
    }

    public void configure(String ipAddress, String subnetMask) {
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.status = true; // bring interface up
    }

    public String getName() { return name; }
    public String getIpAddress() { return ipAddress; }
    public String getSubnetMask() { return subnetMask; }
    public String getMacAddress() { return macAddress; }
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}

// ARPTable class to manage ARP entries
class ARPTable {
    private Map<String, String> arpTable;

    public ARPTable() {
        arpTable = new HashMap<>();
    }

    public void addEntry(String ipAddress, String macAddress) {
        arpTable.put(ipAddress, macAddress);
    }

    public String getMacAddress(String ipAddress) {
        return arpTable.get(ipAddress);
    }

    public void displayTable() {
        System.out.println("ARP Table:");
        arpTable.forEach((ip, mac) -> System.out.println("IP Address: " + ip + " -> MAC Address: " + mac));
    }
}

// Packet class to represent network packets
class Packet {
    private String sourceIP;
    private String destinationIP;
    private String data;

    public Packet(String sourceIP, String destinationIP, String data) {
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.data = data;
    }

    public String getSourceIP() { return sourceIP; }
    public String getDestinationIP() { return destinationIP; }
    public String getData() { return data; }
}

// Abstract class for network devices
abstract class NetworkDevice {
    private String name;
    private List<Interface> interfaces;

    public NetworkDevice(String name) {
        this.name = name;
        this.interfaces = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Interface> getInterfaces() { return interfaces; }
    public void addInterface(Interface iface) { interfaces.add(iface); }
    public abstract void configure();
}

// Router class extending NetworkDevice
class Router extends NetworkDevice {
    private Map<String, String> routingTable;
    private ARPTable arpTable;
    private Map<String, Integer> neighbors; // Neighbor router name and link cost

    public Router(String name) {
        super(name);
        this.routingTable = new HashMap<>();
        this.arpTable = new ARPTable();
        this.neighbors = new HashMap<>();
    }

    public void addNeighbor(String neighborName, int cost) {
        neighbors.put(neighborName, cost);
    }

    public void addArpEntry(String ipAddress, String macAddress) {
        arpTable.addEntry(ipAddress, macAddress);
    }

    public String getMacAddress(String ipAddress) {
        return arpTable.getMacAddress(ipAddress);
    }

    public void showArpTable() {
        arpTable.displayTable();
    }

    public void addRoute(String destination, String nextHop) {
        routingTable.put(destination, nextHop);
    }

    public void showRoutingTable() {
        System.out.println("Routing Table:");
        routingTable.forEach((destination, nextHop) -> System.out.println("Destination: " + destination + " -> Next Hop: " + nextHop));
    }

    private void computeShortestPaths() {
        System.out.println("Computing shortest paths for OSPF...");
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        neighbors.keySet().forEach(neighbor -> {
            distances.put(neighbor, Integer.MAX_VALUE);
            previous.put(neighbor, null);
        });
        distances.put(getName(), 0);
        pq.add(getName());

        while (!pq.isEmpty()) {
            String current = pq.poll();
            for (Map.Entry<String, Integer> neighborEntry : neighbors.entrySet()) {
                String neighbor = neighborEntry.getKey();
                int cost = neighborEntry.getValue();
                int altDist = distances.get(current) + cost;
                if (altDist < distances.get(neighbor)) {
                    distances.put(neighbor, altDist);
                    previous.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        distances.keySet().forEach(destination -> {
            if (previous.get(destination) != null) {
                routingTable.put(destination, previous.get(destination));
            }
        });
    }

    private void populateRoutingTable() {
        System.out.println("Populating OSPF routing table...");
        computeShortestPaths();
        routingTable.forEach((destination, nextHop) -> System.out.println("Route: " + destination + " via " + nextHop));
    }

    public void configureOSPF(String area, String network) {
        populateRoutingTable();
        displayOSPFConfiguration();
    }

    private void displayOSPFConfiguration() {
        System.out.println("OSPF configuration completed.");
    }

    @Override
    public void configure() {
        // Configuration logic for the router
    }

    public void forwardPacket(Packet packet) {
        String destinationIP = packet.getDestinationIP();
        String nextHop = routingTable.get(destinationIP);
        if (nextHop != null) {
            String macAddress = arpTable.getMacAddress(nextHop);
            if (macAddress != null) {
                System.out.println("Forwarding packet to " + destinationIP + " via next hop " + nextHop + " with MAC " + macAddress);
            } else {
                System.out.println("MAC address for next hop " + nextHop + " not found.");
            }
        } else {
            System.out.println("No route to " + destinationIP);
        }
    }
}

// Main class to demonstrate network layer functionalities
public class NetworkLayer {

    public static void main(String[] args) {
        Router router1 = new Router("Router1");
        Router router2 = new Router("Router2");

        // Configure interfaces
        Interface router1Interface1 = new Interface("GigabitEthernet0/0", "00:1A:2B:3C:4D:5E");
        router1Interface1.configure("192.168.1.1", "255.255.255.0");
        router1.addInterface(router1Interface1);

        Interface router1Interface2 = new Interface("GigabitEthernet0/1", "00:1A:2B:3C:4D:5F");
        router1Interface2.configure("192.168.2.1", "255.255.255.0");
        router1.addInterface(router1Interface2);

        Interface router2Interface1 = new Interface("GigabitEthernet0/0", "00:1A:2B:3C:4D:6A");
        router2Interface1.configure("192.168.3.1", "255.255.255.0");
        router2.addInterface(router2Interface1);

        Interface router2Interface2 = new Interface("GigabitEthernet0/1", "00:1A:2B:3C:4D:6B");
        router2Interface2.configure("192.168.4.1", "255.255.255.0");
        router2.addInterface(router2Interface2);

        // Configure static routes
        router1.addRoute("192.168.3.0/24", "192.168.2.2");
        router2.addRoute("192.168.1.0/24", "192.168.4.2");

        // Add ARP entries
        router1.addArpEntry("192.168.2.2", "00:1A:2B:3C:4D:6A");
        router2.addArpEntry("192.168.4.2", "00:1A:2B:3C:4D:5F");

        // Run test cases
        runTestCases(router1, router2);
    }

    public static void runTestCases(Router router1, Router router2) {
        System.out.println("\nRunning Test Cases...\n");

        // Test case 1: Check ARP resolution
        System.out.println("Test Case 1: ARP Resolution");
        String macAddress = router1.getMacAddress("192.168.2.2");
        System.out.println(macAddress != null ? "MAC address for 192.168.2.2: " + macAddress : "MAC address for 192.168.2.2 not found.");

        // Test case 2: Forwarding a packet from router1 to router2
        System.out.println("\nTest Case 2: Forwarding Packet from Router1 to Router2");
        Packet packet1 = new Packet("192.168.1.2", "192.168.3.4", "Hello from Router1");
        router1.forwardPacket(packet1);

        // Test case 3: Forwarding a packet from router2 to router1
        System.out.println("\nTest Case 3: Forwarding Packet from Router2 to Router1");
        Packet packet2 = new Packet("192.168.3.4", "192.168.1.2", "Hello from Router2");
        router2.forwardPacket(packet2);

        // Test case 4: Display routing tables
        System.out.println("\nTest Case 4: Display Routing Tables");
        router1.showRoutingTable();
        router2.showRoutingTable();

        // Test case 5: Display ARP tables
        System.out.println("\nTest Case 5: Display ARP Tables");
        router1.showArpTable();
        router2.showArpTable();

        // Test case 6: OSPF configuration
        System.out.println("\nTest Case 6: OSPF Configuration");
        router1.configureOSPF("0", "192.168.1.0/24");
        router2.configureOSPF("0", "192.168.3.0/24");

        System.out.println("\nAll Test Cases Completed.");
    }
}
