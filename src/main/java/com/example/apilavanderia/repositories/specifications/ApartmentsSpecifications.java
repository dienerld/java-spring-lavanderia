package com.example.apilavanderia.repositories.specifications;

import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class ApartmentsSpecifications {
    public static Specification<Apartment> filters(String phone, String hasLogged) {
        return (root, query, criteriaBuilder) -> {
            var conditions = new ArrayList<Predicate>();

            if(phone != null){
                conditions.add(criteriaBuilder.like(root.get("phone"), "%"+ phone +"%"));
            }
            if(hasLogged != null) {
                if (hasLogged.equalsIgnoreCase("true")) {
                    conditions.add(criteriaBuilder.isTrue(root.get("tokenLogin").isNotNull()));
                } else {
                    conditions.add(criteriaBuilder.isTrue(root.get("tokenLogin").isNull()));
                }
            }
            return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
        };
    }

}
