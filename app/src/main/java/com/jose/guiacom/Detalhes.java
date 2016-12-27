package com.jose.guiacom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jose on 27/12/2016.
 */

public class Detalhes implements Serializable {
    @SerializedName("foto_pref")
    public String foto_pref;
    @SerializedName("descricao")
    public String descricao;
}
