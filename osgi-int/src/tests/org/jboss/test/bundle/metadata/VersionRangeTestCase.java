
package org.jboss.test.bundle.metadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.osgi.plugins.metadata.AbstractVersionRange;
import org.jboss.osgi.spi.metadata.VersionRange;
import org.jboss.test.BaseTestCase;
import org.osgi.framework.Version;

/**
 * Tests of the version range comparision.
 * 
 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionRangeTestCase extends BaseTestCase
{
   public VersionRangeTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return new TestSuite(VersionRangeTestCase.class);
   }

   public void testRangeSpecRE() throws Exception
   {
      String rangeSpec = "1.0.0";
      //                       ( 1  ) (2 (3)  (4)   ) (5 (6)  (7)    (8)   ) (9 (10) (11)   (12)   (13)        )
      String versionPat = "\\[?(\\d+)|((\\d+).(\\d+))|((\\d+).(\\d+).(\\d+))|((\\d+).(\\d+).(\\d+).(\\p{Alnum}))\\]";
      Pattern VERSION_RANGE_RE = Pattern.compile(versionPat);
      Matcher matcher = VERSION_RANGE_RE.matcher(rangeSpec);
      if( matcher.matches() == true )
      {
         System.out.println("groupCount: "+matcher.groupCount());
         for(int n = 1; n <= matcher.groupCount(); n ++)
         {
            String g = matcher.group(n);
            System.out.println(n+":"+g);
         }
      }
   }

   public void testExclusiveRanges() throws Exception
   {
      VersionRange v100to110ExclusiveRange = AbstractVersionRange.parseRangeSpec("(1.0.0,1.1.0)");
      VersionRange v100GAto110GAExclusvieRange = AbstractVersionRange.parseRangeSpec("(1.0.0.GA,1.1.0.GA)");
      Version v100 = new Version("1.0.0");
      Version v100ga = new Version("1.0.0.GA");
      Version v110 = new Version("1.1.0");
      Version v110ga = new Version("1.1.0.GA");
      Version v101ga = new Version("1.0.1.GA");
      Version v102 = new Version("1.0.2");
      assertFalse("1.0.0 is in "+v100to110ExclusiveRange, v100to110ExclusiveRange.isInRange(v100));
      assertFalse("1.1.0 is in "+v100to110ExclusiveRange, v100to110ExclusiveRange.isInRange(v110));
      assertFalse("1.0.0.GA is in "+v100GAto110GAExclusvieRange, v100GAto110GAExclusvieRange.isInRange(v100ga));
      assertFalse("1.1.0.GA is in "+v100GAto110GAExclusvieRange, v100GAto110GAExclusvieRange.isInRange(v110ga));
      assertTrue("1.0.1.GA is in "+v100GAto110GAExclusvieRange, v100GAto110GAExclusvieRange.isInRange(v101ga));
      assertTrue("1.0.2 is in "+v100GAto110GAExclusvieRange, v100GAto110GAExclusvieRange.isInRange(v102));
   }

   public void testInclusiveRanges() throws Exception
   {
      VersionRange v100to110InclusiveRange = AbstractVersionRange.parseRangeSpec("[1.0.0,1.1.0]");
      VersionRange v100GAto110GAInclusiveRange = AbstractVersionRange.parseRangeSpec("[1.0.0.GA,1.1.0.GA]");
      Version v100 = new Version("1.0.0");
      Version v100ga = new Version("1.0.0.GA");
      Version v110 = new Version("1.1.0");
      Version v110ga = new Version("1.1.0.GA");
      Version v101ga = new Version("1.0.1.GA");
      Version v102 = new Version("1.0.2");
      assertTrue("1.0.0 is in "+v100to110InclusiveRange, v100to110InclusiveRange.isInRange(v100));
      assertTrue("1.1.0 is in "+v100to110InclusiveRange, v100to110InclusiveRange.isInRange(v110));
      assertTrue("1.0.0.GA is in "+v100GAto110GAInclusiveRange, v100GAto110GAInclusiveRange.isInRange(v100ga));
      assertTrue("1.1.0.GA is in "+v100GAto110GAInclusiveRange, v100GAto110GAInclusiveRange.isInRange(v110ga));
      assertTrue("1.0.1.GA is in "+v100GAto110GAInclusiveRange, v100GAto110GAInclusiveRange.isInRange(v101ga));
      assertTrue("1.0.2 is in "+v100GAto110GAInclusiveRange, v100GAto110GAInclusiveRange.isInRange(v102));
   }

   public void testMixedRanges() throws Exception
   {
      VersionRange v100to110InclusiveLowerExclusiveUpperRange = AbstractVersionRange.parseRangeSpec("[1.0.0,1.1.0)");
      VersionRange v100to110ExclusiveLowerInclusiveUpperRange = AbstractVersionRange.parseRangeSpec("(1.0.0,1.1.0]");
      VersionRange v100GAto110GAInclusiveLowerExclusiveUpperRange = AbstractVersionRange.parseRangeSpec("[1.0.0.GA,1.1.0.GA)");
      VersionRange v100GAto110GAExclusiveLowerInclusiveUpperRange = AbstractVersionRange.parseRangeSpec("(1.0.0.GA,1.1.0.GA]");
      Version v100 = new Version("1.0.0");
      Version v100ga = new Version("1.0.0.GA");
      Version v110 = new Version("1.1.0");
      Version v110ga = new Version("1.1.0.GA");
      Version v101ga = new Version("1.0.1.GA");
      Version v102 = new Version("1.0.2");
      assertTrue("1.0.0 is in "+v100to110InclusiveLowerExclusiveUpperRange, v100to110InclusiveLowerExclusiveUpperRange.isInRange(v100));
      assertFalse("1.0.0 is in "+v100to110ExclusiveLowerInclusiveUpperRange, v100to110ExclusiveLowerInclusiveUpperRange.isInRange(v100));
      assertFalse("1.1.0 is in "+v100to110InclusiveLowerExclusiveUpperRange, v100to110InclusiveLowerExclusiveUpperRange.isInRange(v110));
      assertTrue("1.1.0 is in "+v100to110ExclusiveLowerInclusiveUpperRange, v100to110ExclusiveLowerInclusiveUpperRange.isInRange(v110));

      assertTrue("1.0.0.GA is in "+v100GAto110GAInclusiveLowerExclusiveUpperRange, v100GAto110GAInclusiveLowerExclusiveUpperRange.isInRange(v100ga));
      assertFalse("1.1.0.GA is in "+v100GAto110GAInclusiveLowerExclusiveUpperRange, v100GAto110GAInclusiveLowerExclusiveUpperRange.isInRange(v110ga));
      assertFalse("1.0.0.GA is in "+v100GAto110GAExclusiveLowerInclusiveUpperRange, v100GAto110GAExclusiveLowerInclusiveUpperRange.isInRange(v100ga));
      assertTrue("1.1.0.GA is in "+v100GAto110GAExclusiveLowerInclusiveUpperRange, v100GAto110GAExclusiveLowerInclusiveUpperRange.isInRange(v110ga));
      assertTrue("1.0.1.GA is in "+v100GAto110GAExclusiveLowerInclusiveUpperRange, v100GAto110GAExclusiveLowerInclusiveUpperRange.isInRange(v101ga));
      assertTrue("1.0.2 is in "+v100GAto110GAExclusiveLowerInclusiveUpperRange, v100GAto110GAExclusiveLowerInclusiveUpperRange.isInRange(v102));
   }

   public void testSingleRange() throws Exception
   {
      VersionRange v100Range = AbstractVersionRange.parseRangeSpec("1.0.0");
      Version v100 = new Version("1.0.0");
      assertTrue("1.0.0 is in "+v100Range, v100Range.isInRange(v100));
      Version v200 = new Version("2.0.0");
      assertTrue("2.0.0 is in "+v100Range, v100Range.isInRange(v200));
      Version v200ga = new Version("2.0.0.GA");
      assertTrue("2.0.0.GA is in "+v100Range, v100Range.isInRange(v200ga));
      Version v09beta = new Version("0.9.0.beta");
      assertFalse("0.9.0.beta is in "+v100Range, v100Range.isInRange(v09beta));
   }
}
