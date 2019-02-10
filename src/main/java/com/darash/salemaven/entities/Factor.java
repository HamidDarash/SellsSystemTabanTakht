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
import java.util.Objects;
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
    @NamedQuery(name = "Factor.findAll", query = "SELECT f FROM Factor f"),
    @NamedQuery(name = "Factor.findById", query = "SELECT f FROM Factor f WHERE f.id = :id"),
    @NamedQuery(name = "Factor.findByDateFactor", query = "SELECT f FROM Factor f WHERE f.dateFactor = :dateFactor"),
    @NamedQuery(name = "Factor.findByCondinationFactor", query = "SELECT f FROM Factor f WHERE f.condinationFactor = :condinationFactor"),
    @NamedQuery(name = "Factor.findByReturned", query = "SELECT f FROM Factor f WHERE f.returned = :returned")})
public class Factor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "prepayable")
    private String prepayable = "0";

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "Installment_count")
    private String installmentCount = "10";

    @Column(name = "percentage")
    private double percentage = 0.0;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "installment_value")
    private String installmentValue = "0";

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "sum_installment_value")
    private String sumInstallmentValue = "0";

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "sum_wage")
    private double sumWage = 0.0;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "sum_purge_and_profit_general")
    private String sumPurgeAndProfitGeneral = "0";

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
            orphanRemoval = true
    )
    @JoinColumn(name = "factor_id")
    private List<FactorDetail> factorDetails = new ArrayList<>();

    public String getSumPurgeAndProfitGeneral() {
        return sumPurgeAndProfitGeneral;
    }

    public void setSumPurgeAndProfitGeneral(String sumPurgeAndProfitGeneral) {
        this.sumPurgeAndProfitGeneral = sumPurgeAndProfitGeneral;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSumWageInteger() {
        return (int) getSumWage();
    }

    public double getSumWage() {
        return sumWage;
    }

    public void setSumWage(double sumWage) {
        this.sumWage = sumWage;
    }

    public String getSumInstallmentValue() {
        return sumInstallmentValue;
    }

    public void setSumInstallmentValue(String sumInstallmentValue) {
        this.sumInstallmentValue = sumInstallmentValue;
    }

    public String getPrepayable() {
        return prepayable;
    }

    public void setPrepayable(String prepayable) {
        this.prepayable = prepayable;
    }

    public String getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(String installmentCount) {
        this.installmentCount = installmentCount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(String installmentValue) {
        this.installmentValue = installmentValue;
    }

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
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.dateFactor);
        hash = 23 * hash + Objects.hashCode(this.condinationFactor);
        hash = 23 * hash + (this.returned ? 1 : 0);
        hash = 23 * hash + (this.finalRegistration ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.sumFactor);
        hash = 23 * hash + Objects.hashCode(this.sumDiscount);
        hash = 23 * hash + Objects.hashCode(this.payable);
        hash = 23 * hash + Objects.hashCode(this.prepayable);
        hash = 23 * hash + Objects.hashCode(this.installmentCount);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.percentage) ^ (Double.doubleToLongBits(this.percentage) >>> 32));
        hash = 23 * hash + Objects.hashCode(this.installmentValue);
        hash = 23 * hash + Objects.hashCode(this.sumInstallmentValue);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.sumWage) ^ (Double.doubleToLongBits(this.sumWage) >>> 32));
        hash = 23 * hash + Objects.hashCode(this.exhibition);
        hash = 23 * hash + Objects.hashCode(this.provider);
        hash = 23 * hash + Objects.hashCode(this.person);
        hash = 23 * hash + Objects.hashCode(this.factorDetails);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Factor other = (Factor) obj;
        if (this.returned != other.returned) {
            return false;
        }
        if (this.finalRegistration != other.finalRegistration) {
            return false;
        }
        if (Double.doubleToLongBits(this.percentage) != Double.doubleToLongBits(other.percentage)) {
            return false;
        }
        if (Double.doubleToLongBits(this.sumWage) != Double.doubleToLongBits(other.sumWage)) {
            return false;
        }
        if (!Objects.equals(this.condinationFactor, other.condinationFactor)) {
            return false;
        }
        if (!Objects.equals(this.sumFactor, other.sumFactor)) {
            return false;
        }
        if (!Objects.equals(this.sumDiscount, other.sumDiscount)) {
            return false;
        }
        if (!Objects.equals(this.payable, other.payable)) {
            return false;
        }
        if (!Objects.equals(this.prepayable, other.prepayable)) {
            return false;
        }
        if (!Objects.equals(this.installmentCount, other.installmentCount)) {
            return false;
        }
        if (!Objects.equals(this.installmentValue, other.installmentValue)) {
            return false;
        }
        if (!Objects.equals(this.sumInstallmentValue, other.sumInstallmentValue)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dateFactor, other.dateFactor)) {
            return false;
        }
        if (!Objects.equals(this.exhibition, other.exhibition)) {
            return false;
        }
        if (!Objects.equals(this.provider, other.provider)) {
            return false;
        }
        if (!Objects.equals(this.person, other.person)) {
            return false;
        }
        if (!Objects.equals(this.factorDetails, other.factorDetails)) {
            return false;
        }
        return true;
    }

}
