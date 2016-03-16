package br.edu.ifpb.pweb.turmas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
	
	public static String diaSemana(int dia){
		String[] diaSemana = new String[]{"","Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado"}; 
		return diaSemana[dia];
	}
	
	public static String mesAno(int mes){
		String[] mesAno = new String[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto"
									   ,"Setembro","Outubro","Novembro","Dezembro"}; 
		return mesAno[mes];
	}
	
	
	
	public static void main(String[] args) throws ParseException {
		Calendar c = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d = sdf.parse("16/03/2016");
		c.setTime(d);
		
		System.out.println(c.getActualMaximum(Calendar.DAY_OF_MONTH));

		System.out.println(c.get(Calendar.DAY_OF_MONTH)+" "+diaSemana(c.get(Calendar.DAY_OF_WEEK))+" / "+(c.get(Calendar.MONTH)+1)+" "+mesAno(c.get(Calendar.MONTH))+" / "+c.get(Calendar.YEAR));
		
	}
}
