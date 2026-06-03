package com.ServiceToQADConnect.Services;


import com.progress.open4gl.*;
import com.progress.open4gl.javaproxy.Connection;
import com.progress.open4gl.javaproxy.OpenAppObject;
import com.progress.open4gl.javaproxy.ParamArray;
import com.progress.open4gl.javaproxy.ParamArrayMode;
import com.ServiceToQADConnect.Dto.Rebucketing.RebucketingReqData;
import com.ServiceToQADConnect.Dto.Rebucketing.RebucketingRequest;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ServiceToQADConnect.Configs.Log;
import com.ServiceToQADConnect.Exceptions.ErrorInServer;


public class ProgressConection {
	
	private OpenAppObject connection;
	private String program;
	private String logRoute;
	private String error;
	private String site;
	private String keyInp = "b8509200daa6f6185fe56ffc625d1d060e2f12c3dcd1ff6c3772ac3a3a22a961";

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param session
	 * @param appServerName
	 * @param programaPuente
	 * @param certStore
	 */
	public ProgressConection(
			String site,
			String url, 
			int session, 
			String appServerName, 
			String programaPuente, 
			String certStore
			) {
				this.site = site;
			
			this.error = "";
			this.logRoute ="log/errorConections";
			this.program= programaPuente ;
			Log report1 = new Log();
			
			report1.fileLog(this.site,this.logRoute, "site=" + site +
							", url=" + url +
							", session=" + session +
							", appServerName=" + appServerName +
							", programaPuente=" + programaPuente +
							", certStore=" + certStore, "ERROR");
			try {
				
				RunTimeProperties.setCertificateStore(certStore);
				RunTimeProperties.setNoHostVerify(true);
				Connection myConn = new Connection(url,"","","");
				myConn.setSessionModel(session);
				this.connection = new OpenAppObject(myConn, appServerName);
			}catch (Exception e) {
				
				this.print_Errors(e);
				throw new RuntimeException("Unable to establish OpenEdge connection",e);				
			}
			
		}
	
	/**
	 * Get Rrrors
	 * 
	 * @return String errors
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * Imprime exepciones en pantalla y en consola
	 * 
	 * @param e
	 */
	private void print_Errors(Exception e) {
			
			Log report = new Log();
			
			report.fileLog(this.site,this.logRoute, e.toString(), "ERROR");
			
			e.printStackTrace();
			
			this.error = e.getMessage();
	}
	
	/**
	 * dispara una exepcion si existe un eerror de tipo
	 * ErrorInServer
	 * 
	 * @throws ErrorInServer
	 */
	public void existerror() throws ErrorInServer{
		if (this.error != "") {
			throw new ErrorInServer(this.error);  
		}
	}
	
	/**
	 * 
	 * @param site
	 * @param tableName
	 * @param whereClause
	 * @param maxRecord
	 * @return
	 */
	public ResultSet fetchTableDataCallOnServer (String site, String tableName, String whereClause, Integer maxRecord){
		
		ResultSet output = null;
		
		ParamArray parms = new ParamArray(6);
		ProResultSetMetaDataImpl proMetaData = new ProResultSetMetaDataImpl(20) ;
		
		try {
			if (this.connection == null) {
				throw new RuntimeException(
					"Connection object is null"
				);
			}

			parms.addTableHandle(0, null, ParamArrayMode.OUTPUT, proMetaData);
			parms.addCharacter(1,keyInp,ParamArrayMode.INPUT);
			parms.addCharacter(2,site,ParamArrayMode.INPUT);
			parms.addCharacter(3,tableName,ParamArrayMode.INPUT);
			parms.addCharacter(4,whereClause, ParamArrayMode.INPUT);
			parms.addInteger(5,maxRecord, ParamArrayMode.INPUT);
			this.connection.runProc(this.program, parms);

			output = (ResultSet) parms.getOutputParameter(0);
		} catch (Exception e) {
			this.print_Errors(e);
		}
		return output;
	}

	/**
	 * 
	 * @param site
	 * @param material
	 * @return
	 */
	public ResultSet fetchMrpDataCallOnServer (String site, String material){
		
		ResultSet output = null;
		
		ParamArray parms = new ParamArray(4);
		ProResultSetMetaDataImpl proMetaData = new ProResultSetMetaDataImpl(20) ;
		
		try {
			if (this.connection == null) {
				throw new RuntimeException(
					"Connection object is null"
				);
			}

			parms.addTableHandle(0, null, ParamArrayMode.OUTPUT, proMetaData);
			parms.addCharacter(1,keyInp,ParamArrayMode.INPUT);
			parms.addCharacter(2,site,ParamArrayMode.INPUT);
			parms.addCharacter(3,material,ParamArrayMode.INPUT);
			this.connection.runProc(this.program, parms);

			output = (ResultSet) parms.getOutputParameter(0);
		} catch (Exception e) {
			this.print_Errors(e);
		}
		return output;
	}

	/**
	 * 
	 * @param site
	 * @param startSeq
	 * @param maxRecord
	 * @return
	 */
	public ResultSet fetchSyncDataCallOnServer (String site, Long startSeq, Integer maxRecord){
		
		ResultSet output = null;
		String strStartSeq = String.valueOf(startSeq);
		ParamArray parms = new ParamArray(5);
		ProResultSetMetaDataImpl proMetaData = new ProResultSetMetaDataImpl(20) ;
		
		try {
			if (this.connection == null) {
				throw new RuntimeException(
					"Connection object is null"
				);
			}

			parms.addTableHandle(0, null, ParamArrayMode.OUTPUT, proMetaData);
			parms.addCharacter(1,keyInp,ParamArrayMode.INPUT);
			parms.addCharacter(2,site,ParamArrayMode.INPUT);
			parms.addCharacter(3,strStartSeq,ParamArrayMode.INPUT);
			parms.addInteger(4,maxRecord, ParamArrayMode.INPUT);
			
			this.connection.runProc(this.program, parms);

			output = (ResultSet) parms.getOutputParameter(0);
		} catch (Exception e) {
			this.print_Errors(e);
		}
		return output;
	}

	/**
	 * 
	 * @param site
	 * @param rebuckRequest
	 * @return
	 */
	public boolean callRebucketingOnServer (String site, RebucketingRequest rebuckRequest){
		
		Boolean success = false;
		
		ParamArray parms = new ParamArray(3);
		ProResultSetMetaDataImpl proMetaData = new ProResultSetMetaDataImpl(8) ;
		
		try {
			if (this.connection == null) {
				throw new RuntimeException(
					"Connection object is null"
				);
			}

			// Meta data object(tabla temporal)  
			ProDataObjectMetaData proDataObjectMetaData = new ProDataObjectMetaData("tt_xxinit_table",7,false,1,"","","");
			proDataObjectMetaData.setFieldMetaData(1,"tt_xxintib_dom",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(2,"tt_xxintib_seq",0,Parameter.PRO_INTEGER,0,0);
			proDataObjectMetaData.setFieldMetaData(3,"tt_xxintib_datetime",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(4,"tt_xxintib_transaction",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(5,"tt_xxintib_keyvalue",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(6,"tt_xxintib_status",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(7,"tt_xxintib_data",0,Parameter.PRO_CHARACTER,0,0);
			proDataObjectMetaData.setFieldMetaData(8,"tt_xxintib_source",0,Parameter.PRO_CHARACTER,0,0);

			// Meta data del datagraph (que contiene el object)            
			ProDataGraphMetaData proDataGraphMetaData = new ProDataGraphMetaData(1);
			proDataGraphMetaData.addTable(proDataObjectMetaData);
			
			//Data Graph que es lo que se envia al PROGRESS            
			ProDataGraph proDataGraph = new ProDataGraph(proDataGraphMetaData);
			// Registros de la tabla temporal(se guardan en el Data Graph)     
			if (rebuckRequest.getRebucketData().length > 0) {
				
				for (RebucketingReqData rebuckData : rebuckRequest.getRebucketData()) {
					
					ProDataObject proDataObject = proDataGraph.createProDataObject("tt_xxinit_table");
					
					proDataObject.set("tt_xxintib_dom",rebuckData.getTt_xxintib_dom());
					proDataObject.set("tt_xxintib_seq",rebuckData.getTt_xxintib_seq());
					proDataObject.set("tt_xxintib_datetime",rebuckData.getTt_xxintib_datetime());
					proDataObject.set("tt_xxintib_transaction",rebuckData.getTt_xxintib_transaction());
					proDataObject.set("tt_xxintib_keyvalue",rebuckData.getTt_xxintib_keyvalue());
					proDataObject.set("tt_xxintib_status",rebuckData.getTt_xxintib_status());
					proDataObject.set("tt_xxintib_data",rebuckData.getTt_xxintib_data());
					proDataObject.set("tt_xxintib_source",rebuckData.getTt_xxintib_source());
					proDataGraph.addProDataObject(proDataObject);
				}     
			} 

			parms.addTable(0,proDataGraph,ParamArrayMode.INPUT,proDataGraphMetaData);
			parms.addCharacter(1,site,ParamArrayMode.INPUT);
			parms.addLogical(2,success,ParamArrayMode.OUTPUT);
			this.connection.runProc(this.program, parms);
		} catch (Exception e) {
			this.print_Errors(e);
		}
		return success;
	}

	public void closeConnection() {

		try {

			if (this.connection != null) {
				this.connection._release();
			}

		} catch (Exception e) {

			this.print_Errors(e);

		}
	}
}
