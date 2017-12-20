package it.cascino.importaflussifinanziarie.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import it.cascino.importaflussifinanziarie.dbas.model.pkey.AsFinax0fPKey;

/**
* The persistent class for the cas_dat/finax0f database table.
* 
*/
@Entity(name="Finax0f")
@NamedQueries({
	@NamedQuery(name = "AsFinax0f.findAll", query = "SELECT a FROM Finax0f a order by a.id.fnfin, a.id.fncop"),
	@NamedQuery(name = "AsFinax0f.findByFncop", query = "SELECT a FROM Finax0f a WHERE a.id.fnfin = :fnfin and a.id.fncop = :fncop")
})
public class AsFinax0f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AsFinax0fPKey id;
	private String fncpv;
	private Integer fndal;
	private Integer fndav;
	private Float fnimp;
	private Float fnims;
	private Float fniml;
	private String fnbap;
	private String fnras;
	private String fntab;
	private String fnrie;
	
	public AsFinax0f(){
	}

	public AsFinax0f(AsFinax0fPKey id, String fncpv, Integer fndal, Integer fndav, Float fnimp, Float fnims, Float fniml, String fnbap, String fnras, String fntab, String fnrie){
		super();
		this.id = id;
		this.fncpv = fncpv;
		this.fndal = fndal;
		this.fndav = fndav;
		this.fnimp = fnimp;
		this.fnims = fnims;
		this.fniml = fniml;
		this.fnbap = fnbap;
		this.fnras = fnras;
		this.fntab = fntab;
		this.fnrie = fnrie;
	}

	public AsFinax0fPKey getId(){
		return id;
	}

	public void setId(AsFinax0fPKey id){
		this.id = id;
	}

	public String getFncpv(){
		return fncpv;
	}

	public void setFncpv(String fncpv){
		this.fncpv = fncpv;
	}

	public Integer getFndal(){
		return fndal;
	}

	public void setFndal(Integer fndal){
		this.fndal = fndal;
	}

	public Integer getFndav(){
		return fndav;
	}

	public void setFndav(Integer fndav){
		this.fndav = fndav;
	}

	public Float getFnimp(){
		return fnimp;
	}

	public void setFnimp(Float fnimp){
		this.fnimp = fnimp;
	}

	public Float getFnims(){
		return fnims;
	}

	public void setFnims(Float fnims){
		this.fnims = fnims;
	}

	public Float getFniml(){
		return fniml;
	}

	public void setFniml(Float fniml){
		this.fniml = fniml;
	}

	public String getFnbap(){
		return fnbap;
	}

	public void setFnbap(String fnbap){
		this.fnbap = fnbap;
	}

	public String getFnras(){
		return fnras;
	}

	public void setFnras(String fnras){
		this.fnras = fnras;
	}

	public String getFntab(){
		return fntab;
	}

	public void setFntab(String fntab){
		this.fntab = fntab;
	}

	public String getFnrie(){
		return fnrie;
	}

	public void setFnrie(String fnrie){
		this.fnrie = fnrie;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fnbap == null) ? 0 : fnbap.hashCode());
		result = prime * result + ((fncpv == null) ? 0 : fncpv.hashCode());
		result = prime * result + ((fndal == null) ? 0 : fndal.hashCode());
		result = prime * result + ((fndav == null) ? 0 : fndav.hashCode());
		result = prime * result + ((fniml == null) ? 0 : fniml.hashCode());
		result = prime * result + ((fnimp == null) ? 0 : fnimp.hashCode());
		result = prime * result + ((fnims == null) ? 0 : fnims.hashCode());
		result = prime * result + ((fnras == null) ? 0 : fnras.hashCode());
		result = prime * result + ((fnrie == null) ? 0 : fnrie.hashCode());
		result = prime * result + ((fntab == null) ? 0 : fntab.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsFinax0f) {
			if(this.id == ((AsFinax0f)obj).id) {
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "AsFinax0f [id=" + id + ", fncpv=" + fncpv + ", fndal=" + fndal + ", fndav=" + fndav + ", fnimp=" + fnimp + ", fnims=" + fnims + ", fniml=" + fniml + ", fnbap=" + fnbap + ", fnras=" + fnras + ", fntab=" + fntab + ", fnrie=" + fnrie + "]";
	}
}