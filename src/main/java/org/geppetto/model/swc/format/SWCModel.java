
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
			throw new SWCException("A point skipped an index. Expected " + size() + 1 + " found " + pointIndex);
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
