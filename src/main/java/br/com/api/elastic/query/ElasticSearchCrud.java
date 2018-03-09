package br.com.api.elastic.query;

public interface ElasticSearchCrud {

    public void CreateDocument();

    public void getDocument();

    public void updateDocument();

    public void deleteDocument();

    public void getMultipleDocument();

    public void insertMultipleDocument();

    public void searchDocument();

	public void getDocumentUsingScroll();

	public String searchAll();

	public String buscaPorUsuario(String usuario);

	public String obterDebitoUsuario(String string);

}
