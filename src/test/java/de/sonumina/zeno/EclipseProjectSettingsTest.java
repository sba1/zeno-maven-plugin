package de.sonumina.zeno;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class EclipseProjectSettingsTest
{
	@Test
	public void testGetCompileSourceRootList() throws IOException, ParserConfigurationException, SAXException
	{
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					 "<classpath>\n" +
					 "<classpathentry kind=\"src\" path=\"src\"/>\n" +
					 "<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n" +
					 "<classpathentry kind=\"output\" path=\"bin\"/>\n" +
					 "</classpath>\n";
		File tmp = File.createTempFile("zeno", ".classpath");
		FileWriter fw = new FileWriter(tmp);
		fw.write(str.toCharArray());
		fw.close();

		EclipseProjectSettings eps = EclipseProjectSettings.createFromFile(tmp);
		assertEquals(1,eps.getCompileSourceRootList().size());
		assertEquals("src",eps.getCompileSourceRootList().get(0));
	}
}
