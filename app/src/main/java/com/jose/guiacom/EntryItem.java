package com.jose.guiacom;


public class EntryItem implements Item{

	public final String nome;
	public final String cidade;
	public final String telefone;

	public EntryItem(String nome, String cidade, String telefone) {
		this.nome = nome;
		this.cidade = cidade;
		this.telefone = telefone;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}
}
