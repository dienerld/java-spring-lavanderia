package com.example.apilavanderia.repositories.specifications;

import com.example.apilavanderia.enums.*;
import com.example.apilavanderia.models.Booking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class BookingsSpecifications {
    public static Specification<Booking> filters(Machine machine, Shift hour) {
        return (root, query, criteriaBuilder) -> {
            var conditions = new ArrayList<Predicate>();

            if (machine != null) {
                conditions.add(criteriaBuilder.equal(root.get("machine"), machine));
            }

            if (hour != null) {
                conditions.add(criteriaBuilder.equal(root.get("hour"), hour));
            }

            return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
        };
    }
}
