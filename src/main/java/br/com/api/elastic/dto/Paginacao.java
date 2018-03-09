package br.com.api.elastic.dto;


public class Paginacao {

	private static final int DEFAULT_PAGINA = 1;
	private static final int DEFAULT_LIMITE = 100;
	
	private Integer pagina = 1;
	
	private Integer limite = 100;
	
	public Paginacao(){}
	public Paginacao(int pagina, int limite) {
		this.pagina = pagina;
		this.limite = limite;
	}
	
	public Integer getPagina() {
		if (pagina == null) {
			return DEFAULT_PAGINA;
		}
		else
			return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	public Integer getLimite() {
		if (limite == null) {
			return DEFAULT_LIMITE;
		}
		else
			return limite;
	}
	public void setLimite(Integer limite) {
		this.limite = limite;
	}
}
