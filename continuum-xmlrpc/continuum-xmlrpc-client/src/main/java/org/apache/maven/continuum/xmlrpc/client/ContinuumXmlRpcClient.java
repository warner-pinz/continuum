package org.apache.maven.continuum.xmlrpc.client;

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

import org.apache.maven.continuum.ContinuumException;
import org.apache.maven.continuum.xmlrpc.ContinuumService;
import org.apache.maven.continuum.xmlrpc.project.AddingResult;
import org.apache.maven.continuum.xmlrpc.project.BuildResult;
import org.apache.maven.continuum.xmlrpc.project.ContinuumProjectState;
import org.apache.maven.continuum.xmlrpc.project.Project;
import org.apache.maven.continuum.xmlrpc.project.ProjectGroup;
import org.apache.maven.continuum.xmlrpc.project.ProjectGroupSummary;
import org.apache.maven.continuum.xmlrpc.project.ProjectSummary;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public class ContinuumXmlRpcClient
    implements ContinuumService
{
    private ContinuumService continuum;

    private static Hashtable statusMap;

    static
    {
        statusMap = new Hashtable();
        statusMap.put( new Integer( ContinuumProjectState.NEW ), "New" );
        statusMap.put( new Integer( ContinuumProjectState.CHECKEDOUT ), "New" );
        statusMap.put( new Integer( ContinuumProjectState.OK ), "OK" );
        statusMap.put( new Integer( ContinuumProjectState.FAILED ), "Failed" );
        statusMap.put( new Integer( ContinuumProjectState.ERROR ), "Error" );
        statusMap.put( new Integer( ContinuumProjectState.BUILDING ), "Building" );
        statusMap.put( new Integer( ContinuumProjectState.CHECKING_OUT ), "Checking out" );
        statusMap.put( new Integer( ContinuumProjectState.UPDATING ), "Updating" );
        statusMap.put( new Integer( ContinuumProjectState.WARNING ), "Warning" );
    }

    public ContinuumXmlRpcClient( URL serviceUrl )
    {
        this( serviceUrl, null, null );
    }

    public ContinuumXmlRpcClient( URL serviceUrl, String login, String password )
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl()
        {
            public boolean isEnabledForExtensions()
            {
                return true;
            }
        };

        if ( login != null )
        {
            config.setBasicUserName( login );
            config.setBasicPassword( password );
        }
        config.setServerURL( serviceUrl );

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig( config );
        ClientFactory factory = new ClientFactory( client );
        continuum = (ContinuumService) factory.newInstance( ContinuumService.class );
    }

    public boolean ping()
        throws ContinuumException
    {
        try
        {
            return continuum.ping();
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Projects
    // ----------------------------------------------------------------------

    public List getProjects( int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.getProjects( projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectSummary getProjectSummary( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.getProjectSummary( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public Project getProjectWithAllDetails( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.getProjectWithAllDetails( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public int removeProject( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.removeProject( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectSummary refreshProjectSummary( ProjectSummary project )
        throws ContinuumException
    {
        if ( project == null )
        {
            return null;
        }
        return getProjectSummary( project.getId() );
    }

    public Project refreshProjectWithAllDetails( ProjectSummary project )
        throws ContinuumException
    {
        if ( project == null )
        {
            return null;
        }
        return getProjectWithAllDetails( project.getId() );
    }

    // ----------------------------------------------------------------------
    // Projects Groups
    // ----------------------------------------------------------------------

    public ProjectGroupSummary getProjectGroupSummary( int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.getProjectGroupSummary( projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectGroup getProjectGroupWithProjects( int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.getProjectGroupWithProjects( projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public int removeProjectGroup( int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.removeProjectGroup( projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectGroupSummary refreshProjectGroupSummary( ProjectGroupSummary projectGroup )
        throws ContinuumException
    {
        if ( projectGroup == null )
        {
            return null;
        }
        return getProjectGroupSummary( projectGroup.getId() );
    }

    public ProjectGroup refreshProjectGroupSummaryWithProjects( ProjectGroupSummary projectGroup )
        throws ContinuumException
    {
        if ( projectGroup == null )
        {
            return null;
        }
        return getProjectGroupWithProjects( projectGroup.getId() );
    }

    // ----------------------------------------------------------------------
    // Building
    // ----------------------------------------------------------------------

    public int addProjectToBuildQueue( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.addProjectToBuildQueue( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }

    }

    public int addProjectToBuildQueue( int projectId, int buildDefinitionId )
        throws ContinuumException
    {
        try
        {
            return continuum.addProjectToBuildQueue( projectId, buildDefinitionId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }

    }

    public int buildProject( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.buildProject( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public int buildProject( int projectId, int buildDefinitionId )
        throws ContinuumException
    {
        try
        {
            return continuum.buildProject( projectId, buildDefinitionId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Build Results
    // ----------------------------------------------------------------------

    public BuildResult getLatestBuildResult( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.getLatestBuildResult( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public BuildResult getBuildResult( int projectId, int buildId )
        throws ContinuumException
    {
        try
        {
            return continuum.getBuildResult( projectId, buildId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public List getBuildResultsForProject( int projectId )
        throws ContinuumException
    {
        try
        {
            return continuum.getBuildResultsForProject( projectId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public String getBuildOutput( int projectId, int buildId )
        throws ContinuumException
    {
        try
        {
            return continuum.getBuildOutput( projectId, buildId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Maven 2.x projects
    // ----------------------------------------------------------------------

    public AddingResult addMavenTwoProject( String url )
        throws ContinuumException
    {
        try
        {
            return continuum.addMavenTwoProject( url );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public AddingResult addMavenTwoProject( String url, int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.addMavenTwoProject( url, projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Maven 1.x projects
    // ----------------------------------------------------------------------

    public AddingResult addMavenOneProject( String url )
        throws ContinuumException
    {
        try
        {
            return continuum.addMavenOneProject( url );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public AddingResult addMavenOneProject( String url, int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.addMavenOneProject( url, projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Maven ANT projects
    // ----------------------------------------------------------------------

    public ProjectSummary addAntProject( ProjectSummary project )
        throws ContinuumException
    {
        try
        {
            return continuum.addAntProject( project );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectSummary addAntProject( ProjectSummary project, int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.addAntProject( project, projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Maven Shell projects
    // ----------------------------------------------------------------------

    public ProjectSummary addShellProject( ProjectSummary project )
        throws ContinuumException
    {
        try
        {
            return continuum.addShellProject( project );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    public ProjectSummary addShellProject( ProjectSummary project, int projectGroupId )
        throws ContinuumException
    {
        try
        {
            return continuum.addShellProject( project, projectGroupId );
        }
        catch ( Exception e )
        {
            throw new ContinuumException( "The remote method failed.", e );
        }
    }

    // ----------------------------------------------------------------------
    // Utils
    // ----------------------------------------------------------------------

    public String getProjectStatusAsString( int status )
    {
        return (String) statusMap.get( new Integer( status ) );
    }
}