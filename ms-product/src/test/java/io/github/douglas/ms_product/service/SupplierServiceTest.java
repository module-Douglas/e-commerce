package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.config.exception.ValidationException;
import io.github.douglas.ms_product.dto.SupplierDTO;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.impl.SupplierServiceImpl;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SupplierServiceTest {

    SupplierService supplierService;

    @MockBean
    SupplierRepository supplierRepository;

    @BeforeEach
    public void setUp() {
        this.supplierService = new SupplierServiceImpl(supplierRepository);
    }

    @Test
    @DisplayName("Register Supplier success.")
    public void registerSupplierSuccess() {
        var supplier = getValidSupplier();
        var request = getValidSupplierDTO();

        when(supplierRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByCnpj(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByEmail(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByPhoneNumber(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.save(any(Supplier.class)))
                .thenReturn(supplier);

        var response = supplierService.registerSupplier(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(supplier.getId());
        assertThat(response.name()).isEqualTo(supplier.getName());
        assertThat(response.cnpj()).isEqualTo(supplier.getCnpj());
        assertThat(response.email()).isEqualTo(supplier.getEmail());
        assertThat(response.phoneNumber()).isEqualTo(supplier.getPhoneNumber());
    }

    @Test
    @DisplayName("Register fail cause name already in use.")
    public void registerFailNameInUseTest() {
        var request = getValidSupplierDTO();

        when(supplierRepository.existsByName(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> supplierService.registerSupplier(request));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Supplier name %s already registered.", request.name()));
    }

    @Test
    @DisplayName("Register fail cause name is empty.")
    public void registerFailNameEmptyTest() {
        var exception = catchThrowable(() -> supplierService.registerSupplier(
                new SupplierDTO(
                        null,
                        "",
                        null,
                        null,
                        null,
                        null,
                        null)));

        assertThat(exception)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid name parameters.");
    }

    @Test
    @DisplayName("Register fail cause email format is invalid.")
    public void registerFailEmailInvalidTest() {
        var exception = catchThrowable(() -> supplierService.registerSupplier(
                new SupplierDTO(
                    null,
                    "SUPPLIER",
                    null,
                    "email.com",
                    null,
                    null,
                    null
                )));

        assertThat(exception)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid email.");
    }

    @Test
    @DisplayName("Register fail cause email already registered.")
    public void registerFailEmailInUseTest() {
        var request = getValidSupplierDTO();

        when(supplierRepository.existsByEmail(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> supplierService.registerSupplier(request));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Email %s already registered.", request.email()));
    }

    @Test
    @DisplayName("Register fail cause phoneNumber format is invalid.")
    public void registerFailPhoneNumberInvalidTest() {
        var exception = catchThrowable(() -> supplierService.registerSupplier(
                new SupplierDTO(
                    null,
                    "SUPPLIER",
                    null,
                    "email@emal.com",
                    "(45) 9999-99999",
                    null,
                    null
                )));

        assertThat(exception)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid phone number.");
    }

    @Test
    @DisplayName("Register fail cause phoneNumber already registered.")
    public void registerFailPhoneNumberInUseTest() {
        var request = getValidSupplierDTO();

        when(supplierRepository.existsByPhoneNumber(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> supplierService.registerSupplier(request));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Phone number %s already registered.", request.phoneNumber()));
    }

    @Test
    @DisplayName("Register fail cause invalid cnpj format.")
    public void registerFailInvalidCnpjFormatTest() {
        var exception = catchThrowable(() -> supplierService.registerSupplier(
                new SupplierDTO(
                        null,
                        "SUPPLIER",
                        "4.8436.955/0001-12",
                        "email@email.com",
                        "(48) 99999-9999",
                        null,
                        null
                )));

        assertThat(exception)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid CNPJ format.");
    }

    @Test
    @DisplayName("Register fail cause invalid cnpj.")
    public void registerFailInvalidCnpjTest() {
        var exception = catchThrowable(() -> supplierService.registerSupplier(
                new SupplierDTO(
                        null,
                        "SUPPLIER",
                        "48.436.955/0001-34",
                        "email@email.com",
                        "(48) 99999-9999",
                        null,
                        null
                )));

        assertThat(exception)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid CNPJ.");
    }

    @Test
    @DisplayName("Register fail cause cnpj already registered.")
    public void registerFailCnpjInUseTest() {
        var request = getValidSupplierDTO();

        when(supplierRepository.existsByCnpj(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> supplierService.registerSupplier(request));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Cnpj %s already registered.", request.cnpj()));
    }

    @Test
    @DisplayName("Get Supplier by id success.")
    public void getSupplierByIdSuccessTest() {
        var supplier = getValidSupplier();

        when(supplierRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(supplier));

        var response = supplierService.getSupplierDetails(supplier.getId());

        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(supplier.getId());
        assertThat(response.name()).isEqualTo(supplier.getName());
        assertThat(response.cnpj()).isEqualTo(supplier.getCnpj());
        assertThat(response.email()).isEqualTo(supplier.getEmail());
        assertThat(response.phoneNumber()).isEqualTo(supplier.getPhoneNumber());
    }

    @Test
    @DisplayName("Get Supplier by id fail.")
    public void getSupplierByIdFailTest() {
        var id = getValidSupplier().getId();

        when(supplierRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> supplierService.getSupplierDetails(id));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Supplier not found with id: %s.", id));
    }

    @Test
    @DisplayName("Update Supplier success.")
    public void updateSupplierSuccessTest() {
        var supplier = getValidSupplier();
        var request = new SupplierDTO(
                supplier.getId(),
                "UPDATED_SUPPLIER",
                "17.824.888/0001-80",
                "email@email.com",
                "(47) 99999-9990",
                null,
                null);

        when(supplierRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByCnpj(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByEmail(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.existsByPhoneNumber(any(String.class)))
                .thenReturn(false);
        when(supplierRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class)))
                .thenReturn(supplier);

        var response = supplierService.updateSupplier(request);

        assertThat(response.id()).isEqualTo(request.id());
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.cnpj()).isEqualTo(request.cnpj());
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.phoneNumber()).isEqualTo(request.phoneNumber());
    }

    @Test
    @DisplayName("Delete Supplier success.")
    public void deleteSupplierSuccessTest() {
        var supplier = getValidSupplier();

        when(supplierRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(getValidSupplierDTOWithId());

        verify(supplierRepository, times(1)).delete(any(Supplier.class));
    }

    @Test
    @DisplayName("Delete Supplier fail.")
    public void deleteSupplierFailTest() {
        when(supplierRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> supplierService.deleteSupplier(getValidSupplierDTOWithId()));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Supplier not found with id: %s.", getValidSupplier().getId()));
    }

    private static Supplier getValidSupplier() {
        return new Supplier(
                UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"),
                "SUPPLIER",
                "48.436.955/0001-12",
                "email@email.com",
                "(47) 99999-9999");
    }

    private static SupplierDTO getValidSupplierDTO() {
        return new SupplierDTO(
                null,
                "SUPPLIER",
                "48.436.955/0001-12",
                "email@email.com",
                "(47) 99999-9999",
                null,
                null);
    }

    private static SupplierDTO getValidSupplierDTOWithId() {
        return new SupplierDTO(
                UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"),
                "SUPPLIER",
                "48.436.955/0001-12",
                "email@email.com",
                "(47) 99999-9999",
                null,
                null);
    }

}
