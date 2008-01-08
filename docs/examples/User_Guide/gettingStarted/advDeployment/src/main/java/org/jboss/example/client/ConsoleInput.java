package org.jboss.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;

/**
 * A simple Text User Interface (TUI) so a user can access
 * the Human Resources service from the command line.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class ConsoleInput implements UserInterface {
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public ConsoleInput(final Client client) {
		System.out.println(getMenu());
		
		Thread eventThread = new Thread(new Runnable() {
			private boolean initialDeployment = false;
			private boolean quit = false;
			
			public void run() {
				
				while (!quit) {

					System.out.print(">");
					
					try {
						String input = in.readLine();
						if (input.length() != 1) {
							System.out.println("Please enter a valid option.");
							continue;
						}
	
						char option = input.charAt(0);
						if (initialDeployment == false &&
						    (option == 'u' || option == 'a' || option == 'l' || option == 'r' ||
							 option == 'g' || option == 's' || option == 't' || option == 'p')) {
							System.out.println("Service has not been deployed yet.");
							continue;
						}
						
						switch (option) {
							case 'd': client.deploy(); initialDeployment = true; break;
							case 'u': client.undeploy(); break;
							case 'a': System.out.println("Added employee: " + client.addEmployee()); break;
							case 'l': System.out.println("Employees: " + client.listEmployees()); break;
							case 'r': client.removeEmployee(); break;
							case 'g': System.out.println("Salary: " + client.getSalary()); break;
							case 's': client.setSalary(); break;
							case 't': System.out.println("Hiring Freeze: " + client.toggleHiringFreeze()); break;
							case 'm': System.out.println(getMenu()); break;
							case 'p': System.out.println(client.printStatus()); break;
							case 'q': quit = true; break;
							default: System.out.println("Invalid option."); break;
						}
					} catch (ParseException e) {
						System.out.println(e.getMessage());
					} catch (NumberFormatException e) {
						System.out.println("Invalid integer " + e.getMessage());
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
			}
		});
		
		eventThread.start();			
	}
	
	private String getMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("-----------------------------------\n");
		buffer.append("Menu:\n");
		buffer.append("\n");
		buffer.append("d) Deploy Human Resources service\n");
		buffer.append("u) Undeploy Human Resources service\n");
		buffer.append("\n");
		buffer.append("a) Add employee\n");
		buffer.append("l) List employees\n");
		buffer.append("r) Remove employee\n");
		buffer.append("g) Get a salary\n");
		buffer.append("s) Set a salary\n");
		buffer.append("t) Toggle hiring freeze\n");
		buffer.append("\n");
		buffer.append("m) Display menu\n");
		buffer.append("p) Print service status\n");
		buffer.append("q) Quit");
		return buffer.toString();
	}

	public Employee getEmployee() throws IllegalArgumentException, IOException {	

		System.out.println("Please enter the employee's name [firstName lastName]:");
		String name = in.readLine();
		String[] names = name.split("\\s");
		if (names.length != 2) { throw new IllegalArgumentException("Employees must have a first and last name."); }
		return new Employee(names[0], names[1]);
	}
	
	public Address getAddress() throws NumberFormatException, IllegalArgumentException, IOException {
	
		System.out.println("Please enter the employee's address [number,street,city]:");
		String address = in.readLine();
		String[] lines = address.split(",");
		if (lines.length != 3) { throw new IllegalArgumentException("Addresses must contain a number, street and city."); }
		return new Address(Integer.parseInt(lines[0]), lines[1], lines[2]);
	}
	
	public Date getDateOfBirth() throws ParseException, IOException {
	
		System.out.println("Please enter the employee's date of birth [dd/MM/yyyy]:");
		String date = in.readLine();
		return new SimpleDateFormat("dd/MM/yyyy").parse(date);
	}
	
	public Integer getSalary()  throws NumberFormatException, IOException {	
	
		System.out.println("Please enter the employee's new salary [integer]: ");
		String salary = in.readLine();
		return Integer.valueOf(salary);
	}
}
