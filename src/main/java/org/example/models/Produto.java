package org.example.models;

import java.math.BigDecimal;

public class Produto {
    private String nome;
    private String dadosValores;
    private BigDecimal valorAVista;
    private BigDecimal valorAPrazo;
    private int parcelas;

    public Produto(String nome, String dadosValores) {
        this.nome = nome;
        this.dadosValores = dadosValores;
        this.parcelas =  getParcelas(dadosValores);
        this.valorAVista  = getValorAVista(dadosValores);
        this.valorAPrazo = getValorAPrazo(dadosValores);
    }

    public BigDecimal getValorAVista() {
        return valorAVista;
    }

    public BigDecimal getValorAPrazo() {
        return valorAPrazo;
    }

    public int getParcelas() {
        return parcelas;
    }

    public String getDadosValores() {
        return dadosValores;
    }

    public void setDadosValores(String dadosValores) {
        this.dadosValores = dadosValores;
    }

    public BigDecimal getValorAVista(String dadosValores) {
        String valorString = dadosValores.substring(dadosValores.indexOf("$"), dadosValores.indexOf("\n")).replace("$","")
                .replaceAll("\\.","")+
                "."
                +dadosValores.substring(dadosValores.indexOf('\n')+1, dadosValores.indexOf('\n')+3).trim();
        System.out.println(valorString);
        return new BigDecimal(valorString);
    }

    public void setValorAVista(BigDecimal valorAVista) {
        this.valorAVista = valorAVista;
    }

    public BigDecimal getValorAPrazo(String dadosValores) {
        if(this.parcelas ==0)
            return BigDecimal.ZERO;

        return new BigDecimal(dadosValores.substring(dadosValores.lastIndexOf('$')+2, dadosValores.lastIndexOf(','))
                .replaceAll("\\.","").replaceAll(",","\\.")).multiply(new BigDecimal(this.parcelas));
    }

    public void setValorAPrazo(BigDecimal valorAPrazo) {

        this.valorAPrazo = valorAPrazo;
    }

    public int getParcelas(String dadosValores) {
        if(dadosValores.indexOf("até ")>0){
            return Integer.parseInt(dadosValores.substring(dadosValores.indexOf("até ") + 4, dadosValores.indexOf("x de")));
        }
        return 0;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Produto\n{" +
                "\nnome='" + nome + '\'' +
                ",\n dadosValores='" + dadosValores + '\'' +
                ",\n valorAVista=" + valorAVista +
                ",\n valorAPrazo=" + valorAPrazo +
                ",\n parcelas=" + parcelas +
                "\n}";
    }
}
