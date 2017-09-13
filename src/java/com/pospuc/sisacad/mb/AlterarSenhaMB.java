/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.mb;

import com.pospuc.sisacad.dao.UsuarioDAO;
import com.pospuc.sisacad.model.Usuario;
import com.pospuc.sisacad.util.Util;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author carlagraca
 */
@ManagedBean(name = "mbAltSnh")
@SessionScoped
public class AlterarSenhaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario, usuarioSession;
    private String senha, senhaAntiga;

    public String alteraSenha() {

        HttpSession session = Util.getSession();
        usuarioSession = (Usuario) session.getAttribute("usuario");

        usuario = usuarioDAO.validaUsuario(usuarioSession.getLogin(), senhaAntiga);

        if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Usuário não encontrado!",
                            "Senha Incorreta!"));

            return "/alterarSenha.xhtml?faces-redirect=true";
        } else {

            usuario.setSenha(usuarioDAO.convertStringToMd5(senha));
            try {

                usuarioDAO.updateUsuario(usuario);
            } catch (Exception e) {
            }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }
    
    

}
