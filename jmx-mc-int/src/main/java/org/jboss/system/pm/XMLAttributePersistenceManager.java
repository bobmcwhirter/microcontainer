/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.system.pm;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.logging.Logger;
import org.jboss.mx.persistence.AttributePersistenceManager;
import org.jboss.system.server.ServerConfigLocator;
import org.jboss.util.Classes;
import org.jboss.util.file.Files;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * XMLAttributePersistenceManager
 * 
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision$
 */
public class XMLAttributePersistenceManager
   implements AttributePersistenceManager
{
   // Constants -----------------------------------------------------
   
   /** The XML configuration element */
   public static final String DATA_DIR_ELEMENT        = "data-directory";
   
   /** Default base directory if one not specified */
   public static final String DEFAULT_BASE_DIR        = "data/xmbean-attrs";
   
   /** The XML attribute-list elements and attributes */
   public static final String AL_ROOT_ELEMENT         = "attribute-list";
   public static final String AL_ID_ATTRIBUTE         = "id";
   public static final String AL_DATE_ATTRIBUTE       = "date";
   public static final String AL_ATTRIBUTE_ELEMENT    = "attribute";
   public static final String AL_NAME_ATTRIBUTE       = "name";
   public static final String AL_TYPE_ATTRIBUTE       = "type";
   public static final String AL_NULL_ATTRIBUTE       = "null";
   public static final String AL_SERIALIZED_ATTRIBUTE = "serialized";
   public static final String AL_TRUE_VALUE           = "true";
   public static final String AL_FALSE_VALUE          = "false";
    
   // Private Data --------------------------------------------------
   
   /** Logger object */
   private static final Logger log = Logger.getLogger(XMLAttributePersistenceManager.class);
   
   /** used for formating timestamps (date attribute) */
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   
   /** for byte-to-hex conversions */
   private static final char[] hexDigits = new char[]
      { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

   /** directory as used internally */
   private File dataDir;
   
   /** enable status */
   private boolean state;
   
   /** id to filename cache */
   private Map idMap;
   
   // Constructors -------------------------------------------------
    
   /**
    * Constructs a <tt>FileAttributePersistenceManager</tt>.
   **/
   public XMLAttributePersistenceManager()
   {
      log.debug("Constructed");
   }
   
   // AttributePersistenceManager Lifecycle -------------------------
   
   /**
    * Initializes the AttributePersistenceManager using
    * the supplied configuration element CONFIG_ELEMENT
    * whose content will be probably different for each
    * particular implementation.
    * 
    * The version string is a tag that must be used by the
    * AttributePersistenceManager implementation to make
    * sure that data saved/loaded under different version
    * tags are partitioned.
    * 
    * Once created, the configuration of the implementation
    * object cannot change.
    * 
    * Calling any other method before create() is executed
    * will result in a IllegalStateException
    * 
    * Finally, the implementation should be prepared to
    * receive multiple concurrent calls. 
    * 
    * @param  version   a tag to identify the version
    * @param  config    XML Element to load arbitrary config
    * @throws Exception	when any error occurs during create
    */
   public void create(String version, Element config)
      throws Exception
   {
      // ignore if already active
      if (getState()) {
         return;
      }
      
      // Decide on the base data directory to use
      String baseDir = null;
      
      if (config == null) {
         baseDir = DEFAULT_BASE_DIR;
      }
      else {
         if (!config.getTagName().equals(DATA_DIR_ELEMENT)) {
            throw new Exception("expected '" + DATA_DIR_ELEMENT +
                                "' XML configuration element, got '" +
                                config.getTagName() + "'");
         }
         else {
            baseDir = getElementContent(config);
         }
      }

      // Initialize the data dir
      this.dataDir = initDataDir(baseDir, version);
      
      log.debug("Using data directory: " + this.dataDir.getCanonicalPath());
      
      // initialize id cache
      this.idMap = Collections.synchronizedMap(new HashMap());

      // mark active status
      setState(true);
   }

   /**
    * Returns true if the AttributePersistenceManager
    * is "in-service" state, i.e. after create() and
    * before destroy() has been called, false otherwise.
    * 
    * @return true if in operational state
    */
   public boolean getState()
   {
      return this.state;
   }
   
   /**
    * Releases resources and destroys the AttributePersistenceManager.
    * The object is unusable after destroy() has been called.
    * 
    * Any call to any method will result to an
    * IllegalStateException.
    *
    */
   public void destroy()
   {
      setState(false);

      // instance can't be use anymore
      this.dataDir = null;
      this.idMap = null;
   }
   
   // AttributePersistenceManager Persistence -----------------------

   /**
    * Uses the specified id to retrieve a previously persisted
    * AttributeList. If no data can be found under the specified
    * id, a null will be returned.
    * 
    * @param id			the key for retrieving the data
    * @return			the data, or null
    * @throws Exception when an error occurs
    */
   public void store(String id, AttributeList attrs)
      throws Exception
   {
      log.debug("store(" + id + ") attrs=" + attrs);

      // make sure we are active
      checkActiveState();
      
      // map to filename - keep the original for storing
      String origId = id;
      id = mapId(id);
      
      if (attrs == null)
         throw new Exception("store() called with null AttributeList");
      
      // will throw an exception if file not r/w or it is a directory
      File file = checkFileForWrite(id);
      
      // build the XML in memory using DOM
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.newDocument();

      // Add a comment
      Comment comment = doc.createComment(" automatically produced by XMLAttributePersistenceManager ");
      doc.appendChild(comment);
      
      // Insert root element
      Element root = doc.createElement(AL_ROOT_ELEMENT);
      root.setAttribute(AL_ID_ATTRIBUTE, origId);
      root.setAttribute(AL_DATE_ATTRIBUTE, dateFormat.format(new Date()));
      doc.appendChild(root);
      
      // iterate over the AttributeList
      for (int i = 0; i < attrs.size(); i++) {
         
         Attribute attr = (Attribute)attrs.get(i);
       
         String name  = attr.getName();
         Object value = attr.getValue();
         
         // create the Element and decide how to fill it in
         Element element = doc.createElement(AL_ATTRIBUTE_ELEMENT);
         element.setAttribute(AL_NAME_ATTRIBUTE, name);
         
         if (value == null) {
            // (a) null value - mark it as null
            element.setAttribute(AL_NULL_ATTRIBUTE, AL_TRUE_VALUE);
            
            // append the attribute to the attribute-list
            root.appendChild(element);
         }
         else if (value instanceof org.w3c.dom.Element) {
            // (b) XML Element - mark the type and append a copy of it
            element.setAttribute(AL_TYPE_ATTRIBUTE, "org.w3c.dom.Element");
            
            Node copy = doc.importNode((org.w3c.dom.Element)value, true);
            element.appendChild(copy);
            
            // append the attribute to the attribute-list
            root.appendChild(element);
         }
         else {
            Class clazz = value.getClass();
            String type = clazz.getName();
	        PropertyEditor peditor = PropertyEditorManager.findEditor(clazz);

	        if (peditor != null) {
		       // (c) use a PropertyEditor - mark the type and append the value as string  	           
	           peditor.setValue(value);
	           
	           element.setAttribute(AL_TYPE_ATTRIBUTE, type);
	           element.appendChild(doc.createTextNode(peditor.getAsText()));
	           
	           // append the attribute to the attribute-list
	           root.appendChild(element);
	        }
	        else if (value instanceof Serializable) {
	           // (d) serialize the object - mark type and serialized attribute
	           //                          - encode the value as stringfied sequence of hex
               String encoded = encodeAsHexString((Serializable)value);
               
               if (encoded != null) {
                  element.setAttribute(AL_TYPE_ATTRIBUTE, type);
                  element.setAttribute(AL_SERIALIZED_ATTRIBUTE, AL_TRUE_VALUE);
                  element.appendChild(doc.createTextNode(encoded));

                  // append the attribute to the attribute-list
                  root.appendChild(element);
               }
               else {
                  // could not serialize the object - write and log a warning
                  root.appendChild(doc.createComment(
                        " WARN <attribute name=\"" + name + "\" type=\"" + type + 
                        "\"/> could not be serialized "));
                  
                  log.warn("Could not serialize attribute '" + name + 
                           "' of type '" + type + "' and value: " + value);
               }
	        }
	        else {
	           // (e) could not find a way to persist - record and log a warning
	           root.appendChild(doc.createComment(
	                 " WARN <attribute name=\"" + name + "\" type=\"" + type + 
                     "\"/> could not be persisted "));
	           
	           log.warn("Could not find a way to persist attribute '" + name +
	                    "' of type '" + type + "' and value: " + value);
	        }
         }
      }
      
      // DOM document ready - save it
      try {
         outputXmlFile(doc, file);
      }
      catch (Exception e) {
         log.warn("Cannot persist AttributeList to: \"" + id + "\"", e);
         throw e;
      }
   }
   
   /**
    * Persists an AttributeList (name/value pair list),
    * under a specified id. The id can be used to retrieve the
    * AttributeList later on. The actual mechanism will differ
    * among implementations.
    * 
    * @param  id 		the key for retrieving the data later on, not null
    * @param  attrs 	the data to be persisted, not null
    * @throws Exception	when data cannot be persisted
    */
   public AttributeList load(String id)
      throws Exception
   {
      log.debug("load(" + id + ")");

      // make sure we are active
      checkActiveState();
      
      // map to filename
      id = mapId(id);
      
      if (!getState())
         return null;
      
      // Real stuff starts here
      AttributeList attrs = null;
      
      // returns null to indicate file does not exist
      File file = checkFileForRead(id);
      
      if (file != null) {
	     // parse the saved XML doc
	     Document doc = parseXmlFile(file);
	     
	     // top level - look for AL_ROOT_ELEMENT
	     NodeList docList = doc.getChildNodes();
	     Element root = null;
	     
	     for (int i = 0; i < docList.getLength(); i++) {
	        Node node = docList.item(i);
	        
	        if (node.getNodeType() == Node.ELEMENT_NODE && 
	            node.getNodeName().equals(AL_ROOT_ELEMENT)) {
	           
	           root = (Element)node;
	           break; // found
	        }
	     }
	     
	     // root element must be there
	     if (root == null) {
	        throw new Exception("Expected XML element: " + AL_ROOT_ELEMENT);
	     }
	     else {
	        // proceed iterating over AL_ATTRIBUTE_ELEMENT elements
	        // and fill the AttributeList
	        attrs = new AttributeList();
	        
	        NodeList rootList = root.getChildNodes();
	        
	        for (int i = 0; i < rootList.getLength(); i++) {
	           Node node = rootList.item(i);
	           
	           // only interested in ELEMENT nodes
	           if (node.getNodeType() == Node.ELEMENT_NODE &&
	               node.getNodeName().equals(AL_ATTRIBUTE_ELEMENT)) {
	              
	              Element element = (Element)node;
	              
	              // name attribute must always be there
	              String name = element.getAttribute(AL_NAME_ATTRIBUTE);
	              
	              if (!(name.length() > 0)) {
	                 throw new Exception("Attribute '" + AL_NAME_ATTRIBUTE +
	                                     "' must be specified for element '" + AL_ATTRIBUTE_ELEMENT + "'");
	              }
	              
	              // Process the attribute depending on how the attributes are set
	              
	              if (element.getAttribute(AL_NULL_ATTRIBUTE).toLowerCase().equals(AL_TRUE_VALUE)) {
                     
	                 //	(a) null value - just add it to the AttributeList
	                 attrs.add(new Attribute(name, null));
	              }
	              else if (element.getAttribute(AL_SERIALIZED_ATTRIBUTE).toLowerCase().equals(AL_TRUE_VALUE)) {
	                 
	                 // (b) serialized value - decode the HexString
	                 String hexStr = getElementContent(element);
	                 Serializable obj = decodeFromHexString(hexStr);
	                 
	                 if (obj == null) {
	                    throw new Exception("Failed to deserialize attribute '" + name + "'");
	                 }
	                 else {
	                    attrs.add(new Attribute(name, obj));
	                 }
	              }
	              else {
	                 String type = element.getAttribute(AL_TYPE_ATTRIBUTE);
	                 
	                 // type must be specified
	                 if (!(type.length() > 0)) {
	                    throw new Exception("Attribute '" + AL_TYPE_ATTRIBUTE +
	                                        "' must be specified for name='" + name + "'");
	                 }
	                 
	                 if (type.equals("org.w3c.dom.Element")) {

	                    // (c) org.w3c.dom.Element - deep copy first Element child node found
	                    
	                     NodeList nlist = element.getChildNodes();
	                     Element el = null;
	                     
	                     for (int j = 0; j < nlist.getLength(); j++) {
	                        
	                        Node n = nlist.item(j);
	                        if (n.getNodeType() == Node.ELEMENT_NODE)
	                        {
	                           el = (Element)n;
	                           break;
	                        }
	                     }
	                     
	                     if (el != null) {
	                        attrs.add(new Attribute(name, el.cloneNode(true)));
	                     }
	                     else {
	                        attrs.add(new Attribute(name, null));
	                     }
	                 }
	                 else {
	                    // Get the classloader for loading attribute classes.
	                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
	                    Class clazz = null;
	                    
	                     try {
	                        clazz = cl.loadClass(type);
	                     }
	                     catch (ClassNotFoundException e) {
	                        throw new Exception("Class not found for attribute '" + name +
	                                            "' of type '" + type + "'");
	                     }

	                     PropertyEditor peditor = PropertyEditorManager.findEditor(clazz);

	         	         if (peditor != null) {
	         		        
	         	            // (d) use a PropertyEditor - extract the value
	         	            
	         	            String value = getElementContent(element);
	         	            peditor.setAsText(value);
	         	            
	         	            attrs.add(new Attribute(name, peditor.getValue()));
	         	         }
	         	         else {
	         	            throw new Exception("Cannot find a way to load attribute '" + name +
	         	                                "' of type '" + type + "'");
	         	         }
	                 }
	              }
	           }
	        } // end for()
	     }
      }
      log.debug("load() returns with: " + attrs);
      
      // will be null if a persistent file was not found
      return attrs;
   }

   // Administrative Functions --------------------------------------
   
   /**
    * Checks if a persistened AttributeList for this particular
    * id exists
    * 
    * @param  id		the key of the image
    * @return true 		if an image exists; false otherwise
    * @throws Exception on any error
    */
   public boolean exists(String id)
      throws Exception
   {
      // make sure we are active
      checkActiveState();
      
      return (new File(this.dataDir, mapId(id))).isFile();
   }
   
   /**
    * Removes the persisted AttributeList, if exists 
    *
    * @param  id		the key of the image
    * @throws Exception	when any error occurs
    */
   public void remove(String id)
      throws Exception
   {
      // make sure we are active
      checkActiveState();
      
      (new File(this.dataDir, mapId(id))).delete();
   }
   
   /**
    * Removes all the persisted data stored under
    * the configured version tag.
    *  
    * @throws Exception when any error occurs
    */
   public void removeAll()
      throws Exception
   {
      // make sure we are active
      checkActiveState();
      
      String[] files = this.dataDir.list(new XMLFilter());
      
      if (files != null) {
         for (int i = 0; i < files.length; i++) {
            (new File(this.dataDir, files[i])).delete();
         }
      }
   }
   
   /**
    * Returns a String array with all the saved ids
    * under the configured version tag.
    * 
    * @return			array with all persisted ids
    * @throws Exception when any error occurs
    */
   public String[] listAll()
      throws Exception
   {
      // make sure we are active
      checkActiveState();
      
      String[] files = this.dataDir.list(new XMLFilter());
      String[] result = null;      
      
      if (files != null) {
         result = new String[files.length];
         
         for (int i = 0; i < files.length; i++) {
            result[i] = mapFile(files[i]);
         }
      }
      return result;
   }
   
   // Private -------------------------------------------------------
   
   /**
    * Private status setter
    * 
    * lifecycle is controlled by create() and destroy()
    */
   private void setState(boolean state)
   {
      this.state = state;
   }
   
   /**
    * Create/check/point to the data directory.
    * 
    * Process the base dir first and append the versionTag dir
    * if specified.
    * 
    * Only a base dir relative to ServerHomeDir is created.
    * External base dirs are only used (i.e. they must exist
    * and we must read/write permission)
    */
   private File initDataDir(String baseDir, String versionTag)
      throws Exception
   {
      File dir = null;
      
      // Process the base directory first
      
	  // baseDir must be valid URL pointing to r/w dir
	  try {
	     URL fileURL = new URL(baseDir);
	      
	     File file = new File(fileURL.getFile());
	      
	     if(file.isDirectory() && file.canRead() && file.canWrite()) {
	        dir = file;
	     }
	  }
	  catch(Exception e) {
		 // Otherwise, try to make it inside the jboss directory hierarchy
	     
	     File homeDir = ServerConfigLocator.locate().getServerHomeDir();
	
	     dir = new File(homeDir, baseDir);
	
	     dir.mkdirs();
	      
	     if (!dir.isDirectory())
	        throw new Exception("The base directory is not valid: "
	                            + dir.getCanonicalPath());
	  }
	  
	  // Now add the versionTag dir, if specified
	  if (versionTag != null && !versionTag.equals("")) {
	     dir = new File(dir, versionTag);
	     
	     dir.mkdirs();
	      
	     if (!dir.isDirectory())
	        throw new Exception("The data directory is not valid: "
	                            + dir.getCanonicalPath());	     
	  }
	  return dir;
   }
   
   /**
    * Serialize an object as a Hex string so it can be saved as text
    * 
    * The length of the encoded object is twice it's byte image
    * 
    * Returns null if serialization fails
    */
   private String encodeAsHexString(Serializable obj)
   {
      String retn = null;
      
      if (obj != null) {
         try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
	      
	        oos.writeObject(obj);
	        byte[] bytes = baos.toByteArray();
	        
            StringBuffer sbuf = new StringBuffer(1024);

            for (int i = 0; i < bytes.length; i++) {
                sbuf.append(hexDigits[ (bytes[i] >> 4) & 0xF ]); // high order digit
                sbuf.append(hexDigits[ (bytes[i]     ) & 0xF ]); // low order digit
            }

            retn = sbuf.toString();	        
	     }
	     catch (IOException e) {
	        // will return null
	     }
      }
      return retn;
   }

   /**
    * Deserialize an object from its hex encoded string representation
    * 
    * Returns null if deserialization fails
    */
   private Serializable decodeFromHexString(String hexStr)
   {
      // hexStr must not contain white space!
      int len = hexStr.length() / 2;
      byte[] bytes = new byte[len];
      
      for (int i = 0; i < len; i++) {
         
          char h1 = hexStr.charAt(i * 2);		// high order hex digit
          char h2 = hexStr.charAt(i * 2 + 1);   // low order hex digit
          
          // convert hex digits to integers
          int d1 = (h1 >= 'a') ? (10 + h1 - 'a')
                : ((h1 >= 'A') ? (10 + h1 - 'A') 
                                   :  (h1 - '0'));
          
          int d2 = (h2 >= 'a') ? (10 + h2 - 'a')
                : ((h2 >= 'A') ? (10 + h2 - 'A')
                                    : (h2 - '0'));
          
          bytes[i] = (byte)(d1 * 16 + d2); // 255 max
      }
      
      Serializable retn = null;
      try
      {
         // Use ObjectInputStreamExt that will utilise the
         // ThreadContextClassLoader to deserialize objects
         ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         ObjectInputStream ois = new ObjectInputStreamExt(bais);           
         retn = (Serializable)ois.readObject();
         ois.close();
      }
      catch (ClassNotFoundException e)
      {
         log.warn("Cannot deserialize object", e);
      }      
      catch (IOException e)
      {
         log.warn("Cought IOException", e);
      }
      return retn;
   }
   
   /**
    * Just output a DOM Document to a file with
    * indentation true
    */
   private void outputXmlFile(Document doc, File file)
      throws Exception
   {
      // Prepare the DOM document for writing
      Source source = new DOMSource(doc);    
      
      // Use an OutputStream rather than a File
      OutputStream out = new FileOutputStream(file);
      
      // Prepare the output
      Result result = new StreamResult(out);
      
      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      
      // Enable indentation
      xformer.setOutputProperty(OutputKeys.INDENT, "yes");
      
      try
      {
         xformer.transform(source, result);
      }
      finally
      {
         // Cleanup
         out.close();
      }
   }

   /**
    * Parse an XML file into a DOM Document
    * with validation set to false
    */
   private Document parseXmlFile(File file)
      throws Exception
   {
	   // Create a builder factory
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   
	   // do not request validation against DTD
	   // no DTD specified in the saved document
	   factory.setValidating(false);

      // The builder and document to return
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc;
      
      // Use an InputStreamer rather than a File when parsing,
      // to avoid pathname encoding problems, like spaces in
      // the pathname encoded as %20
      InputStream in = new FileInputStream(file);
      try
      {
         // Parse the file
         doc = builder.parse(in);
      }
      finally
      {
         // close anyways
         in.close();
      }
      // Return the parsed document
      return doc;
   }
   
   /**
    * Makes sure we can write to this file
    */
   private File checkFileForWrite(String filename)
      throws Exception
   {
      File file = new File(this.dataDir, filename);
      
      if (file.isFile()) {
         if (file.canRead() && file.canWrite()) {
            return file;  // everything fine
         }
         else {
            throw new Exception("file '" + filename + "' is not r/w"); // oops
         }
      }
      else if (file.isDirectory()) {
         throw new Exception(filename + " is a directory!"); // oops
      }
      else {
         return file; // not found - ok
      }     
   }
   
   /**
    * Makes sure we can read from this file.
    * 
    * Returns null if not found
    */
   private File checkFileForRead(String filename)
      throws Exception
   {
      File file = new File(this.dataDir, filename);
      
      if (file.isFile()) {
         if (file.canRead() && file.canWrite()) {
            return file;  // everything fine
         }
         else {
            throw new Exception("file '" + filename + "' is not r/w"); // oops
         }
      }
      else if (file.isDirectory()) {
         throw new Exception(filename + " is a directory!"); // oops
      }
      else {
         return null; // not found
      }
   }
   
   /**
    * Returns the concatenated content of all
    * children TEXT nodes of this element
    */
   private String getElementContent(Element element)
   {
      NodeList nlist = element.getChildNodes();
      
      StringBuffer sbuf = new StringBuffer(1024);
   
      // concatenate Text nodes
      for (int i = 0; i < nlist.getLength(); i++) {
         Node node = nlist.item(i);
         
         if (node.getNodeType() == Node.TEXT_NODE) {
            sbuf.append(((Text)node).getData());
         }
      }
      return sbuf.toString();
   }
   
   /**
    * Make sure we are active before processing a call
    */
   private void checkActiveState()
   {
      if (!getState()) {
         throw new IllegalStateException("AttributePersistenceManager not active");
      }
   }
   
   /**
    * Simple filter for .xml files in a directory listing
    */
   private class XMLFilter
      implements FilenameFilter
   {
      public boolean accept(File dir, String name)
      {
         return name.endsWith(".xml");
      }
   }
   
   /**
    * Map the id to a filename and cache it
    * 
    * A null id results in an exception thrown
    */
   private String mapId(String id)
      throws Exception
   {
      if (id == null) {
         throw new Exception("called with null id");
      }
	  else {
	     // check the cache
	     String file = (String)this.idMap.get(id);
	     
	     // if not in cache encode, and cache it
	     if (file == null) {
	        file = Files.encodeFileName(id) + ".xml";
	        this.idMap.put(id, file);
	     }
	     return file;
      }
   }
   
   /**
    * Do the reverse mapping file to id
    */
   private String mapFile(String file)
   {
      if (file == null) {
         return null;
      }
      else {
         // remove .xml suffix
         file = file.substring(0, file.length() - 4);
         
         return Files.decodeFileName(file);
      }
   }
   
   /**
    * Default implementation ObjectInputStream uses the System
    * classloader to deserialize classes, by we need to use the
    * ThreadContextClassLoader, instead.
    */
   private class ObjectInputStreamExt extends ObjectInputStream
   {
      ObjectInputStreamExt(InputStream is) throws IOException
      {
         super(is);
      }

      protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException
      {
         return Classes.loadClass(v.getName());
      }
   }   
}    
