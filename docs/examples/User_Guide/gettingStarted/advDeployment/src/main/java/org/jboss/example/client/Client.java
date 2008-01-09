package org.jboss.example.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.plugins.main.MainDeployerImpl;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.client.VFSDeploymentFactory;
import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;
import org.jboss.example.service.HRManager;
import org.jboss.example.service.util.SalaryStrategy;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * A simple client that starts JBoss Microcontainer and then  
 * uses the command line as a User Interface to pass requests
 * to the Human Resources service either directly or via the bus.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class Client {
    
	private boolean useBus = false;
	private URL deployerBeansUrl, hrServiceBeansUrl;
	private UserInterface userInterface;
	private HRManager manager;
	private MainDeployerImpl mainDeployer;
	private Deployment deployment;
	
	private EmbeddedBootstrap bootstrap;
	private KernelController controller;
	private KernelBus bus;

	private final static String MAIN_DEPLOYER = "MainDeployer";
	private final static String DEPLOYMENT = "org.jboss.deployers.client.spi.Deployment";
	private final static String HRSERVICE = "HRService";
	private final static String EMPLOYEE = "org.jboss.example.service.Employee";

	public static void main(String[] args) throws Exception {
		if ((args.length == 1 && !args[0].equals("bus")) || args.length > 1) {
			System.out.println("Usage: ./run.sh [bus]");
			System.exit(1);
		}

		Client client = new Client(args.length == 1);
		client.setUserInterface(new ConsoleInput(client));
    }

	public Client(final boolean useBus) throws Exception {
		this.useBus = useBus;
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		deployerBeansUrl = cl.getResource("deployer-beans.xml");
		hrServiceBeansUrl = cl.getResource("hrService-beans.xml");
		
		if (hrServiceBeansUrl == null) {
			hrServiceBeansUrl = cl.getResource("hrService-beans.properties");
		}
		
		if (hrServiceBeansUrl == null) {
			hrServiceBeansUrl = cl.getResource("humanResourcesService-1.0.0.jar");
		}
		
		// Create VFSDeployment to use with aspectized deployers
		File hrServiceBeans = new File(hrServiceBeansUrl.getFile());
		VirtualFile root = VFS.getRoot(hrServiceBeans.toURI());	    
		VFSDeploymentFactory deploymentFactory = VFSDeploymentFactory.getInstance();
	    deployment = deploymentFactory.createVFSDeployment(root);
		
		// Start JBoss Microcontainer
		bootstrap = new EmbeddedBootstrap();
		bootstrap.run();
		
		controller = bootstrap.getKernel().getController();
		bus = bootstrap.getKernel().getBus();		
 	}
	
	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	void deploy() {
		bootstrap.deploy(deployerBeansUrl);	
		if (!useBus && mainDeployer == null) {
			ControllerContext context = controller.getInstalledContext(MAIN_DEPLOYER);
			if (context != null) { mainDeployer = (MainDeployerImpl) context.getTarget(); }			
		}
	}
	
	void undeploy() {
		bootstrap.undeploy(deployerBeansUrl);
	}
	
	void deployService() throws DeploymentException { 
		if (useBus) {
			if (invoke(MAIN_DEPLOYER, "getDeployment", new Object[] {deployment.getName()}, new String[] {"java.lang.String"}) != null) {
				System.out.println("Service is already deployed.");
				return;
			}			
			
			invoke(MAIN_DEPLOYER, "addDeployment", new Object[] {deployment}, new String[] {DEPLOYMENT});
			invoke(MAIN_DEPLOYER, "process", new Object[] {}, new String[] {});
		} else {
			if (mainDeployer.getDeployment(deployment.getName()) != null) {
				System.out.println("Service is already deployed.");
				return;
			}
			
	        mainDeployer.addDeployment(deployment);
	        mainDeployer.process();
	        
	        if (manager == null) {
				ControllerContext hrServiceCtx = controller.getInstalledContext(HRSERVICE);
				if (hrServiceCtx != null) { manager = (HRManager) hrServiceCtx.getTarget(); }	        	
	        }
		}
	}

	void undeployService() throws DeploymentException {		
		if (useBus) {
			if (invoke(MAIN_DEPLOYER, "getDeployment", new Object[] {deployment.getName()}, new String[] {"java.lang.String"}) == null) {
				System.out.println("Service is already undeployed.");
				return;
			}			
			
			invoke(MAIN_DEPLOYER, "removeDeployment", new Object[] {deployment}, new String[] {DEPLOYMENT});
			invoke(MAIN_DEPLOYER, "process", new Object[] {}, new String[] {});
		} else {
			if (mainDeployer.getDeployment(deployment.getName()) == null) {
				System.out.println("Service is already undeployed.");
				return;
			}
			
			mainDeployer.removeDeployment(deployment);
		    mainDeployer.process();
		}
	}

	private Object invoke(String serviceName, String methodName, Object[] args, String[] types) {
		Object result = null;
		try {
			result = bus.invoke(serviceName, methodName, args, types);
		} catch (Throwable t) {
			t.printStackTrace();
		}	
		return result;
	}
	
	boolean addEmployee() throws ParseException, NumberFormatException, IllegalArgumentException, IOException {
		Employee newEmployee = userInterface.getEmployee();		
		Address address = userInterface.getAddress();
		Date dateOfBirth = userInterface.getDateOfBirth();		
		newEmployee.setAddress(address);
		newEmployee.setDateOfBirth(dateOfBirth);
		
		if (useBus)
			return (Boolean) invoke(HRSERVICE, "addEmployee", new Object[] {newEmployee}, new String[] {EMPLOYEE});
		else
			return manager.addEmployee(newEmployee);			
	}
	
	@SuppressWarnings("unchecked")
	Set<Employee> listEmployees() {			
		if (useBus)
			return (Set<Employee>) invoke(HRSERVICE, "getEmployees", new Object[] {}, new String[] {});
		else
			return manager.getEmployees();
	}
	
	void removeEmployee() throws IllegalArgumentException, IOException {			
		Employee employee = userInterface.getEmployee();
		
		if (useBus)
			invoke(HRSERVICE, "removeEmployee", new Object[] {employee}, new String[] {EMPLOYEE});
		else
			manager.removeEmployee(employee);
	}
	
	Integer getSalary() throws IllegalArgumentException, IOException {
		Employee employee = userInterface.getEmployee();

		if (useBus)
			return(Integer) invoke(HRSERVICE, "getSalary", new Object[] {employee}, new String[] {EMPLOYEE});
		else
			return manager.getSalary(employee);
	}
	
	void setSalary() throws NumberFormatException, IllegalArgumentException, IOException {
		Employee employee = userInterface.getEmployee();	
		Integer salary = userInterface.getSalary();		
		
		Employee actualEmployee;
		if (useBus) {
			actualEmployee = (Employee) invoke(HRSERVICE, "getEmployee", new Object[] {employee.getFirstName(), employee.getLastName()}, new String[] {"java.lang.String","java.lang.String"});	
			invoke(HRSERVICE, "setSalary", new Object[] {actualEmployee, salary}, new String[] {EMPLOYEE, "java.lang.Integer"});	
		} else {
			actualEmployee = manager.getEmployee(employee.getFirstName(), employee.getLastName());
			manager.setSalary(actualEmployee, salary);			
		}			
	}
	
	boolean toggleHiringFreeze() {
		boolean hiringFreeze;
		if (useBus) {
			hiringFreeze = (Boolean) invoke(HRSERVICE, "isHiringFreeze", new Object[] {}, new String[] {});	
			invoke(HRSERVICE, "setHiringFreeze", new Object[] {!hiringFreeze}, new String[] {"boolean"});	
		} else {
			hiringFreeze = manager.isHiringFreeze();
			manager.setHiringFreeze(!hiringFreeze);
		}
		return !hiringFreeze;
	}
	
	@SuppressWarnings("unchecked")
	String printStatus() {
		boolean hiringFreeze;
		int totalEmployees;
		SalaryStrategy salaryStrategy;
		
		if (useBus) {
			hiringFreeze = (Boolean) invoke(HRSERVICE, "isHiringFreeze", new Object[] {}, new String[] {});
			Set<Employee> employees = (Set<Employee>) invoke(HRSERVICE, "getEmployees", new Object[] {}, new String[] {});
			totalEmployees = employees.size();				
			salaryStrategy = (SalaryStrategy) invoke(HRSERVICE, "getSalaryStrategy", new Object[] {}, new String[] {});
		} else {
			hiringFreeze = manager.isHiringFreeze();
			totalEmployees = manager.getEmployees().size();
			salaryStrategy = manager.getSalaryStrategy();		
		}	
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Total number of employees: " + totalEmployees);
		buffer.append("\nHiring Freeze: " + hiringFreeze);	
		buffer.append("\nSalary Strategy: ");
		if (salaryStrategy == null) {
			buffer.append("None");
		} else {
			buffer.append(salaryStrategy.toString());
		}
		return buffer.toString();
	}
}
