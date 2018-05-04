package br.nataliakt.gpa2.model;

import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Variable {

	// Propertys para as tabelas
	// Valor da variável
	private final ObjectProperty<Float> value;
	// Hora do valor
	private final ObjectProperty<Calendar> hour;

	/**
	 * Contrutor para as propertys.
	 * @param value Valor da variável.
	 * @param hour Hora da variável.
	 */
	public Variable(float value, Calendar hour) {
		super();
		this.value = new SimpleObjectProperty<Float>(value);
		this.hour = new SimpleObjectProperty<Calendar>(hour);
	}
	
	/**
	 * Get para o valor em float da property.
	 * @return Valor.
	 */
	public float getValue() {
		return value.get();
	}

	/**
	 * Get para o valor em Calendar da property.
	 * @return Hora.
	 */
	public Calendar getHour() {
		return hour.get();
	}

	/**
	 * Get para a property.
	 * @return Value property.
	 */
	public StringProperty valueProperty() {
		return new SimpleStringProperty(Float.toString(value.get()));
	}
	/**
	 * Get para a property.
	 * @return Hour property.
	 */
	public StringProperty hourProperty() {
		String hour = format(Integer.toString(this.hour.get().get(Calendar.HOUR_OF_DAY)));
		String minute = format(Integer.toString(this.hour.get().get(Calendar.MINUTE)));
		String second = format(Integer.toString(this.hour.get().get(Calendar.SECOND)));
		return new SimpleStringProperty(hour + ":" + minute + ":" + second);
	}
	
	/**
	 * Formata o valor adicionando 0 se necessário.
	 * @param value Valor (hora, minuto, segundo)
	 * @return Valor formatado.
	 */
	public String format(String value){
		if(value.length() == 1){
			value = "0" + value;
		}
		return value;
	}

	
}
