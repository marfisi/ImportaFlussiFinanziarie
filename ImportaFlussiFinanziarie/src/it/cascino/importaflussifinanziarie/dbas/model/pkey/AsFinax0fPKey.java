package it.cascino.importaflussifinanziarie.dbas.model.pkey;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class AsFinax0fPKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fnfin;
	private String fncop;
	
	public AsFinax0fPKey(){
	}
	
	public AsFinax0fPKey(String fnfin, String fncop){
		super();
		this.fnfin = fnfin;
		this.fncop = fncop;
	}

	public String getFnfin(){
		return fnfin;
	}


	public void setFnfin(String fnfin){
		this.fnfin = fnfin;
	}


	public String getFncop(){
		return fncop;
	}


	public void setFncop(String fncop){
		this.fncop = fncop;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fncop == null) ? 0 : fncop.hashCode());
		result = prime * result + ((fnfin == null) ? 0 : fnfin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsFinax0fPKey){
			if((this.fnfin == ((AsFinax0fPKey)obj).fnfin) && (this.fncop == ((AsFinax0fPKey)obj).fncop)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "AsFinax0fKey [fnfin=" + fnfin + ", fncop=" + fncop + "]";
	}
}