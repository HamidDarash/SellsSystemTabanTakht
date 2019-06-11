/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daresh
 */
@Entity
@Table(name = "provider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p"),
    @NamedQuery(name = "Provider.findById", query = "SELECT p FROM Provider p WHERE p.id = :id"),
    @NamedQuery(name = "Provider.findByInternationalCode", query = "SELECT p FROM Provider p WHERE p.internationalCode = :internationalCode"),
    @NamedQuery(name = "Provider.findByCodeEghtesadi", query = "SELECT p FROM Provider p WHERE p.codeEghtesadi = :codeEghtesadi"),
    @NamedQuery(name = "Provider.findByEmail", query = "SELECT p FROM Provider p WHERE p.email = :email"),
    @NamedQuery(name = "Provider.findByMobile", query = "SELECT p FROM Provider p WHERE p.mobile = :mobile"),
    @NamedQuery(name = "Provider.findByCode", query = "SELECT p FROM Provider p WHERE p.code = :code"),
    @NamedQuery(name = "Provider.findByWage", query = "SELECT p FROM Provider p WHERE p.wage = :wage"),
    @NamedQuery(name = "Provider.findByNameOrCodeOrFullnameAndActiveAndExhibition", query = "SELECT p FROM Provider p WHERE (p.code LIKE :code or p.shopName LIKE :shopName or p.fullname LIKE :fullname or p.internationalCode LIKE :internationalCode and p.activate = true) AND :exhibition MEMBER OF p.exhibitions"),
    @NamedQuery(name = "Provider.findByNameOrCodeOrFullname", query = "SELECT p FROM Provider p WHERE p.code LIKE :code or p.shopName LIKE :shopName or p.fullname LIKE :fullname or p.internationalCode LIKE :internationalCode and p.activate = true")

})
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "fullname")
    private String fullname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "international_code")
    private String internationalCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code_eghtesadi")
    private String codeEghtesadi;
    @Lob
    @Size(max = 65535)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 11)
    @Column(name = "mobile")
    private String mobile;
    @Lob
    @Size(max = 65535)
    @Column(name = "shop_name")
    private String shopName;
    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Basic(optional = false)
    @Column(name = "activate")
    private boolean activate = false;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "wage")
    private double wage = 0.0;

    @ManyToMany(mappedBy = "providers")
    private List<Exhibition> exhibitions;

    public List<Exhibition> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }

    //relational by factor
    @OneToMany(
            mappedBy = "provider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "provider_id")
    private List<Factor> factors = new ArrayList<>();

    public List<Factor> getFactors() {
        return factors;
    }

    public void setFactors(List<Factor> factors) {
        this.factors = factors;
    }

    //relational by product
    @OneToMany(
            mappedBy = "provider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "provider_id")
    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Provider() {
    }

    public Provider(String fullname, String internationalCode, String codeEghtesadi, String address, String email, String mobile, double wage, String shopName, String code, boolean activate) {
        this.fullname = fullname;
        this.internationalCode = internationalCode;
        this.codeEghtesadi = codeEghtesadi;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.shopName = shopName;
        this.code = code;
        this.wage = wage;
        this.activate = false;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public Integer getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getInternationalCode() {
        return internationalCode;
    }

    public void setInternationalCode(String internationalCode) {
        this.internationalCode = internationalCode;
    }

    public String getCodeEghtesadi() {
        return codeEghtesadi;
    }

    public void setCodeEghtesadi(String codeEghtesadi) {
        this.codeEghtesadi = codeEghtesadi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof Provider)) {
            return false;
        }
        Provider other = (Provider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.Provider[ id=" + id + " ]";
    }

}
