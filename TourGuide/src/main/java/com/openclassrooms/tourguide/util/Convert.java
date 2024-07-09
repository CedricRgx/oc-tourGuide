package com.openclassrooms.tourguide.util;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.tourguide.DTO.NearAttractionsDTO;

import java.util.List;

public class Convert {

    public String listDTOToJSON(List<NearAttractionsDTO> listToConvert){
        return JsonStream.serialize(listToConvert);
    }
}
