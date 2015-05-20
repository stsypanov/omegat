package gen.core.project;

import org.junit.Test;
import org.omegat.core.data.ProjectProperties;
import org.omegat.util.ProjectFileStorage;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by stsypanov on 13.05.2015.
 */
public class ProjectFileStorageTest {
	protected String path = "c:\\Users\\STsypanov\\IdeaProjects\\omegat-test-project\\";

	@Test
	public void testLoadProjectProperties() throws Exception {
		ProjectProperties projectProperties = getProjectProperties();
		String baseFilteringItems = projectProperties.getBaseFilteringItems();
		String expected = path + "baseFilteringItems.xml";
		assertEquals(expected, baseFilteringItems);
	}

	@Test
	public void testWriteProjectFile() throws Exception {

	}


	protected ProjectProperties getProjectProperties() throws Exception {
		File projectDir = new File(path);
		if (!projectDir.exists()){
			throw new Exception("Project dir does not exist");
		}
		return ProjectFileStorage.loadProjectProperties(projectDir);
	}
}