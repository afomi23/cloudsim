package cloudsim.example;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.UtilizationModelFull;


import java.util.*;

public class PrivateCloudSimulation {

	public static void main(String[] args) {
		 try {
	            
	            int numUsers = 1;
	            Calendar calendar = Calendar.getInstance();
	            boolean traceFlag = false;
	            
	            
	            CloudSim.init(numUsers, calendar, traceFlag);
	            
	            Datacenter datacenter = createDatacenter("PrivateDatacenter");

	            DatacenterBroker broker = new DatacenterBroker("Broker");
	            
	            int brokerId = broker.getId();

	            // Create VM list
	            List<Vm> vmList = new ArrayList<>();
	            int VmCount = 4;
	            for (int i = 0; i < VmCount; i++) {
	            Vm vm = new Vm(i, brokerId, 1000, 1, 2048, 10000, 1000, "Xen",
	            		new CloudletSchedulerTimeShared());
	           
	            vmList.add(vm);
	            
	            }
	            
	            broker.submitVmList(vmList);

	            // Create cloudlet list
	            List<Cloudlet> cloudletList = new ArrayList<>();
	            int cloudletCount = 10;
	            for (int i = 0; i < cloudletCount; i++) {
	            	UtilizationModel utilization = new UtilizationModelFull();
	            	
	            Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300, 
	            		utilization, utilization, utilization);
	            
	            cloudlet.setUserId(brokerId);
	            cloudlet.setVmId(vmList.get(i % VmCount).getId()); 
	            cloudletList.add(cloudlet);
	            
	         }
	            broker.submitCloudletList(cloudletList);

	            CloudSim.startSimulation();
	            
	            
	            List<Cloudlet> resultList = broker.getCloudletReceivedList();
	            CloudSim.stopSimulation();

	     
	            printCloudletResults(resultList, vmList);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static Datacenter createDatacenter(String name) throws Exception {
	        List<Host> hostList = new ArrayList<>();
	        
	        List<Pe> peList = new ArrayList<>();
	        peList.add(new Pe(0, new PeProvisionerSimple(1000))); 
	        
	        int ram = 8192; // 8MB
	        long storage = 1000000; 
	        int bw = 10000; 

	        Host host = new Host(
	                0,
	                new RamProvisionerSimple(ram),
	                new BwProvisionerSimple(bw),
	                storage,
	                peList,
	                new VmSchedulerTimeShared(peList));
	        hostList.add(host);

	        String arch = "x86";
	        String os = "Linux";
	        String vmm = "Xen";
	        double timeZone = 10.0;
	        double costPerSec = 3.0;
	        double costPerMem = 0.05;
	        double costPerStorage = 0.001;
	        double costPerBw = 0.0;
	        
	        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
	                arch, os, vmm, hostList, timeZone, costPerSec, costPerMem, costPerStorage, costPerBw );

	        return new Datacenter(name, characteristics, 
	        		new VmAllocationPolicySimple(hostList), new LinkedList<Storage>(), 0);
	    }

	    private static void printCloudletResults(List<Cloudlet> list, List<Vm> vmList) {
	    	 System.out.println("\n========== Cloudlet Execution Results ==========");
	         System.out.println("CloudletID\tStatus\t\tVMID\tTime\tStart\tFinish");

	         for (Cloudlet cloudlet : list) {
	             if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
	                 System.out.printf("%d\t\tSUCCESS\t\t%d\t%.2f\t%.2f\t%.2f\n",
	                         cloudlet.getCloudletId(),
	                         cloudlet.getVmId(),
	                         cloudlet.getActualCPUTime(),
	                         cloudlet.getExecStartTime(),
	                         cloudlet.getFinishTime());
	             }
	         }

	         System.out.println("========== Simulation Summary ==========");
	         System.out.println("Total VMs created: " + vmList.size());
	         System.out.println("Total Cloudlets executed: " + list.size());

	         double totalFinishTime = list.stream()
	                 .mapToDouble(Cloudlet::getFinishTime).max().orElse(0);
	         System.out.printf("Total Execution Time: %.2f\n", totalFinishTime);
	     }
 }