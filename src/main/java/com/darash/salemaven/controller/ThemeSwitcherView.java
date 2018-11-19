package com.darash.salemaven.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import com.darash.salemaven.entities.Theme;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "themeSwitcherView")
@SessionScoped
public class ThemeSwitcherView {

    private List<Theme> themes;

    @ManagedProperty("#{themeService}")
    private ThemeService service;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setService(ThemeService service) {
        this.service = service;
    }

    public void handleThemeName() {
//        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        System.out.println(params.containsKey("theme"));
//        FacesMessage msg = new FacesMessage("عملیات موفق ", "استایل بدرستی تغییر کرد" );
//        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

}
