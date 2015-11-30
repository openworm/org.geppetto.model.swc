/*******************************************************************************
. * The MIT License (MIT)
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

package org.geppetto.model.swc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.beans.ModelInterpreterConfig;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.AModelInterpreter;
import org.geppetto.core.model.GeppettoCommonLibraryAccess;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.swc.format.SWCException;
import org.geppetto.model.swc.format.SWCModel;
import org.geppetto.model.swc.format.SWCPoint;
import org.geppetto.model.swc.format.SWCReader;
import org.geppetto.model.types.Type;
import org.geppetto.model.types.TypesFactory;
import org.geppetto.model.types.VisualType;
import org.geppetto.model.values.Cylinder;
import org.geppetto.model.values.Point;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Sphere;
import org.geppetto.model.values.ValuesFactory;
import org.geppetto.model.values.VisualComposite;
import org.geppetto.model.values.VisualValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class SWCModelInterpreterService extends AModelInterpreter
{

	private static Log logger = LogFactory.getLog(SWCModelInterpreterService.class);

	@Autowired
	private ModelInterpreterConfig swcModelInterpreterConfig;

	private ModelWrapper model;

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

	public ModelWrapper getModel()
	{
		return this.model;
	}

	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoCommonLibraryAccess commonLibraryAccess) throws ModelInterpreterException
	{
		dependentModels.clear();
		SWCReader swcReader=new SWCReader();
		
		SWCModel swcDocument;
		
		try
		{
			swcDocument = swcReader.readSWCFile(url);
		}
		catch(SWCException e)
		{
			throw new ModelInterpreterException(e);
		}
		
		VisualType swcModeType = TypesFactory.eINSTANCE.createVisualType();
		swcModeType.setId(typeName);
		swcModeType.setName(typeName);
		VisualComposite swcModel = ValuesFactory.eINSTANCE.createVisualComposite();
		swcModeType.setDefaultValue(swcModel);
		
		for(SWCPoint point : swcDocument.getPointsMap().values())
		{
			VisualValue object = getVisualValueFromPoint(point);
			if(object != null)
			{
				//the first point doesn't have a parent.
				swcModel.getValue().add(object);
			}
		}
		library.getTypes().add(swcModeType);
		return swcModeType;
		
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
