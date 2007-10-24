package org.jboss.example.client;

import org.jboss.kernel.plugins.bootstrap.standalone.StandaloneBootstrap;

/**
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class CmdLineClient {
    
    public static void main( String[] args ) throws Exception
    {
        System.out.println("Hello from the CmdLineClient main method");
        StandaloneBootstrap.main(args);
        System.out.println("Bye from the CmdLineClient main method");
    }

	public CmdLineClient() {
		System.out.println("Hello from the CmdLineClient constructor");
 	}
}
