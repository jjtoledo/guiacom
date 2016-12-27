package com.jose.guiacom;

import com.google.gson.annotations.SerializedName;

public class Cidade {
    @SerializedName("id")
    public Integer id;
    @SerializedName("nome")
    public String nome;
    @SerializedName("estado_id")
    public String estado_id;
}
