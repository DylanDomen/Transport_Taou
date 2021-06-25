/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import com.mycompany.transport.taou.designPattern.Sujet;

/**
 *
 * @author dylan
 */
public class Client{
    private Integer id;
    private String nom;
    private String prenom;
    private String mobile;
    private String adresse;
    
    public Client(Integer id, String nom, String prenom, String mobile, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mobile = mobile;
        this.adresse = adresse;
    }
    public Client() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        //this.notifierObservateur();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
        
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
        
    }

    @Override
    public String toString() {
        return nom + "," + prenom + "," + mobile + "," + adresse;
    }
    
}
