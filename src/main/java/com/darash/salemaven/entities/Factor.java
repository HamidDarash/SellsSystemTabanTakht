/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "factor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factor.findAll", query = "SELECT f FROM Factor f"),
    @NamedQuery(name = "Factor.findById", query = "SELECT f FROM Factor f WHERE f.id = :id"),
    @NamedQuery(name = "Factor.findByDateFactor", query = "SELECT f FROM Factor f WHERE f.dateFactor = :dateFactor"),
    @NamedQuery(name = "Factor.findByCondinationFactor", query = "SELECT f FROM Factor f WHERE f.condinationFactor = :condinationFactor"),
    @NamedQuery(name = "Factor.findByReturned", query = "SELECT f FROM Factor f WHERE f.returned = :returned")})
public class Factor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_factor")
    @Temporal(TemporalType.DATE)
    private Date dateFactor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "condination_factor")
    private String condinationFactor;
    @Column(name = "returned")
    private Short returned;
    
    @ManyToMany(mappedBy = "factors")
    private List<Exhibition> exhibitions;

    public List<Exhibition> getExhibitions() {
        return exhibitions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
     
    public void setExhibitions(List<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }
    
    public Factor() {
    }

    public Factor(Long id) {
        this.id = id;
    }

    public Factor(Long id, Date dateFactor, String condinationFactor) {
        this.id = id;
        this.dateFactor = dateFactor;
        this.condinationFactor = condinationFactor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateFactor() {
        return dateFactor;
    }

    public void setDateFactor(Date dateFactor) {
        this.dateFactor = dateFactor;
    }

    public String getCondinationFactor() {
        return condinationFactor;
    }

    public void setCondinationFactor(String condinationFactor) {
        this.condinationFactor = condinationFactor;
    }

    public Short getReturned() {
        return returned;
    }

    public void setReturned(Short returned) {
        this.returned = returned;
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
        if (!(object instanceof Factor)) {
            return false;
        }
        Factor other = (Factor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.Factor[ id=" + id + " ]";
    }
    
}
