package drafts;

import moduleObjects.EnvelopeModule;
import moduleObjects.StatementModule;
import moduleObjects.VendorModule;
import tickets.RequestTicket;
import tickets.ResponseTicket;

public class DraftDirector {


	public static ResponseTicket draftStatementRequest(RequestTicket request, StatementModule module) {
		
		ResponseTicket response = new ResponseTicket();
		
		switch(request.request){
			default: {
				response.addErrorMessage("INVALID STATEMENT REQUEST");
				return response;
			}
			case "CREATE": DraftStatement.createStatement(response, module);
			case "REMOVE": DraftStatement.removeStatement(response, module);
			case "EDIT": DraftStatement.editStatement(response, module);
//			case "MERGE": return DraftStatement.mergeStatement(response, module);
//			case "SPLIT": return DraftStatement.splitStatement(response, module);
			case "GET": DraftStatement.getStatement(response, module);
		}
		return response;
	}
	
	public static ResponseTicket draftEnvelopeRequest(RequestTicket request, EnvelopeModule module) {

		ResponseTicket response = new ResponseTicket();

		switch(request.request){
			default: {
				response.addErrorMessage("INVALID ENVELOPE REQUEST");
				return response;
			}
			case "CREATE": DraftEnvelope.createEnvelope(response, module);
			case "REMOVE": DraftEnvelope.removeEnvelope(response, module);
			case "EDIT": DraftEnvelope.editEnvelope(response, module);
			case "GET": DraftEnvelope.getEnvelope(response, module);
		}
		return response;
	}
	
	public static ResponseTicket draftVendorRequest(RequestTicket request, VendorModule module) {
		
		ResponseTicket response = new ResponseTicket();

		switch(request.request){
			default: {
				response.addErrorMessage("INVALID VENDOR REQUEST");
				return response;
			}
			case "CREATE": DraftVendor.createVendor(response, module);
			case "EDIT": DraftVendor.editVendor(response, module);
			case "GET": DraftVendor.getVendor(response, module);
		}
		return response;
	}
	
	public static ResponseTicket draftBalanceRequest(RequestTicket request) {

		ResponseTicket response = new ResponseTicket();
		switch(request.request){
			default: {
				response.addErrorMessage("INVALID BALANCE REQUEST");
				return response;
			}
			case "GET": DraftBalance.getBalance(response);
			case "VALIDATE": DraftBalance.Validate(response);
		}
		return response;
		
	}
	
	
}
