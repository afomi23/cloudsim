# cloudsim
This project is a hands-on exploration of Private Cloud Infrastructure. Using the CloudSim framework, we built a digital twin of a dedicated server environment. The goal was to see how virtualizing physical hardware—like turning one powerful server into multiple virtual ones—affects how efficiently we can process a heavy workload of tasks.

 The Project Setup
we developed this simulation using Java 23 within the Eclipse IDE. It relies on the CloudSim 3.0.3 engine to handle the complex math and event scheduling.

Core Logic: Found in PrivateCloudSimulation.java.

Organization: Structured under the cloudsim.example package.

Key Libraries: CloudSim 3.0.3 and Apache Commons Math 2.1.

How the Simulation Works
The simulation is broken down into three "layers" that mimic a real-world data center:

1. The Physical Layer (The Hardware)
Everything starts with a single, robust physical server named "PrivateDatacenter".

CPU Power: 1000 MIPS (Million Instructions Per Second).

Memory: 8GB RAM.

Storage & Network: 1TB storage and a 10Gbps connection.

2. The Virtual Layer (The "Cloud")
Using a Xen VMM, I split that hardware into 4 Virtual Machines (VMs).

Each VM is assigned 2GB of RAM and 1000 MIPS.

we  used a Time-Shared Scheduler, which allows the VMs to multitask and process multiple requests simultaneously, just like a real server would.

3. The Workload (The Tasks)
we generated 10 Cloudlets (computational tasks) to test the system.

Complexity: Each task requires 40,000 million instructions to complete.

Smart Distribution: To keep the load balanced, we used a Round-Robin approach (assigning tasks to VMs in order: 1, 2, 3, 4, then back to 1).

 Getting Started
To get this running on your local machine:

Check your Java version: You'll need Java 23 or later.

Set up dependencies: Make sure the CloudSim JAR files (found in the lib folder) are added to your project's Build Path.

Run: Launch PrivateCloudSimulation.java as a standard Java Application.

 Reading the Results
Once the simulation finishes, it prints a clean report to the console:

Execution Logs: See exactly which VM handled which task, along with its start and end times.


Efficiency Summary: A final look at the total number of VMs created and the "Wall Clock" time it took to finish all 10 tasks.
