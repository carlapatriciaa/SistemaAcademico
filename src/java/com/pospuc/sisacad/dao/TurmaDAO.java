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
import com.pospuc.sisacad.model.Turma;
import com.pospuc.sisacad.util.HibernateUtil;
import org.hibernate.Query;

/**
 *
 * @author carlagraca
 */
public class TurmaDAO {

    public Turma getTurma(Integer id) {
        Turma turma = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            turma = (Turma) session.get(Turma.class, new Integer(id));
        } finally {
            session.flush();
            session.close();
        }
        return turma;
    }

    @SuppressWarnings("unchecked")
    public List<Turma> getTurmas(int idCurso) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List turmas = null;
        try {
            trns = session.beginTransaction();
            Query qry = session.createQuery("from Turma as t where t.idCurso=:idCurso");
            qry.setParameter("idCurso", idCurso);
            turmas = qry.list();
            trns.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turmas;
    }

    @SuppressWarnings("unchecked")
    public List<Turma> getTurmas() {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List turmas = null;
        try {
            trns = session.beginTransaction();
            Query qry = session.createQuery("from Turma");
            turmas = qry.list();
            trns.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turmas;
    }

}
