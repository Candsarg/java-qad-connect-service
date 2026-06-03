package com.ServiceToQADConnect.Controllers;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.json.Json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.ServiceToQADConnect.Configs.Log;
import com.ServiceToQADConnect.Dto.TableData.TableDataRequest;
import com.ServiceToQADConnect.Dto.MrpData.MrpDataRequest;
import com.ServiceToQADConnect.Dto.Rebucketing.RebucketingRequest;
import com.ServiceToQADConnect.Dto.SyncData.SyncDataRequest;
import com.ServiceToQADConnect.Exceptions.ErrorInServer;
import com.ServiceToQADConnect.Services.ProgressConection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/*
 * Clase que maneja los puntos de entrada a la API
 * Un metodo por cada punto de entrada
 * Las variables que tienen definidas se reciben desde el archivo service.properties
 * Se les asigna el valor que tienen en el archivo con la anotacion @value
 * Todos los puntos de entrada van a empezar con /api 
 * 
 * */

@RestController
@RequestMapping("/api")
public class QADConController {

	private static final String STATUS_ERROR = "ERROR";
	private static final String STATUS_INFO = "INFO";
	private static final String STATUS_OK = "OK";
	private static final String CONNECT_LOG = "/apollo/tmp/LOGS/qadconnectlog";


	@Value("${Appserver.url}")
	private String url;
	
	@Value("${Appserver.modo}")
	private String StrSesMode;
	
	private int SesMode;
	
	@Value("${Appserver.name}")
	private String Appserver;
	
	@Value("${certificado}")
	private String certStore;

	@Value("${Appserver.maxthreads}")
	private String max_threads_str;
	
	private int max_threads;

	
	/**
	 * dispara una exepcion si existe un eerror de tipo
	 * ErrorInServer
	 * 
	 * @throws ErrorInServer
	 */
	private void existerror(String error) throws ErrorInServer{
		
		throw new ErrorInServer(error);  
		
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(path = "/connectqad/fetchtabledata", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTableDataAPICall(
			@RequestBody TableDataRequest request)
			
	{
        Log report = new Log();
		JSONArray JsonRet = null;

		try {
			    String info = "Starting /connectqad/fetchtabledata " + request.getSite() + " " + request.getTableName() + " " + request.getWhereClause();
		        report.consoleLog(info, STATUS_INFO);
				JsonRet = this.fetchTableData(report, request.getSite(),request.getTableName(),request.getWhereClause(),request.getMaxRecord());
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
			}			
		return new ResponseEntity<Object>(JsonRet.toString(), HttpStatus.OK);
		
	}

	/**
	 * 
	 * @param report
	 * @param site
	 * @param tableName
	 * @param whereClause
	 * @param maxRecord
	 * @return
	 * @throws ErrorInServer
	 */
	private JSONArray fetchTableData(Log report, String site, String tableName, String whereClause, Integer maxRecord) throws ErrorInServer{

		long startTime = System.currentTimeMillis(); 

		String info;
		String connector = "xxconqry.p";
		try
		{
			int i = Integer.parseInt(max_threads_str.trim());
			max_threads=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}

		try
		{
			int i = Integer.parseInt(StrSesMode.trim());
			SesMode=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}
		info = "Starting Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);
		ProgressConection connectProg = new ProgressConection(
			site,
			url,
			SesMode,
			Appserver,
			connector,
			certStore
		);

		ResultSet ret = connectProg.fetchTableDataCallOnServer(site,tableName,whereClause,maxRecord);


		long stopTime = System.currentTimeMillis();

		long milliseconds = (stopTime - startTime);
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;
		
		info = "Elapsed time was " + minutes + " minutes and "
		+ seconds + " seconds.";
		report.consoleLog(info, STATUS_INFO);

		info = "Finish Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);

		JSONArray Json = convert_resulset_to_JsonArray(report, ret);

		connectProg.closeConnection();
		ret=null;
		return Json;		
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(path = "/connectqad/fetchmrpdata", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMrpAPICall(
			@RequestBody MrpDataRequest request)
			
	{
        Log report = new Log();
		JSONArray JsonRet = null;

		try {
			    String info = "Starting /connectqad/fetchmrpdata " + request.getSite() + " " + request.getMaterial();
		        report.consoleLog(info, STATUS_INFO);
				JsonRet = this.fetchMrpata(report, request.getSite(),request.getMaterial());
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
			}			
		return new ResponseEntity<Object>(JsonRet.toString(), HttpStatus.OK);
		
	}

	/**
	 * 
	 * @param report
	 * @param site
	 * @param material
	 * @return
	 * @throws ErrorInServer
	 */
	private JSONArray fetchMrpata(Log report, String site, String material) throws ErrorInServer{

		long startTime = System.currentTimeMillis(); 

		String info;
		String connector = "xxconMRP.p";
		try
		{
			int i = Integer.parseInt(max_threads_str.trim());
			max_threads=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}

		try
		{
			int i = Integer.parseInt(StrSesMode.trim());
			SesMode=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}
		info = "Starting Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);
		ProgressConection connectProg = new ProgressConection(
			site,
			url,
			SesMode,
			Appserver,
			connector,
			certStore
		);

		ResultSet ret = connectProg.fetchMrpDataCallOnServer(site,material);


		long stopTime = System.currentTimeMillis();

		long milliseconds = (stopTime - startTime);
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;
		
		info = "Elapsed time was " + minutes + " minutes and "
		+ seconds + " seconds.";
		report.consoleLog(info, STATUS_INFO);

		info = "Finish Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);

		JSONArray Json = convert_resulset_to_JsonArray(report, ret);

		connectProg.closeConnection();
		ret=null;
		return Json;		
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(path = "/connectqad/fetchsyncdata", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSyncDataAPICall(
			@RequestBody SyncDataRequest request)
			
	{
        Log report = new Log();
		JSONArray JsonRet = null;

		try {
			    String info = "Starting /connectqad/fetchsyncdata " + request.getSite() + " " + request.getStartSeq();
		        report.consoleLog(info, STATUS_INFO);

				JsonRet = this.fetchSyncData(report, request.getSite(),request.getStartSeq(),request.getMaxRecord());
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
			}			
		return new ResponseEntity<Object>(JsonRet.toString(), HttpStatus.OK);
		
	}

	/**
	 * 
	 * @param report
	 * @param site
	 * @param startSeq
	 * @param maxRecord
	 * @return
	 * @throws ErrorInServer
	 */
	private JSONArray fetchSyncData(Log report, String site, Long startSeq, Integer maxRecord) throws ErrorInServer{

		long startTime = System.currentTimeMillis(); 

		String info;
		String connector = "xxsyncget.p";
		try
		{
			int i = Integer.parseInt(max_threads_str.trim());
			max_threads=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}

		try
		{
			int i = Integer.parseInt(StrSesMode.trim());
			SesMode=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}
		info = "Starting Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);
		ProgressConection connectProg = new ProgressConection(
			site,
			url,
			SesMode,
			Appserver,
			connector,
			certStore
		);

		ResultSet ret = connectProg.fetchSyncDataCallOnServer(site,startSeq,maxRecord);


		long stopTime = System.currentTimeMillis();

		long milliseconds = (stopTime - startTime);
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;
		
		info = "Elapsed time was " + minutes + " minutes and "
		+ seconds + " seconds.";
		report.consoleLog(info, STATUS_INFO);

		info = "Finish Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);

		JSONArray Json = convert_resulset_to_JsonArray(report, ret);

		connectProg.closeConnection();
		ret=null;
		return Json;		
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(path = "/connectqad/rebucketing", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSyncDataAPICall(
			@RequestBody RebucketingRequest request)
			
	{
        Log report = new Log();
		Boolean lSuccess = false;

		try {
			    String info = "Starting /connectqad/rebucketing " + request.getSite();
		        report.consoleLog(info, STATUS_INFO);

				lSuccess = this.runRebucketing(report, request);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
			}	

		JSONObject JsonRet = new JSONObject();

        JsonRet.put("success", lSuccess);

		return new ResponseEntity<Object>(JsonRet.toString(), HttpStatus.OK);
		
	}

	/**
	 * 
	 * @param report
	 * @param request
	 * @return
	 * @throws ErrorInServer
	 */
	public boolean runRebucketing(Log report, RebucketingRequest request) throws ErrorInServer{

		long startTime = System.currentTimeMillis(); 

		String info;
		String connector = "xxcon_initdet.p";
		try
		{
			int i = Integer.parseInt(max_threads_str.trim());
			max_threads=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}

		try
		{
			int i = Integer.parseInt(StrSesMode.trim());
			SesMode=i;
		}
		catch (NumberFormatException nfe)
		{
			info = "NumberFormatException: " + nfe.getMessage();
			this.existerror(info);
		}
		info = "Starting Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);

		String site = request.getSite();

		ProgressConection connectProg = new ProgressConection(
			site,
			url,
			SesMode,
			Appserver,
			connector,
			certStore
		);

		boolean lSuccess = connectProg.callRebucketingOnServer(site, request);


		long stopTime = System.currentTimeMillis();

		long milliseconds = (stopTime - startTime);
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;
		
		info = "Elapsed time was " + minutes + " minutes and "
		+ seconds + " seconds.";
		report.consoleLog(info, STATUS_INFO);

		info = "Finish Connection whith Progress";
		report.consoleLog(info, STATUS_INFO);

		connectProg.closeConnection();
		return lSuccess;		
	}

	/**
	 * 
	 * @param report
	 * @param rs
	 * @return
	 */
	public JSONArray convert_resulset_to_JsonArray(Log report, ResultSet rs) {

		JSONArray jArray = new JSONArray();

		if (rs == null) {
			report.consoleLog("ResultSet is NULL", "ERROR");
			return jArray;
		}
		
		try {

			ResultSetMetaData rsmd = rs.getMetaData();
			int cantColumm = rsmd.getColumnCount();

			while (rs.next()) {

				JSONObject obj = new JSONObject();

				for (int index = 1; index <= cantColumm; index++) {

					String coumname = rsmd.getColumnName(index);
					Object value = rs.getObject(coumname);

					if (value == null) {

						obj.put(coumname, JSONObject.NULL);

					} else if (value instanceof Integer) {

						obj.put(coumname, (Integer) value);

					} else if (value instanceof String) {

						String valor = (String) value;

						if (valor.equalsIgnoreCase("vacio")) {
							obj.put(coumname, JSONObject.NULL);
						} else {
							obj.put(coumname, valor);
						}

					} else if (value instanceof Boolean) {

						obj.put(coumname, (Boolean) value);

					} else if (value instanceof Date) {

						obj.put(coumname, value.toString());

					} else if (value instanceof Long) {

						obj.put(coumname, (Long) value);

					} else if (value instanceof Double) {

						obj.put(coumname, (Double) value);

					} else if (value instanceof Float) {

						obj.put(coumname, (Float) value);

					} else if (value instanceof BigDecimal) {

						obj.put(coumname, (BigDecimal) value);

					} else if (value instanceof Byte) {

						obj.put(coumname, (Byte) value);

					} else {

						obj.put(coumname, value.toString());

					}
				}

				jArray.put(obj);
			}

		} catch (Exception e) {

			report.consoleLog(e.getMessage(), "ERROR");
		}

		return jArray;
	}
}
