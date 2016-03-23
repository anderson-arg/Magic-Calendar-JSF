package br.edu.ifpb.pweb.calendar.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.ifpb.pweb.calendar.model.DiasCalendar;

@ManagedBean(name="magicCalendar")
@ViewScoped
public class MagicCalendar {
	private List<DiasCalendar> diasCalendar;
	private Date dataAtual;
	private String dataAtualConcatenada;
	
	public MagicCalendar(){
		this.diasCalendar = new ArrayList<DiasCalendar>();
		this.dataAtual = new Date();
	}
	
	public void gerarCalendar(){
		this.dataAtual(0);
		
		for(int i=1; i<=quantidadeDiaMes(this.dataAtual); i++){
			DiasCalendar data = new DiasCalendar();
			data.setId(i);
			this.diasCalendar.add(data);
		}
	}
	
	public int quantidadeDiaMes(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public void dataAtual(int i){
		Calendar c = Calendar.getInstance();
		this.dataAtual.setMonth(dataAtual.getMonth()+i);
		c.setTime(this.dataAtual);
		this.dataAtualConcatenada = mesAno(c.get(Calendar.MONTH))+"/"+c.get(Calendar.YEAR);
		this.diasCalendar = new ArrayList<DiasCalendar>();
	}
	
	public String diaSemana(int dia){
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		d.setDate(dia);
		d.setMonth(this.dataAtual.getMonth());
		d.setYear(this.dataAtual.getYear());
		c.setTime(d);
		dia = c.get(Calendar.DAY_OF_WEEK);
		String[] diaSemana = new String[]{"","Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado"}; 
		return diaSemana[dia];
	}
	
	public  String mesAno(int mes){
		String[] mesAno = new String[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto"
									   ,"Setembro","Outubro","Novembro","Dezembro"}; 
		return mesAno[mes];
	}
	
	public List<DiasCalendar> getDiasCalendar() {
		return diasCalendar;
	}

	public void setDiasCalendar(List<DiasCalendar> diasCalendar) {
		this.diasCalendar = diasCalendar;
	}

	public String getDataAtualConcatenada() {
		return dataAtualConcatenada;
	}

	public void setDataAtualConcatenada(String dataAtualConcatenada) {
		this.dataAtualConcatenada = dataAtualConcatenada;
	}
	
}
