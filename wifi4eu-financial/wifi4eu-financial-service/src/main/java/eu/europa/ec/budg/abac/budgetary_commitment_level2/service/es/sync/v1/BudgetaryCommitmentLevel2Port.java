package eu.europa.ec.budg.abac.budgetary_commitment_level2.service.es.sync.v1;

import eu.europa.ec.budg.abac.budgetary_commitment_level2.v1.BudgetaryCommitmentLevel2GetRequestType;
import eu.europa.ec.budg.abac.budgetary_commitment_level2.v1.BudgetaryCommitmentLevel2GetResponseType;
import eu.europa.ec.budg.abac.messagefault.v1.FaultMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2017-07-28T12:43:19.692+02:00
 * Generated source version: 3.0.4
 */
@WebService(targetNamespace = "http://www.ec.europa.eu/budg/abac/budgetary_commitment_level2/service/es/sync/v1", name = "BudgetaryCommitmentLevel2Port")
@XmlSeeAlso({eu.europa.ec.budg.abac.ares_document.v1.ObjectFactory.class, eu.europa.ec.budg.abac.br.v1.ObjectFactory.class, eu.europa.ec.budg.abac.complex_type.v1.ObjectFactory.class, eu.europa.ec.budg.abac.posting_criteria.v1.ObjectFactory.class, eu.europa.ec.budg.abac.organisation.v1.ObjectFactory.class, eu.europa.ec.budg.abac.local_abac_document_detail.v1.ObjectFactory.class, eu.europa.ec.budg.abac.responsible_user.v1.ObjectFactory.class, eu.europa.ec.budg.abac.legal_entity_bank_account_link.v1.ObjectFactory.class, eu.europa.ec.budg.abac.external_criteria.v1.ObjectFactory.class, eu.europa.ec.budg.abac.recovery_context.v1.ObjectFactory.class, eu.europa.ec.budg.abac.posting_criteria_link.v1.ObjectFactory.class, eu.europa.ec.budg.abac.workflow_object.v1.ObjectFactory.class, eu.europa.ec.budg.abac.currency.v1.ObjectFactory.class, eu.europa.ec.budg.abac.external_criteria_code.v1.ObjectFactory.class, eu.europa.ec.budg.abac.wbs.v1.ObjectFactory.class, eu.europa.ec.budg.abac.suspension.v1.ObjectFactory.class, eu.europa.ec.budg.abac.organisational_group.v1.ObjectFactory.class, eu.europa.ec.budg.abac.performance_guarantee_indicator.v1.ObjectFactory.class, eu.europa.ec.budg.abac.supporting_document.v1.ObjectFactory.class, eu.europa.ec.budg.abac.workflow.v1.ObjectFactory.class, eu.europa.ec.budg.abac.local_abac_document.v1.ObjectFactory.class, eu.europa.ec.budg.abac.central_reference.v1.ObjectFactory.class, eu.europa.ec.budg.abac.abac_object.v1.ObjectFactory.class, eu.europa.ec.budg.abac.prefinancing_clearing.v1.ObjectFactory.class, eu.europa.ec.budg.abac.message.v1.ObjectFactory.class, eu.europa.ec.budg.abac.budgetary_commitment_level2.v1.ObjectFactory.class, eu.europa.ec.budg.abac.contractor_reference.v1.ObjectFactory.class, eu.europa.ec.budg.abac.scanned_document.v1.ObjectFactory.class, eu.europa.ec.budg.abac.address.v1.ObjectFactory.class, eu.europa.ec.budg.abac.external_criteria_type.v1.ObjectFactory.class, eu.europa.ec.budg.abac.country.v1.ObjectFactory.class, eu.europa.ec.budg.abac.bank_account.v1.ObjectFactory.class, eu.europa.ec.budg.abac.associated_country.v1.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface BudgetaryCommitmentLevel2Port {

    @WebMethod(action = "http://www.ec.europa.eu/budg/abac/budgetary_commitment_level2/service/es/sync/get/v1")
    @WebResult(name = "BudgetaryCommitmentLevel2GetResponse", targetNamespace = "http://www.ec.europa.eu/budg/abac/budgetary_commitment_level2/v1", partName = "response")
    public BudgetaryCommitmentLevel2GetResponseType get(
            @WebParam(partName = "request", name = "BudgetaryCommitmentLevel2GetRequest", targetNamespace = "http://www.ec.europa.eu/budg/abac/budgetary_commitment_level2/v1")
                    BudgetaryCommitmentLevel2GetRequestType request
    ) throws FaultMessage;
}
