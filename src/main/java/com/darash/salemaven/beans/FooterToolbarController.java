/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "footerController")
@SessionScoped
public class FooterToolbarController implements Serializable {
    
    private boolean visibleLog = false;
    private String textBtnConsole;
    
    public String getTextBtnConsole() {
        if (visibleLog) {
            setTextBtnConsole("مخفی کردن کنسول");
        } else {
            setTextBtnConsole("نمایش کنسول");
        }
        return textBtnConsole;
    }
    
    public void setTextBtnConsole(String textBtnConsole) {
        this.textBtnConsole = textBtnConsole;
    }
    
    public boolean isVisibleLog() {
        return visibleLog;
    }
    
    public void setVisibleLog(boolean visibleLog) {
        this.visibleLog = visibleLog;
    }
    
    public boolean SwichVisibleLogConsole() {
        this.setVisibleLog(!visibleLog);
        return visibleLog;
    }
    
    public FooterToolbarController() {
    }
    
}
