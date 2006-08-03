Examples

These examples show some of the simpler uses of the MicroContainer.
Each example is in a subfolder with the following structure

readme.txt - a short description of the example
build.xml - the ant build script
src/resources/META-INF/jboss-beans.xml - the MicroContainer configuration for the example
src/main - the java source for the example

To run each example, simply 
> cd examples/<directory>
> ant

To run against the JBoss application server
> <edit> build.properties {change to point at your jboss instance}
> cd examples/<directory>
> ant deploy
followed by
> ant undeploy
to remove the deployment

simple: a simple example to make sure you have everything working
constructor: simple constructor configuration
factory: construction using factories
properties: property configuration
injection: referencing other beans
collections: creating collections
lifecycle: the create/start/stop/destroy lifecycle
locator: examples of how to use the micrcontainer to create a locator

This just skims the surface of the Microcontainer, showing the most common
usecases. Other more complicated examples can be found in the tests (available from cvs).