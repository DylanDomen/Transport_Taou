/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

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

/**
 *
 * @author dylan
 */
public class ClientDAO extends Sujet implements Observateur{

    public ClientDAO() {
        
    }
    
    public void CreerClient(Client client){
        try {
            //création du client
            
            //chargement driver
            Class.forName("org.postgresql.Driver");
            //connexion bdd
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            //récupère le dernier id+1 pour ajouter a la suite
            Statement st=connexion.createStatement();
            ResultSet resultat=st.executeQuery("SELECT MAX(id) FROM clients;");
            resultat.next();
            int id = resultat.getInt("max")+1;
            
            if(!(client.getNom()).equals("")&&!(client.getPrenom()).equals("")&&!(client.getMobile()).equals("")){
                PreparedStatement pst = connexion.prepareStatement("Insert into clients values (?,?,?,?,?)");
                pst.setInt(1,id);
                pst.setString(2,client.getNom());
                pst.setString(3,client.getPrenom());
                pst.setString(4,client.getMobile());
                pst.setString(5,client.getAdresse());
                pst.execute();
                
                this.notifierObservateur();
            } 
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void detecter() {
        System.out.println("TEST OBS");
    }
}
