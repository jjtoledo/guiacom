package com.jose.guiacom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jose on 27/12/2016.
 */

public class Foto implements Serializable {
    @SerializedName("foto")
    public String foto;
    @SerializedName("descricao")
    public String descricao;
}
