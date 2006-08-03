<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2001, ThoughtWorks, Inc.
 * 651 W Washington Ave. Suite 600
 * Chicago, IL 60661 USA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     + Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     + Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     + Neither the name of ThoughtWorks, Inc., CruiseControl, nor the
 *       names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ********************************************************************************-->
<xsl:output method="html"/>
<xsl:decimal-format decimal-separator="." grouping-separator="," />

<!-- ================================================================== -->
<!-- Write a package level report                                       -->
<!-- It creates a table with values from the document:                  -->
<!-- Name | Tests | Errors | Failures | Time                            -->
<!-- ================================================================== -->
<xsl:template match="cruisecontrol" priority="1">
    <table border="0" cellspacing="0" width="100%">
    <xsl:call-template name="table.header" />
    <xsl:for-each select="//testsuite">
        <xsl:sort select="count(testcase/error)" data-type="number" order="descending" />
        <xsl:sort select="count(testcase/failure)" data-type="number" order="descending" />
        <xsl:sort select="@package"/>
        <xsl:sort select="@name"/>

        <xsl:call-template name="print.class" />
        <xsl:apply-templates select="." mode="print.test" />
        <!-- xsl:call-template name="print.properties" /-->
    </xsl:for-each>
    </table>
</xsl:template>
    
<xsl:template match="system-out|system-err" mode="print.test"/>

<xsl:template match="testcase" mode="print.test">
    <tr>
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="error">unittests-error</xsl:when>
                <xsl:when test="failure">unittests-error</xsl:when>
                <xsl:otherwise>unittests-data</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <td />
        <td colspan="2">
            <xsl:value-of select="@name"/>
        </td>
        <td>
            <xsl:choose>
                <xsl:when test="error">
                    <a>
                        <xsl:attribute name="href">javascript:displayMessage('<xsl:value-of select="../@package"/>.<xsl:value-of select="../@name"/>.<xsl:value-of select='@name'/>');</xsl:attribute>
                        Error &#187;
                    </a>
                </xsl:when>
                <xsl:when test="failure">
                    <a>
                        <xsl:attribute name="href">javascript:displayMessage('<xsl:value-of select="../@package"/>.<xsl:value-of select="../@name"/>.<xsl:value-of select='@name'/>');</xsl:attribute>
                        Failure &#187;
                    </a>
                </xsl:when>
                <xsl:otherwise>Success</xsl:otherwise>
            </xsl:choose>
        </td>
        <xsl:if test="not(failure|error)">
            <td>
                <xsl:value-of select="format-number(@time,'0.000')"/>
            </td>
        </xsl:if>
    </tr>
</xsl:template>

<xsl:template name="table.header" >
    <colgroup>
        <col width="10%"></col>
        <col width="45%"></col>
        <col width="25%"></col>
        <col width="10%"></col>
        <col width="10%"></col>
    </colgroup>
    <tr valign="top" class="unittests-sectionheader" align="left" >
        <th colspan="3">Name</th>
        <th>Status</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<xsl:template name="print.class" >
    <tr>
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="testcase/error">unittests-error</xsl:when>
                <xsl:when test="testcase/failure">unittests-error</xsl:when>
                <xsl:otherwise>unittests-data</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <td colspan="5"><xsl:value-of select="@package"/>.<xsl:value-of select="@name"/></td>
    </tr>
</xsl:template>


</xsl:stylesheet>
