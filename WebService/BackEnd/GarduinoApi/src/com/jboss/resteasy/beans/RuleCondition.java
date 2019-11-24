package com.jboss.resteasy.beans;

public class RuleCondition {
	private int id;
	private int idRule;
	private int idCondition;
	private int conditionValue;
	private boolean status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdRule() {
		return idRule;
	}
	public void setIdRule(int idRule) {
		this.idRule = idRule;
	}
	public int getIdCondition() {
		return idCondition;
	}
	public void setIdCondition(int idCondition) {
		this.idCondition = idCondition;
	}
	public int getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(int conditionValue) {
		this.conditionValue = conditionValue;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
