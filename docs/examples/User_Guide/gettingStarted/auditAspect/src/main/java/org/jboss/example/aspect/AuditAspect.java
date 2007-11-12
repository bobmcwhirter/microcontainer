package org.jboss.example.aspect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * An auditing aspect to keep track of all invocations made to
 * methods on an object. A log file is used to store the audit
 * information.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class AuditAspect {

	private String logDir;
	private BufferedWriter out;
	
	public AuditAspect() {
		logDir = System.getProperty("user.dir") + "/log";
		
		File directory = new File(logDir);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}
	
	public Object audit(ConstructorInvocation inv) throws Throwable {
		File auditLog = new File(logDir + "/" + createFilename());
		auditLog.createNewFile();
		out = new BufferedWriter(new FileWriter(auditLog));
		return inv.invokeNext();		
	}
	
	public Object audit(MethodInvocation inv) throws Throwable {		
		String name = inv.getMethod().getName();
		Object[] args = inv.getArguments();
		Object retVal = inv.invokeNext();
		
		StringBuffer buffer = new StringBuffer();
		for (int i=0; i < args.length; i++) {
			if (i > 0) {
				buffer.append(", ");
			}
			buffer.append(args[i].toString());
		}
		
		if (out != null) {
			out.write("Method: " + name);
			if (buffer.length() > 0) {
				out.write(" Args: " + buffer.toString());
			}
			if (retVal != null) {
				 out.write(" Return: " + retVal.toString());
			}
			out.write("\n");
			out.flush();
		}
		return retVal;
	}
	
	private String createFilename() {
		Calendar now = Calendar.getInstance();
		String year = Integer.toString(now.get(Calendar.YEAR));
		String month = Integer.toString(now.get(Calendar.MONTH));
		String day = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(now.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(now.get(Calendar.MINUTE));
		String sec = Integer.toString(now.get(Calendar.SECOND));
		
		return "auditLog-" + day + month + year + "-" + hour + min + sec;			
	}
}
