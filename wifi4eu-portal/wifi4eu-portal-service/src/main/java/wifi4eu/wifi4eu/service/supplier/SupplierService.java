package wifi4eu.wifi4eu.service.supplier;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.SuppliedRegionDTO;
import wifi4eu.wifi4eu.common.dto.model.SupplierDTO;
import wifi4eu.wifi4eu.mapper.supplier.SuppliedRegionMapper;
import wifi4eu.wifi4eu.mapper.supplier.SupplierMapper;
import wifi4eu.wifi4eu.repository.supplier.SuppliedRegionRepository;
import wifi4eu.wifi4eu.repository.supplier.SupplierRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SuppliedRegionMapper suppliedRegionMapper;

    @Autowired
    SuppliedRegionRepository suppliedRegionRepository;

    public List<SupplierDTO> getAllSuppliers() {
        return supplierMapper.toDTOList(Lists.newArrayList(supplierRepository.findAll()));
    }

    public SupplierDTO getSupplierById(int supplierId) {
        return supplierMapper.toDTO(supplierRepository.findOne(supplierId));
    }

    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        if (supplierDTO.getSuppliedRegions().isEmpty()) {
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(supplierDTO)));
        } else {
            Integer supplierId = supplierDTO.getId();
            List<SuppliedRegionDTO> originalRegions = supplierDTO.getSuppliedRegions();
            List<SuppliedRegionDTO> correctRegions = new ArrayList<>();
            if (supplierId == 0) {
                supplierDTO.setSuppliedRegions(null);
                supplierDTO = supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(supplierDTO)));
                supplierId = supplierDTO.getId();
            }
            for (SuppliedRegionDTO region: originalRegions) {
                region.setSupplierId(supplierId);
                correctRegions.add(region);
            }
            supplierDTO.setSuppliedRegions(correctRegions);
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(supplierDTO)));
        }
    }

    public SupplierDTO deleteSupplier(int supplierId) {
        SupplierDTO supplierDTO = supplierMapper.toDTO(supplierRepository.findOne(supplierId));
        if (supplierDTO != null) {
            supplierRepository.delete(supplierMapper.toEntity(supplierDTO));
            return supplierDTO;
        } else {
            return null;
        }
    }

    public List<SuppliedRegionDTO> getAllSuppliedRegions() {
        return suppliedRegionMapper.toDTOList(Lists.newArrayList(suppliedRegionRepository.findAll()));
    }
}