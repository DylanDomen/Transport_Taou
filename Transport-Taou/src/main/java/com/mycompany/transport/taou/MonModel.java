/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dylan
 */
public class MonModel extends AbstractTableModel{

    private ArrayList<Client> clients;

    public MonModel() {
        clients = new ArrayList<Client>();
    }
    
    public void ajoutClient(Client client){
        clients.add(client);
        this.fireTableDataChanged();
        this.fireTableChanged(null);
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0:
                return "Nom";
            case 1:
                return "Prenom";
            case 2:
                return "Mobile";
            case 3:
                return "Adresse";
            case 4:
                return "Id";

        }
        return null; 
    }
    
    
    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return clients.get(rowIndex).getNom();
            case 1:
                return clients.get(rowIndex).getPrenom();
            case 2:
                return clients.get(rowIndex).getMobile();
            case 3:
                return clients.get(rowIndex).getAdresse();
            case 4:
                return clients.get(rowIndex).getId();

        }
        return null;  
    }
    
}
