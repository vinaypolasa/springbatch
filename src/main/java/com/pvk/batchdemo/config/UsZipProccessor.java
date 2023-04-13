package com.pvk.batchdemo.config;

import com.pvk.batchdemo.entity.Uszip;
import org.springframework.batch.item.ItemProcessor;

public class UsZipProccessor implements ItemProcessor<Uszip,Uszip> {

    @Override
    public Uszip process(Uszip item) throws Exception {
        if(item.getCountry().equals("Barnstable")){
            return item;
        }
        return null;
    }
}
