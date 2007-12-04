/**
 * 
 */
package org.apache.maven.continuum.store.api;

import java.util.Date;

import org.apache.maven.continuum.model.project.Project;

/**
 * Wraps up retrieval criteria for {@link Project}s.
 * 
 * @author <a href='mailto:rinku@apache.org'>Rahul Thakur</a>
 * @version $Id$
 * @since 1.2
 */
public class ProjectQuery implements Query<Project>
{

    /**
     * Project artifactId criteria.
     */
    private String artifactId;

    /**
     * Project groupId criteria.
     */
    private String groupId;

    /**
     * Project id criteria.
     */
    private Long id;

    /**
     * Project name criteria.
     */
    private String name;

    /**
     * Project Artifact version criteria.
     */
    private String version;

    private int buildNumber;

    private Date dateCreated;

    private Date dateUpdated;

    private String description;

    private String modelEncoding;

    /**
     * @return the artifactId
     */
    public String getArtifactId()
    {
        return this.artifactId;
    }

    /**
     * @return build number as query critera
     */
    public int getBuildNumber()
    {
        return this.buildNumber;
    }

    /**
     * Determines if a build number criteria was specified in the Project query.
     * 
     * @return
     */
    public boolean hasBuildNumber()
    {
        return ( this.buildNumber > 0 );
    }

    /**
     * @return creation date as query criteria
     */
    public Date getDateCreated()
    {
        return this.dateCreated;
    }

    /**
     * Determines if there was a creation date criteria specified in the ProjectQuery.
     * 
     * @return <code>true</code> if a creation date was specified, else <code>false</code>.
     */
    public boolean hasDateCreated()
    {
        return ( null != this.dateCreated );
    }

    /**
     * @return last update date from query criteria.
     */
    public Date getDateUpdated()
    {
        return this.dateUpdated;
    }

    /**
     * Determine if there was a last update date criteria specified in the ProjectQuery.
     * 
     * @return <code>true</code> if there was a last update date criteria specified, else <code>false</code>.
     */
    public boolean hasDateUpdated()
    {
        return ( null != this.dateUpdated );
    }

    /**
     * @return description criteria from the query.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Determines if there was description criteria specified in the ProjectQuery.
     * 
     * @return <code>true</code> if there was description criteria specified, else <code>false</code>.
     */
    public boolean hasDescription()
    {
        return ( null != this.description && this.description.length() > 0 );
    }

    /**
     * @return the groupId
     */
    public String getGroupId()
    {
        return this.groupId;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return model encoding criteria from the query.
     */
    public String getModelEncoding()
    {
        return this.modelEncoding;
    }

    /**
     * Determine if there is a model encoding specified in the ProjectQuery.
     * 
     * @return <code>true</code> if there is a model encoding specified, else <code>false</code>.
     */
    public boolean hasModelEncoding()
    {
        return ( null != this.modelEncoding && this.modelEncoding.length() > 0 );
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Determines if an artifact Id criteria was specified.
     * 
     * @return
     */
    public boolean hasArtifactId()
    {
        return ( null != this.artifactId );
    }

    /**
     * Determines if a ProjectGroup Id criteria was specified.
     * 
     * @return
     */
    public boolean hasGroupId()
    {
        return ( null != this.groupId );
    }

    /**
     * Determines if a Project id criteria was specified in the query.
     * 
     * @return
     */
    public boolean hasId()
    {
        return ( null != this.id && this.id >= 0 );
    }

    /**
     * Determines if a project name criteria was specified.
     * 
     * @return
     */
    public boolean hasName()
    {
        return ( null != this.name );
    }

    /**
     * Determines if a Version criteria was specified.
     * 
     * @return
     */
    public boolean hasVersion()
    {
        return ( null != this.version );
    }

    /**
     * @param artifactId
     *            the artifactId to set
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    }

    /**
     * @param buildNumber
     *            criteria to set in the ProjectQuery
     */
    public void setBuildNumber( int buildNumber )
    {
        this.buildNumber = buildNumber;
    }

    /**
     * @param created
     *            criteria to set in the Project Query.
     */
    public void setDateCreated( Date dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    /**
     * @param updated
     *            date criteria to set in the Project Query.
     */
    public void setDateUpdated( Date dateUpdated )
    {
        this.dateUpdated = dateUpdated;
    }

    /**
     * @param description
     */
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId( Long id )
    {
        this.id = id;
    }

    /**
     * @param modelEncoding
     * @see org.apache.maven.continuum.model.CommonUpdatableModelEntity#setModelEncoding(java.lang.String)
     */
    public void setModelEncoding( String modelEncoding )
    {
        this.modelEncoding = modelEncoding;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion( String version )
    {
        this.version = version;
    }

}