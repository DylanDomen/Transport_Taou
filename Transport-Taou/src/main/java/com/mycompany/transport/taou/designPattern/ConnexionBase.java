/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou.designPattern;

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
public class ConnexionBase {
    private static ConnexionBase instance;
    Connection connexion;

    private ConnexionBase() {
        
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            
            //connexion avec la base
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnexionBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBase.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static ConnexionBase recupInstance(){
        if(instance == null) {
            instance = new ConnexionBase();
        }
        return instance; 
    }
    
    public ResultSet requeteRecupereTout(String chaineRequete) throws SQLException{
        Statement st= this.connexion.createStatement();
        ResultSet resultat = st.executeQuery(chaineRequete);

        return resultat;
    }

    public Connection getConnexion() {
        return connexion;
    }

    public void setConnexion(Connection connexion) {
        this.connexion = connexion;
    }
    
    
}
