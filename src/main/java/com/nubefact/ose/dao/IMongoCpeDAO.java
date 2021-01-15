package com.nubefact.ose.dao;

import com.nubefact.ose.entity.mongo.MongoCpe;

public interface IMongoCpeDAO{
    
    void saveCpe(MongoCpe mongoCpe);
    
    void saveOnlySecond(MongoCpe mongoCpe);
    
    MongoCpe getByIdTicket(Long idTicket);
    
    MongoCpe getByIdTicketSecond(Long idTicket);
    
    MongoCpe getByNomDoc(String nombreDoc);
    
    void updateCdrSunat(MongoCpe mongoCpe);
    
    void updateCdrOse(MongoCpe mongoCpe);
    
    void deleteByNombreDoc(String nombreDoc);
    
    void deleteAll(String ruc);
    
    String check();

}