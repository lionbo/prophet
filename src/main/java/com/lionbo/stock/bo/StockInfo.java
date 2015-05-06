package com.lionbo.stock.bo;

import java.util.ArrayList;
import java.util.List;

public class StockInfo {

	private String code;
	
	private String name;
	//沪深
	private String market;
	
	private List<StockPrice> prices;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public List<StockPrice> getPrices() {
		if(prices==null)
		{
			prices = new ArrayList<StockPrice>();
		}
		return prices;
	}
	public void setPrices(List<StockPrice> prices) {
		this.prices = prices;
	}
	
}
