package com.butenkos.ad.sdk.adviser.service.validation;

import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestValidatorImpl implements RequestValidator {

  private final Validator beanValidator;

  @Autowired
  public RequestValidatorImpl(Validator beanValidator) {
    this.beanValidator = beanValidator;
  }

  @Override
  public void validateRequest(AdNetworkAdviseRequest request) {
    final Set<ConstraintViolation<AdNetworkAdviseRequest>> validate = beanValidator.validate(request);
    if (!validate.isEmpty()) {
      final String violations = validate.stream()
          .map(violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage())
          .collect(Collectors.joining("; "));
      throw new BeanValidationException("Request validation error. " + violations);
    }
  }
}
