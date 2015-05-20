package org.omegat.gui.dialogs.filter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringParser<T> {

	public T getObject(File file, Class c) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(c);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(file);
	}

	public void saveObject(File file, T o) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(o.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(o, file);
	}
}
