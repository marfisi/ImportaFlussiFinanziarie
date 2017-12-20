package it.cascino.importaflussifinanziarie;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunImportaFlussiFinanziarie{
	private static Logger log;
	
	public static void main(String[] args){
		PropertyConfigurator.configure("logdir/log.properties");
		log = Logger.getLogger(RunImportaFlussiFinanziarie.class);
		log.info("START");
		
		@SuppressWarnings("unused")
		ImportaFlussiFinanziarie importaFlussiFinanziarie = new ImportaFlussiFinanziarie(args);
		
		log.info("STOP");
		System.exit(0);
	}
}
