package edu.espe.springpruebaoscarchanataxi.web.advice;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
