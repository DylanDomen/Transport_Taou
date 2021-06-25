/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import com.mycompany.transport.taou.designPattern.ConnexionBase;
import com.mycompany.transport.taou.designPattern.Observateur;
import com.mycompany.transport.taou.designPattern.Sujet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dylan
 */
public class ClientDAO extends Sujet {

    ConnexionBase connexionBase;

    public ClientDAO() {

    }

    public void CreerClient(Client client) {
        try {
            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();

            //récupère le dernier id+1 pour ajouter a la suite
            Statement st = connexion.createStatement();
            ResultSet resultat = st.executeQuery("SELECT MAX(id) FROM clients;");
            resultat.next();
            int id = resultat.getInt("max") + 1;

            if (!(client.getNom()).equals("") && !(client.getPrenom()).equals("") && !(client.getMobile()).equals("")) {
                PreparedStatement pst = connexion.prepareStatement("Insert into clients values (?,?,?,?,?)");
                pst.setInt(1, id);
                pst.setString(2, client.getNom());
                pst.setString(3, client.getPrenom());
                pst.setString(4, client.getMobile());
                pst.setString(5, client.getAdresse());
                pst.execute();

                this.notifierObservateur("Creation");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SupprimmerClient(Integer id) {
        try {
            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();
            //requete de suppression
            PreparedStatement pst = connexion.prepareStatement("DELETE FROM clients WHERE id = ?");
            pst.setInt(1, id);
            pst.execute();

            this.notifierObservateur("Suppression");

        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ModifierClient(Integer id, Client client) {
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String mobile = client.getMobile();
        String adresse = client.getAdresse();

        connexionBase = ConnexionBase.recupInstance();
        Connection connexion = connexionBase.getConnexion();
        PreparedStatement pst;
        try {
            pst = connexion.prepareStatement("UPDATE clients SET nom = ?, prenom = ?, mobile = ?, adresse = ? WHERE id = ?");
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, mobile);
            pst.setString(4, adresse);
            pst.setInt(5, id);
            pst.execute();  
            
            this.notifierObservateur("Modification");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
