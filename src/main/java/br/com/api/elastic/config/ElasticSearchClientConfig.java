package br.com.api.elastic.config;

import java.io.IOException;
import java.util.Properties;

import javax.ejb.Stateless;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.jboss.logging.Logger;

@Stateless
public class ElasticSearchClientConfig {

	public String ELASTIC_PROPERTIES = "/elasticSearch.properties";

	private static final Logger log = Logger.getLogger(ElasticSearchClientConfig.class);
	
	private RestHighLevelClient restClient = null;

	private Properties elasticProperties = null;

	public ElasticSearchClientConfig() {
		try {
			elasticProperties = new Properties();
			elasticProperties.load(ElasticSearchClientConfig.class.getResourceAsStream(ELASTIC_PROPERTIES));
			log.info(elasticProperties.getProperty("host"));
		} catch (IOException ex) {
			log.error("Erro ao executar chamada do ElasticSearch..", ex.getMessage(), ex);
		}
	}

	public RestHighLevelClient getInstance() {

		if (restClient == null) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			UsernamePasswordCredentials auth = new UsernamePasswordCredentials(elasticProperties.getProperty("user"), elasticProperties.getProperty("password"));
			credentialsProvider.setCredentials(AuthScope.ANY, auth);
			
			HttpHost hosts = new HttpHost(elasticProperties.getProperty("host"), Integer.valueOf(elasticProperties.getProperty("port")));
			RestHighLevelClient builder = new RestHighLevelClient(RestClient.builder(hosts)
					.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
						@Override
						public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
							return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
						}
					}));
			restClient = builder;
		}
		return restClient;
	}

	public void closeClient() throws IOException {
		if (restClient != null) {
			restClient.close();
		}
	}

}
