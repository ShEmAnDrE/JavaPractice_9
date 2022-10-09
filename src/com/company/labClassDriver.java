package com.company;

import java.util.ArrayList;

public class labClassDriver {
    public static void main(String[] args) {
        ArrayList<Student> s = new ArrayList<>();
        s.add(new Student("Палочкин Артем Иванович", 67, 12, 19));
        s.add(new Student("Припукин Геннадий Альбертович", 57, 12, 19));
        s.add(new Student("Сдробышев Илья Васильевич", 93, 12, 20));
        s.add(new Student("Кудесников Василий Федорович", 24, 2, 18));
        s.add(new Student("Людавиков Нестор Петрович", 66, 12, 20));

        new LabClassUI(s);
    }
}