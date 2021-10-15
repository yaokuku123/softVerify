package com.ustb.softverify.exception;

/*
    证书上链存证异常
 */
public class CertificateUpChainException extends RuntimeException {
    public CertificateUpChainException() {
    }

    public CertificateUpChainException(String message) {
        super(message);
    }
}
