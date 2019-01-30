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
    @NamedQuery(name = "FactorDetail.findByTaxesAndTaxes", query = "SELECT f FROM FactorDetail f WHERE f.taxesAndTaxes = :taxesAndTaxes"),
    @NamedQuery(name = "FactorDetail.findByPriceAfterDiscount", query = "SELECT f FROM FactorDetail f WHERE f.priceAfterDiscount = :priceAfterDiscount"),
    @NamedQuery(name = "FactorDetail.findByPureFactot", query = "SELECT f FROM FactorDetail f WHERE f.pureFactot = :pureFactot"),
    @NamedQuery(name = "FactorDetail.findBySumFactor", query = "SELECT f FROM FactorDetail f WHERE f.sumFactor = :sumFactor"),
    @NamedQuery(name = "FactorDetail.findBySumDiscount", query = "SELECT f FROM FactorDetail f WHERE f.sumDiscount = :sumDiscount"),
    @NamedQuery(name = "FactorDetail.findByPayable", query = "SELECT f FROM FactorDetail f WHERE f.payable = :payable")})
public class FactorDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    private String discount;
    @Size(max = 255)
    @Column(name = "taxes_and_taxes")
    private String taxesAndTaxes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "price_after_discount")
    private String priceAfterDiscount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pure_factot")
    private String pureFactot;
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

    public FactorDetail(Long id) {
        this.id = id;
    }

    public FactorDetail(Long id, int productId, String productName, int countProduct, String unit, String price, String priceAfterDiscount, String pureFactot, String sumFactor, String sumDiscount, String payable) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.countProduct = countProduct;
        this.unit = unit;
        this.price = price;
        this.priceAfterDiscount = priceAfterDiscount;
        this.pureFactot = pureFactot;
        this.sumFactor = sumFactor;
        this.sumDiscount = sumDiscount;
        this.payable = payable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTaxesAndTaxes() {
        return taxesAndTaxes;
    }

    public void setTaxesAndTaxes(String taxesAndTaxes) {
        this.taxesAndTaxes = taxesAndTaxes;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getPureFactot() {
        return pureFactot;
    }

    public void setPureFactot(String pureFactot) {
        this.pureFactot = pureFactot;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactorDetail)) {
            return false;
        }
        FactorDetail other = (FactorDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.darash.salemaven.entities.FactorDetail[ id=" + id + " ]";
    }
    
}
