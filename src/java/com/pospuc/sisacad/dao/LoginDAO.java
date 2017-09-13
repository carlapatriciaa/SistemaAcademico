/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.dao;

import com.pospuc.sisacad.model.Usuario;

/**
 *
 * @author carlagraca
 */
public class LoginDAO {

    public interface LoginDao {

        public void inserir(Usuario usuario);

        public void atualiza(Usuario usuario);
    }

}
