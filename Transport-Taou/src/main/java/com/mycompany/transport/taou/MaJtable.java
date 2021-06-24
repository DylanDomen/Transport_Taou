/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import com.mycompany.transport.taou.designPattern.Observateur;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author dylan
 */
public class MaJtable extends javax.swing.JTable implements Observateur{

    Client client;

    public MaJtable() {
        client = new Client();
    }
    MaJtable(Client client) {
        this.client = client;
    } 
    
    
    @Override
    public void detecter(String mode) {
        
        fenetrePrincipal fenPrincipal = new fenetrePrincipal();
        
        switch(mode) {
            case "Creation":
                JOptionPane.showMessageDialog(fenPrincipal, "Client Creer avec succès");
        }
        fenPrincipal.videTableauClients();
        fenPrincipal.afficheTableauClients();
    }

    

    
    
}
