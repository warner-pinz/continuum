package org.apache.maven.continuum.web.tool;

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

import org.apache.maven.continuum.Continuum;
import org.apache.maven.continuum.ContinuumException;
import org.apache.maven.continuum.model.project.BuildResult;
import org.apache.maven.continuum.project.ContinuumProject;
import org.apache.maven.continuum.project.ContinuumProjectState;
import org.codehaus.plexus.formica.web.ContentGenerator;
import org.codehaus.plexus.logging.AbstractLogEnabled;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public class ContinuumStateContentGenerator
    extends AbstractLogEnabled
    implements ContentGenerator
{
    /**
     * @plexus.requirement
     */
    private Continuum continuum;

    public String generate( Object item )
    {
        int state = 0;

        if ( item instanceof ContinuumProject )
        {
            ContinuumProject project = (ContinuumProject) item;

            // TODO: can't we just use project.getState()?
            try
            {
                BuildResult build = continuum.getLatestBuildResultForProject( project.getId() );

                if ( build == null )
                {
                    return "New";
                }

                state = build.getState();
            }
            catch ( ContinuumException e )
            {
                getLogger().warn( "Error while getting latest build for project '" + project.getId() + "'.", e );

                return "Unknown";
            }
        }
        else
        {
            state = ( (BuildResult) item ).getState();
        }

        if ( state == ContinuumProjectState.NEW )
        {
            return "New";
        }
        else if ( state == ContinuumProjectState.OK )
        {
            return "<img src=\"/continuum/images/icon_success_sml.gif\" alt=\"Success\"/>";
        }
        else if ( state == ContinuumProjectState.FAILED )
        {
            return "<img src=\"/continuum/images/icon_error_sml.gif\" alt=\"Failed\"/>";
        }
        else if ( state == ContinuumProjectState.ERROR )
        {
            return "<img src=\"/continuum/images/icon_warning_sml.gif\" alt=\"Error\"/>";
        }
        else if ( state == ContinuumProjectState.BUILDING )
        {
            return "Building";
        }
        else if ( state == ContinuumProjectState.UPDATING )
        {
            return "Updating";
        }
        else if ( state == ContinuumProjectState.CHECKING_OUT )
        {
            return "Checking Out";
        }
        else
        {
            getLogger().warn( "Unknown state '" + state + "'." );

            return "Unknown";
        }
    }
}
