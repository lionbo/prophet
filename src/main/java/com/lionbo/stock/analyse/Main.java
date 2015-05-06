package com.lionbo.stock.analyse;

import java.util.List;

import com.lionbo.stock.bo.StockInfo;
import com.lionbo.stock.bo.StockPrice;
import com.lionbo.stock.utils.StockUtils;

public class Main {

	public static void main(String[] args) {
		List<StockInfo> infos = StockUtils.buildStockInfo(2015,4,30,2015,5,5);
		for(StockInfo info :infos)
		{
			if(info.getPrices().size()>0)
			{
				System.out.println(info.getCode()+":"+info.getName());
				List<StockPrice> prices = info.getPrices();
				for(StockPrice price : prices)
				{
					System.out.println(price.getDate()+":"+price.isRise()+":"+price.getOpen()+":"+price.getClose());
				}
			}
		}
	
	}
}
