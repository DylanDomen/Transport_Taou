/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

/**
 *
 * @author dylan
 */
public class TypeDemande {

    private Integer id;
    private String typeDemande;
    
    public TypeDemande() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    @Override
    public String toString() {
        return typeDemande;
    }
    
}
