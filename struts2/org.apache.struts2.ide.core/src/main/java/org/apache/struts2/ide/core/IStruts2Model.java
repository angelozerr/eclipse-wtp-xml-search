package org.apache.struts2.ide.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;

public interface IStruts2Model {

	/**
	 * Returns "struts-default.xml" {@link IStorage} from the JAR
	 * "struts2-core-*.jar".
	 * 
	 * @param project
	 * @return
	 */
	IStorage getStrutsDefaultFromStruts2CoreJAR(IProject project);
	
	/**
	 * Returns "default.xml" {@link IStorage} from the JAR
	 * "xwork-core-*.jar".
	 * 
	 * @param project
	 * @return
	 */
	IStorage getDefaultFromXWorkCoreJAR(IProject project);

	/**
	 * 
	 * @param project
	 * @return
	 */
	IStorage[] getStrutsXMLFromJAR(IProject project);

}
