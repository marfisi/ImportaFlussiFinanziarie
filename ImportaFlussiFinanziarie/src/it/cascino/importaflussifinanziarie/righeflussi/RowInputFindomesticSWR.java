package it.cascino.importaflussifinanziarie.righeflussi;

import org.apache.commons.lang3.StringUtils;

public class RowInputFindomesticSWR extends RowInput{
	private String cliente;
	private String tipoPratica;
	private String finanziato;
	private String liquidato;
	private String trattenute;
	private String sip;
	private String buonoDOrdine;
	private String forzaVendita;
	private String eco;
	private String modPag;
	private String societa;
	private String data;
	private String cir;
	
	public RowInputFindomesticSWR(String line){
		super(line);
		
		super.setDelimitator(";");
		setCliente("");
		setTipoPratica("");
		setFinanziato("");
		setLiquidato("");
		setTrattenute("");
		setSip("");
		setBuonoDOrdine("");
		setForzaVendita("");
		setEco("");
		setModPag("");
		setSocieta("");
		setData("");
		setCir("");

		String strTmp = StringUtils.join(line, this.getDelimitator());
		
		setCliente(StringUtils.left(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim(), 25));
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
				
		// salto: tipoPratica
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		String importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setFinanziato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setLiquidato(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setTrattenute(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		importo = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		importo = importo.replace(".", "").replace(",", ".").replace(" ", "");
		setSip(importo);
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);

		// salto: buonoDOrdine
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		// salto: forzaVendita
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		// salto: eco
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		setModPag(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		// salto: societa
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1).trim();

		String data = "YYYYMMDD";
		data = strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim();
		data = data.replace("/", "").replace(" ", "");
		if(data.length() == 8){
			data = data.substring(4, 8) + data.substring(2, 4) + data.substring(0, 2);
			setData(data);
		}
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
		
		setCir(strTmp.substring(0, strTmp.indexOf(getDelimitator())).trim());
		strTmp = strTmp.substring(strTmp.indexOf(getDelimitator())+1);
	}

	public String getCliente(){
		return cliente;
	}

	public void setCliente(String cliente){
		this.cliente = cliente;
	}

	public String getTipoPratica(){
		return tipoPratica;
	}

	public void setTipoPratica(String tipoPratica){
		this.tipoPratica = tipoPratica;
	}

	public String getFinanziato(){
		return finanziato;
	}

	public void setFinanziato(String finanziato){
		this.finanziato = finanziato;
	}

	public String getLiquidato(){
		return liquidato;
	}

	public void setLiquidato(String liquidato){
		this.liquidato = liquidato;
	}

	public String getTrattenute(){
		return trattenute;
	}

	public void setTrattenute(String trattenute){
		this.trattenute = trattenute;
	}

	public String getSip(){
		return sip;
	}

	public void setSip(String sip){
		this.sip = sip;
	}

	public String getBuonoDOrdine(){
		return buonoDOrdine;
	}

	public void setBuonoDOrdine(String buonoDOrdine){
		this.buonoDOrdine = buonoDOrdine;
	}

	public String getForzaVendita(){
		return forzaVendita;
	}

	public void setForzaVendita(String forzaVendita){
		this.forzaVendita = forzaVendita;
	}

	public String getEco(){
		return eco;
	}

	public void setEco(String eco){
		this.eco = eco;
	}

	public String getModPag(){
		return modPag;
	}

	public void setModPag(String modPag){
		this.modPag = modPag;
	}

	public String getSocieta(){
		return societa;
	}

	public void setSocieta(String societa){
		this.societa = societa;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getCir(){
		return cir;
	}

	public void setCir(String cir){
		this.cir = cir;
	}
}
