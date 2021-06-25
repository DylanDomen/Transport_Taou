/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou.designPattern;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author dylan
 */
public class Sujet {
    
    private Collection<Observateur> collectionObservateur;
    
    public Sujet(){
        collectionObservateur = new ArrayList();
    }
    
    public void ajouterObservateur(Observateur o){
        collectionObservateur.add(o);
    }
    
    public void supprimerObservateur(Observateur o){
        collectionObservateur.remove(o);
    }
    
    public void notifierObservateur(String mode){
        for(Observateur observateur : collectionObservateur){
            observateur.detecter(mode);
        }
    }
    
    public Collection getCollectionObservateur() {
        return collectionObservateur;
    }

    public void setCollectionObservateur(Collection collectionObservateur) {
        this.collectionObservateur = collectionObservateur;
    }
    
}
