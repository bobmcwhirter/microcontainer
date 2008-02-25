package org.jboss.test.bundle;

import static org.osgi.framework.Constants.BUNDLE_ACTIVATOR;
import static org.osgi.framework.Constants.BUNDLE_CLASSPATH;
import static org.osgi.framework.Constants.BUNDLE_DESCRIPTION;
import static org.osgi.framework.Constants.BUNDLE_LOCALIZATION;
import static org.osgi.framework.Constants.BUNDLE_MANIFESTVERSION;
import static org.osgi.framework.Constants.BUNDLE_NAME;
import static org.osgi.framework.Constants.BUNDLE_REQUIREDEXECUTIONENVIRONMENT;
import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_UPDATELOCATION;
import static org.osgi.framework.Constants.BUNDLE_VERSION;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;

import junit.framework.Test;

import org.jboss.osgi.plugins.facade.BundleHeaders;
import org.jboss.osgi.plugins.metadata.AbstractOSGiMetaData;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.jboss.test.bundle.metadata.AbstractManifestTestCase;
import org.osgi.framework.Version;

public class BundleHeaderTestCase extends AbstractManifestTestCase
{

   public BundleHeaderTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BundleHeaderTestCase.class);
   }

   @SuppressWarnings("unchecked")
   public void testGetHeadersNoLocalization() throws Exception
   {
      Dictionary<String, Object> headers = getBundleHeaders(
            getMetaData("/org/jboss/test/bundle/metadata/SimpleManifest.mf"))
            .getHeaders(Locale.getDefault().toString());
      assertNotNull(headers);
      assertEquals(headers.get(BUNDLE_ACTIVATOR), "org.jboss.test.bundle.metadata.BundleActivator");
      List<String> classpath = Arrays.asList("test.jar", "mc.jar", "seam.jar");
      assertEquals(classpath, headers.get(BUNDLE_CLASSPATH));
      assertEquals("TestHeadersManifest", headers.get(BUNDLE_DESCRIPTION));
      assertEquals("OSGI-INF/l10n/bundle", headers.get(BUNDLE_LOCALIZATION));
      assertEquals(2, headers.get(BUNDLE_MANIFESTVERSION));
      assertEquals("TestBundle", headers.get(BUNDLE_NAME));
      assertEquals("UniqueName", headers.get(BUNDLE_SYMBOLICNAME));
      URL url = new URL("file://test.jar");
      assertEquals(url, headers.get(BUNDLE_UPDATELOCATION));
      Version version = new Version("1.2.3.GA");
      assertEquals(version, headers.get(BUNDLE_VERSION));
      List<String> env = Arrays.asList("ena", "dva", "tri");
      assertEquals(env, headers.get(BUNDLE_REQUIREDEXECUTIONENVIRONMENT));
   }

   @SuppressWarnings("unchecked")
   public void testGetHeadersWithDefaultLocalization() throws Exception
   {
      Dictionary<String, Object> headers = getBundleHeaders(
            getMetaData("/org/jboss/test/bundle/metadata/LocalizedManifest.mf")).getHeaders(
            new Locale("en", "US").toString());
      assertNotNull(headers);
      assertEquals(headers.get(BUNDLE_ACTIVATOR), "org.jboss.test.bundle.metadata.BundleActivator");
      List<String> classpath = Arrays.asList("test.jar", "mc.jar", "seam.jar");
      assertEquals(classpath, headers.get(BUNDLE_CLASSPATH));
      assertEquals("Description with Locale( en_US )", headers.get(BUNDLE_DESCRIPTION));
      assertEquals("OSGI-INF/l10n/bundle", headers.get(BUNDLE_LOCALIZATION));
      assertEquals(2, headers.get(BUNDLE_MANIFESTVERSION));
      assertEquals("TestBundle", headers.get(BUNDLE_NAME));
      assertEquals("DefaultSymbolicName", headers.get(BUNDLE_SYMBOLICNAME));
      URL url = new URL("file://test.jar");
      assertEquals(url, headers.get(BUNDLE_UPDATELOCATION));
      Version version = new Version("1.2.3.GA");
      assertEquals(version, headers.get(BUNDLE_VERSION));
      List<String> env = Arrays.asList("ena", "dva", "tri");
      assertEquals(env, headers.get(BUNDLE_REQUIREDEXECUTIONENVIRONMENT));
   }

   private OSGiMetaData getMetaData(String manifestPath) throws IOException
   {
      return new AbstractOSGiMetaData(getManifest(manifestPath));
   }

   private BundleHeaders getBundleHeaders(OSGiMetaData metaData)
   {
      return new BundleHeaders(metaData);
   }
}
