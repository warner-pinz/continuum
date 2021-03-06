package org.apache.maven.continuum.notification.mail;

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

import org.apache.maven.continuum.AbstractContinuumTest;
import org.apache.maven.continuum.model.project.BuildResult;
import org.apache.maven.continuum.model.project.Project;
import org.apache.maven.continuum.model.project.ProjectNotifier;
import org.apache.maven.continuum.notification.ContinuumNotificationDispatcher;
import org.apache.maven.continuum.notification.MessageContext;
import org.apache.maven.continuum.notification.Notifier;
import org.apache.maven.continuum.project.ContinuumProjectState;
import org.codehaus.plexus.mailsender.MailMessage;
import org.codehaus.plexus.mailsender.MailSender;
import org.codehaus.plexus.mailsender.test.MockMailSender;
import org.codehaus.plexus.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class MailContinuumNotifierTest
    extends AbstractContinuumTest
{
    public void testSuccessfulBuild()
        throws Exception
    {
        MailContinuumNotifier notifier = (MailContinuumNotifier) lookup( Notifier.class.getName(), "mail" );
        notifier.setToOverride( "recipient@host.com" );

        Project project = makeStubProject( "Test Project" );
        project.setGroupId( "foo.bar" );

        BuildResult build = makeBuild( ContinuumProjectState.OK );

        MailMessage mailMessage = sendNotificationAndGetMessage( project, build, "lots out build output" );

        assertEquals( "[continuum] BUILD SUCCESSFUL: foo.bar Test Project", mailMessage.getSubject() );

        dumpContent( mailMessage, "recipient@host.com" );
    }

    public void testFailedBuild()
        throws Exception
    {
        Project project = makeStubProject( "Test Project" );
        project.setGroupId( "foo.bar" );

        BuildResult build = makeBuild( ContinuumProjectState.FAILED );

        MailMessage mailMessage = sendNotificationAndGetMessage( project, build, "output" );

        assertEquals( "[continuum] BUILD FAILURE: foo.bar Test Project", mailMessage.getSubject() );

        dumpContent( mailMessage );
    }

    public void testErrorenousBuild()
        throws Exception
    {
        Project project = makeStubProject( "Test Project" );
        project.setGroupId( "foo.bar" );

        BuildResult build = makeBuild( ContinuumProjectState.ERROR );

        build.setError( "Big long error message" );

        MailMessage mailMessage = sendNotificationAndGetMessage( project, build, "lots of stack traces" );

        assertEquals( "[continuum] BUILD ERROR: foo.bar Test Project", mailMessage.getSubject() );

        dumpContent( mailMessage );
    }

    private void dumpContent( MailMessage mailMessage )
        throws Exception
    {
        dumpContent( mailMessage, null );
    }

    private void dumpContent( MailMessage mailMessage, String toOverride )
        throws Exception
    {
        if ( toOverride != null )
        {
            assertEquals( toOverride, ( (MailMessage.Address) mailMessage.getToAddresses().get( 0 ) ).getMailbox() );
        }
        else
        {
            assertEquals( "foo@bar", ( (MailMessage.Address) mailMessage.getToAddresses().get( 0 ) ).getMailbox() );
        }
        assertTrue( "The template isn't loaded correctly.",
                    mailMessage.getContent().indexOf( "#shellBuildResult()" ) < 0 );
        assertTrue( "The template isn't loaded correctly.",
                    mailMessage.getContent().indexOf( "Build statistics" ) > 0 );

        if ( true )
        {
            System.err.println( mailMessage.getContent() );
        }
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    private MailMessage sendNotificationAndGetMessage( Project project, BuildResult build, String buildOutput )
        throws Exception
    {
        MessageContext context = new MessageContext();

        context.setProject( project );

        context.setBuildResult( build );

        ProjectNotifier projectNotifier = new ProjectNotifier();
        projectNotifier.setType( "mail" );
        Map<String, String> config = new HashMap<String, String>();
        config.put( MailContinuumNotifier.ADDRESS_FIELD, "foo@bar" );
        projectNotifier.setConfiguration( config );
        List<ProjectNotifier> projectNotifiers = new ArrayList<ProjectNotifier>();
        projectNotifiers.add( projectNotifier );
        context.setNotifier( projectNotifiers );

        //context.put( ContinuumNotificationDispatcher.CONTEXT_BUILD_OUTPUT, buildOutput );

        //context.put( "buildHost", "foo.bar.com" );

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        Notifier notifier = (Notifier) lookup( Notifier.class.getName(), "mail" );

        ( (MailContinuumNotifier) notifier ).setBuildHost( "foo.bar.com" );

        notifier.sendMessage( ContinuumNotificationDispatcher.MESSAGE_ID_BUILD_COMPLETE, context );

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        MockMailSender mailSender = (MockMailSender) lookup( MailSender.ROLE );

        assertEquals( 1, mailSender.getReceivedEmailSize() );

        List mails = CollectionUtils.iteratorToList( mailSender.getReceivedEmail() );

        MailMessage mailMessage = (MailMessage) mails.get( 0 );

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        assertEquals( "continuum@localhost", mailMessage.getFrom().getMailbox() );

        assertEquals( "Continuum", mailMessage.getFrom().getName() );

        List to = mailMessage.getToAddresses();

        assertEquals( 1, to.size() );

        //assertEquals( "foo@bar", ( (MailMessage.Address) to.get( 0 ) ).getMailbox() );

        assertNull( ( (MailMessage.Address) to.get( 0 ) ).getName() );

        return mailMessage;
    }

    private BuildResult makeBuild( int state )
    {
        BuildResult build = new BuildResult();

        build.setId( 17 );

        build.setStartTime( System.currentTimeMillis() );

        build.setEndTime( System.currentTimeMillis() + 1234567 );

        build.setState( state );

        build.setTrigger( ContinuumProjectState.TRIGGER_FORCED );

        build.setExitCode( 10 );

        return build;
    }
}
