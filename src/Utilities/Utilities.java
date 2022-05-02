/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.TreeSet;

/**
 *
 * @author tomas
 */
public class Utilities {

    public boolean checkAllowBank(TreeSet<String> b) {
        //El lobo y la oveja se encuentra juntos sin el granjero
        if (b.contains("L") && b.contains("O") && (b.contains("G") == false)) {
            return false;
        }
        //La oveja y la col juntas sin el granjero
        if (b.contains("O") && b.contains("C") && (b.contains("G") == false)) {
            return false;
        }
        return true;
    }

    /**
     * Metodo public que verifica si un estado cumple las restricciones Los
     * estados de rechazo son cuando no cumplen las restricciones
     *
     * Retorna true si un estado esta permitido, falso en caso contrario
     */
    public boolean isAllow(TreeSet<String> left, TreeSet<String> right) {
        if (checkAllowBank(left) && checkAllowBank(right)) {
            return true;
        } else {
            return false;
        }
    }
}
