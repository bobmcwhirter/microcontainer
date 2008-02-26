Since the tests are not run automatically, here is how to run them since i keep forgetting:

mvn surefire-report:report -Ptests-no-weave
mvn surefire-report:report -Ptests-no-weave-secure
mvn surefire-report:report -Pant-tests-weave
mvn surefire-report:report -Pant-tests-weave-secure

To run one test
mvn surefire-report:report -Pant-one-test-weave -Dtest=org.jboss.test.microcontainer.test.DeployersInterceptedAspectManagerJMXTestCase
