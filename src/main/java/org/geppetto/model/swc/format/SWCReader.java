
package org.geppetto.model.swc.format;

import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.utilities.URLReader;

/**
 * @author matteocantarelli
 *
 */
public class SWCReader
{

	private static Log logger = LogFactory.getLog(SWCReader.class);

	/**
	 * @param swcFile
	 * @return
	 * @throws IOException
	 */
	public SWCModel readSWCFile(URL swcFile) throws SWCException
	{
		try
		{
			return readSWCModel(URLReader.readStringFromURL(swcFile));
		}
		catch(IOException e)
		{
			throw new SWCException(e);
		}
	}

	/**
	 * @param swcContent
	 * @return
	 * @throws SWCException
	 */
	public SWCModel readSWCModel(String swcContent) throws SWCException
	{
		SWCModel model = new SWCModel();

		String line;
		double[] lineValues = new double[7];
		int pointIndex, label, parentPointIndex;
		double x, y, z, radius;

		String lines[] = swcContent.split("\\r?\\n");
		int ns = lines.length;
		for(int i = 0; i < ns; i++)
		{
			line = lines[i];
			if(!line.startsWith("#")) // # are comments
			{
				StringTokenizer st = new StringTokenizer(line, ", ");
				if(st.countTokens() == 7)
				{
					for(int j = 0; j < 7; j++)
					{
						String nextToken = st.nextToken();
						if(nextToken.equals("NA"))
						{
							nextToken = "-1";
						}
						lineValues[j] = Double.valueOf(nextToken).doubleValue();
					}
					pointIndex = (int) lineValues[0];
					label = (int) lineValues[1];
					x = lineValues[2];
					y = lineValues[3];
					z = lineValues[4];
					if (lineValues[5] > 0.0){ // enforsing a minimum radius of 1
						radius = lineValues[5];
					}else{
						radius = 1.0;
					}
					parentPointIndex = (int) lineValues[6];

					model.addPoint(pointIndex, new SWCPoint(pointIndex, label, parentPointIndex, x, y, z, radius));

				}
			}
		}

		logger.debug("Parsed " + model.getPointsMap().size() + " SWC points");

		model.resolve();

		return model;
	}

}
