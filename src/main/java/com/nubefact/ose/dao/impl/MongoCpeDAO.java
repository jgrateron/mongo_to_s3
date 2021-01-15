package com.nubefact.ose.dao.impl;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.nubefact.ose.dao.IMongoCpeDAO;
import com.nubefact.ose.entity.mongo.MongoCpe;

@Repository
public class MongoCpeDAO implements IMongoCpeDAO{
	
	@Qualifier(value = "mongoTemplateSecond")
	@Autowired
	private MongoTemplate mongoTemplateSecond;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	//valor que divide las base de datos en mongo, no modificar
	private static long ID_PARTICION = 68840000;
	
	public void saveCpe(MongoCpe mongoCpe) {
		if (mongoCpe.getIdTicket() < ID_PARTICION) {
			mongoTemplate.save(mongoCpe);	
		}
		else {		
			mongoTemplateSecond.save(mongoCpe);
		}
	}
	/**
	 * 
	 */
	public void saveOnlySecond(MongoCpe mongoCpe)
	{
		mongoTemplateSecond.save(mongoCpe);
	}
	/**
	 * 
	 */
	public void updateCdrSunat(MongoCpe mongoCpe) 
	{
		Query query = Query.query(Criteria.where("idTicket").is(mongoCpe.getIdTicket()));
        Update update = Update.update("mongoCdrSunat", mongoCpe.getMongoCdrSunat());

		if (mongoCpe.getIdTicket() < ID_PARTICION) {
	        mongoTemplate.updateFirst(query, update, MongoCpe.class);
		}
		else {
	        mongoTemplateSecond.updateFirst(query, update, MongoCpe.class);			
		}
	}
	/**
	 * 
	 */
	public void updateCdrOse(MongoCpe mongoCpe) 
	{
		Query query = Query.query(Criteria.where("idTicket").is(mongoCpe.getIdTicket()));
        Update update = Update.update("mongoCdr", mongoCpe.getMongoCdr());

		if (mongoCpe.getIdTicket() < ID_PARTICION) {
			mongoTemplate.updateFirst(query, update, MongoCpe.class);
		}
		else {
			mongoTemplateSecond.updateFirst(query, update, MongoCpe.class);
		}
	}
	/**
	 * 
	 */
	public MongoCpe getByIdTicket(Long idTicket) {
		Query query = new Query();
		query.addCriteria(Criteria.where("idTicket").is(idTicket));
		if (idTicket < ID_PARTICION) {
			return mongoTemplate.findOne(query, MongoCpe.class);
		}
		else
		{
			return mongoTemplateSecond.findOne(query, MongoCpe.class);
		}
	}
	/**
	 * 
	 */
	public MongoCpe getByIdTicketSecond(Long idTicket) {
		Query query = new Query();
		query.addCriteria(Criteria.where("idTicket").is(idTicket));
		return mongoTemplateSecond.findOne(query, MongoCpe.class);
	}
	
	/**
	 * 
	 */
	public MongoCpe getByNomDoc(String nombreDoc) {
		Query query = new Query();
		query.addCriteria(Criteria.where("nombreDoc").is(nombreDoc));
		
		MongoCpe cpe1 = mongoTemplate.findOne(query, MongoCpe.class);
		MongoCpe cpe2 = mongoTemplateSecond.findOne(query, MongoCpe.class);
		return cpe1 != null ? cpe1 : cpe2;
	}
	/**
	 * 
	 */
	public void deleteAll(String ruc)
	{
		Query query = new Query();
		query.addCriteria(Criteria.where("ruc").is(ruc));		
		mongoTemplate.remove(query,MongoCpe.class);
		mongoTemplateSecond.remove(query,MongoCpe.class);
	}
	/**
	 * 
	 */
	public void deleteByNombreDoc(String nombreDoc) {
		Query query = new Query();
		query.addCriteria(Criteria.where("nombreDoc").is(nombreDoc));		
		mongoTemplate.remove(query,MongoCpe.class);
		mongoTemplateSecond.remove(query,MongoCpe.class);
	}
	
	public String check() 
	{
		Document result = this.mongoTemplate.executeCommand("{ buildInfo: 1 }");
		String version = "Version: " + result.getString("version");
		result = this.mongoTemplateSecond.executeCommand("{ buildInfo: 1 }");
		version = version + ", Version: " + result.getString("version");
		Query query = new Query();
		query.addCriteria(Criteria.where("idTicket").is(0));
		mongoTemplate.findOne(query, MongoCpe.class);
		mongoTemplateSecond.findOne(query, MongoCpe.class);
		return version;
	}
}
