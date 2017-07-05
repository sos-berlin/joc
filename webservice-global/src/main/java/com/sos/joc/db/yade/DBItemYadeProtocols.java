package com.sos.joc.db.yade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sos.hibernate.classes.DbItem;
import com.sos.jitl.reporting.db.DBLayer;


@Entity
@Table(name = "YADE_PROTOCOLS")
@SequenceGenerator(name = DBLayer.TABLE_YADE_PROTOCOLS_SEQUENCE, sequenceName = DBLayer.TABLE_YADE_PROTOCOLS_SEQUENCE, allocationSize = 1)
public class DBItemYadeProtocols extends DbItem implements Serializable {

    private static final long serialVersionUID = 1L;
    /** Primary key */
    private Long id;
    /** Others */
    private String hostname;
    private Integer port;
    private Integer protocol;
    private String account;
    
    public DBItemYadeProtocols() {
    }
    
    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_YADE_PROTOCOLS_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public Long getId() {
        return id;
    }
    
    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_YADE_PROTOCOLS_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "`HOSTNAME`", nullable = false)
    public String getHostname() {
        return hostname;
    }
    
    @Column(name = "`HOSTNAME`", nullable = false)
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    @Column(name = "`PORT`", nullable = true)
    public Integer getPort() {
        return port;
    }
    
    @Column(name = "`PORT`", nullable = true)
    public void setPort(Integer port) {
        this.port = port;
    }
    
    @Column(name = "`PROTOCOL`", nullable = false)
    public Integer getProtocol() {
        return protocol;
    }
    
    @Column(name = "`PROTOCOL`", nullable = false)
    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }
    
    @Column(name = "`ACCOUNT`", nullable = true)
    public String getAccount() {
        return account;
    }
    
    @Column(name = "`ACCOUNT`", nullable = true)
    public void setAccount(String account) {
        this.account = account;
    }

}
