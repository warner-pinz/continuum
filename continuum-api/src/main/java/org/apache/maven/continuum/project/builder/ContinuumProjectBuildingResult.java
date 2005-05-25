package org.apache.maven.continuum.project.builder;

/*
 * Copyright 2004-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.continuum.project.ContinuumProject;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ContinuumProjectBuildingResult
{
    private List projects;

    public ContinuumProjectBuildingResult()
    {
        projects = new ArrayList();
    }

    public void addProject( ContinuumProject project )
    {
        projects.add( project );
    }

    public void addProject( ContinuumProject project, String executorId )
    {
        project.setExecutorId( executorId );

        projects.add( project );
    }

    public List getProjects()
    {
        return projects;
    }
}
