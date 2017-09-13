/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.pospuc.sisacad.model.EnderecoAluno;
import com.pospuc.sisacad.util.HibernateUtil;

/**
 *
 * @author carlagraca
 */
public class EnderecoDAO {

    public void createEndereco(EnderecoAluno endereco) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(endereco);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao criar endereço");
        } finally {
            session.flush();
            session.close();
        }
    }

    public EnderecoAluno getEndereco(Integer idAluno) {
        EnderecoAluno endereco = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            endereco = (EnderecoAluno) session.get(EnderecoAluno.class, new Integer(idAluno));
        } finally {
            session.flush();
            session.close();
        }
        return endereco;
    }

    public void deleteEndereco(Integer idAluno) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            EnderecoAluno endereco = (EnderecoAluno) session.load(EnderecoAluno.class, new Integer(idAluno));

            session.delete(endereco);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao excluir um endereço");
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<EnderecoAluno> listEnderecos() {
        List<EnderecoAluno> enderecos = new ArrayList<EnderecoAluno>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            enderecos = session.createQuery("from Endereco").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }

        return enderecos;
    }

    public void updateEndereco(EnderecoAluno endereco) throws Exception {
        Transaction trns = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(endereco);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao atualizar endereço");
        } finally {
            session.flush();
            session.close();
        }
    }

}
