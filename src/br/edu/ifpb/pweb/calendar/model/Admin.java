package br.edu.ifpb.pweb.calendar.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.type.CalendarType;

@Entity
public class Admin extends Pessoa {
	@OneToMany(mappedBy="admin", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<CalendarFixedHoliday> listHoliday;
	
	public Admin(){
		super();
		this.listHoliday = new ArrayList<CalendarFixedHoliday>();
	}
	
	public void addHoliday(CalendarFixedHoliday holiday){
		holiday.setAdmin(this);
		this.listHoliday.add(holiday);
	}
	
	public void delHoliday(CalendarFixedHoliday holiday){
		this.listHoliday.remove(holiday);
	}
	
	public List<CalendarFixedHoliday> getAllListHoliday(){
		return this.listHoliday;
	}
	
	public CalendarFixedHoliday getHoliday(int id, int type){
		for(CalendarFixedHoliday holiday : listHoliday){
			if(holiday.getId() == id /*&& holiday.getType() == type*/ ){
				return holiday;
			}
		}
		return null;
	}
	
	public CalendarFixedHoliday getHoliday(int id){
		for(CalendarFixedHoliday holiday : listHoliday){
			if(holiday.getId() == id){
				return holiday;
			}
		}
		return null;
	}
	
	public void setHoliday(CalendarFixedHoliday ch){
		int index = 0;
		for(CalendarFixedHoliday holiday : listHoliday){
			if(holiday.getId() == ch.getId()){
				this.listHoliday.set(index, ch);
			}
			index++;
		}
	}
	
	public List<CalendarFixedHoliday> getListFixedHoliday(){
		List<CalendarFixedHoliday> list = new ArrayList<CalendarFixedHoliday>();
		for(CalendarFixedHoliday holiday : listHoliday){
			/*if(holiday.getType() == CalendarType.CALENDAR_FIXED){
				list.add(holiday);
			}*/
		}
		return list;
	}
	
	public void setListFixedHoliday(List<CalendarFixedHoliday> list){
		for(CalendarFixedHoliday ch : list){
			setHoliday(ch);
		}
	}
	
}
