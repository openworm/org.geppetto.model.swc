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
package org.geppetto.model.swc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.features.IVisualTreeFeature;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.runtime.AVisualObjectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.typesystem.AspectNode;
import org.geppetto.core.model.typesystem.values.CylinderValue;
import org.geppetto.core.model.typesystem.values.SphereValue;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.visualisation.model.Point;
import org.geppetto.model.swc.format.SWCModel;
import org.geppetto.model.swc.format.SWCPoint;

/**
 * Populates visual tree for an aspect, given a SWC object to extract visualization objects from.
 * 
 * @author mcantarelli
 *
 */
public class SWCVisualTreeFeature implements IVisualTreeFeature
{

	private static Log logger = LogFactory.getLog(SWCVisualTreeFeature.class);

	private final GeppettoFeature type = GeppettoFeature.VISUAL_TREE_FEATURE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.features.IFeature#getType()
	 */
	@Override
	public GeppettoFeature getType()
	{
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.features.IVisualTreeFeature#populateVisualTree(org.geppetto.core.model.runtime.AspectNode)
	 */
	@Override
	public boolean populateVisualTree(AspectNode aspectNode) throws ModelInterpreterException
	{
		AspectSubTreeNode visualizationTree = (AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.VISUALIZATION_TREE);

		SWCModel swcModel = (SWCModel) ((ModelWrapper) aspectNode.getModel()).getModel(ServicesRegistry.getModelFormat("SWC"));
		for(SWCPoint point : swcModel.getPointsMap().values())
		{
			AVisualObjectNode object = getVisualObjectFromPoint(point);
			if(object != null)
			{
				//the first point doesn't have a parent.
				visualizationTree.addChild(object);
			}
		}
		visualizationTree.setModified(true);
		return true;
	}

	/**
	 * @param swcPoint
	 * @return
	 * @throws ModelInterpreterException
	 */
	private AVisualObjectNode getVisualObjectFromPoint(SWCPoint swcPoint) throws ModelInterpreterException
	{

		if(swcPoint.isSomaPoint())
		{
			SphereValue sphere = new SphereValue(swcPoint.getIndex().toString());
			sphere.setRadius(swcPoint.getRadius());
			sphere.setPosition(getPoint(swcPoint));
			return sphere;
		}
		else
		{
			if(swcPoint.getParent() != null)
			{
				CylinderValue cyl = new CylinderValue(swcPoint.getIndex().toString());

				cyl.setPosition(getPoint(swcPoint));
				cyl.setRadiusBottom(swcPoint.getRadius());

				cyl.setRadiusTop(swcPoint.getParent().getRadius());
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
		Point point = new Point();
		point.setX(swcPoint.getX());
		point.setY(swcPoint.getY());
		point.setZ(swcPoint.getZ());
		return point;
	}
}
