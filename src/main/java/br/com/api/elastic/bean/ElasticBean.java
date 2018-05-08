package br.com.api.elastic.bean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.api.elastic.dto.ConsultaDadosDTO;
import br.com.api.elastic.dto.DebitoUsuarioDTO;
import br.com.api.elastic.dto.InformacaoLoginDTO;
import br.com.api.elastic.query.ElasticSearchCrud;


@Path("/")
@Stateless
public class ElasticBean {
	
	@Inject
	private ElasticSearchCrud elasticService;
	
	@POST
	@Path("/salvar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvar(String json) {
		 
		elasticService.CreateDocument();
		
		return Response.ok("OK", MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/consultar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultar(ConsultaDadosDTO<InformacaoLoginDTO> request) {
		 
		String retorno = elasticService.buscaPorUsuario(request.getMapaParametros().get("LOGIN").toString());
		
		return Response.ok(retorno, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/ObterDebitoUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response obterDebitoUsuario(ConsultaDadosDTO<DebitoUsuarioDTO> request) {
		 
		String retorno = elasticService.obterDebitoUsuario(request.getMapaParametros().get("COD_USUARIO").toString());
		
		return Response.ok(retorno, MediaType.APPLICATION_JSON).build();
	}

}
