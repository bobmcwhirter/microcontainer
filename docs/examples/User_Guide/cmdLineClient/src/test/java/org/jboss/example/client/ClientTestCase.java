package org.jboss.example.client;

import junit.framework.TestCase;

public class ClientTestCase extends TestCase
{	
    public ClientTestCase(String name) {
		super(name);
	}

	public void testClientWithoutBus() throws Exception {
		Client client = new Client(false);
		assertNotNull(client);
		runTests(client);
	}
	
	public void testClientWithBus() throws Exception {
		Client client = new Client(true);
		assertNotNull(client);
		runTests(client);
	}
	
	private void runTests(Client client) throws Exception {
		client.setUserInterface(new MockUserInterface());
		
		client.deploy();

		assertEquals(true, client.toggleHiringFreeze());
		assertEquals(false, client.addEmployee());
		assertEquals(0, client.listEmployees().size());

		assertEquals(false, client.toggleHiringFreeze());
		assertEquals(true, client.addEmployee());
		assertEquals(1, client.listEmployees().size());
		assertEquals((Integer) 10000, client.getSalary());
		
		client.setSalary();
		assertEquals((Integer) 50000, client.getSalary());
		
		client.removeEmployee();
		assertEquals(0, client.listEmployees().size());
		
		client.undeploy();
	}
}
