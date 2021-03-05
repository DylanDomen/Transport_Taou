/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author dylan
 */
public class fenetrePrincipal extends javax.swing.JFrame {

    /**
     * Creates new form fenetrePrincipal
     */
    
    public fenetrePrincipal() {
        initComponents();
        premiereUtilisation();
    }
    
    public void premiereUtilisation(){//fonction qui sert à vérifier si le logiciel possède déja un utilisateur ou si c'est une première utilisation
        // on vérifie si il y a un utilisateur dans la base ( pour lancer une connexion ou plutot une création )
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            
            //connexion avec la base
            
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            //requete
            Statement st = connexion.createStatement();
            ResultSet resultat=st.executeQuery("SELECT MAX(id) FROM utilisateur;");
            resultat.next();
            Integer id = resultat.getInt("max");
            if(id != 0){
                //il y a un utilisateur : donc on va lancer une connexion et non une création
                btnLog.setText("Se Connecter");
                this.setTitle("Transport T'aou - Connexion");
            }else{
                //il n'y a pas d'utilisateur : donc on va lancer une première inscription
                JOptionPane.showMessageDialog(MessageBienvenue, "Bienvenue sur Transport T'aou ! \n Il s'agit de votre première utilisation."
                + "\n Afin que vos données sur Transport T'aou ne soient accessible que par vous même.\nVeuillez vous inscrire en cliquant sur OK",
                "Inane warning",JOptionPane.WARNING_MESSAGE);
               
                this.setTitle("Transport T'aou - Inscription");
                btnLog.setText("S'inscrire");
                
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connexion(String nomUtilisateur, String motDePasse){
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            
            //connexion avec la base
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            //requete
            PreparedStatement pst;
            pst = connexion.prepareStatement("SELECT * FROM utilisateur WHERE id = ?");
            pst.setInt(1,1);
           
            ResultSet resultat = pst.executeQuery();
            resultat.next();
            
            String nomUtilisateurBase = resultat.getString("nom_utilisateur");
            String motDePasseBase = resultat.getString("mot_de_passe");
            
            if(nomUtilisateur.equals(nomUtilisateurBase) && motDePasseBase.equals(motDePasse) ){
                 pageAccueuil.setVisible(true);
                 pageAccueuil.setBounds(0, 0, 1920, 1080);
                 pageAccueuil.setLocationRelativeTo(null);
                 this.setVisible(false);
                 
            }else{
              JOptionPane.showMessageDialog(MessageBienvenue, "Nom d'utilisateur ou mot de passe incorrects ",
                    "Inane error",JOptionPane.ERROR_MESSAGE);  
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void afficheTableauClients(){
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            //connexion avec la base
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            //requete
            Statement st = connexion.createStatement();
            ResultSet resultat = st.executeQuery("Select * from clients");
            
            Integer i = 0;
            
            while(resultat.next()){
                /*Integer nbLignes = tableClients.getRowCount();
                if(i.equals(nbLignes)){
                    
                }*/
                Client client = new Client();
                
                client.setNom(resultat.getString("nom"));
                client.setPrenom(resultat.getString("prenom"));
                client.setMobile(resultat.getString("mobile"));
                client.setAdresse(resultat.getString("adresse"));
                client.setId(resultat.getInt("id"));
                
                tableClients.setValueAt(client.getNom(), i, 0);
                tableClients.setValueAt(client.getPrenom(), i, 1);
                tableClients.setValueAt(client.getMobile(), i, 2);
                tableClients.setValueAt(client.getAdresse(), i, 3);
                tableClients.setValueAt(client.getId(), i, 4);
                i++;
            }
            tableClients.getColumnModel().getColumn(4).setMinWidth(0);
            tableClients.getColumnModel().getColumn(4).setMaxWidth(0);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void videTableauClients(){
       Integer nbLigne = tableClients.getRowCount();
       for(Integer i = 0; i< nbLigne;i++){
           tableClients.setValueAt("", i, 0);
           tableClients.setValueAt("", i, 1);
           tableClients.setValueAt("", i, 2);
           tableClients.setValueAt("", i, 3);
           tableClients.setValueAt("", i, 4);
       }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MessageBienvenue = new javax.swing.JOptionPane();
        pageAccueuil = new javax.swing.JFrame();
        onglet = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClients = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnNouveau = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnSupprimer = new javax.swing.JButton();
        CreerModifierClient = new javax.swing.JFrame();
        labelTitreCreerModifierClient = new javax.swing.JLabel();
        labelNom = new javax.swing.JLabel();
        labelPrenom = new javax.swing.JLabel();
        labelMobile = new javax.swing.JLabel();
        labelAdresse = new javax.swing.JLabel();
        champNom = new javax.swing.JTextField();
        champPrenom = new javax.swing.JTextField();
        champMobile = new javax.swing.JTextField();
        champAdresse = new javax.swing.JTextField();
        btnValider = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        champNomUtilisateur = new javax.swing.JTextField();
        btnLog = new javax.swing.JButton();
        champMotDePasse = new javax.swing.JPasswordField();

        pageAccueuil.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pageAccueuil.setTitle("Transport T'aou - Accueil");
        pageAccueuil.setPreferredSize(new java.awt.Dimension(1000, 700));
        pageAccueuil.setSize(new java.awt.Dimension(1920, 1080));

        onglet.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ongletStateChanged(evt);
            }
        });

        tableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nom", "Prenom", "Mobile", "Adresse", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableClients.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableClients);
        if (tableClients.getColumnModel().getColumnCount() > 0) {
            tableClients.getColumnModel().getColumn(0).setResizable(false);
            tableClients.getColumnModel().getColumn(1).setResizable(false);
            tableClients.getColumnModel().getColumn(2).setResizable(false);
            tableClients.getColumnModel().getColumn(3).setResizable(false);
            tableClients.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1638, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );

        onglet.addTab("Clients", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1638, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 672, Short.MAX_VALUE)
        );

        onglet.addTab("Rendez-vous", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1638, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 672, Short.MAX_VALUE)
        );

        onglet.addTab("Calendrier", jPanel3);

        btnNouveau.setText("Nouveau Client");
        btnNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNouveauActionPerformed(evt);
            }
        });

        btnModifier.setText("Modifier Client");
        btnModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifierActionPerformed(evt);
            }
        });

        btnSupprimer.setText("Supprimer Client");
        btnSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pageAccueuilLayout = new javax.swing.GroupLayout(pageAccueuil.getContentPane());
        pageAccueuil.getContentPane().setLayout(pageAccueuilLayout);
        pageAccueuilLayout.setHorizontalGroup(
            pageAccueuilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pageAccueuilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(onglet, javax.swing.GroupLayout.PREFERRED_SIZE, 1643, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(pageAccueuilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNouveau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pageAccueuilLayout.setVerticalGroup(
            pageAccueuilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pageAccueuilLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(btnNouveau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModifier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSupprimer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(onglet, javax.swing.GroupLayout.Alignment.LEADING)
        );

        labelTitreCreerModifierClient.setText("Création/modification d'un client");

        labelNom.setText("Nom");

        labelPrenom.setText("Prenom");

        labelMobile.setText("Mobile");

        labelAdresse.setText("Adresse");

        champNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champNomActionPerformed(evt);
            }
        });

        btnValider.setText("Valider");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        jLabel4.setText("*");

        jLabel5.setText("Les champs qui se succèdent de \"*\" sont facultatif");

        javax.swing.GroupLayout CreerModifierClientLayout = new javax.swing.GroupLayout(CreerModifierClient.getContentPane());
        CreerModifierClient.getContentPane().setLayout(CreerModifierClientLayout);
        CreerModifierClientLayout.setHorizontalGroup(
            CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreerModifierClientLayout.createSequentialGroup()
                .addGap(279, 279, 279)
                .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(CreerModifierClientLayout.createSequentialGroup()
                        .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNom)
                            .addComponent(labelMobile)
                            .addComponent(labelPrenom)
                            .addGroup(CreerModifierClientLayout.createSequentialGroup()
                                .addComponent(labelAdresse)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25)
                        .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelTitreCreerModifierClient)
                            .addComponent(champNom)
                            .addComponent(champPrenom)
                            .addComponent(champMobile)
                            .addComponent(champAdresse)
                            .addComponent(btnValider, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))))
                .addContainerGap(492, Short.MAX_VALUE))
        );
        CreerModifierClientLayout.setVerticalGroup(
            CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreerModifierClientLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(labelTitreCreerModifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNom)
                    .addComponent(champNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPrenom)
                    .addComponent(champPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMobile)
                    .addComponent(champMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CreerModifierClientLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelAdresse)
                            .addComponent(champAdresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CreerModifierClientLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addGap(55, 55, 55)
                .addComponent(btnValider)
                .addGap(53, 53, 53)
                .addComponent(jLabel5)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Transport T'aou");

        jLabel1.setText("Nom d'utilisateur");

        jLabel2.setText("Mot de Passe");

        btnLog.setText("jButton1");
        btnLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(champNomUtilisateur)
                    .addComponent(champMotDePasse, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(320, Short.MAX_VALUE)
                .addComponent(btnLog)
                .addGap(281, 281, 281))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champNomUtilisateur))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(champMotDePasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnLog)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogActionPerformed

        String nomUtilisateur = champNomUtilisateur.getText();
        String motDePasse = champMotDePasse.getText();
        String etatBTN = btnLog.getText();
        
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            
            //connexion bdd
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            // Hashage d'un mot de passe
            //String hashed = BCrypt.hashpw(motDePasse,BCrypt.gensalt());

            if(etatBTN == "S'inscrire"){
                //on lui créer un compte utilisateur
                if(!nomUtilisateur.equals("") && !motDePasse.equals("")){
                    PreparedStatement pst = connexion.prepareStatement("Insert into utilisateur values (?,?,?)");
                    pst.setInt(1,1);
                    pst.setString(2,nomUtilisateur);
                    pst.setString(3,motDePasse);
                    pst.execute();
                    JOptionPane.showMessageDialog(MessageBienvenue, "Compte créer avec succès!\n Veuillez à présent vous connecter");
                    btnLog.setText("Se Connecter");
                    champNomUtilisateur.setText("");
                    champMotDePasse.setText("");
                }else{
                    JOptionPane.showMessageDialog(MessageBienvenue, "ERREUR!\n Nom d'utilisateur ou Mot de passe non renseigné ",
                    "Erreur",JOptionPane.ERROR_MESSAGE);
                }
                
            }else{//connexion
                //on test si le nom d'utilisateur + mot de passe correspondent au compte de la base
                connexion(nomUtilisateur,motDePasse);  
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            //connexion avec la base
            
            
    }//GEN-LAST:event_btnLogActionPerformed

    private void btnNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauActionPerformed
        //on est dans le cas bouton nouveau
        Integer index = onglet.getSelectedIndex();
        switch(index) {
            case 0://clients
                CreerModifierClient.setVisible(true);
                CreerModifierClient.setBounds(0, 0, 1920, 1080);
                CreerModifierClient.setLocationRelativeTo(null);
                CreerModifierClient.setTitle("Transport T'aou - Création d'un client");
                labelTitreCreerModifierClient.setText("Création d'un client");
                champNom.setText("");
                champPrenom.setText("");
                champMobile.setText("");
                champAdresse.setText("");
                break;
            case 1://rendez-vous
                
                break;
            case 2://calendrier
                
                break;
            default:
                
        }
    }//GEN-LAST:event_btnNouveauActionPerformed

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
        //on est dans le cas bouton modifier
        Integer index = onglet.getSelectedIndex();
        switch(index) {
            case 0://clients
                CreerModifierClient.setVisible(true);
                CreerModifierClient.setBounds(0, 0, 1920, 1080);
                CreerModifierClient.setLocationRelativeTo(null);
                CreerModifierClient.setTitle("Transport T'aou - Modification d'un client");
                labelTitreCreerModifierClient.setText("Modification d'un client");
                
                Integer ligneSelectionne = tableClients.getSelectedRow();
                if (ligneSelectionne != -1){
                    champNom.setText(tableClients.getValueAt(ligneSelectionne, 0).toString());
                    champPrenom.setText(tableClients.getValueAt(ligneSelectionne, 1).toString());
                    champMobile.setText(tableClients.getValueAt(ligneSelectionne, 2).toString());
                    champAdresse.setText(tableClients.getValueAt(ligneSelectionne, 3).toString());
                }else{
                    CreerModifierClient.setVisible(false);
                    JOptionPane.showMessageDialog(MessageBienvenue, "Aucune ligne n'a été selectionné",
                    "Erreur",JOptionPane.ERROR_MESSAGE);
                }
                
                break;
            case 1://rendez-vous
                
                break;
            case 2://calendrier
                
                break;
            default:
                
        }
    }//GEN-LAST:event_btnModifierActionPerformed

    private void ongletStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ongletStateChanged
        Integer index = onglet.getSelectedIndex();
        switch(index) {
            case 0://clients
                afficheTableauClients();
                btnNouveau.setVisible(true);
                btnModifier.setVisible(true);
                btnSupprimer.setVisible(true);
                btnNouveau.setText("Nouveau Client");
                btnModifier.setText("Modifier Client");
                btnSupprimer.setText("Supprimer Client");
                break;
            case 1://rendez-vous
                btnNouveau.setVisible(true);
                btnModifier.setVisible(true);
                btnSupprimer.setVisible(true);
                btnNouveau.setText("Nouveau Rendez-vous");
                btnModifier.setText("Modifier Rendez-vous");
                btnSupprimer.setText("Supprimer Rendez-vous");
                break;
            case 2://calendrier
                btnNouveau.setVisible(false);
                btnModifier.setVisible(false);
                btnSupprimer.setVisible(false);
                break;
            default:
                
        }
    }//GEN-LAST:event_ongletStateChanged

    private void champNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champNomActionPerformed

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            //connexion bdd
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            String nom = champNom.getText();
            String prenom = champPrenom.getText();
            String mobile = champMobile.getText();
            String adresse = champAdresse.getText();
            
            String titre = labelTitreCreerModifierClient.getText();
            
            if(titre.equals("Création d'un client")){//création d'un client
                //récupère le dernier id+1 pour ajouter a la suite
                Statement st=connexion.createStatement();
                ResultSet resultat=st.executeQuery("SELECT MAX(id) FROM clients;");
                resultat.next();
                int id = resultat.getInt("max")+1;
            
                if(!nom.equals("")&&!prenom.equals("")&&!mobile.equals("")){
                    PreparedStatement pst = connexion.prepareStatement("Insert into clients values (?,?,?,?,?)");
                    pst.setInt(1,id);
                    pst.setString(2,nom);
                    pst.setString(3,prenom);
                    pst.setString(4,mobile);
                    pst.setString(5,adresse);
                    pst.execute();

                    CreerModifierClient.setVisible(false);
                    videTableauClients();
                    afficheTableauClients();
                    
                    JOptionPane.showMessageDialog(MessageBienvenue, "Client ajouter avec succès");
                }else{
                JOptionPane.showMessageDialog(MessageBienvenue, "Veuillez remplir correctement les champs obligatoire",   
                "Erreur",JOptionPane.ERROR_MESSAGE);
                }
            }else{//modification d'un client
                Integer ligneSelectionne = tableClients.getSelectedRow();
                String idLigneChaine = tableClients.getValueAt(ligneSelectionne,4).toString();
                Integer idLigne = Integer.parseInt(idLigneChaine);
               if(!nom.equals("")&&!prenom.equals("")&&!mobile.equals("")){
                PreparedStatement pst = connexion.prepareStatement("UPDATE clients SET nom = ?, prenom = ?, mobile = ?, adresse = ? WHERE id = ?");
                pst.setString(1,nom);
                pst.setString(2,prenom);
                pst.setString(3,mobile);
                pst.setString(4,adresse);
                pst.setInt(5,idLigne);
                pst.execute();

                CreerModifierClient.setVisible(false);
                videTableauClients();
                afficheTableauClients();
                
                JOptionPane.showMessageDialog(MessageBienvenue, "Client Modifier avec succès");
               }else{
                JOptionPane.showMessageDialog(MessageBienvenue, "Veuillez remplir correctement les champs obligatoire",   
                "Erreur",JOptionPane.ERROR_MESSAGE);
               }
            }    
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnValiderActionPerformed

    private void btnSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerActionPerformed
        //supprimer le client de la ligne selectionné
        Integer ligneSelectionne = tableClients.getSelectedRow();
        String idLigneChaine = tableClients.getValueAt(ligneSelectionne,4).toString();
        Integer idLigne = Integer.parseInt(idLigneChaine);
        
        try {
            //chargement driver
            Class.forName("org.postgresql.Driver");
            //connexion bdd
            Connection connexion;
            connexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/transport-taou", "postgres", "admin");
            
            //requete de suppression
            PreparedStatement pst = connexion.prepareStatement("DELETE FROM clients WHERE id = ?");
            pst.setInt(1,idLigne);
            pst.execute();
            CreerModifierClient.setVisible(false);
            videTableauClients();
            afficheTableauClients();
            JOptionPane.showMessageDialog(MessageBienvenue, "Client supprimer avec succès");
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSupprimerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fenetrePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fenetrePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fenetrePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fenetrePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fenetrePrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame CreerModifierClient;
    private javax.swing.JOptionPane MessageBienvenue;
    private javax.swing.JButton btnLog;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnNouveau;
    private javax.swing.JButton btnSupprimer;
    private javax.swing.JButton btnValider;
    private javax.swing.JTextField champAdresse;
    private javax.swing.JTextField champMobile;
    private javax.swing.JPasswordField champMotDePasse;
    private javax.swing.JTextField champNom;
    private javax.swing.JTextField champNomUtilisateur;
    private javax.swing.JTextField champPrenom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAdresse;
    private javax.swing.JLabel labelMobile;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelPrenom;
    private javax.swing.JLabel labelTitreCreerModifierClient;
    private javax.swing.JTabbedPane onglet;
    private javax.swing.JFrame pageAccueuil;
    private javax.swing.JTable tableClients;
    // End of variables declaration//GEN-END:variables
}
