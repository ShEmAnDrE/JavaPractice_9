package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class LabClassUI extends JFrame {

    private ArrayList<Student> students;
    private JTable studTable; // таблица

    public LabClassUI(ArrayList<Student> students){
        // base
        super("Студенты");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        this.students = students;

        // Buttons
        JPanel panel = new JPanel(new FlowLayout());
        JButton addStudentButton = new JButton("Добавить студента");
        JButton removeStudentButton = new JButton("Удалить студента");
        JButton findStudentButton = new JButton("Найти студента");

        panel.add(addStudentButton);
        panel.add(removeStudentButton);
        panel.add(findStudentButton);

        addStudentButton.addActionListener(e -> {
            try {
                addButtonClicked();
            }catch (IllegalArgumentException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        removeStudentButton.addActionListener(e-> removeButtonClicked());

        findStudentButton.addActionListener(e->{
            try{
                findButtonClicked();
            }catch (StudentNotFoundException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // JTable
        Object[] headers = new String[] {"ФИО", "Возраст", "Группа", "Средний балл"};
        Object [][] startStudents = new String[students.size()][4];
        for(int i = 0; i < students.size(); i++){
            startStudents[i][0] = students.get(i).getFio();
            startStudents[i][1] = ((Integer)(students.get(i).getAge())).toString();
            startStudents[i][2] = ((Integer)(students.get(i).getGroupNum())).toString();
            startStudents[i][3] = ((Integer)(students.get(i).getAvgPoint())).toString();
        }
        studTable = new JTable( new DefaultTableModel(startStudents, headers)){
            @Override
            public boolean isCellEditable(int x, int y){ // можно редактировать?
                return false; // нет
            }
        };

        JTableHeader header = studTable.getTableHeader();
        header.setReorderingAllowed(false); //двигать колонки нельзя
        header.setResizingAllowed(false); // менять размер колонок тоже нельзя

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int count = e.getClickCount();
                if (count == 2){
                    if(header.getHeaderRect(0).contains(e.getX(), e.getY()))
                        sortStudents(Student::compareTo);
                    if(header.getHeaderRect(1).contains(e.getX(), e.getY()))
                        sortStudents(Student.AGE_COMPARATOR);
                    if(header.getHeaderRect(2).contains(e.getX(), e.getY()))
                        sortStudents(Student.GROUP_COMPARATOR);
                    if(header.getHeaderRect(3).contains(e.getX(), e.getY()))
                        sortStudents(Student.AVG_COMPARATOR);
                }
            }
        });

        // добавляем полосу прокрутки
        getContentPane().add(new JScrollPane(studTable), BorderLayout.CENTER);
        // добавялем панель на экран
        getContentPane().add(panel, BorderLayout.NORTH);
        // делаем все видимым
        setVisible(true);
    }

    private void sortStudents(Comparator<Student> comp){
        for (int i = 1; i < students.size(); i++) {
            Student current = students.get(i);
            int j = i-1;
            for(; j >= 0 && comp.compare(current, students.get(j)) < 0; j--) { // в зависимости от компаратора, выполнится то или иное сравнение
                students.set(j+1, students.get(j));
            }
            students.set(j+1, current);
        } // отсортировали студентов в зависимости от компаратора
        DefaultTableModel dtm = (DefaultTableModel) studTable.getModel(); // я так понял jTable просто как картинка, а логика прописана в модели, поэтому все через модель

        for (int i = 0; i < students.size(); i++) {
            dtm.removeRow(i);
            Student st = students.get(i); // заново заполяем таблицу из отсортированного списка студентов
            dtm.insertRow(i, new Object[]{st.getFio(), st.getAge(), st.getGroupNum(), st.getAvgPoint()});
        }
    }

    private void findButtonClicked() throws StudentNotFoundException {
        String s = JOptionPane.showInputDialog("Введите искомые ФИО");
        for(int i = 0; i < students.size(); i++){
            if(students.get(i).getFio().equals(s)){
                JOptionPane.showMessageDialog(this, "Найден студент: "+
                        students.get(i).toString());
                return;
            }
        }
        throw new StudentNotFoundException(s);
    }

    private void removeButtonClicked() {
        int c = studTable.getSelectedRowCount(); // смотрим, сколько строк выбрано
        if(c != 1) {
            JOptionPane.showMessageDialog(this, "Выберете ровно одну строку!");
            return;
        }
        c = studTable.getSelectedRow(); // получаем номер выбранной пользователем строки
        students.remove(c); // удаляем из списка студентов выбранного студента
        DefaultTableModel dtm = (DefaultTableModel) studTable.getModel();

        dtm.removeRow(c); // удаляем выбранную строку

    }

    private void addButtonClicked() throws IllegalArgumentException {
        String fio = JOptionPane.showInputDialog("Пожалуйста, введите фио!");
        if(fio.equals("")) throw new EmptyStringException();
        String age = JOptionPane.showInputDialog("Пожалуйста, введите возраст!");
        String group = JOptionPane.showInputDialog("Пожалуйста, введите номер группы!");
        String avg = JOptionPane.showInputDialog("Пожалуйста, введите средний балл!");

        int avgInt =0;
        int groupInt = 0;
        int ageInt =0;
        try {
            avgInt = Integer.parseInt(avg);
            groupInt = Integer.parseInt(group);
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Невозможно строку "+e.getMessage().substring(17) + " в число!");
        }

        students.add(new Student(fio, avgInt, groupInt, ageInt)); // добавляем нового студента в список студентов

        DefaultTableModel dtm = (DefaultTableModel) studTable.getModel();

        dtm.addRow(new Object[]{fio, age, group, avg}); // добавляем новую строку в таблицу
    }
}