package com.pete.bibliogere.utils;

import com.pete.bibliogere.modelo.GenericModel;
import com.pete.bibliogere.modelo.ValidableModel;
import org.hibernate.Hibernate;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import jakarta.validation.Validator;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ReusableEntityOperation {
    private Validator validator;

    public <T extends GenericModel, S extends RuntimeException> void handleEntityExists(Optional<T> optional,
                                                                                        S exception) {
        T generic = null;

        if (optional.isPresent()) {
            generic = optional.get();

            if (generic.getIsDeleted()) {
                generic.setCodigo(generic.getCodigo());
                generic.setIsDeleted(Boolean.FALSE);
            } else if (!generic.getIsDeleted()) {
                throw exception;
            }
        }
    }

    public <T> T buildEntityToUpdate(T entity, Map<Object, Object> fields) {

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), (String) key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, entity, value);
        });

        return entity;
    }

    public <T extends ValidableModel> void handleConstraintViolation(T entity) {
        validator = ValidatorUtil.getValidator();

        BindingResult result = new BeanPropertyBindingResult(entity, entity.getClass().getName());
        SpringValidatorAdapter adapter = new SpringValidatorAdapter(validator);
        adapter.validate(entity, result);

        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }

    }

    public <T> Map<String, Object> buildDataWithPaging(Page results, Class<T> entity) {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("page", results.getNumber());
        data.put("totalRecords", results.getTotalElements());
        data.put("numPages", results.getTotalPages());
        data.put(entity.getSimpleName().toLowerCase() + "s", results.getContent());

        return data;
    }

    public <T extends ValidableModel> T getModelFromProxy(T entity, Class<T> clazz) {
        return Hibernate.unproxy(entity, clazz);
    }

}