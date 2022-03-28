package com.mashibing.lambda;

public class AgeFilter implements StudentFilter {

    @Override
    public boolean compare(Student s) {
        return s.getAge()>24;
    }
}
