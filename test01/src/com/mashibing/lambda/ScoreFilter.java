package com.mashibing.lambda;

public class ScoreFilter implements StudentFilter {
    @Override
    public boolean compare(Student s) {
        return s.getScore()>70;
    }
}
