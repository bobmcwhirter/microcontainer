package org.jboss.example.deployment;

import java.net.URL;
import java.net.URLClassLoader;

import org.jboss.deployers.spi.deployer.helpers.AbstractTopLevelClassLoaderDeployer;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.vfs.plugins.client.AbstractVFSDeployment;
import org.jboss.virtual.VirtualFile;

public class MyClassLoaderDeployer extends AbstractTopLevelClassLoaderDeployer {

	@Override
	protected ClassLoader createTopLevelClassLoader(DeploymentContext context) throws Exception {
		
		ClassLoader current = Thread.currentThread().getContextClassLoader();
		
		AbstractVFSDeployment deployment = (AbstractVFSDeployment) context.getDeployment();
		VirtualFile root = deployment.getRoot();
		URL url = root.getHandler().toURL();
		
		URLClassLoader cl = new URLClassLoader(new URL[] {url}, current);
		context.setClassLoader(cl);
		
		// Hack to get the PreInstallAction working
		Thread.currentThread().setContextClassLoader(cl);
		
		return context.getClassLoader();
	}
}
