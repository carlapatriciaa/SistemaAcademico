/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.dao;

import com.pospuc.sisacad.model.Usuario;
import com.pospuc.sisacad.util.HibernateUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction; 

/**
 *
 * @author carlagraca
 */
public class UsuarioDAO {

    public void createUsuario(Usuario usuario) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao criar usuário");
        } finally {
            session.flush();
            session.close();
        }
    }

    public Usuario validaUsuario(String login, String senha) {

        String qry = "from Usuario as u where u.login=:login and u.senha=:senha";
        String senha2 = convertStringToMd5(senha);
        Usuario usuario = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            Query query = session.createQuery(qry);
            query.setParameter("login", login);
            query.setParameter("senha", senha2);
            usuario = (Usuario) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return usuario;
    }

    public void deleteUsuario(Integer id) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            Usuario usuario = (Usuario) session.load(Usuario.class, new Integer(id));

            session.delete(usuario);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao excluir um usuário");
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listUsuarios() {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            usuarios = session.createQuery("from Usuario").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }

        return usuarios;
    }

    public void updateUsuario(Usuario usuario) throws Exception {
        Transaction trns = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(usuario);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao atualizar usuário");
        } finally {
            session.flush();
            session.close();
        }
    }

    public String convertStringToMd5(String valor) {
        MessageDigest mDigest;
        try {
            //Instanciamos o nosso HASH MD5, poderíamos usar outro como
            //SHA, por exemplo, mas optamos por MD5.
            mDigest = MessageDigest.getInstance("MD5");

            //Convert a String valor para um array de bytes em MD5
            byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));

            //Convertemos os bytes para hexadecimal, assim podemos salvar
            //no banco para posterior comparação se senhas
            StringBuffer sb = new StringBuffer();
            for (byte b : valorMD5) {
                sb.append(Integer.toHexString((b & 0xFF)
                        | 0x100).substring(1, 3));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
