package com.nubefact.ose.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ose_migrado", indexes = {@Index(columnList = "fecha_recepcion_xml"), @Index (columnList = "id_ticket",unique = true)})
public class Migrado {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket")
	private Ticket ticket;
	
	@NotNull
	@Column(name = "fecha_recepcion_xml")
	private Date fechaRecepcionXml;
	
	@NotNull
	private boolean cpe;

	@NotNull
	private boolean cdr_ose;
	
	@NotNull
	private boolean cdr_sunat;

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public boolean isCpe() {
		return cpe;
	}

	public void setCpe(boolean cpe) {
		this.cpe = cpe;
	}

	public boolean isCdr_ose() {
		return cdr_ose;
	}

	public void setCdr_ose(boolean cdr_ose) {
		this.cdr_ose = cdr_ose;
	}

	public boolean isCdr_sunat() {
		return cdr_sunat;
	}

	public void setCdr_sunat(boolean cdr_sunat) {
		this.cdr_sunat = cdr_sunat;
	}


}
