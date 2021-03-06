package org.apache.maven.continuum.core.action;

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

import java.util.Map;

import org.apache.maven.continuum.model.project.Project;
import org.apache.maven.continuum.scm.queue.CheckOutTask;
import org.apache.maven.continuum.store.ContinuumStore;
import org.apache.maven.continuum.utils.WorkingDirectoryService;
import org.codehaus.plexus.taskqueue.TaskQueue;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 * @plexus.component role="org.codehaus.plexus.action.Action"
 * role-hint="add-project-to-checkout-queue"
 */
public class AddProjectToCheckOutQueueAction
    extends AbstractContinuumAction
{
    /**
     * @plexus.requirement
     */
    private WorkingDirectoryService workingDirectoryService;

    /**
     * @plexus.requirement role-hint="check-out-project"
     */
    private TaskQueue checkOutQueue;

    /**
     * @plexus.requirement role-hint="jdo"
     */
    private ContinuumStore store;
    
    @SuppressWarnings("unchecked")
    public void execute( Map context )
        throws Exception
    {
        
        Project project = (Project) getObject( context, KEY_PROJECT, null );
        if (project == null)
        {
            project = store.getProject( getProjectId( context ) );
        }

        CheckOutTask checkOutTask = new CheckOutTask( project.getId(), workingDirectoryService
            .getWorkingDirectory( project ), project.getName(), project.getScmUsername(), project.getScmPassword() );

        checkOutQueue.put( checkOutTask );
    }
}
