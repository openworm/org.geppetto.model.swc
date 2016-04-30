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

/**
 * @author matteocantarelli
 *
 */
public class SWCPoint
{

	// PointNo Label X Y Z Radius Parent
	private int index = -1;
	private int type; // Type of point: 1= soma, 2 = axon, etc.
	private double x;
	private double y;
	private double z;
	private double radius;
	private int parentPointIndex;
	private SWCPoint parent;

	/**
	 * @param index
	 * @param type
	 * @param parentPointIndex
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	SWCPoint(int index, int type, int parentPointIndex, double x, double y, double z, double radius)
	{
		this.index = index;
		this.type = type;
		this.parentPointIndex = parentPointIndex;
		setPosition(x, y, z);
		setRadius(radius);
	}

	/**
	 * @param parent
	 */
	public void setParent(SWCPoint parent)
	{
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public boolean isSomaPoint()
	{
		return ((type == 1) && (parent == -1));
	}

	/*
	 * x,y,z coordinates match
	 */
	public boolean samePoint(SWCPoint p)
	{
		return (x == p.x && y == p.y && z == p.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SWC point #" + this.hashCode() + " index: " + index + " (" + x + ", " + y + ", " + z + ")");
		sb.append("Parent: " + parent + " " + (parent != null ? parent.index : "???") + "; , type: " + type );
		return sb.toString();
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param radius
	 */
	public void setRadius(double radius)
	{
		this.radius = radius;
	}

	/**
	 * @return
	 */
	public int getParentIndex()
	{
		return parentPointIndex;
	}

	/**
	 * @param i
	 */
	public void setIndex(int i)
	{
		index = i;
	}

	/**
	 * @return
	 */
	public Double getRadius()
	{
		return radius;
	}

	/**
	 * @return
	 */
	public Integer getIndex()
	{
		return index;
	}

	/**
	 * @return
	 */
	public Double getX()
	{
		return x;
	}

	/**
	 * @return
	 */
	public Double getY()
	{
		return y;
	}

	/**
	 * @return
	 */
	public Double getZ()
	{
		return z;
	}

	/**
	 * @return
	 */
	public SWCPoint getParent()
	{
		return parent;
	}
}
