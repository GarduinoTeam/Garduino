package com.jboss.resteasy.beans;

import java.sql.Date;

public class RuleTimeCondition {
	private int id;
	private int idRule;
	private boolean status;
	private String startTime;
	private String endTime;
	private String monthsOfTheYear;
	private String daysOfWeek;
	private Date [] specificDates;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdRule() {
		return idRule;
	}
	public void setIdRule(int id_rule) {
		this.idRule = id_rule;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMonthsOfTheYear() {
		return monthsOfTheYear;
	}
	public void setMonthsOfTheYear(String monthsOfTheYear) {
		this.monthsOfTheYear = monthsOfTheYear;
	}
	public String getDaysOfWeek() {
		return daysOfWeek;
	}
	public void setDaysOfWeek(String daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	public Date[] getSpecificDates() {
		return specificDates;
	}
	public void setSpecificDates(Date[] specificDates) {
		this.specificDates = specificDates;
	}
	

}
