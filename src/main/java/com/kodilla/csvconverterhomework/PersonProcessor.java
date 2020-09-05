package com.kodilla.csvconverterhomework;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class PersonProcessor implements ItemProcessor<Person1, Person2> {

    @Override
    public Person2 process(Person1 item) throws Exception {
        return new Person2(item.getFirstName(), item.getLastName(), calculateAge(item.getBirth()));
    }

    private int calculateAge(LocalDate birth) {
        if (Objects.nonNull(birth)) {
            return Period.between(birth, LocalDate.now()).getYears();
        }
        return 0;
    }
}
