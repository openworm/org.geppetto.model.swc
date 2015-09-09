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
package org.geppetto.model.swc.format;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author matteocantarelli
 *
 */
public class SWCModel
{

	private Map<Integer, SWCPoint> points = new LinkedHashMap<Integer, SWCPoint>();

	/**
	 * @return
	 */
	public Map<Integer, SWCPoint> getPointsMap()
	{
		return points;
	}

	/**
	 * @return
	 */
	public List<SWCPoint> getPoints()
	{
		return new ArrayList<SWCPoint>(points.values());
	}

	/**
	 * @param pointIndex
	 * @param swcPoint
	 * @throws SWCException 
	 */
	public void addPoint(int pointIndex, SWCPoint swcPoint) throws SWCException
	{
		if(size() != pointIndex - 1)
		{
			throw new SWCException("A point skipped an index. Expected " + size() + " found " + pointIndex);
		}
		points.put(pointIndex, swcPoint);
	}

	/**
	 * this method resolves parent indexes to actual SWCPoint objects
	 * 
	 * @throws SWCException
	 */
	public void resolve() throws SWCException
	{
		for(SWCPoint p : points.values())
		{
			if(p.getParentIndex() != -1)
			{
				if(points.containsKey(p.getParentIndex()))
				{
					p.setParent(points.get(p.getParentIndex()));
				}
				else
				{
					throw new SWCException("Parent index does not exist");
				}
			}
		}

	}

	/**
	 * @return
	 */
	public int size()
	{
		return points.size();
	}
}
