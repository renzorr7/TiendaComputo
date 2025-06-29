package com.tiendacomputo.controller;

import java.io.OutputStream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

	 @Autowired
	   private DataSource dataSource; 

	   @Autowired
	   private ResourceLoader resourceLoader; 

	   
	   @GetMapping("/index")
	    public String inicio() {
	        return "index";
	    }
	   
	   
	   @GetMapping("/stock")
	   public void stocks(HttpServletResponse response) {
	       response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
	       response.setHeader("Content-Disposition", "inline;");
	       
	       response.setContentType("application/pdf");
	       try {
	           String ru = resourceLoader.getResource("classpath:static/stock.jasper").getURI().getPath();
	           JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
	           OutputStream outStream = response.getOutputStream();
	           JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	   }

	   @GetMapping("/mercancia")
	   public void mercancias(HttpServletResponse response) {
	       response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
	       response.setHeader("Content-Disposition", "inline;");
	       
	       response.setContentType("application/pdf");
	       try {
	           String ru = resourceLoader.getResource("classpath:static/recepcion_mercancia.jasper").getURI().getPath();
	           JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
	           OutputStream outStream = response.getOutputStream();
	           JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	   }
}
