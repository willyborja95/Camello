package com.apptec.registrateapp.repository.webservices.pojoresponse;

public class WrapperData<T> {

    private T dataResponse;


    public WrapperData(T POJOResponse) {
        this.dataResponse = POJOResponse;
    }

    public T getDataResponse() {
        return dataResponse;
    }
}
