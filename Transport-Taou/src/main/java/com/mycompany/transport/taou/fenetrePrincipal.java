/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import static com.mycompany.transport.taou.HashMDP.decrypte;
import static com.mycompany.transport.taou.HashMDP.encrypte;
import static com.mycompany.transport.taou.HashMDP.genereCle;
import com.mycompany.transport.taou.designPattern.ConnexionBase;
import com.mycompany.transport.taou.designPattern.Observateur;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Text;

/**
 *
 * @author dylan
 */
public class fenetrePrincipal extends javax.swing.JFrame {

    /**
     * Creates new form fenetrePrincipal
     */
    List<Client> listeClient; //1) on initialise une array list
    List<TypeDemande> listeTypeDemande;
    List<MoyenPaiment> listeMoyensPaiment;
    Integer nbLigneFiltre = 0;//nb d'enregistrement du tableau actuel
    Client client;
    Integer nbLigne;
    DefaultTableModel modelBase;
    ListSelectionModel selectionModelBase;
    boolean confirme;
    ConnexionBase connexionBase;

    public fenetrePrincipal() {

        initComponents();

        nbLigne = tableClients.getRowCount();
        modelBase = (DefaultTableModel) tableClients.getModel();
        selectionModelBase = (ListSelectionModel) tableClients.getSelectionModel();
        tableClients = new MaJtable(this);
        tableClients.setModel(modelBase);
        tableClients.setSelectionModel(selectionModelBase);
        client = new Client();
        premiereUtilisation();
    }

    public void premiereUtilisation() {//fonction qui sert à vérifier si le logiciel possède déja un utilisateur ou si c'est une première utilisation
        // on vérifie si il y a un utilisateur dans la base ( pour lancer une connexion ou plutot une création )
        try {
            connexionBase = ConnexionBase.recupInstance();
            ResultSet resultat = connexionBase.requeteRecupereTout("SELECT MAX(id) FROM utilisateur;");
            resultat.next();
            Integer id = resultat.getInt("max");
            if (id != 0) {
                //il y a un utilisateur : donc on va lancer une connexion et non une création
                btnLog.setText("Se Connecter");
                this.setTitle("Transport T'aou - Connexion");
            } else {
                //il n'y a pas d'utilisateur : donc on va lancer une première inscription
                JOptionPane.showMessageDialog(MessageBienvenue, "Bienvenue sur Transport T'aou !\nIl s'agit de votre première utilisation."
                        + "\nAfin que vos données sur Transport T'aou ne soient accessible que par vous même.\nVeuillez vous inscrire en cliquant sur OK",
                        "Inane warning", JOptionPane.WARNING_MESSAGE);

                this.setTitle("Transport T'aou - Inscription");
                btnLog.setText("S'inscrire");
            }
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connexion(String nomUtilisateur, String motDePasse) {
        try {

            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();
            //requete
            PreparedStatement pst;
            pst = connexion.prepareStatement("SELECT * FROM utilisateur WHERE id = ?");
            pst.setInt(1, 1);

            ResultSet resultat = pst.executeQuery();
            resultat.next();

            String nomUtilisateurBase = resultat.getString("nom_utilisateur");
            String motDePasseBase = resultat.getString("mot_de_passe");

            //====================TEST HASH=====================pb : cle de hashage pas sotcké , le cryptage change en fonction de la session
            /*try {
                SecretKey cle = genereCle("AES");
                Cipher chiffrement;
                chiffrement = Cipher.getInstance("AES");
                System.out.println(cle);
                System.out.println(chiffrement);
                byte[] texteEncrypter = encrypte(motDePasse, cle, chiffrement);
                String mdpCrypter = new String(texteEncrypter);
                motDePasse = mdpCrypter;
            } catch (Exception e) {
                motDePasse = "";
            }*/
            if (nomUtilisateur.equals(nomUtilisateurBase) && motDePasseBase.equals(motDePasse)) {
                pageAccueuil.setVisible(true);
                pageAccueuil.setBounds(0, 0, 1200, 702);
                pageAccueuil.setLocationRelativeTo(null);
                this.setVisible(false);

            } else {
                JOptionPane.showMessageDialog(MessageBienvenue, "Nom d'utilisateur ou mot de passe incorrects ",
                        "Inane error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void afficheTableauClients() {
        try {
            connexionBase = ConnexionBase.recupInstance();
            ResultSet resultat = connexionBase.requeteRecupereTout("Select * from clients");
            Integer i = 0;

            //MonModel model= new MonModel();
            while (resultat.next()) {
                Client client = new Client();

                client.setNom(resultat.getString("nom"));
                client.setPrenom(resultat.getString("prenom"));
                client.setMobile(resultat.getString("mobile"));
                client.setAdresse(resultat.getString("adresse"));
                client.setId(resultat.getInt("id"));

                //model.ajoutClient(client);
                tableClients.setValueAt(client.getNom(), i, 0);
                tableClients.setValueAt(client.getPrenom(), i, 1);
                tableClients.setValueAt(client.getMobile(), i, 2);
                tableClients.setValueAt(client.getAdresse(), i, 3);
                tableClients.setValueAt(client.getId(), i, 4);
                i++;
            }
            //tableClients.setModel(model);
            tableClients.getColumnModel().getColumn(4).setMinWidth(0);
            tableClients.getColumnModel().getColumn(4).setMaxWidth(0);

        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void videTableauClients() {
        Integer nbLigne = tableClients.getRowCount();
        Integer nbColonne = tableClients.getColumnCount();

        for (Integer i = 0; i < nbLigne; i++) {
            for (Integer j = 0; j < nbColonne; j++) {
                tableClients.getModel().setValueAt("", i, j);
            }
        }
    }

    public Integer afficheTableauClientsFiltre(String recherche) {
        videTableauClients();
        Integer i = 0;
        try {
            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();
            //requete
            PreparedStatement pst = connexion.prepareStatement("SELECT * FROM clients WHERE nom ILIKE ? ORDER BY nom ASC");
            pst.setString(1, recherche + "%");
            ResultSet resultat = pst.executeQuery();
            while (resultat.next()) {
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

        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;

    }

    public void majCombos() throws SQLException {

        //combo type demande
        listeTypeDemande = new ArrayList<TypeDemande>();
        ListTypeModel ListeTypeModel = new ListTypeModel();
        DefaultComboBoxModel defautComboBoxModel = new DefaultComboBoxModel();

        connexionBase = ConnexionBase.recupInstance();
        ResultSet resultat = connexionBase.requeteRecupereTout("SELECT * FROM type_demande");

        while (resultat.next()) {

            TypeDemande typeDemande = new TypeDemande();
            typeDemande.setTypeDemande(resultat.getString("typeDemande"));
            typeDemande.setId(resultat.getInt("id"));

            listeTypeDemande.add(typeDemande);

        }
        for (TypeDemande type : listeTypeDemande) {
            defautComboBoxModel.addElement(type);
        }

        comboTypeRdv.setModel(defautComboBoxModel);
        comboTypeRdv.setRenderer(new MaListeCellRenderer());

        //combo moyen de paiment
        listeMoyensPaiment = new ArrayList<MoyenPaiment>();
        ListPaimentModel ListePaimentModel = new ListPaimentModel();
        DefaultComboBoxModel defautComboBoxModelMoyenpaiment = new DefaultComboBoxModel();

        connexionBase = ConnexionBase.recupInstance();
        resultat = connexionBase.requeteRecupereTout("SELECT * FROM moyens_paiment");

        while (resultat.next()) {

            MoyenPaiment moyenPaiment = new MoyenPaiment();
            moyenPaiment.setMoyenPaiment(resultat.getString("moyenPaiment"));
            moyenPaiment.setId(resultat.getInt("id"));

            listeMoyensPaiment.add(moyenPaiment);

        }
        for (MoyenPaiment moyen : listeMoyensPaiment) {
            defautComboBoxModelMoyenpaiment.addElement(moyen);
        }

        comboMoyenPaimentRDV.setModel(defautComboBoxModelMoyenpaiment);
        comboMoyenPaimentRDV.setRenderer(new MaListeCellRenderer());

        //combo clients
        listeClient = new ArrayList<Client>();
        ListClientModel ListeClientModel = new ListClientModel();
        DefaultComboBoxModel defautComboBoxClient = new DefaultComboBoxModel();

        connexionBase = ConnexionBase.recupInstance();
        resultat = connexionBase.requeteRecupereTout("SELECT * FROM clients");

        while (resultat.next()) {

            Client client = new Client();
            client.setNom(resultat.getString("nom"));
            client.setPrenom(resultat.getString("prenom"));
            client.setMobile(resultat.getString("mobile"));
            client.setAdresse(resultat.getString("adresse"));
            client.setId(resultat.getInt("id"));

            listeClient.add(client);

        }
        for (Client client : listeClient) {
            defautComboBoxClient.addElement(client);
        }

        comboClient.setModel(defautComboBoxClient);
        comboClient.setRenderer(new MaListeCellRenderer());

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
        jPanel5 = new javax.swing.JPanel();
        champRecherche = new javax.swing.JTextField();
        btnRechercher = new javax.swing.JButton();
        onglet = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClients = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnExportCSV = new javax.swing.JButton();
        btnNouveau = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnSupprimer = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        parametre = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        CreerModifierClient = new javax.swing.JFrame();
        jPanel7 = new javax.swing.JPanel();
        labelTitreCreerModifierClient = new javax.swing.JLabel();
        champNom = new javax.swing.JTextField();
        labelNom = new javax.swing.JLabel();
        labelPrenom = new javax.swing.JLabel();
        champPrenom = new javax.swing.JTextField();
        labelMobile = new javax.swing.JLabel();
        champMobile = new javax.swing.JTextField();
        labelAdresse = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        champAdresse = new javax.swing.JTextField();
        btnValider = new javax.swing.JButton();
        btnAnnule = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        selecteurFichier = new javax.swing.JFileChooser();
        MessageConfirmation = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        TexteMessageConfirmation = new javax.swing.JLabel();
        BtnOuiConfirmation = new javax.swing.JButton();
        BtnNonConfirmation = new javax.swing.JButton();
        CreerModifierRDV = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        labelTitreCreerModifierRDV = new javax.swing.JLabel();
        champNomRDV = new javax.swing.JTextField();
        LabelNomRDV = new javax.swing.JLabel();
        LabelTypeRDV = new javax.swing.JLabel();
        comboTypeRdv = new javax.swing.JComboBox<>();
        champLieuDestination = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        champDateDepart = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        champDateRetour = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        champHeureRetour = new javax.swing.JFormattedTextField();
        champHeureDepart = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        champNoteRDV = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        champLieuDepart = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        spinnerNbPersonnes = new javax.swing.JSpinner();
        champPrix = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        comboMoyenPaimentRDV = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        comboEtatRDV = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        btnValiderRDV = new javax.swing.JButton();
        btnAnnulerRDV = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        comboClient = new javax.swing.JComboBox<>();
        CreerTypeOuMoyenPaiment = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        labelTitreCreationTypeOuMoyenPaiment = new javax.swing.JLabel();
        champCreerTypeOuMoyenPaiment = new javax.swing.JTextField();
        labelChampCreerTypeOuMoyenPaiment = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        champNomUtilisateur = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        champMotDePasse = new javax.swing.JPasswordField();
        btnLog = new javax.swing.JButton();

        MessageBienvenue.setBackground(new java.awt.Color(255, 204, 204));

        pageAccueuil.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pageAccueuil.setTitle("Transport T'aou - Accueil");
        pageAccueuil.setIconImages(null);
        pageAccueuil.setPreferredSize(new java.awt.Dimension(1024, 700));
        pageAccueuil.setResizable(false);
        pageAccueuil.setSize(new java.awt.Dimension(1200, 702));

        jPanel5.setBackground(new java.awt.Color(255, 204, 204));

        btnRechercher.setBackground(new java.awt.Color(153, 255, 204));
        btnRechercher.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnRechercher.setText("Rechercher");
        btnRechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercherActionPerformed(evt);
            }
        });

        onglet.setBackground(new java.awt.Color(153, 255, 204));
        onglet.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        onglet.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ongletStateChanged(evt);
            }
        });

        tableClients.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
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
                "Nom", "Prenom", "Mobile", "Adresse", "Id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tableClients.setGridColor(new java.awt.Color(51, 51, 51));
        tableClients.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tableClients.getTableHeader().setReorderingAllowed(false);
        tableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableClients);
        if (tableClients.getColumnModel().getColumnCount() > 0) {
            tableClients.getColumnModel().getColumn(0).setResizable(false);
            tableClients.getColumnModel().getColumn(1).setResizable(false);
            tableClients.getColumnModel().getColumn(2).setResizable(false);
            tableClients.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );

        onglet.addTab("Clients", jPanel1);

        jTable1.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nom", "Type", "Date Départ", "Heure Départ", "Lieu Départ", "Lieu Destination", "Date Retour", "Heure Retour", "Nombre De Personnes", "Prix", "Moyen De Paiment", "État", "IdCLient"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionBackground(new java.awt.Color(255, 204, 204));
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(10).setResizable(false);
            jTable1.getColumnModel().getColumn(11).setResizable(false);
            jTable1.getColumnModel().getColumn(12).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
        );

        onglet.addTab("Rendez-vous", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        onglet.addTab("Calendrier", jPanel3);

        btnExportCSV.setBackground(new java.awt.Color(153, 255, 204));
        btnExportCSV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnExportCSV.setText("Exporter en csv");
        btnExportCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportCSVActionPerformed(evt);
            }
        });

        btnNouveau.setBackground(new java.awt.Color(153, 255, 204));
        btnNouveau.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnNouveau.setText("Nouveau Client");
        btnNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNouveauActionPerformed(evt);
            }
        });

        btnModifier.setBackground(new java.awt.Color(153, 255, 204));
        btnModifier.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnModifier.setText("Modifier Client");
        btnModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifierActionPerformed(evt);
            }
        });

        btnSupprimer.setBackground(new java.awt.Color(153, 255, 204));
        btnSupprimer.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnSupprimer.setText("Supprimer Client");
        btnSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(champRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnRechercher)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(onglet, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModifier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSupprimer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(btnNouveau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportCSV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRechercher))
                .addGap(60, 60, 60)
                .addComponent(onglet, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(btnNouveau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModifier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSupprimer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportCSV)
                .addGap(58, 58, 58))
        );

        jMenuBar1.setBackground(new java.awt.Color(153, 255, 204));

        parametre.setBackground(new java.awt.Color(153, 255, 204));
        parametre.setText("Paramètres");
        parametre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parametreActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Créer un type de demande");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        parametre.add(jMenuItem1);

        jMenuItem2.setText("Créer un moyen de paiment");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        parametre.add(jMenuItem2);

        jMenuBar1.add(parametre);

        pageAccueuil.setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout pageAccueuilLayout = new javax.swing.GroupLayout(pageAccueuil.getContentPane());
        pageAccueuil.getContentPane().setLayout(pageAccueuilLayout);
        pageAccueuilLayout.setHorizontalGroup(
            pageAccueuilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pageAccueuilLayout.setVerticalGroup(
            pageAccueuilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        CreerModifierClient.setResizable(false);

        jPanel7.setBackground(new java.awt.Color(255, 204, 204));

        labelTitreCreerModifierClient.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelTitreCreerModifierClient.setText("Création/modification d'un client");

        champNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champNomActionPerformed(evt);
            }
        });

        labelNom.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelNom.setText("Nom");

        labelPrenom.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelPrenom.setText("Prenom");

        labelMobile.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelMobile.setText("Mobile");

        labelAdresse.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelAdresse.setText("Adresse");

        jLabel4.setText("*");

        champAdresse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champAdresseActionPerformed(evt);
            }
        });

        btnValider.setBackground(new java.awt.Color(153, 255, 204));
        btnValider.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnValider.setText("Valider");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        btnAnnule.setBackground(new java.awt.Color(153, 255, 204));
        btnAnnule.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnAnnule.setText("Annuler");
        btnAnnule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnuleActionPerformed(evt);
            }
        });

        jLabel5.setText("Les champs qui se succèdent de \"*\" sont facultatif");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTitreCreerModifierClient)
                .addGap(128, 128, 128))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelMobile)
                            .addComponent(labelPrenom)
                            .addComponent(labelNom)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(labelAdresse)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(champNom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(champPrenom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(champMobile, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(champAdresse, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(btnValider, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnAnnule, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(labelTitreCreerModifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNom))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPrenom))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMobile)
                    .addComponent(champMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champAdresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAdresse)
                    .addComponent(jLabel4))
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnnule)
                    .addComponent(btnValider))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CreerModifierClientLayout = new javax.swing.GroupLayout(CreerModifierClient.getContentPane());
        CreerModifierClient.getContentPane().setLayout(CreerModifierClientLayout);
        CreerModifierClientLayout.setHorizontalGroup(
            CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CreerModifierClientLayout.setVerticalGroup(
            CreerModifierClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        selecteurFichier.setCurrentDirectory(new java.io.File("C:\\Users"));
        selecteurFichier.setSelectedFile(new java.io.File("C:\\Program Files\\NetBeans-12.2\\export.csv"));

        jPanel6.setBackground(new java.awt.Color(255, 204, 204));

        TexteMessageConfirmation.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        TexteMessageConfirmation.setText("jLabel3");
        TexteMessageConfirmation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        BtnOuiConfirmation.setBackground(new java.awt.Color(153, 255, 204));
        BtnOuiConfirmation.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        BtnOuiConfirmation.setText("jButton1");
        BtnOuiConfirmation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOuiConfirmationActionPerformed(evt);
            }
        });

        BtnNonConfirmation.setBackground(new java.awt.Color(153, 255, 204));
        BtnNonConfirmation.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        BtnNonConfirmation.setText("jButton1");
        BtnNonConfirmation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNonConfirmationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 85, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(TexteMessageConfirmation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(BtnOuiConfirmation)
                        .addGap(71, 71, 71)
                        .addComponent(BtnNonConfirmation)))
                .addGap(87, 87, 87))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(TexteMessageConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnNonConfirmation)
                    .addComponent(BtnOuiConfirmation))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MessageConfirmationLayout = new javax.swing.GroupLayout(MessageConfirmation.getContentPane());
        MessageConfirmation.getContentPane().setLayout(MessageConfirmationLayout);
        MessageConfirmationLayout.setHorizontalGroup(
            MessageConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MessageConfirmationLayout.setVerticalGroup(
            MessageConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        CreerModifierRDV.setPreferredSize(new java.awt.Dimension(1024, 614));
        CreerModifierRDV.setResizable(false);

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        labelTitreCreerModifierRDV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelTitreCreerModifierRDV.setText("Création/modification d'un Rendez-vous");

        LabelNomRDV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        LabelNomRDV.setText("Nom");

        LabelTypeRDV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        LabelTypeRDV.setText("Type");

        comboTypeRdv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboTypeRdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTypeRdvActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel10.setText("Lieu Destination");

        champDateDepart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        champDateDepart.setText("01/01/2021");

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel7.setText("Date Départ");

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel11.setText("Date Retour");

        champDateRetour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        champDateRetour.setText("01/01/2021");

        jLabel12.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel12.setText("Heure Retour");

        champHeureRetour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
        champHeureRetour.setText("00:00");
        champHeureRetour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champHeureRetourActionPerformed(evt);
            }
        });

        champHeureDepart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
        champHeureDepart.setText("00:00");

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel8.setText("Heure Départ");

        champNoteRDV.setColumns(20);
        champNoteRDV.setRows(5);
        jScrollPane3.setViewportView(champNoteRDV);

        jLabel14.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel14.setText("Note");

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel9.setText("Lieu Départ");

        jLabel13.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel13.setText("Nombre De Personnes");

        champPrix.setText("11.99");

        jLabel15.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel15.setText("Prix");

        comboMoyenPaimentRDV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel16.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel16.setText("Moyen De Paiment");

        comboEtatRDV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terminé", "Non effectué" }));
        comboEtatRDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEtatRDVActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel17.setText("État");

        btnValiderRDV.setBackground(new java.awt.Color(153, 255, 204));
        btnValiderRDV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnValiderRDV.setText("Valider");
        btnValiderRDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderRDVActionPerformed(evt);
            }
        });

        btnAnnulerRDV.setBackground(new java.awt.Color(153, 255, 204));
        btnAnnulerRDV.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnAnnulerRDV.setText("Annuler");

        jLabel3.setText("€");

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel6.setText("Client concerné");

        comboClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(labelTitreCreerModifierRDV)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerNbPersonnes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboClient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(LabelNomRDV)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(champNomRDV, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(LabelTypeRDV)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(comboTypeRdv, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel7))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnValiderRDV)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(champLieuDepart, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(champHeureDepart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                            .addComponent(champDateDepart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAnnulerRDV)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(76, 76, 76)
                                .addComponent(champPrix, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(champLieuDestination)
                                    .addComponent(champDateRetour, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                    .addComponent(champHeureRetour)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboEtatRDV, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboMoyenPaimentRDV, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(473, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(labelTitreCreerModifierRDV, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(champNomRDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LabelNomRDV)
                        .addComponent(champLieuDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTypeRDV)
                    .addComponent(jLabel11)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboTypeRdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(champDateRetour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(champHeureRetour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champDateDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(champHeureDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(champLieuDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(spinnerNbPersonnes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(comboClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(champPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboMoyenPaimentRDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(comboEtatRDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnValiderRDV)
                    .addComponent(btnAnnulerRDV))
                .addGap(89, 89, 89))
        );

        javax.swing.GroupLayout CreerModifierRDVLayout = new javax.swing.GroupLayout(CreerModifierRDV.getContentPane());
        CreerModifierRDV.getContentPane().setLayout(CreerModifierRDVLayout);
        CreerModifierRDVLayout.setHorizontalGroup(
            CreerModifierRDVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CreerModifierRDVLayout.setVerticalGroup(
            CreerModifierRDVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        labelTitreCreationTypeOuMoyenPaiment.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        labelTitreCreationTypeOuMoyenPaiment.setText("jLabel3");

        labelChampCreerTypeOuMoyenPaiment.setText("jLabel3");

        jButton1.setBackground(new java.awt.Color(153, 255, 204));
        jButton1.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jButton1.setText("Ajouter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 255, 204));
        jButton2.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jButton2.setText("Annuler");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(labelChampCreerTypeOuMoyenPaiment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelTitreCreationTypeOuMoyenPaiment, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(champCreerTypeOuMoyenPaiment))))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(labelTitreCreationTypeOuMoyenPaiment)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champCreerTypeOuMoyenPaiment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelChampCreerTypeOuMoyenPaiment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CreerTypeOuMoyenPaimentLayout = new javax.swing.GroupLayout(CreerTypeOuMoyenPaiment.getContentPane());
        CreerTypeOuMoyenPaiment.getContentPane().setLayout(CreerTypeOuMoyenPaimentLayout);
        CreerTypeOuMoyenPaimentLayout.setHorizontalGroup(
            CreerTypeOuMoyenPaimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CreerTypeOuMoyenPaimentLayout.setVerticalGroup(
            CreerTypeOuMoyenPaimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Transport T'aou");
        setBackground(new java.awt.Color(255, 204, 204));
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        champNomUtilisateur.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel1.setText("Nom d'utilisateur");

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        jLabel2.setText("Mot de Passe");

        champMotDePasse.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnLog.setBackground(new java.awt.Color(153, 255, 204));
        btnLog.setFont(new java.awt.Font("Comic Sans MS", 1, 11)); // NOI18N
        btnLog.setText("jButton1");
        btnLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(champNomUtilisateur, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(champMotDePasse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLog)
                .addGap(225, 225, 225))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(champNomUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(champMotDePasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(btnLog)))
                .addContainerGap(173, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogActionPerformed

        String nomUtilisateur = champNomUtilisateur.getText();
        String motDePasse = champMotDePasse.getText();
        String etatBTN = btnLog.getText();

        //====================TEST HASH=====================pb : cle de hashage pas sotcké , le cryptage change en fonction de la session
        /*try {
            SecretKey cle = genereCle("AES");
            Cipher chiffrement;
            chiffrement = Cipher.getInstance("AES");

            byte[] texteEncrypter = encrypte(motDePasse, cle, chiffrement);
            String mdpCrypter = new String(texteEncrypter);
            motDePasse = mdpCrypter;
        } catch (Exception e) {
            motDePasse = "";
        }*/
        try {

            connexionBase = ConnexionBase.recupInstance();
            Connection connexion = connexionBase.getConnexion();

            if (etatBTN == "S'inscrire") {
                //on lui créer un compte utilisateur
                if (!nomUtilisateur.equals("") && !motDePasse.equals("")) {
                    PreparedStatement pst = connexion.prepareStatement("Insert into utilisateur values (?,?,?)");
                    pst.setInt(1, 1);
                    pst.setString(2, nomUtilisateur);
                    pst.setString(3, motDePasse);
                    pst.execute();
                    JOptionPane.showMessageDialog(MessageBienvenue, "Compte créer avec succès!\nVeuillez à présent vous connecter");
                    btnLog.setText("Se Connecter");
                    champNomUtilisateur.setText("");
                    champMotDePasse.setText("");
                } else {
                    JOptionPane.showMessageDialog(MessageBienvenue, "ERREUR!\nNom d'utilisateur ou Mot de passe non renseigné",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {//connexion
                //on test si le nom d'utilisateur + mot de passe correspondent au compte de la base
                connexion(nomUtilisateur, motDePasse);
            }
        } catch (SQLException ex) {
            Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLogActionPerformed

    private void btnNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauActionPerformed
        //on est dans le cas bouton nouveau
        Integer index = onglet.getSelectedIndex();
        switch (index) {
            case 0://client   
                CreerModifierClient.setVisible(true);
                CreerModifierClient.setBounds(0, 0, 500, 424);
                CreerModifierClient.setLocationRelativeTo(null);
                CreerModifierClient.setTitle("Transport T'aou - Création d'un client");
                labelTitreCreerModifierClient.setText("Création d'un client");
                champNom.setText("");
                champPrenom.setText("");
                champMobile.setText("");
                champAdresse.setText("");
                break;
            case 1://rendez-vous 
                CreerModifierRDV.setVisible(true);
                CreerModifierRDV.setBounds(0, 0, 1024, 614);
                CreerModifierRDV.setLocationRelativeTo(null);
                CreerModifierRDV.setTitle("Transport T'aou - Création d'un Rendez-vous");
                labelTitreCreerModifierRDV.setText("Création d'un Rendez-vous");
                 {
                    try {
                        //chargement des combos :
                        majCombos();
                    } catch (SQLException ex) {
                        Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case 2://calendrier               
                break;
            default:
        }
    }//GEN-LAST:event_btnNouveauActionPerformed

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
        //on est dans le cas bouton modifier
        Integer index = onglet.getSelectedIndex();
        switch (index) {
            case 0://clients
                CreerModifierClient.setVisible(true);
                CreerModifierClient.setBounds(0, 0, 500, 424);
                CreerModifierClient.setLocationRelativeTo(null);
                CreerModifierClient.setTitle("Transport T'aou - Modification d'un client");
                labelTitreCreerModifierClient.setText("Modification d'un client");

                Integer ligneSelectionne = tableClients.getSelectedRow();

                if (ligneSelectionne != -1) {
                    champNom.setText(tableClients.getValueAt(ligneSelectionne, 0).toString());
                    champPrenom.setText(tableClients.getValueAt(ligneSelectionne, 1).toString());
                    champMobile.setText(tableClients.getValueAt(ligneSelectionne, 2).toString());
                    champAdresse.setText(tableClients.getValueAt(ligneSelectionne, 3).toString());
                } else {
                    CreerModifierClient.setVisible(false);
                    JOptionPane.showMessageDialog(MessageBienvenue, "Aucune ligne n'a été selectionné",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
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
        switch (index) {
            case 0://clients
                afficheTableauClients();
                btnNouveau.setVisible(true);
                btnModifier.setVisible(true);
                btnSupprimer.setVisible(true);
                btnNouveau.setText("Nouveau Client");
                btnModifier.setText("Modifier Client");
                btnSupprimer.setText("Supprimer Client");
                btnExportCSV.setVisible(true);
                break;
            case 1://rendez-vous
                btnNouveau.setVisible(true);
                btnModifier.setVisible(true);
                btnSupprimer.setVisible(true);
                btnNouveau.setText("Nouveau Rendez-vous");
                btnModifier.setText("Modifier Rendez-vous");
                btnSupprimer.setText("Supprimer Rendez-vous");
                btnExportCSV.setVisible(true);
                break;
            case 2://calendrier
                btnNouveau.setVisible(false);
                btnModifier.setVisible(false);
                btnSupprimer.setVisible(false);
                btnExportCSV.setVisible(false);
                break;
            default:

        }
    }//GEN-LAST:event_ongletStateChanged

    private void champNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champNomActionPerformed

    }//GEN-LAST:event_champNomActionPerformed

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        connexionBase = ConnexionBase.recupInstance();
        Connection connexion = connexionBase.getConnexion();
        Client client = new Client();
        client.setNom(champNom.getText());
        client.setPrenom(champPrenom.getText());
        client.setMobile(champMobile.getText());
        client.setAdresse(champAdresse.getText());
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String mobile = client.getMobile();
        String adresse = client.getAdresse();
        //ajouter une ligne en plus dans le tableau
        Integer r = tableClients.getRowCount() + 1;
        modelBase.setRowCount(r);
        tableClients.setModel(modelBase);
        String titre = labelTitreCreerModifierClient.getText();
        ClientDAO sujet = new ClientDAO();
        sujet.ajouterObservateur((MaJtable) tableClients);
        if (titre.equals("Création d'un client")) {//création d'un client
            sujet.CreerClient(client);
            CreerModifierClient.setVisible(false);

        } else {//modification d'un client
            Integer ligneSelectionne = tableClients.getSelectedRow();
            String idLigneChaine = tableClients.getValueAt(ligneSelectionne, 4).toString();
            Integer idLigne = Integer.parseInt(idLigneChaine);
            if (!nom.equals("") && !prenom.equals("") && !mobile.equals("")) {

                sujet.ModifierClient(idLigne, client);
                CreerModifierClient.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(MessageBienvenue, "Veuillez remplir correctement les champs obligatoire",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnValiderActionPerformed

    private void btnSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerActionPerformed
        //supprimer le client de la ligne selectionné
        Integer ligneSelectionne = tableClients.getSelectedRow();

        if (ligneSelectionne.equals(-1)) {
            JOptionPane.showMessageDialog(MessageBienvenue, "Aucune ligne sélectionnée",
                    "Erreur", JOptionPane.ERROR_MESSAGE);

        } else {
            MessageConfirmation.setVisible(true);
            MessageConfirmation.setBounds(0, 0, 400, 300);
            MessageConfirmation.setLocationRelativeTo(null);
            MessageConfirmation.setTitle("Transport T'aou - Confirmation");
            TexteMessageConfirmation.setText("êtes vous sure de vouloir supprimer ce client ? ");
            BtnOuiConfirmation.setText("Oui");
            BtnNonConfirmation.setText("Non");
        }
    }//GEN-LAST:event_btnSupprimerActionPerformed

    private void btnExportCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportCSVActionPerformed
        Integer nbLigne = tableClients.getRowCount();
        String nom;
        String prenom;
        String mobile;
        String adresse;
        Integer id;
        listeClient = new ArrayList<Client>(); //  on instancie

        if (nbLigneFiltre.equals(0)) {
            try {
                connexionBase = ConnexionBase.recupInstance();
                ResultSet resultat = connexionBase.requeteRecupereTout("Select * from clients");
                Integer nbEnregistrement = 0;
                while (resultat.next()) {
                    nbEnregistrement++;
                }
                nbLigneFiltre = nbEnregistrement;
            } catch (SQLException ex) {
                Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (Integer i = 0; i < nbLigneFiltre; i++) {

            nom = tableClients.getValueAt(i, 0).toString();
            prenom = tableClients.getValueAt(i, 1).toString();
            mobile = tableClients.getValueAt(i, 2).toString();
            adresse = tableClients.getValueAt(i, 3).toString();
            String idLigneChaine = tableClients.getValueAt(i, 4).toString();

            id = Integer.parseInt(idLigneChaine);
            Client client = new Client(id, nom, prenom, mobile, adresse);

            listeClient.add(client);
        }

        String resultat = "NOM;PRENOM;MOBILE;ADRESSE \n";
        for (Client client : listeClient) {
            resultat += client + "\n";
        }
        System.out.println(resultat);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier CSV", "csv");
        selecteurFichier.setFileFilter(filter);
        int returnval = selecteurFichier.showSaveDialog(this);
        if (returnval == selecteurFichier.APPROVE_OPTION) {
            File f = selecteurFichier.getSelectedFile();

            try {
                FileWriter fw = new FileWriter(f);
                fw.write(resultat);
                fw.flush();
                fw.close();

            } catch (IOException ex) {
                Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        listeClient.clear();
    }//GEN-LAST:event_btnExportCSVActionPerformed

    private void btnRechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercherActionPerformed

        String recherche = champRecherche.getText();

        if (recherche.equalsIgnoreCase("")) {
            afficheTableauClients();
        } else {
            nbLigneFiltre = afficheTableauClientsFiltre(recherche);
        }
    }//GEN-LAST:event_btnRechercherActionPerformed

    private void champAdresseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champAdresseActionPerformed

    }//GEN-LAST:event_champAdresseActionPerformed

    private void tableClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClientsMouseClicked

    }//GEN-LAST:event_tableClientsMouseClicked

    private void BtnOuiConfirmationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOuiConfirmationActionPerformed
        Integer ligneSelectionne = tableClients.getSelectedRow();

        if (ligneSelectionne.equals(-1)) {
            MessageConfirmation.setVisible(false);
            JOptionPane.showMessageDialog(MessageBienvenue, "Aucune ligne sélectionnée",
                    "Erreur", JOptionPane.ERROR_MESSAGE);

        } else {
            String idLigneChaine = tableClients.getValueAt(ligneSelectionne, 4).toString();
            Integer idLigne = Integer.parseInt(idLigneChaine);
            ClientDAO sujet = new ClientDAO();
            sujet.ajouterObservateur((MaJtable) tableClients);
            sujet.SupprimmerClient(idLigne);
            MessageConfirmation.setVisible(false);
        }
    }//GEN-LAST:event_BtnOuiConfirmationActionPerformed

    private void BtnNonConfirmationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNonConfirmationActionPerformed

        MessageConfirmation.setVisible(false);
        JOptionPane.showMessageDialog(MessageBienvenue, "La suppression annulé");
    }//GEN-LAST:event_BtnNonConfirmationActionPerformed

    private void btnAnnuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnuleActionPerformed

        Integer index = onglet.getSelectedIndex();
        String titre = labelTitreCreerModifierClient.getText();;
        CreerModifierClient.setVisible(false);

        switch (index) {
            case 0://clients
                if (titre.equals("Création d'un client")) {
                    JOptionPane.showMessageDialog(MessageBienvenue, "Création d'un client annulé");
                } else {
                    JOptionPane.showMessageDialog(MessageBienvenue, "Modification du client annulé");
                }
                break;
            case 1://rendez-vous     
                break;
            case 2://calendrier
                break;
            default:
        }
    }//GEN-LAST:event_btnAnnuleActionPerformed

    private void btnValiderRDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderRDVActionPerformed

        String nom = champNomRDV.getText();
        TypeDemande typeDemandeSelectionne = (TypeDemande) comboTypeRdv.getSelectedItem();
        Integer idTypeDemande = typeDemandeSelectionne.getId();
        String dateDepart = champDateDepart.getText();
        String heureDepart = champHeureDepart.getText();
        String lieuDepart = champLieuDepart.getText();
        String lieuDestination = champLieuDestination.getText();
        String dateRetour = champDateRetour.getText();
        String heureRetour = champHeureRetour.getText();
        String note = champNoteRDV.getText();
        MoyenPaiment moyenPaimentSelectionne = (MoyenPaiment) comboMoyenPaimentRDV.getSelectedItem();
        Integer idMoyenPaiment = moyenPaimentSelectionne.getId();
        String chaineSpinner = spinnerNbPersonnes.getValue().toString();
        Integer nbPersonne = Integer.parseInt(chaineSpinner);
        Double prix = Double.parseDouble(champPrix.getText());
        String etat = comboEtatRDV.getSelectedItem().toString();
        Client clientSelectionne = (Client) comboClient.getSelectedItem();
        Integer idClient = clientSelectionne.getId();
        Boolean boolEtat = false;
        if (etat.equals("Terminé")) {
            boolEtat = true;
        }

        RendezVous rdv = new RendezVous(nom, idTypeDemande, dateDepart, heureDepart, lieuDepart, lieuDestination, dateRetour, heureRetour, nbPersonne, prix, idMoyenPaiment, boolEtat, idClient, note);

        if (!(rdv.getNom()).equals("") && !(rdv.getType()).equals(0) && !(rdv.getDateDepart()).equals("") && !(rdv.getHeureDepart()).equals("") && !(rdv.getLieuDepart()).equals("")
                && !(rdv.getLieuDestination()).equals("") && !(rdv.getDateRetour()).equals("") && !(rdv.getHeureRetour()).equals("") && !(rdv.getNbPersonnes()).equals(0)
                && !(rdv.getPrix()).equals("") && !(rdv.getMoyenPaiment()).equals(0) && !(rdv.getEtat()).equals("") && !(rdv.getIdClient()).equals(0)) {
            if ((labelTitreCreerModifierRDV.getText()).equals("Création d'un Rendez-vous")) {//création d'un client
                connexionBase = connexionBase.recupInstance();
                Connection connexion = connexionBase.getConnexion();
                
                RdvDAO sujet = new RdvDAO();
                sujet.ajouterObservateur((MaJtable) tableClients);
                sujet.creerRdv(rdv);
                CreerModifierRDV.setVisible(false);

            } else {//modification d'un client

            }
        } else {//champ sont vide
            
            JOptionPane.showMessageDialog(MessageBienvenue, "Veuillez remplir correctement les champs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnValiderRDVActionPerformed

    private void comboTypeRdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeRdvActionPerformed

    }//GEN-LAST:event_comboTypeRdvActionPerformed

    private void champHeureRetourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champHeureRetourActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champHeureRetourActionPerformed

    private void comboEtatRDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEtatRDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboEtatRDVActionPerformed

    private void parametreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parametreActionPerformed


    }//GEN-LAST:event_parametreActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CreerTypeOuMoyenPaiment.setVisible(true);
        CreerTypeOuMoyenPaiment.setBounds(0, 0, 443, 181);
        CreerTypeOuMoyenPaiment.setLocationRelativeTo(null);
        CreerTypeOuMoyenPaiment.setTitle("Transport T'aou - Création D'un Type de Demande");
        labelTitreCreationTypeOuMoyenPaiment.setText("Création D'un Type de Demande");
        labelChampCreerTypeOuMoyenPaiment.setText("Type de Demande");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (labelTitreCreationTypeOuMoyenPaiment.getText().equals("Création D'un Type de Demande")) {
            //ajout d'un type de demande
            if (!(champCreerTypeOuMoyenPaiment.getText()).equals("")) {
                connexionBase = ConnexionBase.recupInstance();
                Connection connexion = connexionBase.getConnexion();
                //récupère le dernier id+1 pour ajouter a la suite

                try {
                    Statement st;
                    st = connexion.createStatement();
                    ResultSet resultat = st.executeQuery("SELECT MAX(id) FROM type_demande;");
                    resultat.next();
                    int id = resultat.getInt("max") + 1;

                    PreparedStatement pst = connexion.prepareStatement("Insert into type_demande values (?,?)");
                    pst.setInt(1, id);
                    pst.setString(2, champCreerTypeOuMoyenPaiment.getText());
                    pst.execute();
                    CreerTypeOuMoyenPaiment.setVisible(false);
                    //on actualise les combo
                    majCombos();
                    JOptionPane.showMessageDialog(MessageBienvenue, "Création d'un nouveau type de demande effectué avec succès");
                } catch (SQLException ex) {
                    Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(MessageBienvenue, "ERREUR!\nVeuillez remplir correctement le champ type de demande",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //ajout moyen paiment

            if (!(champCreerTypeOuMoyenPaiment.getText()).equals("")) {
                connexionBase = ConnexionBase.recupInstance();
                Connection connexion = connexionBase.getConnexion();
                //récupère le dernier id+1 pour ajouter a la suite

                try {
                    Statement st;
                    st = connexion.createStatement();
                    ResultSet resultat = st.executeQuery("SELECT MAX(id) FROM moyens_paiment;");
                    resultat.next();
                    int id = resultat.getInt("max") + 1;

                    PreparedStatement pst = connexion.prepareStatement("Insert into moyens_paiment values (?,?)");
                    pst.setInt(1, id);
                    pst.setString(2, champCreerTypeOuMoyenPaiment.getText());
                    pst.execute();
                    CreerTypeOuMoyenPaiment.setVisible(false);
                    //on actualise les combo
                    majCombos();
                    JOptionPane.showMessageDialog(MessageBienvenue, "Création d'un nouveau moyen de paiment effectué avec succès");
                } catch (SQLException ex) {
                    Logger.getLogger(fenetrePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(MessageBienvenue, "ERREUR!\nVeuillez remplir correctement le champ moyen de paiment",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_jButton1ActionPerformed
    }
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        CreerTypeOuMoyenPaiment.setVisible(true);
        CreerTypeOuMoyenPaiment.setBounds(0, 0, 443, 181);
        CreerTypeOuMoyenPaiment.setLocationRelativeTo(null);
        CreerTypeOuMoyenPaiment.setTitle("Transport T'aou - Création D'un Moyen de Paiment");
        labelTitreCreationTypeOuMoyenPaiment.setText("Création D'un Moyen de Paiment");
        labelChampCreerTypeOuMoyenPaiment.setText("Moyen de Paiment");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CreerTypeOuMoyenPaiment.setVisible(false);

    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton BtnNonConfirmation;
    private javax.swing.JButton BtnOuiConfirmation;
    private javax.swing.JFrame CreerModifierClient;
    private javax.swing.JFrame CreerModifierRDV;
    private javax.swing.JFrame CreerTypeOuMoyenPaiment;
    private javax.swing.JLabel LabelNomRDV;
    private javax.swing.JLabel LabelTypeRDV;
    private javax.swing.JOptionPane MessageBienvenue;
    private javax.swing.JDialog MessageConfirmation;
    private javax.swing.JLabel TexteMessageConfirmation;
    private javax.swing.JButton btnAnnule;
    private javax.swing.JButton btnAnnulerRDV;
    private javax.swing.JButton btnExportCSV;
    private javax.swing.JButton btnLog;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnNouveau;
    private javax.swing.JButton btnRechercher;
    private javax.swing.JButton btnSupprimer;
    private javax.swing.JButton btnValider;
    private javax.swing.JButton btnValiderRDV;
    private javax.swing.JTextField champAdresse;
    private javax.swing.JTextField champCreerTypeOuMoyenPaiment;
    private javax.swing.JFormattedTextField champDateDepart;
    private javax.swing.JFormattedTextField champDateRetour;
    private javax.swing.JFormattedTextField champHeureDepart;
    private javax.swing.JFormattedTextField champHeureRetour;
    private javax.swing.JTextField champLieuDepart;
    private javax.swing.JTextField champLieuDestination;
    private javax.swing.JTextField champMobile;
    private javax.swing.JPasswordField champMotDePasse;
    private javax.swing.JTextField champNom;
    private javax.swing.JTextField champNomRDV;
    private javax.swing.JTextField champNomUtilisateur;
    private javax.swing.JTextArea champNoteRDV;
    private javax.swing.JTextField champPrenom;
    private javax.swing.JTextField champPrix;
    private javax.swing.JTextField champRecherche;
    private javax.swing.JComboBox<String> comboClient;
    private javax.swing.JComboBox<String> comboEtatRDV;
    private javax.swing.JComboBox<String> comboMoyenPaimentRDV;
    private javax.swing.JComboBox<String> comboTypeRdv;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelAdresse;
    private javax.swing.JLabel labelChampCreerTypeOuMoyenPaiment;
    private javax.swing.JLabel labelMobile;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelPrenom;
    private javax.swing.JLabel labelTitreCreationTypeOuMoyenPaiment;
    private javax.swing.JLabel labelTitreCreerModifierClient;
    private javax.swing.JLabel labelTitreCreerModifierRDV;
    private javax.swing.JTabbedPane onglet;
    private javax.swing.JFrame pageAccueuil;
    private javax.swing.JMenu parametre;
    private javax.swing.JFileChooser selecteurFichier;
    private javax.swing.JSpinner spinnerNbPersonnes;
    private javax.swing.JTable tableClients;
    // End of variables declaration//GEN-END:variables

}
