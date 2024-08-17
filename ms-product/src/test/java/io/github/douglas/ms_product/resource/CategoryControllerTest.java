package io.github.douglas.ms_product.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_product.config.JacksonConfig;
import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.service.CategoryService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc
@Import(JacksonConfig.class)
class CategoryControllerTest {

    private final static String CATEGORY_URL = "/category";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @Test
    @DisplayName("Register Category success.")
    public void registerCategorySuccessTest() throws Exception {
        var response = new CategoryDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "CATEGORY", LocalDateTime.now());

        given(categoryService.registerCategory(any(CategoryDTO.class)))
                .willReturn(response);
        var json = objectMapper.writeValueAsString(getValidCategoryDTO());

        var requestBuilder = MockMvcRequestBuilders
                .post(CATEGORY_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(response.name()))
                .andExpect(jsonPath("createdAt").value(response.createdAt().toString()));
    }

    @Test
    @DisplayName("Register Category fail.")
    public void registerCategoryFailTest() throws Exception {
        given(categoryService.registerCategory(any(CategoryDTO.class)))
                .willThrow(new DataIntegrityViolationException(format("Name %s already registered", getValidCategoryDTO().id())));
        var json = objectMapper.writeValueAsString(getValidCategoryDTO());

        var requestBuilder = MockMvcRequestBuilders
                .post(CATEGORY_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("path").value(CATEGORY_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("message").value(format("Name %s already registered", getValidCategoryDTO().id())))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get Category by id success.")
    public void getCategoryByIdSuccessTest() throws Exception {
        var category = getValidCategoryDTO();

        given(categoryService.getCategoryById(any(UUID.class)))
                .willReturn(category);

        var requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_URL.concat("/" + category.id().toString()))
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(category.id().toString()))
                .andExpect(jsonPath("name").value(category.name()))
                .andExpect(jsonPath("createdAt").value(category.createdAt().toString()));
    }

    @Test
    @DisplayName("Get Category by id fail.")
    public void getCategoryByIdFailTest() throws Exception {
        var id = getValidCategoryDTO().id();
        given(categoryService.getCategoryById(any(UUID.class)))
                .willThrow(new ResourceNotFoundException(format("Category not found with id: %s", id)));

        var requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_URL.concat("/" + id))
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("path").value(CATEGORY_URL.concat("/" + id)))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message").value(format("Category not found with id: %s", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get all Categories")
    public void getAllCategoriesTest() throws Exception {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(getValidCategoryDTO());
        categories.add(getValidCategoryDTO());

        given(categoryService.getAll())
                .willReturn(categories);

        var requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_URL)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categories.get(0).id().toString()))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).name()))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(categories.get(1).id().toString()))
                .andExpect(jsonPath("$[1].name").value(categories.get(1).name()))
                .andExpect(jsonPath("$[1].createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("Get empty list of categories.")
    public void getEmptyCategoriesListTest() throws Exception {
        given(categoryService.getAll())
                .willReturn(new ArrayList<>());

        var requestBuilder = MockMvcRequestBuilders
                .get(CATEGORY_URL)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Delete category success.")
    public void deleteCategorySuccessTest() throws Exception {
        var json = objectMapper.writeValueAsString(getValidCategoryDTO());

        var requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).deleteCategory(any(CategoryDTO.class));
    }

    @Test
    @DisplayName("Delete not found Category.")
    public void deleteNotFoundCategoryTest() throws Exception {
        var id = getValidCategoryDTO().id();

        doThrow(new ResourceNotFoundException(format("Category not found with id: %s", id)))
                .when(categoryService).deleteCategory(any(CategoryDTO.class));
        var json = objectMapper.writeValueAsString(getValidCategoryDTO());

        var requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("path").value(CATEGORY_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message").value(format("Category not found with id: %s", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Delete Category already in use.")
    public void deleteCategoryInUseTest() throws Exception {
        var id = getValidCategoryDTO().id();

        doThrow(new DataIntegrityViolationException(format("Category %s cannot be deleted as it is referenced by products.", id)))
                .when(categoryService).deleteCategory(any(CategoryDTO.class));
        var json = objectMapper.writeValueAsString(getValidCategoryDTO());

        var requestBuilder = MockMvcRequestBuilders
                .delete(CATEGORY_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("path").value(CATEGORY_URL))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("message").value(format("Category %s cannot be deleted as it is referenced by products.", id)))
                .andExpect(jsonPath("raisedAt").isNotEmpty());
    }




    private static CategoryDTO getValidCategoryDTO() {
        return new CategoryDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "CATEGORY", LocalDateTime.now());
    }

}
