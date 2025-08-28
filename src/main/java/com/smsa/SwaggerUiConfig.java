///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.smsa;
//
///**
// *
// * @author abcom
// */
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//public class SwaggerUiConfig implements WebMvcConfigurer {
//
//    @Value("${server.servlet.context-path:}")
//    private String contextPath;
//
//    @Value("${springdoc.swagger-ui.url:}")
//    private String swaggerUrl;
//
//    @PostConstruct
//    public void init() {
//        if (swaggerUrl == null || swaggerUrl.isEmpty()) {
//            System.setProperty("springdoc.swagger-ui.url", contextPath + "/v3/api-docs");
//        }
//    }
//}
//
