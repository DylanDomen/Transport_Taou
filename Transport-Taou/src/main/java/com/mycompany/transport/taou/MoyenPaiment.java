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
public class MoyenPaiment {

    private Integer id;
    private String moyenPaiment;
    
    public MoyenPaiment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoyenPaiment() {
        return moyenPaiment;
    }

    public void setMoyenPaiment(String moyenPaiment) {
        this.moyenPaiment = moyenPaiment;
    }

    @Override
    public String toString() {
        return moyenPaiment;
    }
    
    
    
}
