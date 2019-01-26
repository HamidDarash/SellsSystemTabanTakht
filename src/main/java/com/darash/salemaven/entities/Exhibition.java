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
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hamid
 */
@Entity
@Table(name = "exhibition")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exhibition.findAll", query = "SELECT e FROM Exhibition e"),
    @NamedQuery(name = "Exhibition.findById", query = "SELECT e FROM Exhibition e WHERE e.id = :id"),
    @NamedQuery(name = "Exhibition.findByNameExhibition", query = "SELECT e FROM Exhibition e WHERE e.nameExhibition = :nameExhibition"),
    @NamedQuery(name = "Exhibition.findByDateStart", query = "SELECT e FROM Exhibition e WHERE e.dateStart = :dateStart"),
    @NamedQuery(name = "Exhibition.findByDateEnd", query = "SELECT e FROM Exhibition e WHERE e.dateEnd = :dateEnd"),
    @NamedQuery(name = "Exhibition.findByManagerName", query = "SELECT e FROM Exhibition e WHERE e.managerName = :managerName"),
    @NamedQuery(name = "Exhibition.findByActivate", query = "SELECT e FROM Exhibition e WHERE e.activate = :activate")})
public class Exhibition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name_exhibition")
    private String nameExhibition;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_start")
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_end")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @Size(max = 255)
    @Column(name = "manager_name")
    private String managerName;
    @Lob
    @Size(max = 65535)
    @Column(name = "desciption")
    private String desciption;
    @Basic(optional = false)
    @Column(name = "activate")
    private boolean activate = false;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "exhibition_factor", joinColumns = @JoinColumn(name = "exhibition_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "factor_id", referencedColumnName = "id"))
    private List<Factor> factors = new ArrayList<>();

    public List<Factor> getFactors() {
        return factors;
    }

    public void setFactors(List<Factor> factors) {
        this.factors = factors;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "exhibition_provider", joinColumns = @JoinColumn(name = "exhibition_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id", referencedColumnName = "id"))
    private List<Provider> providers = new ArrayList<>();

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public Exhibition() {
    }

    public Exhibition(Integer id) {
        this.id = id;
    }

    public Exhibition(Integer id, String nameExhibition, Date dateStart, Date dateEnd, boolean activate) {
        this.id = id;
        this.nameExhibition = nameExhibition;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.activate = activate;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameExhibition() {
        return nameExhibition;
    }

    public void setNameExhibition(String nameExhibition) {
        this.nameExhibition = nameExhibition;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public boolean getActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Exhibition)) {
            return false;
        }
        Exhibition other = (Exhibition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.Exhibition[ id=" + id + " ]";
    }

}
