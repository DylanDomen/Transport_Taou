/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import java.math.BigDecimal;
import org.w3c.dom.Text;

/**
 *
 * @author dylan
 */
public class RendezVous {
    private String nom;
    private Integer type;
    private String dateDepart;
    private String heureDepart;
    private String lieuDepart;
    private String lieuDestination;
    private String dateRetour;
    private String heureRetour;
    private String note;
    private Integer nbPersonnes;
    private Double prix;
    private Integer moyenPaiment;
    private Boolean etat;

    public RendezVous() {
    }

    public RendezVous(String nom, Integer type, String dateDepart, String heureDepart, String lieuDepart, String lieuDestination, String dateRetour, String heureRetour, String note, Integer nbPersonnes, Double prix, Integer moyenPaiment, Boolean etat) {
        this.nom = nom;
        this.type = type;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.lieuDepart = lieuDepart;
        this.lieuDestination = lieuDestination;
        this.dateRetour = dateRetour;
        this.heureRetour = heureRetour;
        this.note = note;
        this.nbPersonnes = nbPersonnes;
        this.prix = prix;
        this.moyenPaiment = moyenPaiment;
        this.etat = etat;
    }

    @Override
    public String toString() {
        return nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuDestination() {
        return lieuDestination;
    }

    public void setLieuDestination(String lieuDestination) {
        this.lieuDestination = lieuDestination;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getHeureRetour() {
        return heureRetour;
    }

    public void setHeureRetour(String heureRetour) {
        this.heureRetour = heureRetour;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(Integer nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Integer getMoyenPaiment() {
        return moyenPaiment;
    }

    public void setMoyenPaiment(Integer moyenPaiment) {
        this.moyenPaiment = moyenPaiment;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    
    
    
}
