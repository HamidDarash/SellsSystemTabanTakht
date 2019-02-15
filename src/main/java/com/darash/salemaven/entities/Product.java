/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daresh
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
    ,
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id")
    ,
    @NamedQuery(name = "Product.findByModel", query = "SELECT p FROM Product p WHERE p.model = :model")
    ,
    @NamedQuery(name = "Product.findByColor", query = "SELECT p FROM Product p WHERE p.color = :color")
    ,
    @NamedQuery(name = "Product.findByUnit", query = "SELECT p FROM Product p WHERE p.unit = :unit")
    ,
    @NamedQuery(name = "Product.findByPrice", query = "SELECT p FROM Product p WHERE p.price = :price")
    ,
    @NamedQuery(name = "Product.findByProductNameOrModelOrId",
            query = "SELECT p FROM Product p WHERE (p.productName LIKE :productName or p.model LIKE :model) or p.id = :id")
    ,
    @NamedQuery(name = "Product.findByProductNameOrModelOrIdByProvider",
            query = "SELECT p FROM Product p WHERE ((p.productName LIKE :productName or p.model LIKE :model) or p.id = :id) and p.provider = :provider")
})
public class Product implements Serializable {

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
    @Column(name = "product_name")
    private String productName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "model")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "color")
    private String color;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "unit")
    private String unit;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "price")
    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    public Product() {
    }

    public Product(String productName, String model, String color, String unit, String description, String price, Provider provider) {
        this.productName = productName;
        this.model = model;
        this.color = color;
        this.unit = unit;
        this.description = description;

        this.price = price;
        this.provider = provider;
    }

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }
    
    

    public void setPrice(String price) {
        this.price = price;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.productName + " | " + this.model + " | " + this.color + " | " + this.provider.getShopName();
    }

}
