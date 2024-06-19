# ITL355: Computer Networks Lab - Network Simulator Project

This repository contains the implementation of a network simulator covering the entire protocol stack, developed in Java as part of the ITL355 Computer Networks Lab.

## Project Overview

The project is divided into three submissions, each focusing on implementing different layers of the protocol stack:

1. **Submission 1: Physical and Data Link Layer Functionalities**
2. **Submission 2: Network Layer Functionalities**
3. **Submission 3: Transport and Application Layer Functionalities**

## Submission 1: Physical and Data Link Layer Functionalities

### Objectives
- Implement Physical and Data Link layer functionalities.

### Minimum Deliverables
#### Physical Layer
- Create end devices and hubs.
- Create connections to form network topologies.
- Enable data transmission and reception.

#### Data Link Layer
- Create Layer 2 devices: bridge and switch.
- Implement address learning for switches.
- Implement an error control protocol (e.g., Parity Check).
- Implement an access control protocol (e.g., CSMA/CD).
- Implement a sliding window-based flow control protocol.

### Test Cases
#### Test Case 1: Two End Devices with Dedicated Connection
- **Scenario:** Establish a dedicated connection between two end devices.
- **Test Steps:**
  - Create two end devices with a direct connection.
  - Demonstrate data transmission between them.

#### Test Case 2: Star Topology with Five End Devices Connected to a Hub
- **Scenario:** Create a star topology with a hub connecting five end devices.
- **Test Steps:**
  - Create a hub and connect five end devices.
  - Show communication within the star topology.

#### Test Case 3: Switch with Five End Devices Connected to It
- **Scenario:** Create a switch with five end devices connected to it.
- **Test Steps:**
  - Implement and demonstrate error control, access control, and sliding window flow control protocols between end devices.

#### Test Case 4: Two Star Topologies Connected via a Switch
- **Scenario:** Connect two star topologies via a switch and enable communication between all devices.
- **Test Steps:**
  - Create two star topologies with hubs and connect them via a switch.
  - Show communication between all connected end devices.

## Submission 2: Network Layer Functionalities

### Objectives
- Implement Network layer functionalities.

### Minimum Deliverables
- Create and configure a router.
- Assign IPv4 addresses to devices.
- Use ARP for MAC address resolution.
- Implement static routing.
- Implement a dynamic routing protocol (RIP or OSPF).

### Test Cases
#### Test Case 1: ARP Resolution
- **Scenario:** Resolve MAC address for a given IP address using ARP.
- **Test Steps:**
  - Perform ARP resolution and display the resolved MAC address.

#### Test Case 2: Forwarding Packets Between Routers
- **Scenario:** Forward packets between two routers with static routing configured.
- **Test Steps:**
  - Simulate packet forwarding between two routers.
  - Verify the routing table entries.

#### Test Case 3: OSPF Configuration
- **Protocol:** OSPF.
- **Test Steps:**
  - Configure OSPF for dynamic routing between routers.
  - Compute shortest paths and populate routing tables.

## Submission 3: Transport and Application Layer Functionalities

### Objectives
- Implement Transport and Application layer functionalities.

### Minimum Deliverables
#### Transport Layer
- Assign port numbers to processes.
- Enable process-process communication using TCP or UDP.
- Implement at least one sliding window flow control protocol (Go Back N or Selective Repeat).

#### Application Layer
- Implement at least two application layer services (e.g., HTTP, FTP).

### Test Cases
#### Test Case 1: Process-Process Communication
- **Scenario:** Establish communication between two processes using assigned port numbers.
- **Test Steps:**
  - Assign port numbers to processes on two end devices.
  - Demonstrate data exchange using TCP or UDP.

#### Test Case 2: Sliding Window Flow Control
- **Protocol:** Go-Back-N or Selective Repeat.
- **Test Steps:**
  - Implement and demonstrate sliding window flow control protocol.
  - Show transmission, acknowledgment, and retransmission (if necessary) of segments.

#### Test Case 3: Application Layer Services
- **Service 1:** HTTP Request
  - **Functionality:** Send an HTTP GET request between two end devices.
  - **Test Steps:**
    - Initiate an HTTP request and receive the response.
  
- **Service 2:** FTP Request
  - **Functionality:** Transfer a file using FTP between two end devices.
  - **Test Steps:**
    - Initiate an FTP request to download a file and verify the successful transfer.
## Cloning and Usage

To clone this repository and use the network simulator, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/network-simulator.git
   cd network-simulator
Compile Java Files:
Compile each Java file using Java compiler (javac):

- javac PhysicalAndDataLinkLayer.java
- javac NetworkLayer.java
- javac TransportAndApplicationLayer.java
Run the Simulations:
Execute each Java file to simulate the respective layer functionalities:


java PhysicalAndDataLinkLayer
java NetworkLayer
java TransportAndApplicationLayer


Make sure to replace `https://github.com/your-username/network-simulator.git` with the actual URL of your repository. This section provides clear, concise instructions on how to clone the repository, compile the Java files, run the simulations, and verify the output for your network simulator project. Adjust the commands and paths as necessary based on your project structure and setup.


## Implementation Notes
- Each submission is implemented in Java.
- Ensure all Java files (`PhysicalAndDataLinkLayer.java`, `NetworkLayer.java`, `TransportAndApplicationLayer.java`, and related classes) are compiled and executed to simulate the network functionalities.
- Proper documentation, comments, and citations for external sources (if any) are included in the project report.


## License
TUSHAR VERMA
