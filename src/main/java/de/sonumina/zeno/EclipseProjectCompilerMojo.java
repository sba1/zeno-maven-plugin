package de.sonumina.zeno;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.CompilationFailureException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.manager.CompilerManager;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal compile
 * 
 * @phase compile
 * 
 * @requiresDependencyResolution compile
 * 
 * @description Compiles the application sources with eclipse project configuration
 */
public class EclipseProjectCompilerMojo extends AbstractCompilerMojo
{
    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * The maven project
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The maven session
     * @parameter expression="${session}"
     */
    private MavenSession session;
    
    /**
     * Eclipse project settings.
     * 
     * TODO: Turn this into a real component.
     */
    private EclipseProjectSettings eps;

    public EclipseProjectCompilerMojo() throws ParserConfigurationException, SAXException, IOException
    {
    	eps = EclipseProjectSettings.createFromFile(new File(".classpath"));
	}
    
	@Override
	protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis)
	{
		return new StaleSourceScanner(staleMillis,Collections.singleton("**/*"),eps.getExcludes());
	}

	@Override
	protected SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding)
	{
		System.out
				.println("EclipseProjectCompilerMojo.getSourceInclusionScanner()");
		return new SimpleSourceInclusionScanner(Collections.singleton("**/*." + inputFileEnding), Collections.EMPTY_SET);
	}

	@Override
	protected List<String> getClasspathElements()
	{
		return eps.getClasspathElements();
	}

	@Override
	protected List<String> getCompileSourceRoots()
	{
		return eps.getCompileSourceRootList();
	}

	@Override
	protected File getOutputDirectory()
	{
		return new File(eps.getOutputDirectory());
	}

	@Override
	protected String getSource()
	{
		return "1.5";
	}

	@Override
	protected String getTarget()
	{
		return "1.5";
	}

	@Override
	protected String getCompilerArgument()
	{
		System.out.println("MyMojo.getCompilerArgument()");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, String> getCompilerArguments()
	{
		System.out.println("MyMojo.getCompilerArguments()");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected File getGeneratedSourcesDirectory()
	{
		System.out.println("MyMojo.getGeneratedSourcesDirectory()");
		// TODO Auto-generated method stub
		return null;
	}
}
