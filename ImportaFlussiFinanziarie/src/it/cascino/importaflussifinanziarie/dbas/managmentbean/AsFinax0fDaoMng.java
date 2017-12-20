package it.cascino.importaflussifinanziarie.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.importaflussifinanziarie.dbas.dao.AsFinax0fDao;
import it.cascino.importaflussifinanziarie.dbas.model.AsFinax0f;
import it.cascino.importaflussifinanziarie.utils.Resources;

public class AsFinax0fDaoMng implements AsFinax0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsFinax0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsFinax0f> getAll(){
		List<AsFinax0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsFinax0f.findAll");
				o = (List<AsFinax0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void salva(AsFinax0f a){
		try{
			try{
				utx.begin();
				log.info("salva: " + a.toString());
				em.persist(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void aggiorna(AsFinax0f a){
		try{
			try{
				utx.begin();
				log.info("aggiorna: " + a.toString());
				em.merge(a);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
//	
//	public void elimina(AsFinax0f aElimina){
//		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
//		try{
//			try{
//				utx.begin();
//				AsFinax0f a = em.find(AsFinax0f.class, aElimina.getMcoda());
//				log.info("elimina: " + a.toString());
//				em.remove(a);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
	
	public AsFinax0f getDaFnfinFncop(String fnfin, String fncop){
		AsFinax0f o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsFinax0f.findByFncop");
				query.setParameter("fnfin", fnfin);
				query.setParameter("fncop", fncop);
				o = (AsFinax0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
