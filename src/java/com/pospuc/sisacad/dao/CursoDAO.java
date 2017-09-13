/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.pospuc.sisacad.model.Curso;
import com.pospuc.sisacad.util.HibernateUtil;
import org.hibernate.Query;

/**
 *
 * @author carlagraca
 */
public class CursoDAO {

    public Curso getCurso(Integer id) {
        Curso curso = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            curso = (Curso) session.get(Curso.class, new Integer(id));
        } finally {
            session.flush();
            session.close();
        }
        return curso;
    }

    @SuppressWarnings("unchecked")
    public List<Curso> getCursos() {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List cursos = null;
        try {
            trns = session.beginTransaction();
            Query qry = session.createQuery("from Curso");
            cursos = qry.list();
            trns.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursos;
    }

}
