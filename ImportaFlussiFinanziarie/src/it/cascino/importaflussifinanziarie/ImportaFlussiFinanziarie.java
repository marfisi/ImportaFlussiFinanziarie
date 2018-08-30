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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class ImportaFlussiFinanziarie{
	
	private Logger log = Logger.getLogger(ImportaFlussiFinanziarie.class);
	
	StringBuilder stringBuilder = new StringBuilder();
		
	// AS400
	private AsFinax0fDao asFinax0fDao = new AsFinax0fDaoMng();
	
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
			
			String codicePratica = "";
			if(finanziaria.equals("FIN")){
				rowInput = (RowInputFindomestic)iter_rowInputLs.next();
				codicePratica =  ((RowInputFindomestic)rowInput).getCir();
			}else if(finanziaria.equals("COM")){
				rowInput = (RowInputCompass)iter_rowInputLs.next();
				codicePratica =  ((RowInputCompass)rowInput).getCod_Pratica();
			}else if(finanziaria.equals("AGO")){
				rowInput = (RowInputAgos)iter_rowInputLs.next();
				codicePratica =  ((RowInputAgos)rowInput).getCd_pratica();
			}else{
				log.error("non e' possibile questa finanziaria: " + finanziaria);
			}
			
			AsFinax0f asFinax0f = asFinax0fDao.getDaFnfinFncop(finanziaria, codicePratica);
			if(asFinax0f != null){
				log.info("Pratica " + asFinax0f.getId() + " gia' presente");
				continue;		// se la pratica e' gia' presente la salto
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
				asFinax0f.setFnimp(Float.parseFloat(rowInputCompass.getImporto_Finanziato()));
				asFinax0f.setFnims(Float.parseFloat(rowInputCompass.getContributoDealer()));
				asFinax0f.setFniml(Float.parseFloat(rowInputCompass.getImporto_Liquidato()));
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
				asFinax0f.setFnimp(Float.parseFloat(rowInputAgos.getIm_finanziato()));
				try{
					asFinax0f.setFnims(Float.parseFloat(rowInputAgos.getIm_contributo()));
				}catch(NumberFormatException e){
					asFinax0f.setFnims(0.0f);
				}
				asFinax0f.setFniml(Float.parseFloat(rowInputAgos.getIm_erogato()));
				asFinax0f.setFnbap("B");
				asFinax0f.setFnras(rowInputAgos.getCliente());
				asFinax0f.setFntab(rowInputAgos.getTabella_finanziaria());
				if(StringUtils.equals(rowInputAgos.getTp_credito(), "R")){
					asFinax0f.setFntab("FSL");
				}
				asFinax0f.setFnrie(" ");
				
				asFinax0fDao.salva(asFinax0f);
				
				log.info("Pratica " + asFinax0f.getId() + " inserita");
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