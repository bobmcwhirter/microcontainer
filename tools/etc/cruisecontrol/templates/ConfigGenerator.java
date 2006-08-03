/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.Element;
import org.jdom.Attribute;

/**
 * @author <a href="joriskuipers@xs4all.nl">Joris Kuipers</a>
 *
 * This class can be used to generate a CruiseControl config.xml.
 * It parses the given xml-file for &lt;project&gt;-elements, and then
 * calls the Velocity-template "config.vm" to generate the xml-file.
 * The list of &lt;project&gt; JDom-elements is put into the context
 * as "projects".
 */
public class ConfigGenerator {

    public static void main(String[] args) {
        try {
            Velocity.init();

            SAXBuilder builder = new SAXBuilder();
            Document projectsDoc = builder.build(new File(args[0]));

            ConfigGenerator configGenerator = new ConfigGenerator();

			Element rootElement = projectsDoc.getRootElement();

			rootElement = configGenerator.replaceWithDefaultValues(rootElement);


			if(rootElement.getChild("testattributes").getAttribute("testMode").getValue().toString().equals("yes"))
			{
				rootElement = configGenerator.replaceWithTestValues(rootElement);
			}

            VelocityContext context = new VelocityContext();
            context.put("root", rootElement);
            Writer writer = new BufferedWriter(new FileWriter(args[1]));
            Template configTemplate = Velocity.getTemplate("config.vm");
            configTemplate.merge(context, writer);
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    //Sets Project Attributes with the default values
    public Element replaceWithDefaultValues(Element rootElement)
    {
		List projectAttributeList;
		List defaultProjectAttributeList;
		Attribute tempAttr;
		Attribute tempProjAttr;
		Attribute newAttr;
		boolean attrExists = false;

		defaultProjectAttributeList = rootElement.getChild("defaultproject").getAttributes();
		for(int i=0; i<rootElement.getChildren("project").size(); i++)
		{
			projectAttributeList = ((Element)rootElement.getChildren("project").get(i)).getAttributes();
			int projectAttributeListSize = projectAttributeList.size();
			for(int j=0; j<defaultProjectAttributeList.size(); j++)
			{
				tempAttr = (Attribute)defaultProjectAttributeList.get(j);
				for(int k=0; k<projectAttributeListSize; k++)
				{
					tempProjAttr = (Attribute)projectAttributeList.get(k);
					if(tempProjAttr.getName().equals(tempAttr.getName()))
					{
						attrExists = true;
						break;
					}
					attrExists = false;
				}
				if(!attrExists)
				{
					newAttr = new Attribute(tempAttr.getName(),tempAttr.getValue());
					projectAttributeList.add(newAttr);
				}


			}
		}
		return rootElement;
	}

    //Sets Project Attributes with the test values if testMode="yes" in testattributes tag
    public Element replaceWithTestValues(Element rootElement)
    {
		List projectAttributeList;
		List testAttributesList;
		Attribute tempAttr;
		Attribute tempProjAttr;
		Attribute newAttr;

		testAttributesList = rootElement.getChild("testattributes").getAttributes();
		for(int i=0; i<rootElement.getChildren("project").size(); i++)
		{
			projectAttributeList = ((Element)rootElement.getChildren("project").get(i)).getAttributes();
			int projectAttributeListSize = projectAttributeList.size();
			for(int j=0; j<testAttributesList.size(); j++)
			{
				tempAttr = (Attribute)testAttributesList.get(j);
				for(int k=0; k<projectAttributeListSize; k++)
				{
					tempProjAttr = (Attribute)projectAttributeList.get(k);
					if(tempProjAttr.getName().equals(tempAttr.getName()))
					{
						newAttr = new Attribute(tempAttr.getName(),tempAttr.getValue());
						projectAttributeList.set(k,newAttr);
						break;
					}
				}
			}
		}
		return rootElement;
	}

}
