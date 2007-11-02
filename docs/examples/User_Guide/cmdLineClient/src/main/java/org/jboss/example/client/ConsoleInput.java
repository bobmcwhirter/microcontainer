package org.jboss.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;

public class ConsoleInput {
	
	public ConsoleInput(final CmdLineClient client, final EmbeddedBootstrap bootstrap, final boolean useBus, final URL url) {
		printMenu();
		
		Thread eventThread = new Thread(new Runnable() {
			private boolean initialDeployment = false;
			private boolean quit = false;
			
			public void run() {
				
				while (!quit) {

					System.out.print(">");
					
					try {
						String input = in.readLine();
						if (input.length() != 1) {
							continue;
						}
	
						char option = input.charAt(0);
						if ((option == '2' || option == '3' || option == 'a' || option == 'l' || option == 'r'
							    || option == 'g' || option == 's' || option == 't' || option == 'p')
								&& !useBus && initialDeployment == false) {
							System.out.println("Service has not been deployed yet.");
							continue;
						}
						
						switch (option) {
							case '1': bootstrap.deploy(url); client.cacheServiceRef(); initialDeployment = true; break;
							case '2': bootstrap.undeploy(url); bootstrap.deploy(url); client.cacheServiceRef(); break;
							case '3': bootstrap.undeploy(url); break;
							case 'a': client.addEmployee(); break;
							case 'l': client.listEmployees(); break;
							case 'r': client.removeEmployee(); break;
							case 'g': client.getSalary(); break;
							case 's': client.setSalary(); break;
							case 't': client.toggleHiringFreeze(); break;
							case 'm': printMenu(); break;
							case 'p': client.printStatus(); break;
							case 'q': quit = true; break;
							default: break;
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
	
	private void printMenu() {
		System.out.println("-----------------------------------");
		System.out.println("Menu:");
		System.out.println();
		System.out.println("1) Deploy Human Resources service");
		System.out.println("2) Redeploy Human Resources service");
		System.out.println("3) Undeploy Human Resources service");
		System.out.println();
		System.out.println("a) Add employee");
		System.out.println("l) List employees");
		System.out.println("r) Remove employee");
		System.out.println("g) Get a salary");
		System.out.println("s) Set a salary");
		System.out.println("t) Toggle hiring freeze");
		System.out.println();
		System.out.println("m) Display menu");
		System.out.println("p) Print service status");
		System.out.println("q) Quit");
	}

	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public static Employee getEmployee() throws IllegalArgumentException, IOException {	

		System.out.println("Please enter the employee's name [firstName lastName]:");
		String name = in.readLine();
		String[] names = name.split("\\s");
		if (names.length != 2) { throw new IllegalArgumentException("Employees must have a first and last name."); }
		return new Employee(names[0], names[1]);
	}
	
	public static Address getAddress() throws NumberFormatException, IllegalArgumentException, IOException {
	
		System.out.println("Please enter the employee's address [number,street,city]:");
		String address = in.readLine();
		String[] lines = address.split(",");
		if (lines.length != 3) { throw new IllegalArgumentException("Addresses must contain a number, street and city."); }
		return new Address(Integer.parseInt(lines[0]), lines[1], lines[2]);
	}
	
	public static Date getDateOfBirth() throws ParseException, IOException {
	
		System.out.println("Please enter the employee's date of birth [dd/MM/yyyy]:");
		String date = in.readLine();
		return new SimpleDateFormat("dd/MM/yyyy").parse(date);
	}
	
	public static Integer getSalary()  throws NumberFormatException, IOException {	
	
		System.out.println("Please enter the employee's new salary [integer]: ");
		String salary = in.readLine();
		return Integer.valueOf(salary);
	}
}
