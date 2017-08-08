
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
		return ((type == 1) && (parentPointIndex == -1));
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
