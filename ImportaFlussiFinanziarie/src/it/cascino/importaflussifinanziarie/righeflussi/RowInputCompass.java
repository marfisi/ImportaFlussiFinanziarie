package it.cascino.importaflussifinanziarie.righeflussi;

import org.apache.commons.lang3.StringUtils;

public class RowInputCompass extends RowInput{
	private String convenzionato;
	private String cod_PuntoVendita;
	private String puntoVendita;
	private String cliente;
	private String codFiscaleCliente;
	private String cod_Pratica;
	private String tabella_Utilizzata;
	private String dataLiquidazione;
	private String dt_Valuta;
	private String anno_Mese_Valuta;
	private String importo_Finanziato;
	private String importo_Liquidato;
	private String contributoDealer;
	private String causalePlf;
	private String importoPl;
	
	
	
	public RowInputCompass(String line){
		super(line);
		
		super.setDelimitator(";");
		setConvenzionato("");
		setCod_PuntoVendita("");
		setPuntoVendita("");
		setCliente("");
		setCodFiscaleCliente("");
		setCod_Pratica("");
		setTabella_Utilizzata("");
		setDataLiquidazione("");
		setDt_Valuta("");
		setAnno_Mese_Valuta("");
		setImporto_Finanziato(line);
		setImporto_Liquidato("");
		setContributoDealer("");
		setCausalePlf("");
		setImportoPl("");

		String strTmp = line;
		
		// salto: convenzionato
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();
		
		setCod_PuntoVendita(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		// salto: puntoVendita
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		setCliente(StringUtils.left(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim(), 25));
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		// salto: codFiscaleCliente
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();
		
		String codPratica = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		codPratica = StringUtils.leftPad(codPratica, 12, "0");
		codPratica = StringUtils.join("CO", codPratica);
		setCod_Pratica(codPratica);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		setTabella_Utilizzata(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		String data = "YYYYMMDD";
		data = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		data = data.replace("/", "").replace(" ", "");
		if(data.length() == 8){
			data = data.substring(4, 8) + data.substring(2, 4) + data.substring(0, 2);
			setDataLiquidazione(data);
		}
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		data = "YYYYMMDD";
		data = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		data = data.replace("/", "").replace(" ", "");
		if(data.length() == 8){
			data = data.substring(4, 8) + data.substring(2, 4) + data.substring(0, 2);
			setDt_Valuta(data);
		}
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		// salto: anno_Mese_Valuta
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();
		
		String importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setImporto_Finanziato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setImporto_Liquidato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setContributoDealer(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		setCausalePlf(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		// salto: importoPl
		strTmp = "";
	}
	
	public String getConvenzionato(){
		return convenzionato;
	}

	public void setConvenzionato(String convenzionato){
		this.convenzionato = convenzionato;
	}

	public String getCod_PuntoVendita(){
		return cod_PuntoVendita;
	}

	public void setCod_PuntoVendita(String cod_PuntoVendita){
		this.cod_PuntoVendita = cod_PuntoVendita;
	}

	public String getPuntoVendita(){
		return puntoVendita;
	}

	public void setPuntoVendita(String puntoVendita){
		this.puntoVendita = puntoVendita;
	}

	public String getCliente(){
		return cliente;
	}

	public void setCliente(String cliente){
		this.cliente = cliente;
	}

	public String getCodFiscaleCliente(){
		return codFiscaleCliente;
	}

	public void setCodFiscaleCliente(String codFiscaleCliente){
		this.codFiscaleCliente = codFiscaleCliente;
	}

	public String getCod_Pratica(){
		return cod_Pratica;
	}

	public void setCod_Pratica(String cod_Pratica){
		this.cod_Pratica = cod_Pratica;
	}

	public String getTabella_Utilizzata(){
		return tabella_Utilizzata;
	}

	public void setTabella_Utilizzata(String tabella_Utilizzata){
		this.tabella_Utilizzata = tabella_Utilizzata;
	}

	public String getDataLiquidazione(){
		return dataLiquidazione;
	}

	public void setDataLiquidazione(String dataLiquidazione){
		this.dataLiquidazione = dataLiquidazione;
	}

	public String getDt_Valuta(){
		return dt_Valuta;
	}

	public void setDt_Valuta(String dt_Valuta){
		this.dt_Valuta = dt_Valuta;
	}

	public String getAnno_Mese_Valuta(){
		return anno_Mese_Valuta;
	}

	public void setAnno_Mese_Valuta(String anno_Mese_Valuta){
		this.anno_Mese_Valuta = anno_Mese_Valuta;
	}

	public String getImporto_Finanziato(){
		return importo_Finanziato;
	}

	public void setImporto_Finanziato(String importo_Finanziato){
		this.importo_Finanziato = importo_Finanziato;
	}

	public String getImporto_Liquidato(){
		return importo_Liquidato;
	}

	public void setImporto_Liquidato(String importo_Liquidato){
		this.importo_Liquidato = importo_Liquidato;
	}

	public String getContributoDealer(){
		return contributoDealer;
	}

	public void setContributoDealer(String contributoDealer){
		this.contributoDealer = contributoDealer;
	}

	public String getCausalePlf(){
		return causalePlf;
	}

	public void setCausalePlf(String causalePlf){
		this.causalePlf = causalePlf;
	}

	public String getImportoPl(){
		return importoPl;
	}

	public void setImportoPl(String importoPl){
		this.importoPl = importoPl;
	}
}
