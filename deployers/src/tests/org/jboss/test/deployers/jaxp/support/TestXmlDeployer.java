package org.jboss.test.deployers.jaxp.support;

import org.jboss.deployers.plugins.deployers.helpers.JAXPDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;
import org.w3c.dom.Document;

public class TestXmlDeployer extends JAXPDeployer<SomeBean>
{
   private SomeBean lastBean;

   public TestXmlDeployer()
   {
      super(SomeBean.class);
   }

   public SomeBean getLastBean()
   {
      return lastBean;
   }

   @Override
   protected SomeBean parse(DeploymentUnit unit, VirtualFile file, Document doc)
      throws Exception
   {
      String name = doc.getDocumentElement().getAttribute("name");
      String version = doc.getDocumentElement().getAttribute("version");

      SomeBean bean = new SomeBean();
      bean.setName(name);
      bean.setVersion(version);
      lastBean = bean;
      return bean;
   }

   @Override
   public void deploy(DeploymentUnit unit)
      throws DeploymentException
   {
      createMetaData(unit, null, ".jbean");
   }

}
