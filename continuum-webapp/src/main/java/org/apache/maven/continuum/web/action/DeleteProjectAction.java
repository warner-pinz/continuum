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

import org.apache.maven.continuum.ContinuumException;
import org.apache.maven.continuum.model.project.Project;
import org.apache.maven.continuum.web.exception.AuthorizationRequiredException;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 * @plexus.component role="com.opensymphony.xwork.Action" role-hint="deleteProject"
 */
public class DeleteProjectAction
    extends ContinuumActionSupport
{
    private int projectId;

    private String projectName;

    private int projectGroupId;

    private String projectGroupName = "";

    public String execute()
        throws ContinuumException
    {
        try
        {
            checkRemoveProjectFromGroupAuthorization( getProjectGroupName() );
        }
        catch ( AuthorizationRequiredException e )
        {
            return REQUIRES_AUTHORIZATION;
        }

        getContinuum().removeProject( projectId );

        return SUCCESS;
    }

    public String doDefault()
        throws ContinuumException
    {
        try
        {
            checkRemoveProjectFromGroupAuthorization( getProjectGroupName() );
        }
        catch ( AuthorizationRequiredException e )
        {
            return REQUIRES_AUTHORIZATION;
        }

        Project project = getContinuum().getProject( projectId );
        projectName = project.getName();

        return "delete";
    }

    public void setProjectId( int projectId )
    {
        this.projectId = projectId;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectName( String projectName )
    {
        this.projectName = projectName;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectGroupId( int projectGroupId )
    {
        this.projectGroupId = projectGroupId;
    }

    public int getProjectGroupId()
    {
        return projectGroupId;
    }

    public String getProjectGroupName()
        throws ContinuumException
    {
        if ( projectGroupName == null || "".equals( projectGroupName ) )
        {
            if ( projectGroupId != 0 )
            {
                projectGroupName = getContinuum().getProjectGroup( projectGroupId ).getName();
            }
            else
            {
                projectGroupName = getContinuum().getProjectGroupByProjectId( projectId ).getName();
            }
        }

        return projectGroupName;
    }
}
