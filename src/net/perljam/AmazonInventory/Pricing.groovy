package net.perljam.AmazonInventory

import java.awt.event.ItemEvent;

import groovy.json.JsonSlurper

class Pricing {

	// not officially supported, but it comes from AWS proper: http://stackoverflow.com/a/8676062/659298
	def pricingUrl = "http://aws.amazon.com/ec2/pricing/pricing-on-demand-instances.json"

	static main(args) {
		println "hello world!"
		def m = new Pricing();
		println m.getHourlyPrice("us-east", "m1.large" )
	}


	Pricing() {
		getPricing()
	}

	def pricing;
	def getPricing() {
		// do we already have it cached?
		if (pricing) {
			return;
		}

		def slurper = new JsonSlurper()
		pricing = slurper.parseText(pricingUrl.toURL().text)
	}

	void printPrices() {

		pricing.config.regions.find { region.equals(region) }.instanceTypes.each { instanceType ->
			println instanceType.type
			instanceType.sizes.each { instanceSize ->
				println instanceSize.size + " = "
				instanceSize.valueColumns.each { instanceValue ->
					println " " + instanceValue.name + " = " + instanceValue.prices.USD
				}
			}
		}

	}

	double getHourlyPrice(String region, String machineSpec) {

		def (mClass, mSize) = machineSpec.tokenize('.')[0..1]
		println "$mClass / $mSize"
		
		def regionTypes = pricing.config.regions.find { it.region.equals(region) }
		println "regionTypes: $regionTypes"
		
		def regionClass = regionTypes.instanceTypes.find { it.type.equals( getMachineClass(mClass)) }
		println "regionClass: $regionClass"
		
		def regionSize = regionClass.sizes.find { it.size.equals( getMachineSize(mSize)) }
		
		String price = regionSize.valueColumns.find { it.name.equals("linux") }.prices.USD
		return price.toDouble();
	}

	def machineClassMap = [ t1:'uODI', m1:'stdODI', m2:'hiMemODI', c1:'hiCPUODI', cc1:'clusterComputeI', cg1:'clusterGPUI'  ]
	String getMachineClass(String machineClass) {
		return machineClassMap.get(machineClass) ?: machineClass
	}

	def machineSizeMap = [ small:'sm', large:'lg', xlarge:'xl', micro:'u', '2xlarge':'xxl', '4xlarge':'xxxxl', medium:'med', '8xlarge':'xxxxxxxxl' ]
	String getMachineSize(String machineSize) {
		return machineSizeMap.get(machineSize) ?: machineSize
	}
}
