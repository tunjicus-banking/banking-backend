package com.tunjicus.bank.offers;

public enum OfferStatus {
    ACCEPTED((short) 1), REJECTED((short) -1), OFFERED((short) 0);

    public final short status;

    OfferStatus(short status) {
        this.status = status;
    }
}
