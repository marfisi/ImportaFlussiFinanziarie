package it.cascino.importaflussifinanziarie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import it.cascino.importaflussifinanziarie.dbas.dao.AsFinax0fDao;
import it.cascino.importaflussifinanziarie.dbas.managmentbean.AsFinax0fDaoMng;
import it.cascino.importaflussifinanziarie.dbas.model.AsFinax0f;
import it.cascino.importaflussifinanziarie.dbas.model.pkey.AsFinax0fPKey;
import it.cascino.importaflussifinanziarie.righeflussi.RowInput;
import it.cascino.importaflussifinanziarie.righeflussi.RowInputAgos;
import it.cascino.importaflussifinanziarie.righeflussi.RowInputCompass;
import it.cascino.importaflussifinanziarie.righeflussi.RowInputFindomestic;
import it.cascino.importaflussifinanziarie.righeflussi.RowInputFindomesticSWR;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class ImportaFlussiFinanziarie{
	
	private Logger log = Logger.getLogger(ImportaFlussiFinanziarie.class);
	
	StringBuilder stringBuilder = new StringBuilder();
		
	// AS400
	private AsFinax0fDao asFinax0fDao = new AsFinax0fDaoMng();
	
	private List<AsFinax0f> asFinax0fLs;
	
	private File fileInput = null;
	private List<RowInput> rowInputLs = new ArrayList<RowInput>();
	RowInput rowInput = null;
	
	private String finanziaria = null;
	
	public ImportaFlussiFinanziarie(String args[]){
		log.info("[" + "ImportaFlussiFinanziarie");
		
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-file") == 0) {
				numArg++;
				fileInput = new File(args[numArg]);
			}else if(args[numArg].compareTo("-finanziaria") == 0) {
				numArg++;
				finanziaria = args[numArg];
			}else{ // se c'e' almeno un parametro e non e' tra quelli previsti stampo il messaggio d'aiuto
			}
		}
		
		Boolean fileInteramenteElaborato = true;
		
		log.info("File: " + fileInput.getAbsolutePath());
		
		String line = null;
		try{
			FileReader fstream = new FileReader(fileInput);
			BufferedReader in = new BufferedReader(fstream);
			
			int rigo = 0;
			while((line = in.readLine()) != null){
				if(finanziaria.equals("FIN")){
					rowInput = new RowInputFindomestic(line);
				}else if(StringUtils.equals(finanziaria, "FINST") || StringUtils.equals(finanziaria, "FINSB")){
					rowInput = new RowInputFindomesticSWR(line);
				}else if(finanziaria.equals("COM")){
					rowInput = new RowInputCompass(line);
				}else if(finanziaria.equals("AGO")){
					rowInput = new RowInputAgos(line);
				}else{
					rowInput = new RowInput(line);
					fileInteramenteElaborato = false;
				}
								
				rigo++;
								
				rowInputLs.add(rowInput);
			}
			
			// Close the input stream
			in.close();
		}catch(IOException ioe){// Catch exception if any
			log.error(ioe.getMessage());
			fileInteramenteElaborato = false;
			return;
		}
		
		Iterator<RowInput> iter_rowInputLs = rowInputLs.iterator();
		// salto l'intestazione
		rowInput = (RowInput)iter_rowInputLs.next();
		while(iter_rowInputLs.hasNext()){
			
			String finStr = finanziaria;
			
			String codicePratica = "";
			if(StringUtils.equals(finanziaria, "FIN")){
				rowInput = (RowInputFindomestic)iter_rowInputLs.next();
				codicePratica =  ((RowInputFindomestic)rowInput).getCir();
			}else if(StringUtils.equals(finanziaria, "FINST") || StringUtils.equals(finanziaria, "FINSB")){
				rowInput = (RowInputFindomesticSWR)iter_rowInputLs.next();
				codicePratica =  ((RowInputFindomesticSWR)rowInput).getCir();
				finStr = "FIN";
			}else if(StringUtils.equals(finanziaria, "COM")){
				rowInput = (RowInputCompass)iter_rowInputLs.next();
				codicePratica =  ((RowInputCompass)rowInput).getCod_Pratica();
			}else if(StringUtils.equals(finanziaria, "AGO")){
				rowInput = (RowInputAgos)iter_rowInputLs.next();
				codicePratica =  ((RowInputAgos)rowInput).getCd_pratica();
			}else{
				log.error("non e' possibile questa finanziaria: " + finanziaria);
			}
			
			AsFinax0f asFinax0f = asFinax0fDao.getDaFnfinFncop(finStr, codicePratica);
			if(asFinax0f != null){
				// e' gia' presente, ma se e' una AGOS Fastline, devo comunque analizzare il rigo
				if(!(StringUtils.equals(finanziaria, "AGO") && (StringUtils.equals(asFinax0f.getFntab(), "FSF") || StringUtils.equals(asFinax0f.getFntab(), "FSV")))){
					log.info("Pratica " + asFinax0f.getId() + " gia' presente");
					continue;		// se la pratica e' gia' presente la salto
				}
			}
			
			// se sono qui' la pratica non e' stata trovata, si deve inserire/importare
			
			if(finanziaria.equals("FIN")){
				RowInputFindomestic rowInputFindomestic = (RowInputFindomestic)rowInput;
				
				asFinax0f = new AsFinax0f();
				AsFinax0fPKey asFinax0fPKey = new AsFinax0fPKey();
				asFinax0fPKey.setFnfin(finanziaria);
				asFinax0fPKey.setFncop(rowInputFindomestic.getCir());
				asFinax0f.setId(asFinax0fPKey);
				asFinax0f.setFncpv(rowInputFindomestic.getTvei());
				asFinax0f.setFndal(Integer.parseInt(rowInputFindomestic.getData()));
				asFinax0f.setFndav(0);
				BigDecimal bd = new BigDecimal(rowInputFindomestic.getFinanziato());
				bd = bd.subtract(new BigDecimal(rowInputFindomestic.getSip()));
				asFinax0f.setFnimp(bd.floatValue());
				asFinax0f.setFnims(-Float.parseFloat(rowInputFindomestic.getTrattenute()));
				asFinax0f.setFniml(Float.parseFloat(rowInputFindomestic.getLiquidato()));
				asFinax0f.setFnbap((rowInputFindomestic.getModPag().compareTo("B") == 0) ? "B" : "B");
				asFinax0f.setFnras(rowInputFindomestic.getCliente());
				asFinax0f.setFntab("ND");
				asFinax0f.setFnrie(" ");
				
				asFinax0fDao.salva(asFinax0f);
				
				log.info("Pratica " + asFinax0f.getId() + " inserita");	
			}else if(StringUtils.equals(finanziaria, "FINST") || StringUtils.equals(finanziaria, "FINSB")){
				RowInputFindomesticSWR rowInputFindomesticSWR = (RowInputFindomesticSWR)rowInput;
				
				asFinax0f = new AsFinax0f();
				AsFinax0fPKey asFinax0fPKey = new AsFinax0fPKey();
				asFinax0fPKey.setFnfin(finStr);	// la finanziaria e' comunque findomestic
				asFinax0fPKey.setFncop(rowInputFindomesticSWR.getCir());
				asFinax0f.setId(asFinax0fPKey);
				switch(finanziaria){
					case "FINST":
						asFinax0f.setFncpv("3186244");	// Showroom Termini
						break;
					case "FINSB":
						asFinax0f.setFncpv("4091666");	// Showroom Bagheria
						break;
					default:
						asFinax0f.setFncpv("9999999");
						break;
				}
				asFinax0f.setFndal(Integer.parseInt(rowInputFindomesticSWR.getData()));
				asFinax0f.setFndav(0);
				BigDecimal bd = new BigDecimal(rowInputFindomesticSWR.getFinanziato());
				bd = bd.subtract(new BigDecimal(rowInputFindomesticSWR.getSip()));
				asFinax0f.setFnimp(bd.floatValue());
				try{
					asFinax0f.setFnims(-Float.parseFloat(rowInputFindomesticSWR.getTrattenute()));
				}catch(NumberFormatException e){
					asFinax0f.setFnims(0.0f);
				}
				try{
					asFinax0f.setFniml(Float.parseFloat(rowInputFindomesticSWR.getLiquidato()));
				}catch(NumberFormatException e){
					asFinax0f.setFniml(0.0f);
				}
				asFinax0f.setFnbap((rowInputFindomesticSWR.getModPag().compareTo("B") == 0) ? "B" : "B");
				asFinax0f.setFnras(rowInputFindomesticSWR.getCliente());
				asFinax0f.setFntab("ND");
				asFinax0f.setFnrie(" ");
				
				asFinax0fDao.salva(asFinax0f);
				
				log.info("Pratica " + asFinax0f.getId() + " inserita");	
			}else if(finanziaria.equals("COM")){
				RowInputCompass rowInputCompass = (RowInputCompass)rowInput;
				
				asFinax0f = new AsFinax0f();
				AsFinax0fPKey asFinax0fPKey = new AsFinax0fPKey();
				asFinax0fPKey.setFnfin(finanziaria);
				asFinax0fPKey.setFncop(rowInputCompass.getCod_Pratica());
				asFinax0f.setId(asFinax0fPKey);
				asFinax0f.setFncpv(rowInputCompass.getCod_PuntoVendita());
				asFinax0f.setFndal(Integer.parseInt(rowInputCompass.getDataLiquidazione()));
				asFinax0f.setFndav(Integer.parseInt(rowInputCompass.getDt_Valuta()));
				try{
					asFinax0f.setFnimp(Float.parseFloat(rowInputCompass.getImporto_Finanziato()));
				}catch(NumberFormatException e){
					asFinax0f.setFnimp(0.0f);
				}
				try{
					asFinax0f.setFnims(Float.parseFloat(rowInputCompass.getContributoDealer()));
				}catch(NumberFormatException e){
					asFinax0f.setFnims(0.0f);
				}
				try{
					asFinax0f.setFniml(Float.parseFloat(rowInputCompass.getImporto_Liquidato()));
				}catch(NumberFormatException e){
					asFinax0f.setFniml(0.0f);
				}
				asFinax0f.setFnbap((rowInputCompass.getCausalePlf().compareTo("CLQ") == 0) ? "P" : "B");
				asFinax0f.setFnras(rowInputCompass.getCliente());
				asFinax0f.setFntab(rowInputCompass.getTabella_Utilizzata());
				asFinax0f.setFnrie(" ");
				
				asFinax0fDao.salva(asFinax0f);
				
				log.info("Pratica " + asFinax0f.getId() + " inserita");				
			}else if(finanziaria.equals("AGO")){
				RowInputAgos rowInputAgos = (RowInputAgos)rowInput;
				
				asFinax0f = new AsFinax0f();
				AsFinax0fPKey asFinax0fPKey = new AsFinax0fPKey();
				asFinax0fPKey.setFnfin(finanziaria);
				asFinax0fPKey.setFncop(rowInputAgos.getCd_pratica());
				asFinax0f.setId(asFinax0fPKey);
				asFinax0f.setFncpv(rowInputAgos.getCd_punto_vendita());
				asFinax0f.setFndal(Integer.parseInt(rowInputAgos.getData_liquidazione()));
				asFinax0f.setFndav(0);
				try{
					asFinax0f.setFnimp(Float.parseFloat(rowInputAgos.getIm_finanziato()));
				}catch(NumberFormatException e){
					asFinax0f.setFnimp(0.0f);
				}
				try{
					asFinax0f.setFnims(Float.parseFloat(rowInputAgos.getIm_contributo()));
				}catch(NumberFormatException e){
					asFinax0f.setFnims(0.0f);
				}
				try{
					asFinax0f.setFniml(Float.parseFloat(rowInputAgos.getIm_erogato()));
				}catch(NumberFormatException e){
					asFinax0f.setFniml(0.0f);
				}
				asFinax0f.setFnbap("B");
				asFinax0f.setFnras(rowInputAgos.getCliente());
				asFinax0f.setFntab(rowInputAgos.getTabella_finanziaria());
				if(StringUtils.equals(rowInputAgos.getTp_credito(), "R")){	// FastLine 
					asFinax0f.setFntab("F..");
					if(StringUtils.equals(rowInputAgos.getCcpra_tp_acquisto(), "F")){
						asFinax0f.setFntab("FSF");		// FSF e' alla creazione
					}else if(StringUtils.equals(rowInputAgos.getCcpra_tp_acquisto(), "V")){
						asFinax0f.setFntab("FSV");		// FSV e' dalla seconda in poi (ovvero i voucher)
						// in caso di voucher come data al posto della data liquidazione metto quella di operazione
						asFinax0f.setFndal(Integer.parseInt(rowInputAgos.getCcpra_dt_acquisto()));
					}
				}
				asFinax0f.setFnrie(" ");
				
				Boolean daInserire = true;
				asFinax0fLs = asFinax0fDao.getDaFnfinLikeFncop(finanziaria, codicePratica);
				AsFinax0f asFinax0fInTabella = null;
				Iterator<AsFinax0f> iter_asFinax0f = asFinax0fLs.iterator();
				while(iter_asFinax0f.hasNext()){
					asFinax0fInTabella = iter_asFinax0f.next();
					
					if((StringUtils.equals(asFinax0f.getFntab(), "FSF") && (StringUtils.equals(asFinax0f.getId().getFncop(), StringUtils.trim(asFinax0fInTabella.getId().getFncop()))))){
						daInserire = false;
						break;
					}
					
					if((StringUtils.equals(asFinax0f.getFntab(), "FSV")) && (StringUtils.equals(asFinax0fInTabella.getFntab(), "FSV")) && (StringUtils.startsWith(asFinax0fInTabella.getId().getFncop(), StringUtils.join(asFinax0f.getId().getFncop(), ".")))){
						if((Integer.compare(asFinax0fInTabella.getFndal(), asFinax0f.getFndal()) == 0) && (Float.compare(asFinax0fInTabella.getFnimp(), asFinax0f.getFnimp()) == 0)){
							daInserire = false;
							break;
						}
					}
				}

				if(daInserire){
					if(StringUtils.equals(asFinax0f.getFntab(), "FSV")){
						int finNumVoucher = asFinax0fDao.getDaFnfinLikeFncop(finanziaria, StringUtils.join(codicePratica, ".")).size();	// con il "." sono i voucher (FSV)
						finNumVoucher = finNumVoucher + 1;
						asFinax0f.getId().setFncop(StringUtils.join(asFinax0f.getId().getFncop(), ".", Integer.toString(finNumVoucher)));
					}
					
					asFinax0fDao.salva(asFinax0f);
					
					log.info("Pratica " + asFinax0f.getId() + " inserita");
				}else{
					log.info("Pratica " + asFinax0f.getId() + " gia' presente");
				}
			}else{
				log.error("non e' possibile questa finanziaria: " + finanziaria);
			}
		}
				
		if(fileInteramenteElaborato) {
			Path source = Paths.get(fileInput.getAbsolutePath());
			try{
				DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String dataSyncStr = formatter.format(new Date());
				
				Files.move(source, source.resolveSibling(StringUtils.join(dataSyncStr, "_elaborato_", fileInput.getName())), StandardCopyOption.REPLACE_EXISTING);
			}catch(IOException e){
				e.printStackTrace();
			}
			log.info("file rinominato");
		}else{
			log.info("file non lo rinomino dato che ancora c'e' qualcosa di utile");
		}
		
		asFinax0fDao.close();
		
		log.info("]" + "ImportaFlussiFinanziarie");
	}
}