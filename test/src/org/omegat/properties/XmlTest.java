package org.omegat.properties;

import gen.core.preference.Omegat;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by stsypanov on 05.02.2015.
 */
public class XmlTest {
	private static final Logger logger = Logger.getLogger(XmlTest.class.getName());

	@Test()
	public void testRead() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Omegat.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		try (InputStream resourceAsStream = new FileInputStream("test/data/preferences/omegat.prefs")) {
			Omegat result = (Omegat) unmarshaller.unmarshal(new InputStreamReader(resourceAsStream));
			assertNotNull(result.getPreference());
			logger.info(result.getPreference().toString());
		}
	}
}
