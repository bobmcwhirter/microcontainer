package org.jboss.example.deployment;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;

public class MyDeployer extends AbstractDeployer {

	public MyDeployer() {
		// We want this deployer to be called after the installed stage
		setStage(new DeploymentStage("MyDeploymentStage", DeploymentStages.INSTALLED));
	}
	
	public void deploy(DeploymentUnit unit) throws DeploymentException {
		System.out.println("Calling deploy() within MyDeployer...");
	}
	
	public void undeploy(DeploymentUnit unit) {
		System.out.println("Calling undeploy() within MyDeployer...");
	}
}
