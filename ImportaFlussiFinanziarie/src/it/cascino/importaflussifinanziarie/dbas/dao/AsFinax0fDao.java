package it.cascino.importaflussifinanziarie.dbas.dao;

import java.util.List;
import it.cascino.importaflussifinanziarie.dbas.model.AsFinax0f;

public interface AsFinax0fDao{
	List<AsFinax0f> getAll();
	
	void salva(AsFinax0f a);
	
	void aggiorna(AsFinax0f a);
	
//	void elimina(AsFinax0f a);

	AsFinax0f getDaFnfinFncop(String fnfin, String fncop);
		
	void close();
}
