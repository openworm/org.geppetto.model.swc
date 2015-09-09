package org.geppetto.model.swc.test;

import static org.junit.Assert.*;

import java.net.URL;

import org.geppetto.model.swc.format.SWCException;
import org.geppetto.model.swc.format.SWCModel;
import org.geppetto.model.swc.format.SWCReader;
import org.junit.Before;
import org.junit.Test;

public class SWCReaderTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testReadSWC() throws SWCException
	{
		SWCReader reader = new SWCReader();
		URL swcFile = this.getClass().getResource("/5-HT1B-F-000000.swc");
		SWCModel model = reader.readSWCFile(swcFile);
		System.out.println(model.size()+" points created");
		assertEquals(113,model.size());
	}

}
