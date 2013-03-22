Eclipse WTP/XML Search
======================

# Overview

**Eclipse WTP/XML Search** is set of plugins which gives you 2 features : 
 
 * it provides a **XML/Search dialog** (like File/Search). You can search XML (HTML, JSP) files from your workspace with XPath:

![XML Search dialog](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/images/JettyXMLSearch2.png)

 * develop your own plugin to manage (easily) **completion, hyperlink, validation and Ctrl+Shift+G** (to retrieve referenced nodes) for your 
  custom XML, HTML, JSP files (ex : XML Spring files, web.xml, XML Jetty configuration, etc). The basic idea is to declare your link (ex: XML to XML, XML to Java , etc)
  with XPath Eclipse extension point. For instance XML Jetty file uses @class attributes to declare Java classes. You can declare this XML 2 Java link like this :

        <extension  point="org.eclipse.wst.xml.search.editor.xmlReferences">
                <references contentTypeIds="org.eclipse.jst.server.jetty.xml.contenttype.jettyConfigFile">            
                        <!-- reference with class attribute -->
                        <reference>			
                                <from path="//" targetNodes="@class" />
                                <toJava />
                        </reference>												
                </references>
        </extension>

  and Eclipse WT/XML Search manages automaticly **completion, hyperlink, validation and Ctrl+Shift+G** for this XML to Java like. Here a screenshot about completion : 
  
![Jetty XMl Completion1](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/images/JettyXMLCompletion.png)    

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
