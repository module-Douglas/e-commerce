package io.github.douglas.ms_product.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_product.config.JacksonConfig;
import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.model.entity.Brand;
import io.github.douglas.ms_product.service.BrandService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BrandController.class)
@AutoConfigureMockMvc
@Import(JacksonConfig.class)
class BrandControllerTest {

    private final static String BRAND_URL = "/brand";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BrandService brandService;

    @Test
    @DisplayName("Register Brand success.")
    public void registerBrandSuccessTest() throws Exception {
        var response = new BrandDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "BRAND", LocalDateTime.now(), LocalDateTime.now());

        given(brandService.registerBrand(any(BrandDTO.class)))
                .willReturn(response);
        var json = objectMapper.writeValueAsString(getValidBrandDTO());

        var requestBuilder = MockMvcRequestBuilders
                .post(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(response.id().toString()))
                .andExpect(jsonPath("name").value(response.name()))
                .andExpect(jsonPath("createdAt").isNotEmpty())
                .andExpect(jsonPath("updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Register Brand fail.")
    public void registerBrandFailTest() throws Exception {
        given(brandService.registerBrand(any(BrandDTO.class)))
                .willThrow(new DataIntegrityViolationException(format("Name %s already registered.", getValidBrand().getName())));
        var json = objectMapper.writeValueAsString(getValidBrandDTO());

        var requestBuilder = MockMvcRequestBuilders
                .post(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("path").value(BRAND_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("message").value(format("Name %s already registered.", getValidBrand().getName())))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get Brand by id success.")
    public void getBrandByIdSuccessTest() throws Exception {
        var brand = getValidBrandDTOWithId();

        given(brandService.getBrandById(any(UUID.class)))
                .willReturn(brand);

        var requestBuilder = MockMvcRequestBuilders
                .get(BRAND_URL.concat("/" + brand.id().toString()))
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(brand.id().toString()))
                .andExpect(jsonPath("name").value(brand.name()))
                .andExpect(jsonPath("createdAt").isNotEmpty())
                .andExpect(jsonPath("updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get Brand by id fail.")
    public void getBrandByIdFailTest() throws Exception {
        var id = getValidBrand().getId();
        given(brandService.getBrandById(any(UUID.class)))
                .willThrow(new ResourceNotFoundException(format("Brand not found with id: %s.", id)));

        var requestBuilder = MockMvcRequestBuilders
                .get(BRAND_URL.concat("/" + id))
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("path").value(BRAND_URL.concat("/" + id)))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message").value(format("Brand not found with id: %s.", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get all Brands.")
    public void getAllBrandsTest() throws Exception {
        List<BrandDTO> brands = new ArrayList<>();
        brands.add(getValidBrandDTOWithId());
        brands.add(getValidBrandDTOWithId());

        given(brandService.getAllBrands())
                .willReturn(brands);

        var requestBuilder = MockMvcRequestBuilders
                .get(BRAND_URL)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(brands.get(0).id().toString()))
                .andExpect(jsonPath("$[0].name").value(brands.get(0).name()))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(brands.get(1).id().toString()))
                .andExpect(jsonPath("$[1].name").value(brands.get(1).name()))
                .andExpect(jsonPath("$[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[1].updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get empty list of Brands.")
    public void getEmptyListOfBrandsTest() throws Exception {
        given(brandService.getAllBrands())
                .willReturn(new ArrayList<>());

        var requestBuilder = MockMvcRequestBuilders
                .get(BRAND_URL)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Update Brand success.")
    public void updateBrandSuccessTest() throws Exception {
        var brand = getValidBrandDTOWithId();

        given(brandService.updateBrand(any(BrandDTO.class)))
                .willReturn(brand);
        var json = objectMapper.writeValueAsString(brand);

        var requestBuilder = MockMvcRequestBuilders
                .patch(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(brand.id().toString()))
                .andExpect(jsonPath("name").value(brand.name()))
                .andExpect(jsonPath("createdAt").isNotEmpty())
                .andExpect(jsonPath("updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Update Brand fail cause not found.")
    public void updateBrandFailNotFoundTest() throws Exception {
        var id = getValidBrand().getId();
        var brand = getValidBrandDTOWithId();

        given(brandService.updateBrand(any(BrandDTO.class)))
                .willThrow(new ResourceNotFoundException(format("Brand not found with id: %s.", id)));
        var json = objectMapper.writeValueAsString(brand);

        var requestBuilder = MockMvcRequestBuilders
                .patch(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("path").value(BRAND_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message").value(format("Brand not found with id: %s.", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Update Brand fail cause name already registered.")
    public void updateBrandFailNameInUseTest() throws Exception{
        var name = getValidBrand().getName();
        var brand = getValidBrandDTOWithId();

        given(brandService.updateBrand(any(BrandDTO.class)))
                .willThrow(new DataIntegrityViolationException(format("Name %s already registered.", name)));
        var json = objectMapper.writeValueAsString(brand);

        var requestBuilder = MockMvcRequestBuilders
                .patch(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("path").value(BRAND_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("message").value(format("Name %s already registered.", name)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Delete category success.")
    public void deleteCategorySuccessTest() throws Exception {
        var json = objectMapper.writeValueAsString(getValidBrandDTOWithId());

        var requestBuilder = MockMvcRequestBuilders
                .delete(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
        verify(brandService, times(1)).deleteBrand(any(BrandDTO.class));
    }

    @Test
    @DisplayName("Delete Brand fail cause not found.")
    public void deleteBrandCauseNotFoundTest() throws Exception {
        var id = getValidBrand().getId();

        doThrow(new ResourceNotFoundException(format("Brand not found with id: %s.", id)))
                .when(brandService).deleteBrand(any(BrandDTO.class));
        var json = objectMapper.writeValueAsString(getValidBrandDTOWithId());

        var requestBuilder = MockMvcRequestBuilders
                .delete(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("path").value(BRAND_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message").value(format("Brand not found with id: %s.", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Delete Brand fail cause name already registered.")
    public void deleteBrandCauseNameRegisteredTest() throws Exception {
        var name = getValidBrand().getName();

        doThrow(new DataIntegrityViolationException(format("Brand %s cannot be deleted as it is referenced by products.", name)))
                .when(brandService).deleteBrand(any(BrandDTO.class));
        var json = objectMapper.writeValueAsString(getValidBrandDTOWithId());

        var requestBuilder = MockMvcRequestBuilders
                .delete(BRAND_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("path").value(BRAND_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("message").value(format("Brand %s cannot be deleted as it is referenced by products.", name)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    private static Brand getValidBrand() {
        return new Brand(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "BRAND");
    }

    private static BrandDTO getValidBrandDTO() {
        return new BrandDTO(null, "BRAND", null, null);
    }

    private static BrandDTO getValidBrandDTOWithId() {
        return new BrandDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "UPDATED_BRAND", LocalDateTime.now(), LocalDateTime.now());
    }

}
