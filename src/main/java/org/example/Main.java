package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try(Scanner scanner = new Scanner(System.in)){

            System.out.println("Введите следующие данные через пробел ");
            System.out.println("Фамилия Имя Отчество датарождения номертелефона пол: ");
            String input = scanner.nextLine();
            String[] data = input.split(" ");

            Person person1 = new Person(data);
            try{
                person1.writeInFile(person1.surname + ".txt");
            }
            catch (WriteFileException e){
                System.out.printf("Исключение при попытки записи в файл %s\n", e.getFileName());
                System.out.println(e.getStackTrace());
            }
        }
        catch (InputDataAmountException e){
            System.out.printf("Исключение при вводе количества данных человека, %s вместо 6\n", e.getCurrentAmount());
            System.out.println(e.getMessage());
        }
        catch (PersonSurnameException e){
            System.out.printf("Исключение при вводе фамилии %s\n", e.getSurname());
            System.out.println(e.getMessage());
        }
        catch (PersonNameException e){
            System.out.printf("Исключение при вводе имени %s\n", e.getName());
            System.out.println(e.getMessage());
        }
        catch (PersonPatronymicException e){
            System.out.printf("Исключение при вводе отчества %s\n", e.getPatronymic());
            System.out.println(e.getMessage());
        }
        catch (PersonBirtdayException e) {
            System.out.printf("Исключение при вводе даты дня рождения %s\n", e.getBirthday());
            System.out.println(e.getMessage());
        }
        catch (PersonPhoneException e) {
            System.out.printf("Исключение при вводе номера телефона %s\n", e.getPhone());
            System.out.println(e.getMessage());
        }
        catch (PersonSexException e) {
            System.out.printf("Исключение при вводе пола человека %s\n", e.getSex());
            System.out.println(e.getMessage());
        }
    }

}

abstract class PersonData {
    String[] data;
    String surname;
    String name;
    String patronymic;
    String birthday;
    String phone;
    String sex;

    final int DATAELEMENTS = 6;
    final String datePattern = "dd/MM/yyyy";

    public PersonData(String[] data) throws InputDataAmountException, PersonSurnameException, PersonNameException,
            PersonPatronymicException, PersonBirtdayException, PersonPhoneException, PersonSexException {
        if (!(data.length == DATAELEMENTS))
            throw new InputDataAmountException("Передано некорректное количество данных", data.length);
        this.data = data;
        for (int i = 0; i < data[0].length(); i++) {
            if (!Character.isLetter(data[0].charAt(i)))
                throw new PersonSurnameException("В фамилии человека указаны некорректные символы", data[0]);
        }
        this.surname = data[0];

        for (int i = 0; i < data[1].length(); i++) {
            if (!Character.isLetter(data[1].charAt(i)))
                throw new PersonNameException("В имени человека указаны некорректные символы", data[1]);
        }
        this.name = data[1];

        for (int i = 0; i < data[2].length(); i++) {
            if (!Character.isLetter(data[2].charAt(i)))
                throw new PersonPatronymicException("В отчестве человека указаны некорректные символы", data[2]);
        }
        this.patronymic = data[2];

        if (!data[3].matches("\\d{2}/\\d{2}/\\d{4}"))
            throw new PersonBirtdayException("Переданы некорректные данные даты рождения", data[3]);
        this.birthday = data[3];

        for (int i = 0; i < data[4].length(); i++) {
            if (!Character.isDigit(data[4].charAt(i)))
                throw new PersonPhoneException("В номере указаны некорректные символы", data[4]);
        }
        this.phone = data[4];

        if (!((data[5].equals("m")) | (data[5].equals("f"))))
            throw new PersonSexException("Пол человека указан не корректно", data[5]);
        this.sex = data[5];
    }

    abstract void writeInFile(String fileName) throws WriteFileException;
}

class Person extends PersonData {
    public Person(String[] data) throws InputDataAmountException, PersonSurnameException, PersonNameException,
            PersonPatronymicException, PersonBirtdayException, PersonPhoneException, PersonSexException {
        super(data);
        this.surname = data[0];
        this.name = data[1];
        this.patronymic = data[2];
        this.birthday = data[3];
        this.phone = data[4];
        this.sex = data[5];
    }

    @Override
    void writeInFile(String fileName) throws WriteFileException {
        try(FileWriter writer = new FileWriter(fileName, true))
        {
            writer.write(data[0] + " " + data[1] + " " + data[2] + " " + data[3] + " " + data[4]
                    + " " + data[5] + "\n");
            writer.flush();
        }
        catch(IOException ex){
            throw new WriteFileException("Не удалось записать данные в файл", fileName);
        }
    }
}

abstract class AppException extends Exception {
    public AppException(String message) {
        super(message);
    }
}

class InputDataAmountException extends AppException {

    int currentAmount;

    public int getCurrentAmount() {
        return currentAmount;
    }

    public InputDataAmountException(String message, int currentAmount) {
        super(message);
        this.currentAmount = currentAmount;
    }
}

class PersonSurnameException extends AppException {
    String surname;

    public String getSurname() {
        return surname;
    }

    public PersonSurnameException(String message, String surname) {
        super(message);
        this.surname = surname;
    }
}

class PersonNameException extends AppException {
    String name;

    public String getName() {
        return name;
    }

    public PersonNameException(String message, String name) {
        super(message);
        this.name = name;
    }
}

class PersonPatronymicException extends AppException {
    String patronymic;

    public String getPatronymic() {
        return patronymic;
    }

    public PersonPatronymicException(String message, String patronymic) {
        super(message);
        this.patronymic = patronymic;
    }
}

class PersonBirtdayException extends AppException {
    String birthday;

    public String getBirthday() {
        return birthday;
    }

    public PersonBirtdayException(String message, String birthday) {
        super(message);
        this.birthday = birthday;
    }
}

class PersonPhoneException extends AppException{
    String phone;

    public String getPhone() {
        return phone;
    }

    public PersonPhoneException(String message, String phone) {
        super(message);
        this.phone = phone;
    }
}

class PersonSexException extends AppException{
    String sex;

    public String getSex() {
        return sex;
    }

    public PersonSexException(String message, String sex) {
        super(message);
        this.sex = sex;
    }
}

class WriteFileException extends AppException {
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public WriteFileException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }
}