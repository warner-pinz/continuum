package org.apache.maven.continuum.project.builder.maven;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.continuum.model.repository.LocalRepository;
import org.apache.maven.continuum.builddefinition.BuildDefinitionService;
import org.apache.maven.continuum.builddefinition.BuildDefinitionServiceException;
import org.apache.maven.continuum.configuration.ConfigurationService;
import org.apache.maven.continuum.execution.maven.m2.MavenBuilderHelper;
import org.apache.maven.continuum.execution.maven.m2.MavenTwoBuildExecutor;
import org.apache.maven.continuum.model.project.BuildDefinition;
import org.apache.maven.continuum.model.project.BuildDefinitionTemplate;
import org.apache.maven.continuum.model.project.Project;
import org.apache.maven.continuum.model.project.ProjectGroup;
import org.apache.maven.continuum.model.project.Schedule;
import org.apache.maven.continuum.project.builder.AbstractContinuumProjectBuilder;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuilder;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuilderException;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuildingResult;
import org.apache.maven.continuum.store.ContinuumStore;
import org.apache.maven.continuum.store.ContinuumStoreException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 * @plexus.component role="org.apache.maven.continuum.project.builder.ContinuumProjectBuilder" role-hint="maven-two-builder"
 */
public class MavenTwoContinuumProjectBuilder
    extends AbstractContinuumProjectBuilder
    implements ContinuumProjectBuilder
{
    public static final String ID = "maven-two-builder";

    private static final String POM_PART = "/pom.xml";

    /**
     * @plexus.requirement
     */
    private MavenBuilderHelper builderHelper;

    /**
     * @plexus.requirement role-hint="jdo"
     */
    private ContinuumStore store;

    /**
     * @plexus.requirement
     */
    private BuildDefinitionService buildDefinitionService;

    /**
     * @plexus.configuration
     */
    private List<String> excludedPackagingTypes = new ArrayList<String>();

    // ----------------------------------------------------------------------
    // AbstractContinuumProjectBuilder Implementation
    // ----------------------------------------------------------------------
    public ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password )
        throws ContinuumProjectBuilderException
    {
        return buildProjectsFromMetadata( url, username, password, true );
    }

    public ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password,
                                                                     boolean loadRecursiveProjects )
        throws ContinuumProjectBuilderException
    {
        try
        {
            return buildProjectsFromMetadata( url, username, password, loadRecursiveProjects, buildDefinitionService
                .getDefaultMavenTwoBuildDefinitionTemplate() );
        }
        catch ( BuildDefinitionServiceException e )
        {
            throw new ContinuumProjectBuilderException( e.getMessage(), e );
        }
    }

    public ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password,
                                                                     boolean loadRecursiveProjects,
                                                                     BuildDefinitionTemplate buildDefinitionTemplate )
        throws ContinuumProjectBuilderException
    {
        // ----------------------------------------------------------------------
        // We need to roll the project data into a file so that we can use it
        // ----------------------------------------------------------------------

        ContinuumProjectBuildingResult result = new ContinuumProjectBuildingResult();

        try
        {
            readModules( url, result, true, username, password, null, loadRecursiveProjects, buildDefinitionTemplate );
        }
        catch ( BuildDefinitionServiceException e )
        {
            throw new ContinuumProjectBuilderException( e.getMessage(), e );
        }
        return result;
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    private void readModules( URL url, ContinuumProjectBuildingResult result, boolean groupPom, String username,
                              String password, String scmUrl, boolean loadRecursiveProjects,
                              BuildDefinitionTemplate buildDefinitionTemplate )
        throws ContinuumProjectBuilderException, BuildDefinitionServiceException
    {

        MavenProject mavenProject;

        File pomFile = null;
        
        try
        {
            pomFile = createMetadataFile( url, username, password );
            mavenProject = builderHelper.getMavenProject( result, pomFile );

            if ( result.hasErrors() )
            {
                return;
            }
        }
        catch ( MalformedURLException e )
        {
            getLogger().debug( "Error adding project: Malformed URL " + url, e );
            result.addError( ContinuumProjectBuildingResult.ERROR_MALFORMED_URL );
            return;
        }
        catch ( FileNotFoundException e )
        {
            getLogger().debug( "Error adding project: File not found " + url, e );
            result.addError( ContinuumProjectBuildingResult.ERROR_POM_NOT_FOUND );
            return;
        }
        catch ( ConnectException e )
        {
            getLogger().debug( "Error adding project: Unable to connect " + url, e );
            result.addError( ContinuumProjectBuildingResult.ERROR_CONNECT );
            return;
        }
        catch ( IOException e )
        {
            if ( e.getMessage() != null )
            {
                if ( e.getMessage().indexOf( "Server returned HTTP response code: 401" ) >= 0 )
                {
                    getLogger().debug( "Error adding project: Unauthorized " + url, e );
                    result.addError( ContinuumProjectBuildingResult.ERROR_UNAUTHORIZED );
                    return;
                }
            }
            getLogger().info( "Error adding project: Unknown error downloading from " + url, e );
            result.addError( ContinuumProjectBuildingResult.ERROR_UNKNOWN );
            return;
        }
        finally
        {
            if ( pomFile != null && pomFile.exists() )
            {
                pomFile.delete();
            }
        }
        getLogger().debug( "groupPom " + groupPom );
        if ( groupPom )
        {
            ProjectGroup projectGroup = buildProjectGroup( mavenProject, result );

            // project groups have the top lvl build definition which is the default build defintion for the sub
            // projects
            getLogger().debug( "projectGroup != null" + ( projectGroup != null ) );
            if ( projectGroup != null )
            {
                List<BuildDefinition> buildDefinitions =
                    getBuildDefinitions( buildDefinitionTemplate, loadRecursiveProjects );
                boolean defaultSetted = false;
                for ( BuildDefinition buildDefinition : buildDefinitions )
                {
                    if ( !defaultSetted && buildDefinition.isDefaultForProject() )
                    {
                        buildDefinition.setDefaultForProject( true );
                        defaultSetted = true;
                    }
                    buildDefinition = buildDefinitionService.addBuildDefinition(
                        buildDefinitionService.cloneBuildDefinition( buildDefinition ) );
                    //CONTINUUM-1296
                    String defaultGoal = mavenProject.getBuild().getDefaultGoal();
                    if ( StringUtils.isNotEmpty( defaultGoal ) )
                    {
                        buildDefinition.setGoals( defaultGoal );
                    }
                    if ( buildDefinition.getSchedule() == null )
                    {
                        try
                        {
                            Schedule schedule = store.getScheduleByName( ConfigurationService.DEFAULT_SCHEDULE_NAME );

                            buildDefinition.setSchedule( schedule );
                        }
                        catch ( ContinuumStoreException e )
                        {
                            getLogger().warn( "Can't get default schedule.", e );
                        }
                    }
                    // jdo complains that Collections.singletonList(bd) is a second class object and fails.
                    //ArrayList arrayList = new ArrayList();

                    //arrayList.add( buildDefinition );

                    projectGroup.addBuildDefinition( buildDefinition );
                    // .setBuildDefinitions( arrayList );
                }
                result.addProjectGroup( projectGroup );
            }
        }

        if ( !excludedPackagingTypes.contains( mavenProject.getPackaging() ) )
        {
            Project continuumProject = new Project();

            /*
            We are interested in having the scm username and password being passed into this method be taken into
            account during project mapping so make sure we set it to the continuum project instance.
             */
            if ( username != null && StringUtils.isNotEmpty( username ) )
            {
                continuumProject.setScmUsername( username );

                if ( password != null && StringUtils.isNotEmpty( password ) )
                {
                    continuumProject.setScmPassword( password );
                }
            }

            builderHelper.mapMavenProjectToContinuumProject( result, mavenProject, continuumProject, groupPom );

            if ( result.hasErrors() )
            {
                getLogger().info(
                    "Error adding project: Unknown error mapping project " + url + ": " + result.getErrorsAsString() );
                return;
            }

            // Rewrite scmurl from the one found in added project due to a bug in scm url resolution
            // for projects that doesn't have module name != artifactId
            if ( StringUtils.isNotEmpty( scmUrl ) )
            {
                continuumProject.setScmUrl( scmUrl );
            }
            else
            {
                scmUrl = continuumProject.getScmUrl();
            }

            if ( !"HEAD".equals( mavenProject.getScm().getTag() ) )
            {
                continuumProject.setScmTag( mavenProject.getScm().getTag() );
            }

            result.addProject( continuumProject, MavenTwoBuildExecutor.ID );
        }

        List<String> modules = mavenProject.getModules();

        String prefix = url.toExternalForm();

        String suffix = "";

        int i = prefix.indexOf( '?' );

        int lastSlash;

        if ( i != -1 )
        {
            suffix = prefix.substring( i );

            lastSlash = prefix.lastIndexOf( "/", i );
        }
        else
        {
            lastSlash = prefix.lastIndexOf( "/" );
        }

        prefix = prefix.substring( 0, lastSlash );
        if ( loadRecursiveProjects )
        {
            for ( String module : modules )
            {
                if ( StringUtils.isNotEmpty( module ) )
                {
                    String urlString = prefix + "/" + module + POM_PART + suffix;

                    URL moduleUrl;

                    try
                    {
                        moduleUrl = new URL( urlString );
                    }
                    catch ( MalformedURLException e )
                    {
                        getLogger().debug( "Error adding project module: Malformed URL " + urlString, e );
                        result.addError( ContinuumProjectBuildingResult.ERROR_MALFORMED_URL, urlString );
                        continue;
                    }

                    String moduleScmUrl;
                    if ( scmUrl.endsWith( "/" ) )
                    {
                        moduleScmUrl = scmUrl + module;
                    }
                    else
                    {
                        moduleScmUrl = scmUrl + "/" + module;
                    }
                    // we are in recursive loading mode
                    readModules( moduleUrl, result, false, username, password, moduleScmUrl, true,
                                 buildDefinitionTemplate );
                }
            }
        }
    }

    private List<BuildDefinition> getBuildDefinitions( BuildDefinitionTemplate template, boolean loadRecursiveProjects )
        throws ContinuumProjectBuilderException, BuildDefinitionServiceException
    {
        List<BuildDefinition> buildDefinitions = new ArrayList<BuildDefinition>();
        for ( BuildDefinition buildDefinition : (List<BuildDefinition>) template.getBuildDefinitions() )
        {
            // due to CONTINUUM-1207 CONTINUUM-1436 user can do what they want with arguments
            // we must remove if exists --non-recursive or -N
            if ( !loadRecursiveProjects )
            {
                if ( StringUtils.isEmpty( buildDefinition.getArguments() ) )
                {
                    // strange for a mvn build 
                    getLogger().info( "build definition has empty args" );
                }
                else
                {
                    String arguments = buildDefinition.getArguments().replace( "--non-recursive", "" );
                    arguments = arguments.replace( "-N", "" );
                    buildDefinition.setArguments( arguments );
                }
            }
            buildDefinition.setTemplate( false );
            buildDefinitions.add( buildDefinition );
        }
        return buildDefinitions;

    }

    private ProjectGroup buildProjectGroup( MavenProject mavenProject, ContinuumProjectBuildingResult result )
    {
        ProjectGroup projectGroup = new ProjectGroup();

        // ----------------------------------------------------------------------
        // Group id
        // ----------------------------------------------------------------------

        if ( StringUtils.isEmpty( mavenProject.getGroupId() ) )
        {
            result.addError( ContinuumProjectBuildingResult.ERROR_MISSING_GROUPID );

            return null;
        }

        projectGroup.setGroupId( mavenProject.getGroupId() );

        // ----------------------------------------------------------------------
        // Name
        // ----------------------------------------------------------------------

        String name = mavenProject.getName();

        if ( StringUtils.isEmpty( name ) )
        {
            name = mavenProject.getGroupId();
        }

        projectGroup.setName( name );

        // ----------------------------------------------------------------------
        // Description
        // ----------------------------------------------------------------------

        projectGroup.setDescription( mavenProject.getDescription() );

        // ----------------------------------------------------------------------
        // Local Repository
        // ----------------------------------------------------------------------
        
        try
        {
            LocalRepository repository = store.getLocalRepositoryByName( "DEFAULT" );
        
            projectGroup.setLocalRepository( repository );
        }
        catch ( ContinuumStoreException e )
        {
            getLogger().warn( "Can't get default repository.", e );
        }
        
        return projectGroup;
    }


    public BuildDefinitionTemplate getDefaultBuildDefinitionTemplate()
        throws ContinuumProjectBuilderException
    {
        try
        {
            return buildDefinitionService.getDefaultMavenTwoBuildDefinitionTemplate();
        }
        catch ( BuildDefinitionServiceException e )
        {
            throw new ContinuumProjectBuilderException( e.getMessage(), e );
        }
    }

}
