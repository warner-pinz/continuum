package org.apache.maven.continuum.project.builder.maven;

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

import java.io.File;
import java.net.URL;

import org.apache.maven.continuum.execution.maven.m1.MavenOneBuildExecutor;
import org.apache.maven.continuum.execution.maven.m1.MavenOneMetadataHelper;
import org.apache.maven.continuum.execution.maven.m1.MavenOneMetadataHelperException;
import org.apache.maven.continuum.project.MavenOneProject;
import org.apache.maven.continuum.project.builder.AbstractContinuumProjectBuilder;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuilder;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuilderException;
import org.apache.maven.continuum.project.builder.ContinuumProjectBuildingResult;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class MavenOneContinuumProjectBuilder
    extends AbstractContinuumProjectBuilder
    implements ContinuumProjectBuilder
{
    public static final String ID = "maven-one-builder";

    /** @requirement */
    private MavenOneMetadataHelper metadataHelper;

    // ----------------------------------------------------------------------
    // ProjectCreator Implementation
    // ----------------------------------------------------------------------

    public ContinuumProjectBuildingResult createProjectsFromMetadata( URL url )
        throws ContinuumProjectBuilderException
    {
        ContinuumProjectBuildingResult result = new ContinuumProjectBuildingResult();

        File pomFile = createMetadataFile( url );

        MavenOneProject project = new MavenOneProject();

        try
        {
            metadataHelper.mapMetadata( pomFile, project );
        }
        catch ( MavenOneMetadataHelperException e )
        {
            throw new ContinuumProjectBuilderException( "Cannot create continuum project.", e );
        }

        result.addProject( project, MavenOneBuildExecutor.ID );

        return result;
    }
}
