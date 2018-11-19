/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.controller;

import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "currentTheme")
@SessionScoped
public class CurrentTheme implements Serializable {

    private static final long serialVersionUID = 1L;
    private String theme = "bootstrap";

    public String getTheme() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("theme")) {
            theme = params.get("theme");
        }
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}
