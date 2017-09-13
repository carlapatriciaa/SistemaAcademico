/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.mb;

import br.com.correios.bsb.sigep.master.bean.cliente.EnderecoERP;
import java.io.File;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.servlet.http.HttpServletResponse;

import com.pospuc.sisacad.boleto.BoletoAluno;
import com.pospuc.sisacad.cep.ConsultaCep; 
import com.pospuc.sisacad.dao.AlunoDAO;
import com.pospuc.sisacad.dao.CursoDAO;
import com.pospuc.sisacad.dao.EnderecoDAO;
import com.pospuc.sisacad.dao.TurmaDAO;
import com.pospuc.sisacad.dao.UsuarioDAO;
import com.pospuc.sisacad.email.EnviaEmail;
import com.pospuc.sisacad.model.Aluno;
import com.pospuc.sisacad.model.Curso;
import com.pospuc.sisacad.model.EnderecoAluno;
import com.pospuc.sisacad.model.Turma;
import com.pospuc.sisacad.model.Usuario;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;

/**
 *
 * @author carlagraca
 */
@ManagedBean(name = "mbMatALu")
@ViewScoped
public class MatriculaAlunoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    final AlunoDAO alunoDAO = new AlunoDAO();
    final EnderecoDAO enderecoDAO = new EnderecoDAO();
    final UsuarioDAO usuarioDAO = new UsuarioDAO();
    final CursoDAO cursoDAO = new CursoDAO();
    final TurmaDAO turmaDAO = new TurmaDAO();
    private Aluno aluno = new Aluno();
    final EnviaEmail enviaEmail = new EnviaEmail();
    final ConsultaCep consultaCep = new ConsultaCep();
    private EnderecoAluno endereco = new EnderecoAluno();
    private Usuario usuario = new Usuario();
    private BoletoAluno boletoAluno = new BoletoAluno();
    private Double valorMat;
    private HttpServletResponse response;
    private OutputStream out;
    private File arquivoPdf;
    private Boolean email;
    private EnderecoERP enderecoErp;
    private int curso;
    private int turma;
    private SortedMap<Integer, SortedMap<String, Integer>> data = new TreeMap<Integer, SortedMap<String, Integer>>();
    private SortedMap<String, Integer> cursos;
    private SortedMap<String, Integer> turmas;

    @PostConstruct
    public void curso() {

        Curso cursoAux;
        Turma turmaAux;

        cursos = new TreeMap<String, Integer>();

        SortedMap<String, Integer> map = new TreeMap<String, Integer>();
        for (Iterator iter = cursoDAO.getCursos().iterator(); iter.hasNext();) {
            cursoAux = (Curso) iter.next();
            //armazenando os cursos encontrados no bd em um map
            cursos.put(cursoAux.getNome(), cursoAux.getId());

            map = new TreeMap<String, Integer>();
            for (Iterator iter2 = turmaDAO.getTurmas(cursoAux.getId()).iterator(); iter2.hasNext();) {
                turmaAux = (Turma) iter2.next();
                //armazenando as turmas encontradas no bd em um map
                map.put(turmaAux.getNome(), turmaAux.getId());
            }
            data.put(cursoAux.getId(), map);
        }

    }

    public void onCursoChange() {

        if (curso != 0) {
            turmas = data.get(curso);
        } else {
            turmas = new TreeMap<String, Integer>();
        }
    }

    public String finaliza() throws Exception {

        String login;
        String senha;
        Boleto boleto;
        Boolean alunoCriado;
        Boolean alunoAlterado;
        Boolean enderecoCriado;
        Boolean usuarioCriado;

        aluno.setTurma(turma);

        alunoCriado = createAluno(aluno);

        if (alunoCriado == true) {
            endereco.setIdAluno(aluno.getMatricula());
            enderecoCriado = createEndereco(endereco);

            login = "A" + aluno.getMatricula();

            aluno.setUsuario(login);

            alunoAlterado = alteraAluno(aluno);

            usuario.setLogin(login);
            senha = alunoDAO.gerarNovaSenha();
            usuario.setSenha(usuarioDAO.convertStringToMd5(senha));
            usuario.setIdPerfil("4");

            usuarioCriado = createUsuario(usuario);

            if (valorMat != 0) {

                boleto = boletoAluno.boletoAluno(aluno, endereco, valorMat);

                BoletoViewer boletoViewer = new BoletoViewer(boleto);

                byte[] pdfAsBytes = boletoViewer.getPdfAsByteArray();
                
                arquivoPdf = boletoViewer.getPdfAsFile("/Boletos/" + aluno.getUsuario() +
                        "_" + aluno.getNome() + ".pdf");
            }

            if (enderecoCriado == true && usuarioCriado == true) {

                email = enviaEmail.enviaEmail(aluno, senha);

                if (email == true) {
                    return "/Secretario/sucesso.xhtml?faces-redirect=true";

                } else {
                    return "/Secretario/erro.xhtml?faces-redirect=true";
                }
            }

            return "/Secretario/sucesso.xhtml?faces-redirect=true";
        }
        return "/Secretario/erro.xhtml?faces-redirect=true";
    }

    private Boolean createAluno(Aluno aluno) {

        try {

            alunoDAO.createAluno(aluno);
            if (aluno == null) {
                aluno = new Aluno();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro nos dados cadastrais do Aluno!",
                                "Erro na Matrícula!"));
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean alteraAluno(Aluno aluno) {

        try {

            alunoDAO.updateAluno(aluno);
            if (aluno == null) {
                aluno = new Aluno();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro nos dados cadastrais do Aluno!",
                                "Erro na Matrícula!"));
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean createEndereco(EnderecoAluno endereco) {

        try {
            enderecoDAO.createEndereco(endereco);
            if (endereco == null) {
                endereco = new EnderecoAluno();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no endereço do Aluno!",
                                "Erro na Matrícula!"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean createUsuario(Usuario usuario) {

        try {
            usuarioDAO.createUsuario(usuario);
            if (usuario == null) {
                usuario = new Usuario();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao criar usuário e senha!",
                                "Erro na Matrícula!"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void buscaEndereco(String cep) {

        try {
            enderecoErp = consultaCep.consultaCEP(cep);

            endereco.setLogradouro(enderecoErp.getEnd());
            endereco.setBairro(enderecoErp.getBairro());
            endereco.setCidade(enderecoErp.getCidade());
            endereco.setEstado(enderecoErp.getUf());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mostreBoletoNaTela(File arquivoBoleto) {

        try {
            java.awt.Desktop.getDesktop().open(arquivoBoleto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public EnderecoAluno getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoAluno endereco) {
        this.endereco = endereco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BoletoAluno getBoletoAluno() {
        return boletoAluno;
    }

    public void setBoletoAluno(BoletoAluno boletoAluno) {
        this.boletoAluno = boletoAluno;
    }

    public Double getValorMat() {
        return valorMat;
    }

    public void setValorMat(Double valorMat) {
        this.valorMat = valorMat;
    }

    public Map<String, Integer> getCursos() {
        return cursos;
    }

    public Map<String, Integer> getTurmas() {
        return turmas;
    }

    public SortedMap<Integer, SortedMap<String, Integer>> getData() {
        return data;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public int getTurma() {
        return turma;
    }

    public void setTurma(int turma) {
        this.turma = turma;
    }

}
