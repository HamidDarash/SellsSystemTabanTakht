/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daresh
 */
@Entity
@Table(name = "credit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credit.findAll", query = "SELECT c FROM Credit c")
    ,
    @NamedQuery(name = "Credit.findById", query = "SELECT c FROM Credit c WHERE c.id = :id")
    ,
    @NamedQuery(name = "Credit.findByCredit", query = "SELECT c FROM Credit c WHERE c.credit = :credit")
    ,
    @NamedQuery(name = "Credit.findByCreateAt", query = "SELECT c FROM Credit c WHERE c.createAt = :createAt")})
public class Credit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "credit")
    private long credit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createAt")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    public Credit() {
    }

    public Credit(Integer id) {
        this.id = id;
    }

    public Credit(long credit) {
        this.credit = credit;
        this.createAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credit)) {
            return false;
        }
        Credit other = (Credit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.Credit[ id=" + id + " ]";
    }

}
