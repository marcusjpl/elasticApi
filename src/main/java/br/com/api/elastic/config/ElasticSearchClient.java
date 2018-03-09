package br.com.api.elastic.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.ejb.Stateless;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.jboss.logging.Logger;

@Stateless
public class ElasticSearchClient {
	
	public String ELASTIC_PROPERTIES = "/elasticSearch.properties";

	private static final Logger log = Logger.getLogger(ElasticSearchClient.class);
	private TransportClient client = null;

	private Properties elasticPro = null;

	public ElasticSearchClient() {
		try {
			elasticPro = new Properties();
			elasticPro.load(ElasticSearchClient.class.getResourceAsStream(ELASTIC_PROPERTIES));
			log.info(elasticPro.getProperty("host"));
		} catch (IOException ex) {
			log.info("Exception occurred while load elastic properties : " + ex, ex);
		}
	}

	public TransportClient getInstant() {
		if (client == null) {
			client = getElasticClient();
		}
		return client;
	}

	private TransportClient getElasticClient() {
		try {

			Settings settings = Settings.builder().put("cluster.name", elasticPro.getProperty("cluster"))
					.put("client.transport.sniff", Boolean.valueOf(elasticPro.getProperty("transport.sniff"))).build();

			setClient(new PreBuiltTransportClient(settings).addTransportAddress(
					new TransportAddress(InetAddress.getByName(elasticPro.getProperty("host")), Integer.valueOf(elasticPro.getProperty("port")))));

		} catch (UnknownHostException ex) {
			log.error("Exception occurred while getting Client : " + ex, ex);
		}
		return getClient();
	}

	public void closeClient(TransportClient client) {
		if (client != null) {
			client.close();
		}
	}

	public TransportClient getClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}
}
