/*******************************************************************************
 * Copyright (c) 2011 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.search.core.statics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;

public class DefaultStaticValueVisitor implements IStaticValueVisitor {

	private Map<String, IStaticValue> values = new HashMap<String, IStaticValue>();

	public void registerValue(String key, String description) {
		registerValue(new StaticValue(key, description));
	}

	public void registerValue(IStaticValue staticValue) {
		values.put(staticValue.getKey(), staticValue);
	}

	public void clearValues() {
		values.clear();
	}

	public void visit(Object selectedNode, IFile file, String matching,
			boolean startsWith, IStaticValueCollector collector) {
		if (startsWith) {
			Set<Entry<String, IStaticValue>> entries = values.entrySet();
			for (Entry<String, IStaticValue> entry : entries) {
				if (entry.getKey().startsWith(matching)) {
					collector.add(entry.getValue());
				}
			}
		} else {
			IStaticValue value = values.get(matching);
			if (value != null) {
				collector.add(value);
			}
		}
	}

}
