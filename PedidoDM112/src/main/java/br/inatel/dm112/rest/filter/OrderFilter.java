package br.inatel.dm112.rest.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import br.inatel.dm112.model.entities.OrderEntity;

public class OrderFilter {

	public Example<OrderEntity> filterByQueryParams(Map<String, String> queryParams) {
		OrderEntity entity = getEntityByQueryParams(queryParams);
		String[] ignoredFields = getFieldsToIgnore(queryParams);

		ExampleMatcher matcher = ExampleMatcher
			    .matching()
			    .withIgnorePaths(ignoredFields)
			    .withIgnoreNullValues();

		return Example.of(entity, matcher);
	}

	public OrderEntity getEntityByQueryParams(Map<String, String> queryParams) {
		OrderEntity entity = new OrderEntity();

		entity.setCPF(queryParams.get("CPF"));
		if(queryParams.get("status") != null) {
			entity.setStatus(Integer.valueOf(queryParams.get("status")));
		}
		if(queryParams.get("number") != null) {
			entity.setNumber(Integer.valueOf(queryParams.get("number")));
		}
		if(queryParams.get("deliveryStatus") != null) {
			entity.setDeliveryStatus(Integer.valueOf(queryParams.get("deliveryStatus")));
		}

		return entity;
	}

	public String[] getFieldsToIgnore(Map<String, String> queryParams) {
		List<String> ignoredFieldsList = new ArrayList<>();

		if(queryParams.get("status") == null) {
			ignoredFieldsList.add("status");
		}
		if(queryParams.get("deliveryStatus") == null) {
			ignoredFieldsList.add("deliveryStatus");
		}
		ignoredFieldsList.add("value");

		String[] ignoredFields = ignoredFieldsList.toArray(new String[0]);

		return ignoredFields;
	}
}
