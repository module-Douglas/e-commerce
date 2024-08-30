package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.config.exception.ValidationException;
import io.github.douglas.ms_product.dto.SupplierDTO;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.SupplierService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Integer CNPJ_LENGTH = 14;
    private static final Integer FIRST_DIGIT_INDEX = 12;
    private static final Integer SECOND_DIGIT_INDEX = 13;
    private static final Integer[] FIRST_DIGIT_VALIDATION = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final Integer[] SECOND_DIGIT_VALIDATION = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final String CNPJ_PATTERN = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}";
    private static final String PHONE_NUMBER_PATTERN = "\\(\\d{2}\\) \\d{5}-\\d{4}";
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDTO registerSupplier(SupplierDTO request) {
        checkName(request.name());
        checkEmail(request.email());
        checkPhoneNumber(request.phoneNumber());
        checkCnpj(request.cnpj());

        var supplier = new Supplier(
                request.name().toUpperCase(),
                request.email(),
                cleanCnpj(request.cnpj()),
                request.phoneNumber());

        return new SupplierDTO(
                supplierRepository.save(supplier));
    }

    @Override
    public SupplierDTO getSupplierDetails(UUID id) {
        return new SupplierDTO(
                supplierRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(supplierNotFoundMessage(id))));
    }

    @Override
    public SupplierDTO updateSupplier(SupplierDTO request) {
        var supplier = supplierRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(supplierNotFoundMessage(request.id())));

        if (!supplier.getCnpj().equals(request.cnpj()))
            checkCnpj(request.cnpj());
        if (!supplier.getEmail().equals(request.email()))
            checkEmail(request.email());
        if (!supplier.getPhoneNumber().equals(request.phoneNumber()))
            checkPhoneNumber(request.phoneNumber());
        if(!supplier.getName().equals(request.name()))
            checkName(request.name());

        supplier.setName(request.name());
        supplier.setCnpj(request.cnpj());
        supplier.setEmail(request.email());
        supplier.setPhoneNumber(request.phoneNumber());

        return new SupplierDTO(
                supplierRepository.save(supplier));
    }

    @Override
    public void deleteSupplier(SupplierDTO request) {
        var supplier = supplierRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(supplierNotFoundMessage(request.id())));
        supplierRepository.delete(supplier);
    }

    private void checkName(String name) {
        if (name == null || name.isEmpty() || name.isBlank())
            throw new ValidationException("Invalid name parameters.");

        if (supplierRepository.existsByName(name))
            throw new DataIntegrityViolationException(format("Supplier name %s already registered.", name));
    }

    private void checkEmail(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email))
            throw new ValidationException("Invalid email.");

        if (supplierRepository.existsByEmail(email))
            throw new DataIntegrityViolationException(format("Email %s already registered.", email));
    }

    private void checkPhoneNumber(String phoneNumber) {
        if (!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber))
            throw new ValidationException("Invalid phone number.");

        if (supplierRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException(format("Phone number %s already registered.", phoneNumber));
    }

    private void checkCnpj(String cnpj) {
        if (!Pattern.matches(CNPJ_PATTERN, cnpj))
            throw new ValidationException("Invalid CNPJ format.");

        var formatedCnpj = cleanCnpj(cnpj);
        if (formatedCnpj.length() != CNPJ_LENGTH)
            throw new ValidationException("Invalid CNPJ size.");

        var sum = 0;
        for (int i = 0; i < FIRST_DIGIT_INDEX; i ++) {
            sum += parseInt(valueOf(formatedCnpj.charAt(i))) * FIRST_DIGIT_VALIDATION[i];
        }

        var mod = (sum % 11);
        char firstDigit = (mod == 0 || mod == 1) ? '0' : (char) ('0' + (11 - mod));

        sum = 0;
        for (int i = 0; i < SECOND_DIGIT_INDEX; i++) {
            sum += parseInt(valueOf(formatedCnpj.charAt(i))) * SECOND_DIGIT_VALIDATION[i];
        }

        mod = (sum % 11);
        char secondDigit = (mod == 0 || mod == 1) ? '0': (char) ('0' + (11 - mod));

        if ((formatedCnpj.charAt(FIRST_DIGIT_INDEX) != firstDigit) || (formatedCnpj.charAt(SECOND_DIGIT_INDEX) != secondDigit))
            throw new ValidationException("Invalid CNPJ.");

        if (supplierRepository.existsByCnpj(formatedCnpj))
            throw new DataIntegrityViolationException(format("Cnpj %s already registered.", cnpj));
    }

    private String cleanCnpj(String cnpj) {
        return cnpj.replaceAll("\\D", "");
    }

    private static String supplierNotFoundMessage(UUID id) {
        return format("Supplier not found with id: %s.", id);
    }
}
