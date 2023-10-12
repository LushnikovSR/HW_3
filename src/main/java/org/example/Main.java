package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите следующие данные через пробел ");
            System.out.println("Фамилия Имя Отчество датарождения номертелефона пол: ");
            String input = scanner.nextLine();
            String[] data = input.split(" ");

            Person person1 = new Person(data);
            try{
                person1.writeInFile("myTextFile.txt");
            }
            catch (WriteFileException e){
                System.out.printf("Исключение при попытки записи в файл %s", e.getFileName());
            }
            scanner.close();
        }
        catch (InputDataAmountException e){
            System.out.printf("Исключение при вводе количества данных человека, %s из 6", e.getCurrentAmount());
        }
        catch (PersonSurnameException e){
            System.out.printf("Исключение при вводе фамилии человека %s", e.surname);
        }
        catch (PersonNameException e){
            System.out.printf("Исключение при вводе имени человека %s", e.getName());
        }
        catch (PersonPatronymicException e){
            System.out.printf("Исключение при вводе отчества человека %s", e.getPatronymic());
        }
        catch (PersonBirtdayException e) {
            System.out.printf("Исключение при вводе даты дня рождения человека %s", e.getBirthday());
        }
        catch (PersonPhoneException e) {
            System.out.printf("Исключение при вводе номера телефона человека %s", e.getPhone());
        }
        catch (PersonSexException e) {
            System.out.printf("Исключение при вводе пола человека %s", e.getSex());
        }
    }

}

/*
class String[] DataReader(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите следующие данные через пробел ");
        System.out.println("Фамилия Имя Отчество датарождения номертелефона пол: ");
        String input = scanner.nextLine();

        String[] words = input.split(" ");
        return words;
        }

 */
abstract class PersonData {
    String[] data;
    String surname;
    String name;
    String patronymic;
    String birthday;
    int phone;
    char sex;

    final int DATAELEMENTS = 6;
    final String datePattern = "dd/MM/yyyy";

    public PersonData(String[] data) throws InputDataAmountException, PersonSurnameException, PersonNameException,
            PersonPatronymicException, PersonBirtdayException, PersonPhoneException, PersonSexException {
        if (!(data.length == DATAELEMENTS)) {
            throw new InputDataAmountException("Передано некорректное количество данных", data.length);
        }
        this.data = data;
        for (int i = 0; i < data[0].length(); i++) {
            if (!Character.isLetter(data[0].toCharArray()[i])) {
                throw new PersonSurnameException("В фамилии человека указаны некорректные символы", data[0]);
            }
        }
        this.surname = data[0];
        for (int i = 0; i < data[1].length(); i++) {
            if (!Character.isLetter(data[1].toCharArray()[i])) {
                throw new PersonNameException("В имени человека указаны некорректные символы", data[1]);
            }
        }
        this.name = data[1];
        for (int i = 0; i < data[2].length(); i++) {
            if (!Character.isLetter(data[2].toCharArray()[i])) {
                throw new PersonPatronymicException("В отчестве человека указаны некорректные символы", data[2]);
            }
        }
        this.patronymic = data[2];
        if (!data[3].matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new PersonBirtdayException("Переданы некорректные данные даты рождения", data[3]);
        }
        this.birthday = data[3];
        for (int i = 0; i < data[4].length(); i++) {
            if (!Character.isDigit(data[4].toCharArray()[i])) {
                throw new PersonPhoneException("В номере указаны некорректные символы", data[4]);
            }
        }
        this.phone = Integer.getInteger(data[4]);
        if ((data[5] == "m") || (data[5] == "f")){
            this.sex = data[5].charAt(0);
        }
        throw new PersonSexException("Пол человека указан не корректно", data[5]);
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
        this.phone = Integer.parseInt(data[4]);
        this.sex = data[5].charAt(0);
    }

    @Override
    void writeInFile(String fileName) throws WriteFileException {
        throw new WriteFileException("Не удалось записать данные в файл", fileName);
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