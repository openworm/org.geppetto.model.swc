
package org.geppetto.model.swc.format;


/**
 * @author matteocantarelli
 *
 */
public class SWCException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SWCException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SWCException(String message)
	{
		super(message);
	}

	public SWCException(Throwable e)
	{
		super(e);
	}

}
