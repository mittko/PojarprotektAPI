package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtinguisherModel implements Comparable<ExtinguisherModel> {

	public ExtinguisherModel(){}

	private String type;
	private String wheight;
	private String category;
	private String brand;
	private String quantity;
	private String price;
	private String invoiceByKontragent;
	private String dateString;
	private String kontragent;


	@JsonIgnore
	final private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Date date;
	private String saller;
	private String percentProfit;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWheight() {
		return wheight;
	}

	public void setWheight(String wheight) {
		this.wheight = wheight;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getInvoiceByKontragent() {
		return invoiceByKontragent;
	}

	public void setInvoiceByKontragent(String invoiceByKontragent) {
		this.invoiceByKontragent = invoiceByKontragent;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
		try {
			this.date = sdf.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String getKontragent() {
		return kontragent;
	}

	public void setKontragent(String kontragent) {
		this.kontragent = kontragent;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public String getSaller() {
		return saller;
	}

	public void setSaller(String saller) {
		this.saller = saller;
	}

	public String getPercentProfit() {
		return percentProfit;
	}

	public void setPercentProfit(String percentProfit) {
		this.percentProfit = percentProfit;
	}

	@Override
	public int compareTo(ExtinguisherModel o) {
		// TODO Auto-generated method stub
		return this.date.compareTo(o.date);
	}



}
