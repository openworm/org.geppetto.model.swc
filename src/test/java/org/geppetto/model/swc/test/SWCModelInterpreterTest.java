/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.geppetto.model.swc.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.swc.SWCModelInterpreterService;
import org.geppetto.model.swc.format.SWCException;
import org.geppetto.model.swc.format.SWCReader;
import org.geppetto.model.values.Cylinder;
import org.geppetto.model.values.Sphere;
import org.geppetto.model.values.util.ValuesSwitch;
import org.junit.Before;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class SWCModelInterpreterTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		ServicesRegistry.registerModelFormat("SWC");
	}

	/**
	 * @throws ModelInterpreterException
	 * @throws SWCException
	 */
	@Test
	public void testVisualTreeFeature() throws ModelInterpreterException, SWCException
	{
		SWCReader swcReader=new SWCReader();
		URL swcFile = this.getClass().getResource("/5-HT1B-F-000000.swc");

		SWCModelInterpreterService modelInterpreter=new SWCModelInterpreterService();
		GeppettoLibrary library=GeppettoFactory.eINSTANCE.createGeppettoLibrary();
				
		modelInterpreter.importType(swcFile, "MySWCType", library, null);
		TestSwitch testSwitch=new TestSwitch();
		testSwitch.doSwitch(library);
		
	}

	private class TestSwitch extends ValuesSwitch<Object>
	{
		
		@Override
		public Object caseSphere(Sphere sphereNode)
		{
			assertNotNull(sphereNode.getRadius());
			assertFalse(sphereNode.getRadius() == 0);
			return true;
		}
		
		@Override
		public Object caseCylinder(Cylinder sphereNode)
		{
			assertNotNull(sphereNode.getBottomRadius());
			assertFalse(sphereNode.getBottomRadius()==0);
			assertNotNull(sphereNode.getTopRadius());
			assertFalse(sphereNode.getTopRadius()==0);
			return true;
		}		
	};
}
