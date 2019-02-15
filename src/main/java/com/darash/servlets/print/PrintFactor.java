/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.servlets.print;

import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.services.FactorFacade;
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hamid
 */
@WebServlet(name = "PrintFactor", urlPatterns = {"/printfactor"}, initParams = {
    @WebInitParam(name = "id", value = "")})
public class PrintFactor extends HttpServlet {

    @EJB
    private FactorFacade ejbFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            if (request.getParameter("id").isEmpty()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
                out.println("<title>فاکتور</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>فاکتور شما یافت نشد</h1>");
                out.println("</body>");
                out.println("</html>");
            } else {
                Factor factor = ejbFacade.find(Integer.valueOf(request.getParameter("id")));
                if (factor != null) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html style='direction: rtl;'>");
                    out.println("<head>");
                   
                    out.print("<link type='text/css' rel='stylesheet' href='" + request.getContextPath() + "/resources/css/factor.css' />");
                    out.println("<title>فاکتور</title>");
                    out.println("</head>");
                    out.println("<body class='bodyFactor'>");
                    out.println("<h1>نمایش فاکتور</h1>");
                     out.print("<img src='" + request.getContextPath() + "/resources/img/factor-logo.png'/>");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>فاکتور</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>خطا در جستجوی فاکتور</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PrintFactor</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PrintFactor at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
