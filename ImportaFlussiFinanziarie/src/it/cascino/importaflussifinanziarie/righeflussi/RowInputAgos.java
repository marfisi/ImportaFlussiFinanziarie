package it.cascino.importaflussifinanziarie.righeflussi;

import org.apache.commons.lang3.StringUtils;

public class RowInputAgos extends RowInput{
	private String cd_pratica;
	private String cliente;
	private String cd_punto_vendita;
	private String ragione_sociale;
	private String data_liquidazione;
	private String data_valuta;
	private String im_finanziato;
	private String im_erogato;
	private String im_contributo;
	private String im_provvigione;
	private String tabella_finanziaria;
	private String nm_rate;
	private String flag_cpi;
	private String flag_good_p;
	private String tp_credito;	
	
	public RowInputAgos(String line){
		super(line);
		
		super.setDelimitator(";");
		setCd_pratica("");
		setCliente("");
		setCd_punto_vendita("");
		setRagione_sociale("");
		setData_liquidazione("");
		setData_valuta("");
		setIm_finanziato("");
		setIm_erogato("");
		setIm_contributo("");
		setIm_provvigione("");
		setTabella_finanziaria("");
		setNm_rate("");
		setFlag_cpi("");
		setFlag_good_p("");
		setTp_credito("");
		
		String strTmp = line;

		setCd_pratica(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		setCliente(StringUtils.left(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim(), 25));
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		setCd_punto_vendita(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		// salto: ragione_sociale
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		String data = "YYYYMMDD";
		data = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		data = StringUtils.right("0" + data, 10);
		data = data.replace("/", "").replace(" ", "");
		if(data.length() == 8){
			data = data.substring(4, 8) + data.substring(2, 4) + data.substring(0, 2);
			setData_liquidazione(data);
		}
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		data = "YYYYMMDD";
		data = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		data = StringUtils.right("0" + data, 10);
		data = data.replace("/", "").replace(" ", "");
		if(data.length() == 8){
			data = data.substring(4, 8) + data.substring(2, 4) + data.substring(0, 2);
			setData_valuta(data);
		}
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		String importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setIm_finanziato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setIm_erogato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setIm_contributo(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		// salto: im_provvigione
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		setTabella_finanziaria(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		// salto: nm_rate
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();
		
		// salto: flag_cpi
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		// salto: flag_good_p
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		// salto: tp_credito
		strTmp = "";
	}

	public String getCd_pratica(){
		return cd_pratica;
	}

	public void setCd_pratica(String cd_pratica){
		this.cd_pratica = cd_pratica;
	}

	public String getCliente(){
		return cliente;
	}

	public void setCliente(String cliente){
		this.cliente = cliente;
	}

	public String getCd_punto_vendita(){
		return cd_punto_vendita;
	}

	public void setCd_punto_vendita(String cd_punto_vendita){
		this.cd_punto_vendita = cd_punto_vendita;
	}

	public String getRagione_sociale(){
		return ragione_sociale;
	}

	public void setRagione_sociale(String ragione_sociale){
		this.ragione_sociale = ragione_sociale;
	}

	public String getData_liquidazione(){
		return data_liquidazione;
	}

	public void setData_liquidazione(String data_liquidazione){
		this.data_liquidazione = data_liquidazione;
	}

	public String getData_valuta(){
		return data_valuta;
	}

	public void setData_valuta(String data_valuta){
		this.data_valuta = data_valuta;
	}

	public String getIm_finanziato(){
		return im_finanziato;
	}

	public void setIm_finanziato(String im_finanziato){
		this.im_finanziato = im_finanziato;
	}

	public String getIm_erogato(){
		return im_erogato;
	}

	public void setIm_erogato(String im_erogato){
		this.im_erogato = im_erogato;
	}

	public String getIm_contributo(){
		return im_contributo;
	}

	public void setIm_contributo(String im_contributo){
		this.im_contributo = im_contributo;
	}

	public String getIm_provvigione(){
		return im_provvigione;
	}

	public void setIm_provvigione(String im_provvigione){
		this.im_provvigione = im_provvigione;
	}

	public String getTabella_finanziaria(){
		return tabella_finanziaria;
	}

	public void setTabella_finanziaria(String tabella_finanziaria){
		this.tabella_finanziaria = tabella_finanziaria;
	}

	public String getNm_rate(){
		return nm_rate;
	}

	public void setNm_rate(String nm_rate){
		this.nm_rate = nm_rate;
	}

	public String getFlag_cpi(){
		return flag_cpi;
	}

	public void setFlag_cpi(String flag_cpi){
		this.flag_cpi = flag_cpi;
	}

	public String getFlag_good_p(){
		return flag_good_p;
	}

	public void setFlag_good_p(String flag_good_p){
		this.flag_good_p = flag_good_p;
	}

	public String getTp_credito(){
		return tp_credito;
	}

	public void setTp_credito(String tp_credito){
		this.tp_credito = tp_credito;
	}
}
