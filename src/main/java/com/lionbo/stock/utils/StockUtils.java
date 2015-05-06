package com.lionbo.stock.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.lionbo.stock.bo.StockInfo;
import com.lionbo.stock.bo.StockPrice;

/**
 * StockCode
 */
public class StockUtils 
{
	private static String codeFile = "ALLCODES.txt";
	private static CloseableHttpClient client = HttpClients.createDefault();
    
    public static List<StockInfo> buildStockInfo(int startYear,int startMonth,int startDay,int endYear,int endMonth,int endDay)
    {
    	List<StockInfo> res = new ArrayList<StockInfo>();
    	try {
			InputStreamReader reader = new InputStreamReader(StockUtils.class.getClassLoader().getResourceAsStream(codeFile),"GBK");
			BufferedReader breader = new BufferedReader(reader);
			String title = breader.readLine();
			String line = breader.readLine();
			//忽略第一行的title
			while(line!=null && !line.equals(""))
			{
				String[] array = line.split("\t");
				if(array!=null && array.length>2)
				{
					res.add(buildSingleStockInfo(array));
					line = breader.readLine();
				}
				else{
					line=null;
				}
			}
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	for(StockInfo meta : res)
    	{
    		String url = getUrl(meta, startYear, startMonth, startDay, endYear, endMonth, endDay);
        	buildStockPrice(url,meta);
    	}
    	return res;
    }
    
    /**
     * 完成build之后，保存在本地数据，以后可以搞个db
     * @description 以后这个类可以结合build写成一个模板方法，用来build沪深、香港、美国的
     * @param stockInfos
     */
    public static void doSaveStockInfos(List<StockInfo> stockInfos)
    {
    	//TODO
    }
    
    
    private static String getUrl(StockInfo info,int startYear,int startMonth,int startDay,int endYear,int endMonth,int endDay)
    {
    	StringBuilder url = new StringBuilder("http://table.finance.yahoo.com/table.csv?a=");
    	url.append(startMonth-1);
    	url.append("&b=");
    	url.append(startDay);
    	url.append("&c=");
    	url.append(startYear);
    	url.append("&d=");
    	url.append(endMonth-1);
    	url.append("&e=");
    	url.append(endDay);
    	url.append("&f=");
    	url.append(endYear);
    	url.append("&s=");
    	url.append(info.getCode());
    	url.append(".");
    	url.append(info.getMarket());
    	return url.toString();
    }
    
    private static void buildStockPrice(String url,StockInfo first)
    {
    	HttpGet get = new HttpGet(url);
    	
    	try {
			CloseableHttpResponse response = client.execute(get);
			InputStream is = response.getEntity().getContent();
			String res = IOUtils.toString(is);
			String[] lines = res.split("\n");
			for(int i = 1;i<lines.length;i++)
			{
				String line = lines[i];
				StockPrice price = new StockPrice();
				String[] items = line.split(",");
				price.setDate(items[0]);
				price.setOpen(items[1]);
				price.setHighest(items[2]);
				price.setLowest(items[3]);
				price.setClose(items[4]);
				price.setVolume(items[5]);
				price.setAdjClose(items[6]);
				
				Double open = Double.valueOf(price.getOpen());
				Double close = Double.valueOf(price.getClose());
				if(open>close)
				{
					price.setRise(false);
				}else{
					price.setRise(true);
				}
				first.getPrices().add(price);
			}
    	} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static StockInfo buildSingleStockInfo(String[] array)
    {
    	StockInfo info = new StockInfo();
    	info.setCode(array[0]);
    	info.setName(array[1].trim());
    	if(array[0].trim().startsWith("6"))
    	{
    		info.setMarket("ss");
    	}
    	else
    	{
    		info.setMarket("sz");
    	}
    	
    	return info;
    }
    
}
