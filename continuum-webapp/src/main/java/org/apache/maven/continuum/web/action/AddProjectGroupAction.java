package org.apache.maven.continuum.web.action;

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

import java.util.Iterator;
import java.util.List;

import org.apache.continuum.model.repository.LocalRepository;
import org.apache.continuum.repository.RepositoryServiceException;
import org.apache.maven.continuum.ContinuumException;
import org.apache.maven.continuum.model.project.ProjectGroup;
import org.apache.maven.continuum.web.exception.AuthorizationRequiredException;

import com.opensymphony.xwork.Validateable;

/**
 * @author Henry Isidro <hisidro@exist.com>
 * @plexus.component role="com.opensymphony.xwork.Action" role-hint="addProjectGroup"
 */
public class AddProjectGroupAction
    extends ContinuumActionSupport
    implements Validateable
{
    private String name;

    private String groupId;

    private String description;
    
    private int repositoryId;
    
    private List<LocalRepository> repositories;

    public void validate()
    {
        clearErrorsAndMessages();
        if ( name != null && name.equals( "" ) )
        {
            addActionError( "projectGroup.error.name.required" );
        }
        else if ( name != null && name.trim().equals( "" ) )
        {
            addActionError( "projectGroup.error.name.cannot.be.spaces" );
        }
        else if ( name != null && !name.equals( "" ) )
        {
            Iterator iterator = getContinuum().getAllProjectGroups().iterator();
            while ( iterator.hasNext() )
            {
                ProjectGroup projectGroup = (ProjectGroup) iterator.next();
                if ( name.equals( projectGroup.getName() ) )
                {
                    addActionError( "projectGroup.error.name.already.exists" );
                    break;
                }
            }
        }
        if ( groupId != null && groupId.equals( "" ) )
        {
            addActionError( "projectGroup.error.groupId.required" );
        }
        else if ( groupId != null && groupId.trim().equals( "" ) )
        {
            addActionError( "projectGroup.error.groupId.cannot.be.spaces" );
        }
        else
        {
            try
            {
                if ( getContinuum().getProjectGroupByGroupId( groupId ) != null )
                {
                    addActionError( "projectGroup.error.groupId.already.exists" );
                }
            }
            catch ( ContinuumException e )
            {
                //since we want to add a new project group, we should be getting
                //this exception
            }
        }
    }

    public String execute()
    {
        try
        {
            checkAddProjectGroupAuthorization();
        }
        catch ( AuthorizationRequiredException authzE )
        {
            addActionError( authzE.getMessage() );
            return REQUIRES_AUTHORIZATION;
        }

        ProjectGroup projectGroup = new ProjectGroup();

        projectGroup.setName( name );

        projectGroup.setGroupId( groupId );

        projectGroup.setDescription( description );
        
        try
        {
            if ( repositoryId > 0 )
            {
                LocalRepository repository = getContinuum().getRepositoryService().getLocalRepository( repositoryId );
                projectGroup.setLocalRepository( repository );
            }
        }
        catch ( RepositoryServiceException e )
        {
            getLogger().error( "Error adding project group" + e.getLocalizedMessage() );
            
            return ERROR;
        }

        try
        {
            getContinuum().addProjectGroup( projectGroup );
        }
        catch ( ContinuumException e )
        {
            getLogger().error( "Error adding project group: " + e.getLocalizedMessage() );

            return ERROR;
        }

        return SUCCESS;
    }

    public String input()
    {
        try
        {
            checkAddProjectGroupAuthorization();

            repositories = getContinuum().getRepositoryService().getAllLocalRepositories();
            
            return INPUT;
        }
        catch ( AuthorizationRequiredException authzE )
        {
            addActionError( authzE.getMessage() );
            return REQUIRES_AUTHORIZATION;
        }
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
    
    public int getRepositoryId()
    {
        return repositoryId;
    }
    
    public void setRepositoryId( int repositoryId )
    {
        this.repositoryId = repositoryId;
    }
    
    public List<LocalRepository> getRepositories()
    {
        return repositories;
    }
    
    public void setRepositories( List<LocalRepository> repositories )
    {
        this.repositories = repositories;
    }
}
