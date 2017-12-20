package it.cascino.importaflussifinanziarie.righeflussi;

public class RowInputAgos extends RowInput{
	public RowInputAgos(String line){
		super(line);
		
		super.setDelimitator("$");
	}
}
