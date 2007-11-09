package org.jboss.example.aspect;

import org.jboss.aop.joinpoint.Invocation;

public class AuditAspect {

	public Object audit(Invocation inv) throws Throwable {
		System.out.println("Audit Aspect !!!");
		return inv.invokeNext();
	}
}
