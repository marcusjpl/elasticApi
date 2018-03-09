package br.com.api.elastic.dto;



import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsultaDadosDTO<T> {
	
	private String visao;
	
	private String ordenacao;
	
	private T parametros;
	
	private Paginacao paginacao;
	
	@JsonIgnore
	private HashMap<String, Object> mapaParametros = new HashMap<String, Object>();
	
	public String getVisao() {
		return visao;
	}
	public void setVisao(String visao) {
		this.visao = visao;
	}
	public Paginacao getPaginacao() {
		return paginacao;
	}
	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	public void setMapaParametros(HashMap<String, Object> mapaParametros) {
		this.mapaParametros = mapaParametros;
	}
	public HashMap<String, Object> getMapaParametros() {
		return mapaParametros;
	}
	public T getParametros() {
		return parametros;
	}
	
	@SuppressWarnings("unchecked")
	public void setParametros(T parametros) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(parametros);
			this.mapaParametros = mapper.readValue(jsonInString, HashMap.class);
			this.parametros = parametros;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getOrdenacao() {
		return ordenacao;
	}
	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
}