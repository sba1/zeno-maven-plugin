package de.sonumina.zeno;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EclipseProjectSettings
{
	private Map<String,String[]> compileSourceRoots = new LinkedHashMap<String,String[]>(); 
	private List<String> classpathElementList = new ArrayList<String>();
	private String outputDirectory;

	/**
	 * Return the list of all source roots.
	 * 
	 * @return
	 */
	public List<String> getCompileSourceRootList()
	{
		ArrayList<String> sourceRoots = new ArrayList<String>();
		for (String sourceRoot : compileSourceRoots.keySet())
			sourceRoots.add(sourceRoot);
		return sourceRoots;
		
	}

	public String getOutputDirectory()
	{
		return outputDirectory;
	}

	public List<String> getClasspathElements()
	{
		return classpathElementList;
	}

	public Set<String> getExcludes()
	{
		LinkedHashSet<String> excludes = new LinkedHashSet<String>();
		for (Entry<String,String[]> e : compileSourceRoots.entrySet())
		{
			for (String v : e.getValue())
				excludes.add(v);
		}
		return excludes;
	}

	static public EclipseProjectSettings createFromFile(File f) throws ParserConfigurationException, SAXException, IOException
	{
		EclipseProjectSettings eps = new EclipseProjectSettings();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("classpathentry");
		for (int i=0;i<nList.getLength();i++)
		{
			Node n = nList.item(i);
			Node kind = n.getAttributes().getNamedItem("kind");
			Node path = n.getAttributes().getNamedItem("path");
			if (kind != null && path != null)
			{
				String kindVal = kind.getNodeValue();
				String pathVal = path.getNodeValue();
				if (kindVal.equalsIgnoreCase("src"))
				{
					String [] excludingArray;
					Node excluding = n.getAttributes().getNamedItem("excluding");
					if (excluding != null) excludingArray = excluding.getNodeValue().split("\\|");
					else excludingArray = new String[0];
					eps.compileSourceRoots.put(pathVal, excludingArray);
				}
				else if (kindVal.equalsIgnoreCase("output")) eps.outputDirectory = pathVal;
				else if (kindVal.equalsIgnoreCase("lib")) eps.classpathElementList.add(pathVal);
			}
		}
		return eps;
	}

}
