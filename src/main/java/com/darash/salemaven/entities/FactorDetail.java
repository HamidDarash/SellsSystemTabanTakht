/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.entities;

import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "factor_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactorDetail.findAll", query = "SELECT f FROM FactorDetail f"),
    @NamedQuery(name = "FactorDetail.findById", query = "SELECT f FROM FactorDetail f WHERE f.id = :id"),
    @NamedQuery(name = "FactorDetail.findByProductId", query = "SELECT f FROM FactorDetail f WHERE f.productId = :productId"),
    @NamedQuery(name = "FactorDetail.findByCountProduct", query = "SELECT f FROM FactorDetail f WHERE f.countProduct = :countProduct"),
    @NamedQuery(name = "FactorDetail.findByUnit", query = "SELECT f FROM FactorDetail f WHERE f.unit = :unit"),
    @NamedQuery(name = "FactorDetail.findByPrice", query = "SELECT f FROM FactorDetail f WHERE f.price = :price"),
    @NamedQuery(name = "FactorDetail.findByDiscount", query = "SELECT f FROM FactorDetail f WHERE f.discount = :discount"),
    @NamedQuery(name = "FactorDetail.findByPriceAfterDiscount", query = "SELECT f FROM FactorDetail f WHERE f.priceAfterDiscount = :priceAfterDiscount")
})
public class FactorDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "product_name")
    private String productName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "count_product")
    private int countProduct;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "unit")
    private String unit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "price")
    private String price;
    @Size(max = 255)
    @Column(name = "discount")
    private String discount = "0";

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "price_after_discount")
    private String priceAfterDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factor_id")
    private Factor factor;
 
    public Factor getFactor() {
        return factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public FactorDetail() {
    }

    public FactorDetail(int productId, String productName, int countProduct, String unit, String price, String discount, String priceAfterDiscount) {
        this.productId = productId;
        this.productName = productName;
        this.countProduct = countProduct;
        this.unit = unit;
        this.price = price;
        this.discount = discount;
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 83 * hash + this.productId;
        hash = 83 * hash + Objects.hashCode(this.productName);
        hash = 83 * hash + this.countProduct;
        hash = 83 * hash + Objects.hashCode(this.unit);
        hash = 83 * hash + Objects.hashCode(this.price);
        hash = 83 * hash + Objects.hashCode(this.discount);
        hash = 83 * hash + Objects.hashCode(this.priceAfterDiscount);
        hash = 83 * hash + Objects.hashCode(this.factor);
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
        final FactorDetail other = (FactorDetail) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        if (this.countProduct != other.countProduct) {
            return false;
        }
     
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.discount, other.discount)) {
            return false;
        }
        if (!Objects.equals(this.priceAfterDiscount, other.priceAfterDiscount)) {
            return false;
        }
        if (!Objects.equals(this.factor, other.factor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.FactorDetail[ id=" + id + " ]";
    }

}
