package com.nubefact.ose.dao;

import java.util.Date;
import java.util.List;

import com.nubefact.ose.entity.Migrado;

public interface IMigradoDAO {

	List<Migrado> getRegistros(Date fechaInicio, Date fechaFin);
	
	void update(Migrado migrado);
}
