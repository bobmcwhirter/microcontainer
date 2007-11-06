package org.jboss.example.client;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;
import org.jboss.example.service.HRManager;
import org.jboss.example.service.util.AgeBasedSalaryStrategy;
import org.jboss.example.service.util.LocationBasedSalaryStrategy;
import org.jboss.example.service.util.SalaryStrategy;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistry;

public class Client {
    
	private boolean useBus = false;
	private URL url;
	private UserInterface userInterface;
	private HRManager manager;
	
	private EmbeddedBootstrap bootstrap;
	private Kernel kernel;
	private KernelRegistry registry;
	private KernelBus bus;

	private final static String HRSERVICE = "HRService";
	private final static String EMPLOYEE = "org.jboss.example.service.Employee";

	public static void main(String[] args) throws Exception {
		if ((args.length == 1 && !args[0].equals("bus")) || args.length > 1) {
			System.out.println("Usage: java -jar client-1.0.0.jar [bus]");
			System.exit(1);
		}

		Client client = new Client(args.length == 1);
		client.setUserInterface(new ConsoleInput(client));
    }

	public Client(final boolean useBus) throws Exception {
		this.useBus = useBus;
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		url = cl.getResource("service-beans.xml");
	
		// Start JBoss Microcontainer
		bootstrap = new EmbeddedBootstrap();
		bootstrap.run();
		
		kernel = bootstrap.getKernel();
		registry = kernel.getRegistry();
		bus = kernel.getBus();		
 	}
	
	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	void deploy() {
		bootstrap.deploy(url);
		if (manager == null) {
			KernelControllerContext context = (KernelControllerContext) registry.getEntry(HRSERVICE);
			if (context != null) { manager = (HRManager) context.getTarget(); }
		}
	}
	
	void undeploy() {
		bootstrap.undeploy(url);
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
		
		String strategy = "Unknown";
		if (salaryStrategy == null) { strategy = "None"; }
		else if (salaryStrategy instanceof AgeBasedSalaryStrategy ) { strategy = "AgeBased"; }
		else if (salaryStrategy instanceof LocationBasedSalaryStrategy ) { strategy = "LocationBased"; }
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Total number of employees: " + totalEmployees);
		buffer.append("\nHiring Freeze: " + hiringFreeze);	
		buffer.append("\nSalary Strategy: " + strategy);
		if (salaryStrategy != null) {
			buffer.append(" - MinSalary: " + salaryStrategy.getMinSalary() + " MaxSalary: " + salaryStrategy.getMaxSalary());
		}
		return buffer.toString();
	}
}
