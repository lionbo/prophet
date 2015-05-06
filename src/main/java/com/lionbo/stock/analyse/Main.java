package com.lionbo.stock.analyse;

import java.util.List;

import com.lionbo.stock.bo.StockInfo;
import com.lionbo.stock.bo.StockPrice;
import com.lionbo.stock.utils.StockUtils;

public class Main {

	public static void main(String[] args) {
		List<StockInfo> infos = StockUtils.buildStockInfo(2015,5,4,2015,5,6);
		
		//以下部分可以抽离为满足条件的策略分析，分为条件、结论、分析三个部分
		for(StockInfo info :infos)
		{
			if(info.getPrices().size()>0)
			{
				List<StockPrice> prices = info.getPrices();
				boolean isCondtionSatisfy = true;
				for(int i = prices.size()-1;i>=0;i--)
				{
					StockPrice price = prices.get(i);
					isCondtionSatisfy = isCondtionSatisfy&(!price.isRise());
					if(!isCondtionSatisfy)
					{
						break;
					}
				}
				if(isCondtionSatisfy)
				{
					System.out.println(info.getName()+":"+info.getCode());
				}
			}
			
		}
	
	}
}
