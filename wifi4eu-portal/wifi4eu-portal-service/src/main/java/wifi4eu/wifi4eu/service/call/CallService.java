package wifi4eu.wifi4eu.service.call;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.CallDTO;
import wifi4eu.wifi4eu.mapper.call.CallMapper;
import wifi4eu.wifi4eu.mapper.voucherManagement.VoucherManagementMapper;
import wifi4eu.wifi4eu.repository.call.CallRepository;
import wifi4eu.wifi4eu.repository.voucherManagement.VoucherManagementRepository;

import java.util.List;

@Service
public class CallService {
    @Autowired
    CallMapper callMapper;

    @Autowired
    CallRepository callRepository;

    @Autowired
    VoucherManagementRepository voucherManagementRepository;

    @Autowired
    VoucherManagementMapper voucherManagementMapper;

    public List<CallDTO> getAllCalls() {
        return callMapper.toDTOList(Lists.newArrayList(callRepository.findAll()));
    }

    public CallDTO getCallById(int callId) {
        return callMapper.toDTO(callRepository.findOne(callId));
    }

//    public CallDTO createCall(CallDTO callDTO) {
//        CallDTO resCallDTO = callMapper.toDTO(callRepository.save(callMapper.toEntity(callDTO)));
//        VoucherManagementDTO voucherManagementDTO = new VoucherManagementDTO();
//        voucherManagementDTO.setCall_id(resCallDTO.getId());
//        voucherManagementRepository.save(voucherManagementMapper.toEntity(voucherManagementDTO));
//        return callMapper.toDTO(callRepository.findOne(resCallDTO.getId()));
//    }
//
//    public CallDTO deleteCall(int callId) {
//
//        //TODO: change to a logic delete
//        CallDTO callDTO = callMapper.toDTO(callRepository.findOne(callId));
//        if (callDTO != null) {
//            callRepository.delete(callMapper.toEntity(callDTO));
//            return callDTO;
//        } else {
//            return null;
//        }
//    }
}