package com.koolearn.cloud.ireport.controller;

import java.util.Arrays;
import java.util.Collection;

public class ExamFactory {
	
private static Exam[] data={new Exam(1),new Exam(2)}; 

public static Object[] getBeanArray() {
     return data;
}

public static Collection<?> getBeanCollection() {
        return Arrays.asList(data);
    }
}