

package org.geppetto.model.swc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geppetto.core.beans.ModelInterpreterConfig;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.AModelInterpreter;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.ModelFormat;
import org.geppetto.model.swc.format.SWCException;
import org.geppetto.model.swc.format.SWCModel;
import org.geppetto.model.swc.format.SWCPoint;
import org.geppetto.model.swc.format.SWCReader;
import org.geppetto.model.types.CompositeVisualType;
import org.geppetto.model.types.Type;
import org.geppetto.model.types.TypesFactory;
import org.geppetto.model.types.TypesPackage;
import org.geppetto.model.types.VisualType;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.values.Cylinder;
import org.geppetto.model.values.Point;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Sphere;
import org.geppetto.model.values.ValuesFactory;
import org.geppetto.model.values.VisualValue;
import org.geppetto.model.variables.Variable;
import org.geppetto.model.variables.VariablesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class SWCModelInterpreterService extends AModelInterpreter
{

	@Autowired
	private ModelInterpreterConfig swcModelInterpreterConfig;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.IModelInterpreter#getName()
	 */
	@Override
	public String getName()
	{
		return this.swcModelInterpreterConfig.getModelInterpreterName();
	}

	@Override
	public void registerGeppettoService()
	{
		List<ModelFormat> modelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("SWC")));
		ServicesRegistry.registerModelInterpreterService(this, modelFormats);
	}

	@Override
	public List<ModelFormat> getSupportedOutputs(Pointer pointer) throws ModelInterpreterException
	{
		List<ModelFormat> supportedOutputs = super.getSupportedOutputs(pointer);
		supportedOutputs.add(ServicesRegistry.getModelFormat("SWC"));
		return supportedOutputs;
	}

	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibraryAccess) throws ModelInterpreterException
	{
		try
		{
			dependentModels.clear();
			SWCReader swcReader = new SWCReader();

			SWCModel swcDocument = swcReader.readSWCFile(url);
			CompositeVisualType swcModeType = TypesFactory.eINSTANCE.createCompositeVisualType();

			VisualType visualType = (VisualType) commonLibraryAccess.getType(TypesPackage.Literals.VISUAL_TYPE);

			swcModeType.setId(typeName);
			swcModeType.setName(typeName);

			for(SWCPoint point : swcDocument.getPointsMap().values())
			{

				VisualValue object = getVisualValueFromPoint(point);
				if(object != null)
				{
					Variable v = VariablesFactory.eINSTANCE.createVariable();
					// the first point doesn't have a parent.
					v.getInitialValues().put(visualType, object);
					v.getTypes().add(visualType);
					v.setId("swcPoint" + point.getIndex());
					v.setName("SWC Segment " + point.getIndex());
					swcModeType.getVariables().add(v);
				}

			}
			library.getTypes().add(swcModeType);
			return swcModeType;
		}
		catch(GeppettoVisitingException e)
		{
			throw new ModelInterpreterException(e);
		}
		catch(SWCException e)
		{
			throw new ModelInterpreterException(e);
		}
	}

	@Override
	public File downloadModel(Pointer pointer, ModelFormat format, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException
	{
		throw new ModelInterpreterException("Download model not implemented for SWC model interpreter");
	}

	/**
	 * @param swcPoint
	 * @return
	 * @throws ModelInterpreterException
	 */
	private VisualValue getVisualValueFromPoint(SWCPoint swcPoint) throws ModelInterpreterException
	{

		if(swcPoint.isSomaPoint())
		{
			Sphere sphere = ValuesFactory.eINSTANCE.createSphere();
			sphere.setRadius(swcPoint.getRadius());
			sphere.setPosition(getPoint(swcPoint));
			return sphere;
		}
		else
		{
			if(swcPoint.getParent() != null)
			{
				Cylinder cyl = ValuesFactory.eINSTANCE.createCylinder();

				cyl.setPosition(getPoint(swcPoint));
				cyl.setBottomRadius(swcPoint.getRadius());
				cyl.setTopRadius(swcPoint.getParent().getRadius());
				cyl.setDistal(getPoint(swcPoint.getParent()));
				cyl.setHeight(0d);
				return cyl;
			}
		}
		return null;
	}

	/**
	 * @param swcPoint
	 * @return
	 */
	private Point getPoint(SWCPoint swcPoint)
	{
		Point point = ValuesFactory.eINSTANCE.createPoint();
		point.setX(swcPoint.getX());
		point.setY(swcPoint.getY());
		point.setZ(swcPoint.getZ());
		return point;
	}

}
