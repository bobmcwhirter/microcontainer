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
package org.jboss.test.kernel.config.support;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple bean
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SimpleBean extends SimplerBean implements Serializable
{
   public enum Alphabet {A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z}

   // Constants -----------------------------------------------------

   private static final long serialVersionUID = 3258126972906387766L;

   // Attributes ----------------------------------------------------

   /** Constructor used */
   private String constructorUsed;

   /** Object */
   private Object anObject;
   
   /** A string */
   private String aString;

   /** Byte */
   private Byte aByte;

   /** Boolean */
   private Boolean aBoolean;

   /** Character */
   private Character aCharacter;

   /** Short */
   private Short aShort;

   /** Int */
   private Integer anInt;

   /** Long */
   private Long aLong;

   /** Float */
   private Float aFloat;

   /** Double */
   private Double aDouble;

   /** Date */
   private Date aDate;

   /** BigDecimal */
   private BigDecimal aBigDecimal;

   /** BigDecimal */
   private BigInteger aBigInteger;

   /** byte */
   private byte abyte;

   /** boolean */
   private boolean aboolean;

   /** char */
   private char achar;

   /** short */
   private short ashort;

   /** int */
   private int anint;

   /** long */
   private long along;

   /** float */
   private float afloat;

   /** double */
   private double adouble;

   /** number */
   private Number aNumber;

   /** collection */
   private Collection<?> collection;

   /** set */
   private Set<?> set;

   /** list */
   private List<?> list;

   /** array */
   private Object[] array;

   /** preInstantiated */
   private Object[] preInstantiatedArray = new Object[]{"string1", "string2", "string2", "string1"};

   /** map */
   private Map<?,?> map;

   /** Overloaded property */
   private String overloadedProperty;

   private Alphabet enumProperty = Alphabet.A;

   // public just for testing purpose

   /** Just a setter for this collection */
   public Collection<?> setterCollection;

   /** Just a setter for this set */
   public Set<?> setterSet;

   /** Just a setter for this list */
   public List<?> setterList;

   /** Just a setter for this map */
   public Map<?,?> setterMap;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public SimpleBean()
   {
      constructorUsed = "()";
   }

   public SimpleBean(String string)
   {
      super(string);
      constructorUsed = string;
      aString = string;
      preInstantiatedArray = new Object[]{string};
   }

   public SimpleBean(String string1, String string2)
   {
      super(string1, string2);
   }

   public SimpleBean(Integer integer)
   {
      constructorUsed = integer.getClass().getName() + ":" + integer.toString();
      anInt = integer;
   }

   public SimpleBean(Comparable<?> comparable)
   {
      constructorUsed = "java.lang.Comparable:" + comparable.getClass().getName() + ":" + comparable.toString();
      anObject = comparable;
   }

   public SimpleBean(Collection<?> collection)
   {
      constructorUsed = "java.util.Collection:" + collection.getClass().getName() + ":" + collection.toString();
      this.collection = collection;
   }

   public SimpleBean(List<?> list)
   {
      constructorUsed = "java.util.List:" + list.getClass().getName() + ":" + list.toString();
      this.list = list;
   }

   public SimpleBean(Set<?> set)
   {
      constructorUsed = "java.util.Set:" + set.getClass().getName() + ":" + set.toString();
      this.set = set;
   }

   public SimpleBean(Object[] array)
   {
      constructorUsed = "Array:" + array.getClass().getName() + ":" + Arrays.asList(array);
      this.array = array;
   }

   public SimpleBean(Map<?, ?> map)
   {
      constructorUsed = "java.util.Map:" + map.getClass().getName() + ":" + map.toString();
      this.map = map;
   }

   public SimpleBean(Hashtable<?, ?> hashtable)
   {
      constructorUsed = "java.util.Hashtable:" + hashtable.getClass().getName() + ":" + hashtable.toString();
      this.map = hashtable;
   }

   // Public --------------------------------------------------------

   public String getConstructorUsed()
   {
      return constructorUsed;
   }

   public Object getAnObject()
   {
      return anObject;
   }

   public void setAnObject(Object object)
   {
      anObject = object;
   }

   public BigDecimal getABigDecimal()
   {
      return aBigDecimal;
   }

   public void setABigDecimal(BigDecimal bigDecimal)
   {
      aBigDecimal = bigDecimal;
   }

   public BigInteger getABigInteger()
   {
      return aBigInteger;
   }

   public void setABigInteger(BigInteger bigInteger)
   {
      aBigInteger = bigInteger;
   }

   public boolean isAboolean()
   {
      return aboolean;
   }

   public void setAboolean(boolean aboolean)
   {
      this.aboolean = aboolean;
   }

   public Boolean getABoolean()
   {
      return aBoolean;
   }

   public void setABoolean(Boolean boolean1)
   {
      aBoolean = boolean1;
   }

   public Number getANumber()
   {
      return aNumber;
   }

   public void setANumber(Number number)
   {
      aNumber = number;
   }

   public byte getAbyte()
   {
      return abyte;
   }

   public void setAbyte(byte abyte)
   {
      this.abyte = abyte;
   }

   public Byte getAByte()
   {
      return aByte;
   }

   public void setAByte(Byte byte1)
   {
      aByte = byte1;
   }

   public char getAchar()
   {
      return achar;
   }

   public void setAchar(char achar)
   {
      this.achar = achar;
   }

   public Character getACharacter()
   {
      return aCharacter;
   }

   public void setACharacter(Character character)
   {
      aCharacter = character;
   }

   public Date getADate()
   {
      return aDate;
   }

   public void setADate(Date date)
   {
      aDate = date;
   }

   public double getAdouble()
   {
      return adouble;
   }

   public void setAdouble(double adouble)
   {
      this.adouble = adouble;
   }

   public Double getADouble()
   {
      return aDouble;
   }

   public void setADouble(Double double1)
   {
      aDouble = double1;
   }

   public float getAfloat()
   {
      return afloat;
   }

   public void setAfloat(float afloat)
   {
      this.afloat = afloat;
   }

   public Float getAFloat()
   {
      return aFloat;
   }

   public void setAFloat(Float float1)
   {
      aFloat = float1;
   }

   public long getAlong()
   {
      return along;
   }

   public void setAlong(long along)
   {
      this.along = along;
   }

   public Long getALong()
   {
      return aLong;
   }

   public void setALong(Long long1)
   {
      aLong = long1;
   }

   public int getAnint()
   {
      return anint;
   }

   public void setAnint(int anint)
   {
      this.anint = anint;
   }

   public Integer getAnInt()
   {
      return anInt;
   }

   public void setAnInt(Integer anInt)
   {
      this.anInt = anInt;
   }

   public short getAshort()
   {
      return ashort;
   }

   public void setAshort(short ashort)
   {
      this.ashort = ashort;
   }

   public Short getAShort()
   {
      return aShort;
   }

   public void setAShort(Short short1)
   {
      aShort = short1;
   }

   public String getAString()
   {
      return aString;
   }

   public void setAString(String string)
   {
      aString = string;
   }

   public Set<?> getSet()
   {
      return set;
   }
   
   public void setSet(Set<?> set)
   {
      this.set = set;
   }

   public CustomSet getCustomSet()
   {
      return (CustomSet) set;
   }
   
   public void setCustomSet(CustomSet set)
   {
      this.set = set;
   }

   public void setPreInstantiatedSet(CustomSet preInstantiatedSet)
   {
      this.preInstantiatedSet = preInstantiatedSet;
   }

   public Collection<?> getCollection()
   {
      return collection;
   }
   
   public void setCollection(Collection<?> collection)
   {
      this.collection = collection;
   }

   public CustomCollection getCustomCollection()
   {
      return (CustomCollection) collection;
   }
   
   public void setCustomCollection(CustomCollection collection)
   {
      this.collection = collection;
   }

   public void setPreInstantiatedCollection(CustomCollection preInstantiatedCollection)
   {
      this.preInstantiatedCollection = preInstantiatedCollection;
   }

   public List<?> getList()
   {
      return list;
   }
   
   public void setList(List<?> list)
   {
      this.list = list;
   }

   public CustomList getCustomList()
   {
      return (CustomList) list;
   }
   
   public void setCustomList(CustomList list)
   {
      this.list = list;
   }

   public void setPreInstantiatedList(CustomList preInstantiatedList)
   {
      this.preInstantiatedList = preInstantiatedList;
   }

   public Object[] getArray()
   {
      return array;
   }
   
   public void setArray(Object[] array)
   {
      this.array = array;
   }

   public Object[] getPreInstantiatedArray()
   {
      return preInstantiatedArray;
   }

   public void setPreInstantiatedArray(Object[] preInstantiatedArray)
   {
      this.preInstantiatedArray = preInstantiatedArray;
   }

   public String[] getCustomArray()
   {
      return (String[]) array;
   }
   
   public void setCustomArray(String[] array)
   {
      this.array = array;
   }

   public Map<?,?> getMap()
   {
      return map;
   }
   
   public void setMap(Map<?,?> map)
   {
      this.map = map;
   }

   public CustomMap getCustomMap()
   {
      return (CustomMap) map;
   }
   
   public void setCustomMap(CustomMap map)
   {
      this.map = map;
   }

   public void setPreInstantiatedMap(CustomMap preInstantiatedMap)
   {
      this.preInstantiatedMap = preInstantiatedMap;
   }
   
   public String getOverloadedProperty()
   {
      return overloadedProperty;
   }
   
   public void setOverloadedProperty(Long broken)
   {
      throw new RuntimeException("Invoked the wrong setter");
   }
   
   public void setOverloadedProperty(String overloadedProperty)
   {
      this.overloadedProperty = overloadedProperty;
   }
   
   public void setOverloadedProperty(Integer broken)
   {
      throw new RuntimeException("Invoked the wrong setter");
   }

   public Alphabet getEnumProperty()
   {
      return enumProperty;
   }

   public void setEnumProperty(Alphabet enumProperty)
   {
      this.enumProperty = enumProperty;
   }

   public void setSetterCollection(Collection<?> setterCollection)
   {
      this.setterCollection = setterCollection;
   }

   public void setSetterSet(Set<?> setterSet)
   {
      this.setterSet = setterSet;
   }

   public void setSetterList(List<?> setterList)
   {
      this.setterList = setterList;
   }

   public void setSetterMap(Map<?, ?> setterMap)
   {
      this.setterMap = setterMap;
   }
}