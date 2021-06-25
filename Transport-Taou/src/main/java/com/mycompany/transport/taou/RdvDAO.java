/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;


import com.mycompany.transport.taou.designPattern.ConnexionBase;
import com.mycompany.transport.taou.designPattern.Sujet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author dylan
 */
public class RdvDAO extends Sujet {
    
    ConnexionBase connexionBase;

    public RdvDAO() {
        
    }
    
    //en cours de codage !!!!
    public void creerRdv(RendezVous rdv){
        
        try {
            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();
            
            //récupère le dernier id+1 pour ajouter a la suite
            Statement st = connexion.createStatement();
            ResultSet resultat = st.executeQuery("SELECT MAX(id) FROM rendez_vous;");
            resultat.next();
            int id = resultat.getInt("max") + 1;
            
            if (!(rdv.getNom()).equals("") && !(rdv.getType()).equals("")&& !(rdv.getDateDepart()).equals("") && !(rdv.getHeureDepart()).equals("") && !(rdv.getLieuDepart()).equals("")
                    && !(rdv.getLieuDestination()).equals("") && !(rdv.getDateRetour()).equals("") && !(rdv.getHeureRetour()).equals("") && !(rdv.getNbPersonnes()).equals("")
                    && !(rdv.getPrix()).equals("")&& !(rdv.getMoyenPaiment()).equals("")&& !(rdv.getEtat()).equals(""))
            {
                PreparedStatement pst = connexion.prepareStatement("Insert into rendez_vous values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, id);
                pst.setString(2, rdv.getNom());
                pst.setString(3, rdv.getDateDepart());
                pst.setString(4, rdv.getHeureDepart());
                pst.setString(5, rdv.getLieuDepart());
                pst.setString(6, rdv.getLieuDestination());
                pst.setString(7, rdv.getDateRetour());
                pst.setString(8, rdv.getHeureRetour());
                pst.setInt(9, rdv.getNbPersonnes());
                pst.setDouble(10, rdv.getPrix());
                pst.setInt(11, rdv.getMoyenPaiment());
                pst.setBoolean(12, rdv.getEtat());
                pst.setInt(13, rdv.getIdClient());
                pst.setString(14, rdv.getNote());
                pst.execute();

                this.notifierObservateur("Creation RDV");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RdvDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
