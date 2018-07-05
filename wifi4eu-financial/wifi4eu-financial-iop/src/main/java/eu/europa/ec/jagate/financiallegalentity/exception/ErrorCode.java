package eu.europa.ec.jagate.financiallegalentity.exception;

/**
 * Index of all (business) errors codes in PDM.
 * Corresponding client and log messages are stored in ErrorMessages.properties.
 */
public enum ErrorCode {
	MOD_SYSTEM(0),
	SYSTEM_FAULT(1),
	UNKNOWN_ERROR(9999),

	//Modules Codes
	MOD_SERVICE(10000),
	MOD_PDM_WEB(20000),
	MOD_PDM_WS(30000),
	MOD_EVENTS(40000),
	MOD_URF_WEB(50000),

	//Service layer error Codes
	SERVICE_UNKNOWN_ERROR(19999),
	SERVICE_DATA_NOT_FOUND(10009),
	SERVICE_LEGAL_ENTITY_DATA_NOT_FOUND(10010),
	SERVICE_LEGAL_ENTITY_AMBIGUOUS_DATA(10011),
	SERVICE_LEGAL_ENTITY_IN_INCORRECT_STATE(10012),
	SERVICE_LEGAL_ENTITY_AMBIGUOUS_STATE(10013),
	SERVICE_USER_NOT_FOUND(10100),
	SERVICE_DOCUMENT_NOT_FOUND(10111),
	SERVICE_DOCUMENT_MANAGEMENT_ERROR(10112),
	SERVICE_ABAC_DATA_NOT_FOUND(10113),
	SERVICE_ENUM_KEY_NOT_KNOWN(10114),
	SERVICE_MESSAGE_ATTACHMENT_ERROR(10115),
	SERVICE_DETECTED_ATTEMPT_TO_CREATE_CIRCULAR_HIERARCHICAL_RELATIONSHIP(10116),
	SERVICE_ABAC_REMOTE(10117),
	SERVICE_EMAIL_NOTIFICATION_FAIL(10118),
	SERVICE_EXTERNAL_NOTIFICATION_RECIPIENT_NULL(10119),
	SERVICE_ABAC_DATA_NOT_ALLOWED_FOR_ACTION(10120),
	SERVICE_ABAC_AMBIGUOUS_DATA(10121),

	SERVICE_ACTION_NOT_DEFINED_FOR_WORKFLOW_STATE(11000),


	// Events error Codes
	EVENTS_UNKNOWN_ERROR(49999),
	EVENTS_MANDATORY_FIELD_MISSING(40001),
	EVENTS_UNKNOWN_FIELD_VALUE(40002),


	NO_ERROR(11000);

	private static ErrorCode[] modules = {MOD_SYSTEM, MOD_SERVICE, MOD_PDM_WEB, MOD_PDM_WS, MOD_EVENTS, MOD_URF_WEB};

	private int code;

	private ErrorCode(int code) {
	    this.code = code;
    }

	public int getCode() {
	    return code;
	}

	public ErrorCode getModule() {
	    for (ErrorCode eCode : modules) {
	        int res = this.code - eCode.getCode();
	        if (res >= 0 && res <= 9999) {
	            return eCode;
	        }
        }
	    return MOD_SYSTEM;
	}
}
