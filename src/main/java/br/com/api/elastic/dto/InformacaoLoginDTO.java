package br.com.api.elastic.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class InformacaoLoginDTO {

	@JsonProperty("LOGIN")
	private String login;
	
	@JsonProperty("IND_SITUACAO")
	private String indSituacao;
}
