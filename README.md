Eclipse WTP/XML Search
======================

[![Build Status](https://secure.travis-ci.org/angelozerr/eclipse-wtp-xml-search.png)](http://travis-ci.org/angelozerr/eclipse-wtp-xml-search)
[![Eclipse install](https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=1121986)

# Overview

**Eclipse WTP/XML Search** is set of plugins which gives you 2 features : 
 
 * it provides a **XML/Search dialog** (like File/Search). You can search XML (HTML, JSP) files from your workspace with XPath:

![XML Search dialog](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/images/jetty/JettyXMLSearch2.png)

 * develop your own plugin to manage (easily) **completion, hyperlink, validation and Ctrl+Shift+G** (to retrieve referenced nodes) for your 
  custom XML, HTML, JSP files (ex : XML Spring files, web.xml, XML Jetty configuration, etc). The basic idea is to declare your link (ex: XML to XML, XML to Java , etc)
  with XPath Eclipse extension point. For instance XML Jetty file uses @class attributes to declare Java classes. You can declare this XML 2 Java link like this :

        <extension point="org.eclipse.wst.xml.search.editor.xmlReferences">
                <references contentTypeIds="org.eclipse.jst.server.jetty.xml.contenttype.jettyConfigFile">            
                        <!-- reference with class attribute -->
                        <reference>			
                                <from path="//" targetNodes="@class" />
                                <toJava />
                        </reference>												
                </references>
        </extension>

  and Eclipse WT/XML Search manages automaticly **completion, hyperlink, validation and Ctrl+Shift+G** for this XML to Java link. Here a screenshot about completion : 
  
![Jetty XMl Completion1](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/images/jetty/JettyXMLCompletion.png)    

Eclipse WTP/XML Search provides several modules: 
 
 * [core](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/WTP-XML-Search-Core) : the XML/Search dialog feature and XML/Search editor (to extends your own XML, HTML, JSP files with completion, hyperlink, validation and Ctrl+Shift+G).
 * [web](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/XML-web) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) in the web.xml.
 * [jetty](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/XML-Jetty-Plugins) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) for XML jetty configuration files.
 * [struts2](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/Struts2-IDE) : manage  completion, hyperlink, validation and (Ctrl+Shift+G) in the struts.xml and JSP.

# Installation

Use the update site [http://oss.opensagres.fr/eclipse-wtp-xml-search/1.0.0//](http://oss.opensagres.fr/eclipse-wtp-xml-search/1.0.0//) or [https://opensagres.ci.cloudbees.com/job/eclipse-wtp-xml-search/ws/update-site/target/repository/
](https://opensagres.ci.cloudbees.com/job/eclipse-wtp-xml-search/ws/update-site/target/repository/) to install Eclipse WTP/XML Search 
and read [Installation Guide](https://github.com/angelozerr/eclipse-wtp-xml-search/wiki/Installation-Guide) for more information.

# Who is using Eclipse WTP/XML Search?

 * [Liferay IDE](https://github.com/liferay/liferay-ide)
  
# Build

See cloudbees job: [https://opensagres.ci.cloudbees.com/job/eclipse-wtp-xml-search/](https://opensagres.ci.cloudbees.com/job/eclipse-wtp-xml-search/)

# Articles
 
I will hope than one day, **Eclipse WTP/XML Search**  will be host in the Eclipse WTP (see [bug 330576](https://bugs.eclipse.org/bugs/show_bug.cgi?id=330576)).
 
 You can read following [articles](http://angelozerr.wordpress.com/about/eclipse-wtp-xml-search/) about Eclipse WTP/XML Search.
