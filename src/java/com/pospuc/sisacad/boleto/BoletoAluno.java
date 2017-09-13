/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pospuc.sisacad.boleto;

import com.pospuc.sisacad.model.Aluno;
import com.pospuc.sisacad.model.EnderecoAluno;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.jrimum.utilix.text.DateFormat;

/**
 *
 * @author carlagraca
 */
public class BoletoAluno {

    public Boleto boletoAluno(Aluno aluno, EnderecoAluno enderecoAluno, Double valorMat) {

        Boleto boleto = crieUmBoleto(aluno, enderecoAluno, valorMat);

        return boleto;
    }

    Boleto crieUmBoleto(Aluno aluno, EnderecoAluno enderecoAluno, Double valorMat) {

        ContaBancaria contaBancaria = crieUmaContaBancaria();

        Cedente cedente = crieUmCedente();

        Sacado sacado = crieUmSacado(aluno, enderecoAluno);

        Titulo titulo = crieOsDadosDoNovoTitulo(new Titulo(contaBancaria, sacado, cedente), valorMat);

        Boleto boleto = crieOsDadosDoNovoBoleto(new Boleto(titulo));

        return boleto;

    }

    final ContaBancaria crieUmaContaBancaria() {

        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(77465, "9"));
        contaBancaria.setCarteira(new Carteira(06));
        contaBancaria.setAgencia(new Agencia(2000));

        return contaBancaria;
    }

    final Cedente crieUmCedente() {

        return new Cedente("Instituição de Ensino", "17.178.195/0014-81");
    }

    final Sacado crieUmSacado(Aluno aluno, EnderecoAluno enderecoAluno) {

        Sacado sacado = new Sacado(aluno.getNome(), aluno.getCpf());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.valueOfSigla(enderecoAluno.getEstado()));
        enderecoSac.setLocalidade(enderecoAluno.getCidade());
        enderecoSac.setCep(new CEP(enderecoAluno.getCep()));
        enderecoSac.setBairro(enderecoAluno.getBairro());
        enderecoSac.setLogradouro(enderecoAluno.getLogradouro());
        enderecoSac.setNumero(Integer.toString(enderecoAluno.getNumero()));
        sacado.addEndereco(enderecoSac);

        return sacado;
    }

    final Titulo crieOsDadosDoNovoTitulo(Titulo titulo, Double valorMat) {
        String dataDoc = dataDocumento();
        String dataVenc = dataVencimento();

        titulo.setNumeroDoDocumento("06");
        titulo.setNossoNumero("00275877736");
        titulo.setDigitoDoNossoNumero("1");
        titulo.setValor(BigDecimal.valueOf(valorMat));

        titulo.setDataDoDocumento(DateFormat.DDMMYYYY_B.parse(dataDoc));

        titulo.setDataDoVencimento(DateFormat.DDMMYYYY_B.parse(dataVenc));

        titulo.setTipoDeDocumento(TipoDeTitulo.ME_MENSALIDADE_ESCOLAR);
        titulo.setAceite(Aceite.A);
        titulo.setDesconto(new BigDecimal(0.05));
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        return titulo;
    }

    final Boleto crieOsDadosDoNovoBoleto(Boleto boleto) {

        String dataMaxima = dataMaxima();

        boleto.setLocalPagamento("Até o vencimento, pagável em qualquer banco. Após, somente no Banco Bradesco S.A");
        boleto.setInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor cobrado não é o esperado, aproveite o DESCONTÃO!");
        boleto.setInstrucao1("Receber até " + dataMaxima + ", sem encargos");

        return boleto;
    }

    private String dataVencimento() {

        Calendar cal = GregorianCalendar.getInstance(Locale.ROOT);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String dataVencimento;

        cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), 1);

        cal.add(Calendar.MONTH, 1);

        if (cal.get(cal.MONTH) == 1) {
            cal.add(Calendar.YEAR, 1);
        }

        dataVencimento = s.format(cal.getTime());

        return dataVencimento;
    }

    private String dataMaxima() {

        Calendar cal = GregorianCalendar.getInstance(Locale.ROOT);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String dataMaxima;

        cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), 1);

        cal.add(Calendar.MONTH, 1);

        if (cal.get(cal.MONTH) == 1) {
            cal.add(Calendar.YEAR, 1);
        }

        cal.add(Calendar.DATE, 6);

        if (cal.get(cal.DAY_OF_WEEK) == cal.SUNDAY) {
            cal.add(Calendar.DATE, 1);
        }

        if (cal.get(cal.DAY_OF_WEEK) == cal.SATURDAY) {
            cal.add(Calendar.DATE, 2);
        }

        dataMaxima = s.format(cal.getTime());

        return dataMaxima;
    }

    public String dataDocumento() {

        Calendar cal = GregorianCalendar.getInstance(Locale.ROOT);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String dataDocumento;

        dataDocumento = s.format(cal.getTime());

        return dataDocumento;

    }
}
