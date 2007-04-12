/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.test.bundle.metadata;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import junit.framework.Test;
import org.jboss.osgi.plugins.metadata.AbstractOSGiMetaData;
import org.jboss.osgi.plugins.metadata.AbstractPackageAttribute;
import org.jboss.osgi.plugins.metadata.AbstractParameter;
import org.jboss.osgi.plugins.metadata.AbstractParameterizedAttribute;
import org.jboss.osgi.plugins.metadata.OSGiParameters;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.jboss.osgi.spi.metadata.PackageAttribute;
import org.jboss.osgi.spi.metadata.Parameter;
import org.jboss.osgi.spi.metadata.ParameterizedAttribute;
import org.jboss.osgi.spi.metadata.VersionRange;
import org.osgi.framework.Version;

/**
 * Test OSGi header values.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class HeaderValuesTestCase extends AbstractManifestTestCase
{
   public HeaderValuesTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(HeaderValuesTestCase.class);
   }

   public void testSerializable() throws Exception
   {
      Manifest manifest = getManifest(createName(null));
      AbstractOSGiMetaData metaData = new AbstractOSGiMetaData(manifest);
      byte[] bytes = serialize(metaData);
      metaData = (AbstractOSGiMetaData)deserialize(bytes);
      String vendor = metaData.getMainAttribute("Implementation-Vendor");
      assertEquals("jboss.org", vendor);
   }

   public void testSimpleManifest() throws Exception
   {
      Manifest manifest = getManifest(createName("Simple"));
      OSGiMetaData metaData = new AbstractOSGiMetaData(manifest);

      assertEquals(metaData.getBundleActivator(), "org.jboss.test.bundle.metadata.BundleActivator");
      List<String> classpath = Arrays.asList("test.jar", "mc.jar", "seam.jar");
      assertEquals(metaData.getBundleClassPath(), classpath);
      assertEquals(metaData.getBundleDescription(), "TestHeadersManifest");
      assertEquals(metaData.getBundleLocalization(), "OSGI-INF/l10n/bundle");
      assertEquals(metaData.getBundleManifestVersion(), 2);
      assertEquals(metaData.getBundleName(), "TestBundle");
      assertEquals(metaData.getBundleSymbolicName(), "UniqueName");
      URL url = new URL("file://test.jar");
      assertEquals(metaData.getBundleUpdateLocation(), url);
      Version version = new Version("1.2.3.GA");
      assertEquals(metaData.getBundleVersion(), version);
      List<String> env = Arrays.asList("ena", "dva", "tri");
      assertEquals(metaData.getRequiredExecutionEnvironment(), env);
   }

   public void testJavaccManifest() throws Exception
   {
      Manifest manifest = getManifest(createName("JavaCC"));
      OSGiMetaData metaData = new AbstractOSGiMetaData(manifest);

      List<ParameterizedAttribute> bundleNativeCode = new ArrayList<ParameterizedAttribute>();
      Map<String, Parameter> bnc1 = new HashMap<String, Parameter>();
      bnc1.put("osname", new AbstractParameter("QNX"));
      bnc1.put("osversion", new AbstractParameter("3.1"));
      bundleNativeCode.add(new AbstractPackageAttribute("/lib/http.DLL", bnc1));
      Map<String, Parameter> bnc2 = new HashMap<String, Parameter>();
      bnc2.put("osname", new AbstractParameter("QWE"));
      bnc2.put("osversion", new AbstractParameter("4.0"));
      bundleNativeCode.add(new AbstractPackageAttribute("/lib/tcp.DLL", bnc2));
      bundleNativeCode.add(new AbstractPackageAttribute("/lib/iiop.DLL", bnc2));
      List<ParameterizedAttribute> metadataBNC = metaData.getBundleNativeCode();
      assertNotNull(metadataBNC);
      assertEquals(bundleNativeCode.size(), metadataBNC.size());
      for(int i=0; i < metadataBNC.size(); i++)
      {
         ParameterizedAttribute paMD = metadataBNC.get(i);
         ParameterizedAttribute myPA = bundleNativeCode.get(i);
         assertEquals(paMD.getAttribute(), myPA.getAttribute());
         assertEquals(paMD.getParameters(), myPA.getParameters());
      }

      List<PackageAttribute> dynamicImports = new ArrayList<PackageAttribute>();
      Map<String, Parameter> dyna1 = new HashMap<String, Parameter>();
      dyna1.put("user", new AbstractParameter("alesj"));
      dynamicImports.add(new AbstractPackageAttribute("org.jboss.test", dyna1));
      Map<String, Parameter> dyna2 = new HashMap<String, Parameter>();
      dyna2.put("version", new AbstractParameter("1.2.3.GA"));
      dynamicImports.add(new AbstractPackageAttribute("com.acme.plugin.*", dyna2));
      Map<String, Parameter> dyna3 = new HashMap<String, Parameter>();
      dyna3.put("test", new AbstractParameter("test"));
      dynamicImports.add(new AbstractPackageAttribute("*", dyna3));
      List<PackageAttribute> metadataDyna = metaData.getDynamicImports();
      assertNotNull(metadataDyna);
      assertEquals(dynamicImports.size(), metadataDyna.size());
      for(int i=0; i < metadataDyna.size(); i++)
      {
         PackageAttribute paMD = metadataDyna.get(i);
         PackageAttribute myPA = dynamicImports.get(i);
         assertEquals(paMD.getAttribute(), myPA.getAttribute());
         assertEquals(paMD.getPackageInfo(), myPA.getPackageInfo());
         assertEquals(paMD.getParameters(), myPA.getParameters());
      }

      List<PackageAttribute> exportPackages = new ArrayList<PackageAttribute>();
      Map<String, Parameter> ep1 = new HashMap<String, Parameter>();
      ep1.put("version", new AbstractParameter("1.3"));
      exportPackages.add(new AbstractPackageAttribute("org.osgi.util.tracker", ep1));
      exportPackages.add(new AbstractPackageAttribute("net.osgi.foo", ep1));
      Map<String, Parameter> ep2 = new HashMap<String, Parameter>();
      ep2.put("version", new AbstractParameter("\"[1.0,2.0)\""));
      exportPackages.add(new AbstractPackageAttribute("org.jboss.test", ep2));
      List<PackageAttribute> metadataExport = metaData.getExportPackages();
      assertNotNull(metadataExport);
      assertEquals(exportPackages.size(), metadataExport.size());
      for(int i=0; i < metadataExport.size(); i++)
      {
         PackageAttribute paMD = metadataExport.get(i);
         PackageAttribute myPA = exportPackages.get(i);
         assertEquals(paMD.getAttribute(), myPA.getAttribute());
         assertEquals(paMD.getPackageInfo(), myPA.getPackageInfo());
         assertEquals(paMD.getParameters(), myPA.getParameters());
         OSGiParameters o1 = new OSGiParameters(paMD.getParameters());
         OSGiParameters o2 = new OSGiParameters(myPA.getParameters());
         VersionRange v1 = o1.getVersion();
         VersionRange v2 = o2.getVersion();
         assertEquals(v1, v2);
      }

      Map<String, Parameter> parameters = new HashMap<String, Parameter>();
      parameters.put("bundle-version", new AbstractParameter("\"[3.0.0,4.0.0)\""));
      ParameterizedAttribute fragmentHost = new AbstractParameterizedAttribute("org.eclipse.swt", parameters);
      ParameterizedAttribute metadataFragment = metaData.getFragmentHost();
      assertNotNull(metadataFragment);
      assertEquals(fragmentHost.getAttribute(), metadataFragment.getAttribute());
      OSGiParameters o1 = new OSGiParameters(fragmentHost.getParameters());
      OSGiParameters o2 = new OSGiParameters(metadataFragment.getParameters());
      VersionRange v1 = o1.getBundleVersion();
      VersionRange v2 = o2.getBundleVersion();
      assertNotNull(v1);
      assertNotNull(v2);
      assertEquals(v1, v2);

      List<PackageAttribute> importPackages = new ArrayList<PackageAttribute>();
      Map<String, Parameter> ip1 = new HashMap<String, Parameter>();
      ip1.put("version", new AbstractParameter("1.4"));
      ip1.put("name", new AbstractParameter("osgi"));
      importPackages.add(new AbstractPackageAttribute("org.osgi.util.tracker", ip1));
      importPackages.add(new AbstractPackageAttribute("org.osgi.service.io", ip1));
      Map<String, Parameter> ip2 = new HashMap<String, Parameter>();
      ip2.put("version", new AbstractParameter("\"[2.0,3.0)\""));
      ip2.put("resolution", new AbstractParameter("osgi-int"));
      importPackages.add(new AbstractPackageAttribute("org.jboss.test", ip2));
      List<PackageAttribute> metadataImport = metaData.getImportPackages();
      assertNotNull(metadataImport);
      assertEquals(importPackages.size(), metadataImport.size());
      for(int i=0; i < metadataImport.size(); i++)
      {
         PackageAttribute paMD = metadataImport.get(i);
         PackageAttribute myPA = importPackages.get(i);
         assertEquals(paMD.getAttribute(), myPA.getAttribute());
         assertEquals(paMD.getPackageInfo(), myPA.getPackageInfo());
         assertEquals(paMD.getParameters(), myPA.getParameters());
         OSGiParameters oi1 = new OSGiParameters(paMD.getParameters());
         OSGiParameters oi2 = new OSGiParameters(myPA.getParameters());
         VersionRange vi1 = oi1.getVersion();
         VersionRange vi2 = oi2.getVersion();
         assertEquals(vi1, vi2);
      }

      List<ParameterizedAttribute> requireBundles = new ArrayList<ParameterizedAttribute>();
      Map<String, Parameter> rb1 = new HashMap<String, Parameter>();
      rb1.put("visibility", new AbstractParameter("true"));
      requireBundles.add(new AbstractParameterizedAttribute("com.acme.chess", rb1));
      Map<String, Parameter> rb2 = new HashMap<String, Parameter>();
      rb2.put("bundle-version", new AbstractParameter("1.2"));
      requireBundles.add(new AbstractParameterizedAttribute("com.alesj.test", rb2));
      List<ParameterizedAttribute> metadataRB = metaData.getRequireBundles();
      assertNotNull(metadataRB);
      assertEquals(requireBundles.size(), metadataRB.size());
      for(int i=0; i < metadataRB.size(); i++)
      {
         ParameterizedAttribute paMD = metadataRB.get(i);
         ParameterizedAttribute myPA = requireBundles.get(i);
         assertEquals(paMD.getAttribute(), myPA.getAttribute());
         assertEquals(paMD.getParameters(), myPA.getParameters());
         OSGiParameters oi1 = new OSGiParameters(paMD.getParameters());
         OSGiParameters oi2 = new OSGiParameters(myPA.getParameters());
         String vis1 = oi1.getVisibility();
         String vis2 = oi2.getVisibility();
         assertEquals(vis1, vis2);
         VersionRange vr1 = oi1.getBundleVersion();
         VersionRange vr2 = oi2.getBundleVersion();
         assertEquals(vr1, vr2);
      }
   }

   public void testIllegalManifest() throws Exception
   {
      Manifest manifest = getManifest(createName("Illegal"));
      // todo
   }

}
