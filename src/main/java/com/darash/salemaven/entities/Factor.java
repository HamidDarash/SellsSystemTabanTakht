/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Factor.findAll", query = "SELECT f FROM Factor f")
    ,
    @NamedQuery(name = "Factor.findById", query = "SELECT f FROM Factor f WHERE f.id = :id")
    ,
    @NamedQuery(name = "Factor.findByDateFactor", query = "SELECT f FROM Factor f WHERE f.dateFactor = :dateFactor")
    ,
    @NamedQuery(name = "Factor.findByCondinationFactor", query = "SELECT f FROM Factor f WHERE f.condinationFactor = :condinationFactor")
    ,
    @NamedQuery(name = "Factor.findByReturned", query = "SELECT f FROM Factor f WHERE f.returned = :returned")})
public class Factor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
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
    private boolean returned = false;

    @Column(name = "final_registration")
    private boolean finalRegistration = false;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sum_factor")
    private String sumFactor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sum_discount")
    private String sumDiscount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "payable")
    private String payable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(
            mappedBy = "factor",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    @JoinColumn(name = "factor_id")
    private List<FactorDetail> factorDetails = new ArrayList<>();

    public List<FactorDetail> getFactorDetails() {
        return factorDetails;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public String getSumFactor() {
        return sumFactor;
    }

    public void setSumFactor(String sumFactor) {
        this.sumFactor = sumFactor;
    }

    public String getSumDiscount() {
        return sumDiscount;
    }

    public void setSumDiscount(String sumDiscount) {
        this.sumDiscount = sumDiscount;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public boolean isFinalRegistration() {
        return finalRegistration;
    }

    public void setFinalRegistration(boolean finalRegistration) {
        this.finalRegistration = finalRegistration;
    }

    public void setFactorDetails(List<FactorDetail> factorDetails) {
        this.factorDetails = factorDetails;
    }

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

    public Factor() {
    }
 
    public Factor(Date dateFactor, String condinationFactor) {
        this.dateFactor = dateFactor;
        this.condinationFactor = condinationFactor;
    }

    public Long getId() {
        return id;
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

    public boolean getReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
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
