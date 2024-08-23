package com.example.demo.controllers.working_book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArtikulInfo implements Comparable<ArtikulInfo> {
	private String artikulName;
	private int quantity;
	private String kontragent;
	private String invoiceByKontragent;
	private String dateString;
	private Date date;
	final private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	public ArtikulInfo(String artikulName, String quantityStr,
                       String kontragent, String invoiceByKontragent, String dateString) {
		super();
		this.artikulName = artikulName;
		this.quantity = Integer.parseInt(quantityStr);
		this.invoiceByKontragent = invoiceByKontragent;
		this.kontragent = kontragent;
		this.dateString = dateString;
		try {
			this.date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDateString() {
		return dateString;
	}

	public String getKontragent() {
		return kontragent;
	}

	public String getArtikulName() {
		return artikulName;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getInvoiceByKontragent() {
		return invoiceByKontragent;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(ArtikulInfo anotherArt) {
		// TODO Auto-generated method stub
		return this.date.compareTo(anotherArt.date);
	}

}