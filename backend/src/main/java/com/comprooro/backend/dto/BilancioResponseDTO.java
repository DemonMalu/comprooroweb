package com.comprooro.backend.dto;

import java.math.BigDecimal;

public class BilancioResponseDTO {
    private BigDecimal totaleEntrate;
    private BigDecimal totaleUscite;
    private BigDecimal totaleRimanenze;

    public BilancioResponseDTO(BigDecimal totaleEntrate, BigDecimal totaleUscite, BigDecimal totaleRimanenze) {
        this.totaleEntrate = totaleEntrate;
        this.totaleUscite = totaleUscite;
        this.totaleRimanenze = totaleRimanenze;
    }

    public BigDecimal getTotaleEntrate() {
        return totaleEntrate;
    }

    public BigDecimal getTotaleUscite() {
        return totaleUscite;
    }
    
    public BigDecimal getTotaleRimanenze() {
    	return totaleRimanenze;
    }
    
    public void setTotaleEntrate(BigDecimal totaleEntrate) {
		this.totaleEntrate = totaleEntrate;
	}

	public void setTotaleUscite(BigDecimal totaleUscite) {
		this.totaleUscite = totaleUscite;
	}

	public void setTotaleRimanenze(BigDecimal totaleRimanenze) {
		this.totaleRimanenze = totaleRimanenze;
	}
}


