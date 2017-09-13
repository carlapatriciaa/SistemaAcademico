/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.email;

import com.pospuc.sisacad.model.Aluno;
import java.io.File;
import javax.swing.JOptionPane;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 *
 * @author carlagraca
 */
public class EnviaEmail { 

    final EmailAttachment attachment = new EmailAttachment();
    final MultiPartEmail email = new MultiPartEmail();
    private String mensagem;
    private File file;
    
    public EnviaEmail() {
    }

    public Boolean enviaEmail(Aluno aluno, String senha) {

        mensagem = mensagem(aluno, senha);

        file = new File("/Boletos/" + aluno.getUsuario() +
                        "_" + aluno.getNome() + ".pdf");
        attachment.setPath(file.getPath()); //caminho da imagem
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Boleto Matrícula");
        attachment.setName(file.getName());

        try {
            email.setDebug(true);
            email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("gabriel.carla.patricia@gmail.com", "Cg04102014"));

            email.getMailSession().getProperties().put("mail.smtp.auth", "true");
            email.getMailSession().getProperties().put("mail.debug", "true");
            email.getMailSession().getProperties().put("mail.smtp.port", "587");
            email.getMailSession().getProperties().put("mail.smtp.socketFactory.port", "587");
            email.getMailSession().getProperties().put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            email.getMailSession().getProperties().put("mail.smtp.socketFactory.fallback", "false");
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");

            email.addTo(aluno.getEmail(), aluno.getNome()); //destinatario
            email.setFrom("gabriel.carla.patricia@gmail.com", "Instituição de Ensino"); //remetente
            email.setSubject("Instituição de Ensino - Matrícula"); //Assunto
            email.setMsg(mensagem); //conteudo do e-mail
            email.attach(attachment); // adiciona o anexo à mensagem

            email.send();// envia o e-mail
            return true;

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage(), "Informação do Sistema", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Email.class.getName()).log(Priority.ERROR, null, e);
            return false;
        }

    }

    public String mensagem(Aluno aluno, String senha) {

        mensagem = "Bem Vindo " + aluno.getNome() + "!!! \n"
                + "Seguem seus dados de acesso ao Sistema Acadêmico: \n"
                + "Usuário: " + aluno.getUsuario() + "\n"
                + "Senha: " + senha + "\n"
                + "\n"
                + "O boleto referente ao valor de sua matrícula está anexo. \n"
                + "\n"
                + "Obrigado!\n"
                + "Instituição de Ensino";
        return mensagem;
    }

}
