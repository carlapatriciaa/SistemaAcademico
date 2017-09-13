package com.pospuc.sisacad.model;
// Generated 22/08/2017 13:55:59 by Hibernate Tools 4.3.1

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * EnderecoAluno generated by hbm2java
 */
@Entity
public class EnderecoAluno implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;  

    @Id
    private Integer idAluno;
    private String logradouro;
    private Integer numero;
    private String comple;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public EnderecoAluno() {
    }

    public EnderecoAluno(Integer idAluno, String logradouro, Integer numero, String bairro, String cidade, String estado, String cep) {
        this.idAluno = idAluno;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public EnderecoAluno(Integer idAluno, String logradouro, Integer numero, String comple, String bairro, String cidade, String estado, String cep) {
        this.idAluno = idAluno;
        this.logradouro = logradouro;
        this.numero = numero;
        this.comple = comple;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Integer getIdAluno() {
        return this.idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComple() {
        return this.comple;
    }

    public void setComple(String comple) {
        this.comple = comple;
    }

    public String getBairro() {
        return this.bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return this.cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.idAluno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnderecoAluno other = (EnderecoAluno) obj;
        if (!Objects.equals(this.idAluno, other.idAluno)) {
            return false;
        }
        return true;
    }
    
    

}
