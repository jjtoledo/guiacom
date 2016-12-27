package com.jose.guiacom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Empresa implements Serializable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("nome")
    public String nome;
    @SerializedName("segmento_id")
    public Integer segmento_id;
    @SerializedName("apresentacao")
    public String apresentacao;
    @SerializedName("nomeResponsavel")
    public String nomeResponsavel;
    @SerializedName("endereco")
    public String endereco;
    @SerializedName("numero")
    public Integer numero;
    @SerializedName("complemento")
    public String complemento;
    @SerializedName("bairro")
    public String bairro;
    @SerializedName("telefone1")
    public String telefone1;
    @SerializedName("telefone2")
    public String telefone2;
    @SerializedName("email")
    public String email;
    @SerializedName("site")
    public String site;
    @SerializedName("pendente")
    public Integer pendente;
    @SerializedName("tipoCadastro")
    public Integer tipoCadastro;
    @SerializedName("logo")
    public String logo;
    @SerializedName("cidade_id")
    public Integer cidade_id;
    @SerializedName("wantsPremium")
    public Integer wantsPremium;
    @SerializedName("cidade_nome")
    public String cidade_nome;
    @SerializedName("segmento_nome")
    public String segmento_nome;
}
