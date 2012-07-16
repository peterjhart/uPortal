package org.jasig.portal.version.dao.jpa;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.jasig.portal.version.AbstractVersion;

@Entity
@Table(name = "UP_VERSION")
@SequenceGenerator(
        name="UP_VERSION_GEN",
        sequenceName="UP_VERSION_SEQ",
        allocationSize=1
    )
@TableGenerator(
        name="UP_VERSION_GEN",
        pkColumnValue="UP_VERSION",
        allocationSize=1
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class VersionImpl extends AbstractVersion {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UP_VERSION_GEN")
    @Column(name = "VERSION_ID")
    private final long id;
    
    @javax.persistence.Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
    
    @NaturalId
    @Column(name = "PRODUCT", length = 128, nullable = false)
    @Type(type = "fname")
    private final String product;
    
    @Column(name = "MAJOR_VER", nullable = false)
    private int major;

    @Column(name = "MINOR_VER", nullable = false)
    private int minor;
    
    @Column(name = "PATCH_VER", nullable = false)
    private int patch;
    
    @SuppressWarnings("unused")
    private VersionImpl() {
        this.id = -1;
        this.entityVersion = -1;
        this.product = null;
    }
    
    public VersionImpl(String product, int major, int minor, int patch) {
        this.id = -1;
        this.entityVersion = -1;
        this.product = product;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public int getMajor() {
        return this.major;
    }

    @Override
    public int getMinor() {
        return this.minor;
    }

    @Override
    public int getPatch() {
        return this.patch;
    }
    
    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }
}
