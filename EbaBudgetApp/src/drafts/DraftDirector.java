package drafts;

import dataObjects.Vendor;
import moduleObjects.EnvelopeModule;
import moduleObjects.StatementModule;
import moduleObjects.VendorModule;
import tickets.RequestTicket;
import tickets.ResponseTicket;

public class DraftDirector {

	/*
	 * CREATE
	 * REMOVE
	 * EDIT
	 * MERGE
	 * SPLIT
	 * 
	 * 
	 * STATEMENT
	 * ENVELOPE
	 * VENDOR
	 * BALANCE
	 * 
	 * */
	public static ResponseTicket draftStatementRequest(RequestTicket request, StatementModule module) {
		
		ResponseTicket response = new ResponseTicket();
		
		switch(request.request){
			default: {
				response.addErrorMessage("INVALID STATEMENT REQUEST");
				return response;
			}
			case "CREATE": return DraftStatement.createStatement(response, module);
			case "REMOVE": return DraftStatement.removeStatement(response, module);
			case "EDIT": return DraftStatement.editStatement(response, module);
			case "MERGE": return DraftStatement.mergeStatement(response, module);
			case "SPLIT": return DraftStatement.splitStatement(response, module);
			case "GET": return DraftStatement.getStatement(response, module);
		}

	}
	
	public static ResponseTicket draftEnvelopeRequest(RequestTicket request, EnvelopeModule module) {

		ResponseTicket response = new ResponseTicket();

		switch(request.request){
			default: {
				response.addErrorMessage("INVALID ENVELOPE REQUEST");
				return response;
			}
			case "CREATE": return DraftEnvelope.createEnvelope(response, module);
			case "REMOVE": return DraftEnvelope.removeEnvelope(response, module);
			case "EDIT": return DraftEnvelope.editEnvelope(response, module);
			case "GET": return DraftEnvelope.getEnvelope(response, module);
		}

	}
	
	public static ResponseTicket draftVendorRequest(RequestTicket request, VendorModule module) {
		
		ResponseTicket response = new ResponseTicket();

		switch(request.request){
			default: {
				response.addErrorMessage("INVALID VENDOR REQUEST");
				return response;
			}
			case "CREATE": return DraftVendor.createVendor(response, module);
			case "EDIT": return DraftVendor.editVendor(response, module);
			case "GET": return DraftVendor.getVendor(response, module);
		}

	}
	
	public static ResponseTicket draftBalanceRequest(RequestTicket request) {

		ResponseTicket response = new ResponseTicket();
		switch(request.request){
			default: {
				response.addErrorMessage("INVALID BALANCE REQUEST");
				return response;
			}
			case "GET": return DraftBalance.getBalance(response);
			case "VALIDATE": return DraftBalance.Validate(response);
		}
	}
	
	
}
