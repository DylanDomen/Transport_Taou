/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transport.taou;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

/**
 *
 * @author dylan
 */
public class HashMDP {

    

    private static final String UNICODE_FORMAT = "UTF-8";
    
    /*public static void main(String[] args){//pour test
        String test = "123456";
        try {
            SecretKey cle = genereCle("AES");
            Cipher chiffrement;
            chiffrement = Cipher.getInstance("AES");
            
            byte[] texteEncrypter = encrypte(test,cle,chiffrement);
            String restexteEncrypter = new String(texteEncrypter);
            System.out.println(restexteEncrypter);
            String texteDecrypter = decrypte(texteEncrypter,cle,chiffrement);
            System.out.println(texteDecrypter); 
        } catch (Exception e) {
        }
    }*/
    
    public HashMDP() {
    }
        
    public static SecretKey genereCle(String typeEncrypte){
        
        try {
            
            KeyGenerator cle = KeyGenerator.getInstance(typeEncrypte);
            SecretKey maCle = cle.generateKey();
            
            return maCle;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashMDP.class.getName()).log(Level.SEVERE, null, ex);
            
            return null;
        }
    }
    
    public static byte[] encrypte(String chaineACrypter, SecretKey maCle, Cipher chiffrement){
        try {
            
            byte[] texte = chaineACrypter.getBytes(UNICODE_FORMAT);
            chiffrement.init(Cipher.ENCRYPT_MODE, maCle);
            byte[] texteEncrypter = chiffrement.doFinal(texte);
            
            return texteEncrypter;
            
        } catch (Exception e) {
            return null;
        }

    }
    
    public static String decrypte(byte[] texteEncrypter,SecretKey maCle, Cipher chiffrement){
    
        try {
            
            chiffrement.init(Cipher.DECRYPT_MODE, maCle);
            byte[] texteDecrypte = chiffrement.doFinal(texteEncrypter);
            String resultat = new String(texteDecrypte);
            
            return resultat;
            
        } catch (Exception e) {
            return null;
        }
    }
}
