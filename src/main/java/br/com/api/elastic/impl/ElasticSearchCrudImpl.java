package br.com.api.elastic.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.unit.TimeValue;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jboss.logging.Logger;

import br.com.api.elastic.config.ElasticSearchClient;
import br.com.api.elastic.query.ElasticSearchCrud;


public class ElasticSearchCrudImpl implements ElasticSearchCrud {

	private static final Logger log = Logger.getLogger(ElasticSearchCrudImpl.class);
	private ElasticSearchClient ESclient = null;
	private TransportClient client = null;

	public ElasticSearchCrudImpl() {
		ESclient = new ElasticSearchClient();
	}

	/**
	 * This method Create the Index and insert the document(s)
	 */
	@Override
	public void CreateDocument() {

		try {
			client = ESclient.getInstant();
			IndexResponse response = client.prepareIndex("ecad", "teste", "2")
					.setSource(jsonBuilder().startObject().field("name", "MARCUS").endObject()).get();
			if (response != null) {
				String _index = response.getIndex();
				String _type = response.getType();
				String _id = response.getId();
				long _version = response.getVersion();
				RestStatus status = response.status();
				log.info("Index has been created successfully with Index: " + _index + " / Type: " + _type + "ID: "
						+ _id);
			}
		} catch (IOException ex) {
			log.error("Exception occurred while Insert Index : " + ex, ex);
		}
	}

	/**
	 * This method get the matched document
	 */
	@Override
	public void getDocument() {
		try {
			client = ESclient.getInstant();
			GetResponse response = client.prepareGet("school", "tytenthpe", "1").get();
			if (response != null) {
				Map<String, DocumentField> FieldsMap = response.getFields();
				log.info("Response Data : " + FieldsMap.toString());
			}
		} catch (Exception ex) {
			log.error("Exception occurred while get Document : " + ex, ex);
		}
	}

	/**
	 * This method delete the matched Document
	 */
	@Override
	public void deleteDocument() {
		try {
			client = ESclient.getInstant();
			DeleteResponse deleteResponse = client.prepareDelete("school", "tenth", "1")
					// .setOperationThreaded(false)
					.get();
			if (deleteResponse != null) {
				deleteResponse.status();
				deleteResponse.toString();
				log.info("Document has been deleted...");
			}
		} catch (Exception ex) {
			log.error("Exception occurred while delete Document : " + ex, ex);
		}
	}

	/**
	 * This method updated the matched Document
	 */
	@Override
	public void updateDocument() {
		try {
			client = ESclient.getInstant();
			UpdateRequest updateReguest = new UpdateRequest("school", "tenth", "1")
					.doc(jsonBuilder().startObject().field("name", "Sundar S").endObject());
			UpdateResponse updateResponse = client.update(updateReguest).get();
			if (updateResponse != null) {
				updateResponse.getGetResult();
				log.info("Index has been updated successfully...");
			}
		} catch (IOException | InterruptedException | ExecutionException ex) {
			log.error("Exception occurred while update Document : " + ex, ex);
		}
	}

	/**
	 * This method get the multiple Documents
	 */
	@Override
	public void getMultipleDocument() {
		try {
			client = ESclient.getInstant();
			MultiGetResponse multipleItems = client.prepareMultiGet().add("school", "tenth", "1")
					.add("school", "nineth", "1", "2", "3", "4").add("college", "be", "1").get();

			multipleItems.forEach(multipleItem -> {
				GetResponse response = multipleItem.getResponse();
				if (response.isExists()) {
					String json = response.getSourceAsString();
					log.info("Respense Data : " + json);
				}
			});
		} catch (Exception ex) {
			log.error("Exception occurred while get Multiple Document : " + ex, ex);
		}
	}

	/**
	 * This method insert the more than one Document at a time
	 */
	@Override
	public void insertMultipleDocument() {
		try {
			client = ESclient.getInstant();
			BulkRequestBuilder bulkDocument = client.prepareBulk();

			// either use client#prepare, or use Requests# to directly build index/delete
			// requests
			bulkDocument.add(client.prepareIndex("school", "tenth", "1").setSource(jsonBuilder().startObject()
					.field("name", "Sundar").field("age", "23").field("gender", "Male").endObject()));

			bulkDocument.add(client.prepareIndex("school", "tenth", "2").setSource(jsonBuilder().startObject()
					.field("name", "Sundar S").field("age", "23").field("gender", "Male").endObject()));
			BulkResponse bulkResponse = bulkDocument.get();
			if (bulkResponse.hasFailures()) {
				// process failures by iterating through each bulk response item
			} else {
				log.info("All Documents inserted successfully...");
			}

		} catch (IOException ex) {
			log.error("Exception occurred while get Multiple Document : " + ex, ex);
		}
	}

	/**
	 * This method Search the available Document
	 */
	@Override
	public void searchDocument() {
		SearchHits hits = null;
		try {
			client = ESclient.getInstant();
			SearchResponse response = client.prepareSearch("school", "college").setTypes("tenth", "be")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.termQuery("name", "sundar"))
					.setPostFilter(QueryBuilders.rangeQuery("age").from(15).to(24)).setFrom(0).setSize(60)
					.setExplain(true).get();

			if (response != null) {
				hits = response.getHits();
			}
			if (hits != null) {

				while (hits.iterator().hasNext()) {
					hits.iterator().next();
				}
			}
		} catch (Exception ex) {
			log.error("Excption occurred while Search Document : " + ex);
		}
	}
	
	
	@Override
    public void getDocumentUsingScroll() {
        try {
            client = ESclient.getInstant();
            QueryBuilder qb = termQuery("name", "sundar");
            SearchResponse scrollResp = client.prepareSearch("school", "college")
                    .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                    .setScroll(new TimeValue(60000))
                    .setQuery(qb)
                    .setSize(100).get(); //max of 100 hits will be returned for each scroll, Scroll until no hits are returned
            do {
                for (SearchHit hit : scrollResp.getHits().getHits()) {
                    //Handle the hit...
                    hit.getFields();
                }

                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            } while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
        } catch (Exception ex) {
            log.info("Exception occurred while Scroll Document : " + ex);
        }
    }

    @Override
    public String buscaPorUsuario(String usuario) {
        try {
            client = ESclient.getInstant();
            GetResponse response = client.prepareGet("sga", "usuario", usuario).execute().actionGet();
            log.info("RETORNO : " + response);
            return response.getSourceAsString();
        } catch (Exception ex) {
        	log.error("Erro ao executar buscaPorUsuario : " + ex, ex);
            return ex.getMessage().toString();
        }
    }

	@Override
	public String obterDebitoUsuario(String codigo) {
		try {
            client = ESclient.getInstant();
            GetResponse response = client.prepareGet("sgi", "debitousuario", codigo).execute().actionGet();
            log.info("RETORNO : " + response);
            return response.getSourceAsString();
        } catch (Exception ex) {
            log.error("Erro ao executar obterDebitoUsuario : " + ex, ex);
            return ex.getMessage().toString();
        }
	}

	@Override
	public String searchAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
}
