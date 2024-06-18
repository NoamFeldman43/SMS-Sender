package com.example.smssender.ui.studentTable;

public class Student
{
    private String name;
    private String phone;
    private String document_id;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name, String phone, String document_id) {
        this.name = name;
        this.phone = phone;
        this.document_id = document_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDocumentID()
    {
        return document_id;
    }
}
