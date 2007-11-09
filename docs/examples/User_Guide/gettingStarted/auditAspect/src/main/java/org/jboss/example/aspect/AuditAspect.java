package org.jboss.example.aspect;

import org.jboss.aop.joinpoint.Invocation;

/**
 * An auditing aspect to keep track of all invocations made to
 * methods on an object. A log file is used to store the audit
 * information.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class AuditAspect {

	public Object audit(Invocation inv) throws Throwable {
		System.out.println("Audit Aspect !!!");
		return inv.invokeNext();
	}
}
