package org.apache.maven.continuum.configuration;

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

import org.codehaus.plexus.spring.PlexusInSpringTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public class ConfigurationServiceTest
    extends PlexusInSpringTestCase
{
    private Logger log = LoggerFactory.getLogger( getClass() );

    private String confFile = "target/test-classes/conf/continuum.xml";

    @Override
    protected void setUp()
        throws Exception
    {
        File originalConf = new File( getBasedir(), "src/test/resources/conf/continuum.xml" );

        File confUsed = new File( getBasedir(), confFile );
        if ( confUsed.exists() )
        {
            confUsed.delete();
        }
        FileUtils.copyFile( originalConf, confUsed );
        super.setUp();
    }

    public void testLoad()
        throws Exception
    {
        ConfigurationService service = (ConfigurationService) lookup( "configurationService" );
        assertNotNull( service );

        assertNotNull( service.getUrl() );
        assertEquals( "http://test", service.getUrl() );
        log.info( service.getFile( "myBuildOutputDir" ).getAbsolutePath() );
        log.info( service.getBuildOutputDirectory().getAbsolutePath() );
        assertEquals( service.getFile( "myBuildOutputDir" ).getAbsolutePath(),
                      service.getBuildOutputDirectory().getAbsolutePath() );
    }

    public void testConfigurationService()
        throws Exception
    {
        File conf = new File( getBasedir(), confFile );
        if ( conf.exists() )
        {
            conf.delete();
        }

        ConfigurationService service = (ConfigurationService) lookup( "configurationService" );

        assertNotNull( service );

//        service.load();

//        assertEquals( "http://test", service.getUrl() );

//        assertEquals( "build-output-directory", service.getBuildOutputDirectory().getName() );

//        assertEquals( "working-directory", service.getWorkingDirectory().getName() );

        service.setUrl( "http://test/zloug" );
        service.setBuildOutputDirectory( new File( "testBuildOutputDir" ) );

        service.store();

        String contents = FileUtils.fileRead( conf );
        //assertTrue( contents.indexOf( "http://test/zloug" ) > 0 );

        service.reload();

        assertEquals( "http://test/zloug", service.getUrl() );
    }
}
