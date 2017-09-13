/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.dao;

import com.pospuc.sisacad.model.Aluno;
import com.pospuc.sisacad.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author carlagraca
 */
public class AlunoDAO { 

    public void createAluno(Aluno aluno) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(aluno);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao cadastrar Aluno");
        } finally {
            session.flush();
            session.close();
        }
    }

    public Aluno getAluno(Integer matricula) {
        Aluno aluno = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            aluno = (Aluno) session.get(Aluno.class, new Integer(matricula));
        } finally {
            session.flush();
            session.close();
        }
        return aluno;
    }

    public void deleteAluno(Integer matricula) throws Exception {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            Aluno aluno = (Aluno) session.load(Aluno.class, new Integer(matricula));

            session.delete(aluno);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao excluir um aluno");
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Aluno> listAlunos() {
        List<Aluno> alunos = new ArrayList<Aluno>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            alunos = session.createQuery("from Aluno").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }

        return alunos;
    }

    public void updateAluno(Aluno aluno) throws Exception {
        Transaction trns = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(aluno);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            throw new Exception("Error ao atualizar aluno");
        } finally {
            session.flush();
            session.close();
        }
    }

    public String gerarNovaSenha() {
        String[] carct = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

        String senha = "";

        for (int x = 0; x < 10; x++) {
            int j = (int) (Math.random() * carct.length);
            senha += carct[j];

        }

        return senha;
    }

}
