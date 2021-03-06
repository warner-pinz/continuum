package org.apache.maven.continuum.scm.queue;

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

import org.codehaus.plexus.taskqueue.Task;

import java.io.File;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CheckOutTask
    implements Task
{
    private int projectId;

    private File workingDirectory;
    
    private String projectName;
    
    private String scmUserName;

    private String scmPassword;

    public CheckOutTask( int projectId, File workingDirectory, String projectName, String scmUserName, String scmPassword )
    {
        this.projectId = projectId;

        this.workingDirectory = workingDirectory;
        
        this.projectName = projectName;
        
        this.scmUserName = scmUserName;
        
        this.scmPassword = scmPassword;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public File getWorkingDirectory()
    {
        return workingDirectory;
    }

    public long getMaxExecutionTime()
    {
        return 0;
    }

    public String getProjectName()
    {
        return projectName;
    }
    
    
    public String getScmUserName()
    {
        return scmUserName;
    }

    public String getScmPassword()
    {
        return scmPassword;
    }
    
    
    public int getHashCode()
    {
        return this.hashCode();
    }    
}
