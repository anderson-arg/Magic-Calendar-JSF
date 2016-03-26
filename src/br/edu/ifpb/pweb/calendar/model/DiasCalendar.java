package br.edu.ifpb.pweb.calendar.model;

import java.util.ArrayList;
import java.util.List;

public class DiasCalendar extends Calendar {
	
	private List<Calendar> calendarios;
	
	public DiasCalendar(){
		this.calendarios = new ArrayList<Calendar>();
	}
	
	public void addCalendar(Calendar calendar) {
		this.calendarios.add(calendar);
	}
	
	public List<Calendar> getCalendarios() {
		return calendarios;
	}
	
	public void updateCalendar(Calendar calendar){
		int index = 0;
		for(Calendar c : this.calendarios){
			if(c.equals(calendar))
				this.calendarios.set(index, calendar);
			index++;
		}
	}

	public void deleteCalendar(Calendar calendar){
		this.calendarios.remove(calendar);
	}
	
	public List<CalendarComment> getComentarios(){
		List<CalendarComment> cms = new ArrayList<CalendarComment>();
		for (Calendar c : this.calendarios) {
			if(c instanceof CalendarComment)
				cms.add((CalendarComment)c);
		}
		return cms;
	}
	
}
