package br.edu.ifpb.pweb.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CalendarHoliday extends Calendar {
	
	@ManyToOne
	private Admin admin;
	
	public CalendarHoliday(){}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}
