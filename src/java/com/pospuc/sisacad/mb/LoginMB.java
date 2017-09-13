/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.mb;

import com.pospuc.sisacad.dao.UsuarioDAO;
import com.pospuc.sisacad.model.Usuario;
import com.pospuc.sisacad.util.Util;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author carlagraca
 */
@ManagedBean(name = "mbUsu")
@SessionScoped
public class LoginMB implements Serializable {

    private static final long serialVersionUID = 1L;

    final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario;
    private String login, senha;

//Realiza o login caso de tudo certo
    public String doLogin() {

        usuario = usuarioDAO.validaUsuario(login, senha);

        if (usuario == null) {
            
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Usuário não encontrado!",
                            "Erro no Login!"));

            return "/login.xhtml?faces-redirect=true";
        } else {

            HttpSession session = Util.getSession();
            session.setAttribute("usuario", usuario);

            switch (usuario.getIdPerfil()) {
                case "1":

                    return "/Admin/principalAdmin.xhtml?faces-redirect=true";

                case "2":

                    return "/Secretario/principalSecretario.xhtml?faces-redirect=true";

                case "3":

                    return "/Professor/principalProfessor.xhtml?faces-redirect=true";

                case "4":

                    return "/Aluno/principalAluno.xhtml?faces-redirect=true";
            }
        }
        
        return null;
    }

    //Realiza o logout do usuário logado
    public String doLogout() {

        HttpSession session = Util.getSession();
        session.invalidate();
        return "/login.xhtml?faces-redirect=true";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
