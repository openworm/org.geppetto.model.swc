
package org.geppetto.model.swc.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.manager.SharedLibraryManager;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.swc.SWCModelInterpreterService;
import org.geppetto.model.swc.format.SWCException;
import org.geppetto.model.util.GeppettoModelTraversal;
import org.geppetto.model.util.GeppettoVisitingException;
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
	 * @throws GeppettoVisitingException 
	 * @throws GeppettoInitializationException 
	 */
	@Test
	public void testVisualTreeFeature() throws ModelInterpreterException, SWCException, GeppettoVisitingException, GeppettoInitializationException
	{
		URL swcFile = this.getClass().getResource("/5-HT1B-F-000000.swc");

		SWCModelInterpreterService modelInterpreter=new SWCModelInterpreterService();
		GeppettoLibrary library=GeppettoFactory.eINSTANCE.createGeppettoLibrary();
		library.setId("SWC");
		GeppettoLibrary commonLibrary=SharedLibraryManager.getSharedCommonLibrary();
		GeppettoModel geppettoModel=GeppettoFactory.eINSTANCE.createGeppettoModel();
		geppettoModel.getLibraries().add(library);
		geppettoModel.getLibraries().add(commonLibrary);
				
		GeppettoModelAccess commonLibraryAccess=new GeppettoModelAccess(geppettoModel);
				
		modelInterpreter.importType(swcFile, "MySWCType", library, commonLibraryAccess);
		GeppettoModelTraversal.apply(geppettoModel, new TestSwitch());
		
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
