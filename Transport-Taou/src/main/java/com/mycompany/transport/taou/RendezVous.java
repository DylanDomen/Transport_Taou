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
    private Short dateDepart;
    private Short heureDepart;
    private String lieuDepart;
    private String lieuDestination;
    private Short dateRetour;
    private Short heureRetour;
    private Text note;
    private Integer nbPersonnes;
    private BigDecimal prix;
    private Integer moyenPaiment;
    private Boolean etat;

    public RendezVous() {
    }

    public RendezVous(String nom, Integer type, Short dateDepart, Short heureDepart, String lieuDepart, String lieuDestination, Short dateRetour, Short heureRetour, Text note, Integer nbPersonnes, BigDecimal prix, Integer moyenPaiment, Boolean etat) {
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

    public Short getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Short dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Short getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Short heureDepart) {
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

    public Short getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Short dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Short getHeureRetour() {
        return heureRetour;
    }

    public void setHeureRetour(Short heureRetour) {
        this.heureRetour = heureRetour;
    }

    public Text getNote() {
        return note;
    }

    public void setNote(Text note) {
        this.note = note;
    }

    public Integer getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(Integer nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
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
