package br.edu.ifpb.pweb.model;

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
	private List<CalendarHoliday> listHoliday;
	
	public Admin(){
		super();
		this.listHoliday = new ArrayList<CalendarHoliday>();
	}
	
	public void addHoliday(CalendarHoliday holiday){
		holiday.setAdmin(this);
		this.listHoliday.add(holiday);
	}
	
	public void delHoliday(CalendarHoliday holiday){
		this.listHoliday.remove(holiday);
	}
	
	public List<CalendarHoliday> getAllListHoliday(){
		return this.listHoliday;
	}
	
	public CalendarHoliday getHoliday(int id, int type){
		for(CalendarHoliday holiday : listHoliday){
			if(holiday.getId() == id /*&& holiday.getType() == type*/ ){
				return holiday;
			}
		}
		return null;
	}
	
	public CalendarHoliday getHoliday(int id){
		for(CalendarHoliday holiday : listHoliday){
			if(holiday.getId() == id){
				return holiday;
			}
		}
		return null;
	}
	
	public void setHoliday(CalendarHoliday ch){
		int index = 0;
		for(CalendarHoliday holiday : listHoliday){
			if(holiday.getId() == ch.getId()){
				this.listHoliday.set(index, ch);
			}
			index++;
		}
	}
	
	public List<CalendarHoliday> getListFixedHoliday(){
		List<CalendarHoliday> list = new ArrayList<CalendarHoliday>();
		for(CalendarHoliday holiday : listHoliday){
			/*if(holiday.getType() == CalendarType.CALENDAR_FIXED){
				list.add(holiday);
			}*/
		}
		return list;
	}
	
	public void setListFixedHoliday(List<CalendarHoliday> list){
		for(CalendarHoliday ch : list){
			setHoliday(ch);
		}
	}
	
}
