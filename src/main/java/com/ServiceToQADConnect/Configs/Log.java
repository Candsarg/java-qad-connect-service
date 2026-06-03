package com.ServiceToQADConnect.Configs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
	
	/**
	 * 
	 * @param cURL
	 * @param Data
	 * @param status
	 */
	public void fileLog(String site, String cURL, String Data, String status ) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		DateTimeFormatter filePatt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha =dtf.format(LocalDateTime.now());
		String fechaFile = filePatt.format(LocalDateTime.now());
        PrintWriter pw = null;
		FileWriter output=null;
		cURL = cURL + "_" + site + "_" + fechaFile + ".log";
		
		try {
			
			output = new FileWriter(cURL,true);
			pw = new PrintWriter(output);
			pw.println("["+ fecha + "] -- ["+ status + "] -- " +Data);
			
		} catch (IOException e2) {
			
			e2.printStackTrace();
			
		}finally {
			
	           try {
	        	   
	               if (null != output)
	            	   
	            	   output.close();
	               
	               } catch (Exception e2) {
	            	   
	                  e2.printStackTrace();
	                  
	               }
	           
	            }
	}
	
	public void consoleLog (String Data, String status ) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String fecha =dtf.format(LocalDateTime.now());
        
        System.out.println("["+ fecha + "] -- ["+ status + "] -- " +Data);
	}

}
