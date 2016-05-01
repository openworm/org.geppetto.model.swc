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
