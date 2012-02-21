package net.perljam.AmazonInventory

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesResult
import com.amazonaws.services.ec2.model.Instance



class Main {

	static main(args) {
		println "hello world!"
		def m = new Main();
		//m.getInstances()
		m.getRegions();
	}


	AmazonEC2 ec2 = null;
	Main() {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJBV5WIWLJKOFPFJQ", "diTOtB2lwJKo2gjLvN6g5q3Mww62x3KtiRDuzoDu");
		ec2 = new AmazonEC2Client(credentials);
	}
	
	def getRegions() {
		ec2.describeRegions().getRegions().each { reg ->
			println reg.getRegionName() + " " + reg.getEndpoint();
		}
	}
	
	def getInstances() {
		DescribeInstancesResult ir = ec2.describeInstances()
		def machines = []
		
		ir.getReservations().each { reservation ->
			reservation.getInstances().each { instance ->
				machines.add( new Machine(instance) )
				
				//				println "id: " + instance.getInstanceId()
				//				println "state: " + instance.getState().getName()
				//
				//				println "launch time: " + instance.getLaunchTime()
				//				println "instance type: " + instance.getInstanceType()
				
//				def tags = instance.getTags()
//				tags.each { tag ->
//					println " " + tag.getKey() + ":" + tag.getValue();
//				}
			}
		}
		
		println machines
	}
	
	
	class Machine {
		private Instance instance = null;
		
		Machine( Instance newInstance  ) {
			this.instance = newInstance
		}
		
		public String getName() {
			return instance.getTags().find { it.getKey().equals("Name") }?.getValue()
		}
		
		public String toString() {
			if (instance == null ) {
				return  "null instance."
			}
			
			return instance.getInstanceId() + " " + instance.getState().getName() + " " + instance.getInstanceType() + " " + getName();
		}
		
		public String getPrice() {
			def p = new Pricing()
		}
		
	}
}
