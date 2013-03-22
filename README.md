Eclipse WTP/XML Search
======================

# Overview

**Eclipse WTP/XML Search** is set of plugins which gives you 2 features : 
 
 * it provides a **XML/Search dialog** (like File/Search). You can search XML (HTML, JSP) files from your workspace with XPath:

![XML Search dialog](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/images/JettyXMLSearch2.png)

 * develop your own plugin to manage (easily) **completion, hyperlink, validation and Ctrl+Shift+G** (to retrieve referenced nodes) for your 
  custom XML, HTML, JSP files (ex : XML Spring files, web.xml, XML Jetty configuration, etc). Thee basic idea is to declare your link (ex: XML to XML, XML to Java , etc)
  with XPath Eclipse extension point. Ex for Jetty, the XML Ref/@id attribute references :

   <extension
         point="org.eclipse.wst.xml.search.editor.xmlReferences">
         <references
               contentTypeIds="org.eclipse.jst.server.jetty.xml.contenttype.jettyConfigFile">
            
            <!-- reference with class attribute -->
 		<reference>			
				<from path="//"
					  targetNodes="@class"
					  querySpecificationId="jetty.querySpecification" />
				<toJava querySpecificationId="jetty.java.querySpecification"/>
			</reference>			

			<!-- reference Get => with Java method -->
			<reference>			
				<from path="/Configure//Get"
					  targetNodes="@name"
					  querySpecificationId="jetty.querySpecification" />
				<toJavaMethod querySpecificationId="jetty.javamethod.get.querySpecification" />
			</reference>
						
			<!-- reference Set => with Java method -->
			<reference>			
				<from path="/Configure//Set"
					  targetNodes="@name"
					  querySpecificationId="jetty.querySpecification" />
				<toJavaMethod querySpecificationId="jetty.javamethod.set.querySpecification" />
			</reference>

			<!-- reference Call => with Java method -->
			<reference>			
				<from path="/Configure//Call"
					  targetNodes="@name"
					  querySpecificationId="jetty.querySpecification" />
				<toJavaMethod querySpecificationId="jetty.javamethod.call.querySpecification" />
			</reference>
			
			<!-- reference with Ref id attribute -->
			<reference>
				<from path="/Configure//Ref"
					  targetNodes="@id"
					  querySpecificationId="jetty.querySpecification" />
				<to path="/Configure//" 
					targetNodes="@id"
					querySpecificationId="jetty.querySpecificationIgnoreRef"
					additionalProposalInfoProviderId="jetty.default.info" />							
			</reference> 		
  </references>
   </extension>

  and Eclipse WT/XML Search manages automaticly **completion, hyperlink, validation and Ctrl+Shift+G**
  
  JettyXMLRefCompletion.png

Eclipse WTP/XML Search provides several modules: 
 
 * [core](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/WTP-XML-Search-Core) : the XML/Search dialog feature and XML/Search editor (to extends your own XML, HTML, JSP files with completion, hyperlink, validation and Ctrl+Shift+G).
 * [web](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/XML-web) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) in the web.xml.
 * [jetty](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/XML-Jetty-Plugins) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) for XML jetty configuration files.
 * [struts2](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/Struts2-IDE) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) in the struts.xml and JSP.

# Installation

TODO : create an update site.

# Articles
 
 I will hope than one day, **Eclipse WTP/XML Search**  will be host in the Eclipse WTP (see [bug 330576](https://bugs.eclipse.org/bugs/show_bug.cgi?id=330576)).
 
 You can read following [articles](http://angelozerr.wordpress.com/about/eclipse-wtp-xml-search/) about Eclipse WTP/XML Search.
