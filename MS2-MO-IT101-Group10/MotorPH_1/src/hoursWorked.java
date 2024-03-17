import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class hoursWorked {
    public static void main(String[] args) {
        try {
            File file = new File("C:/Users/joykr/Downloads/attendance.csv");
            Scanner scanner = new Scanner(file);

            // Read and print header
            String headerLine = scanner.nextLine();

            // Prompt user for employee number
            Scanner userInputScanner = new Scanner(System.in);
            System.out.print("Enter employee number: ");
            String employeeNumber = userInputScanner.nextLine();

            // Read and print data
            boolean employeeFound = false;
            String firstName = "";
            String lastName = "";
            String birthday = "";
            String hourlyRate = "";
            List<String[]> employeeRecords = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(employeeNumber)) { // Check if the employee number matches
                    employeeFound = true;
                    firstName = parts[2];
                    lastName = parts[1];
                    birthday = parts[6];
                    hourlyRate = parts[12];
                    employeeRecords.add(parts);
                }
            }

            if (employeeFound) {
                System.out.println("Employee Number: " + employeeNumber);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Birthday: " + birthday);
                System.out.println("Hourly Rate: " + hourlyRate);

                // Calculate total hours worked in a week
                double totalHoursWorked = calculateTotalHours(employeeRecords);
                System.out.println("Total Hours Worked in a Week: " + totalHoursWorked + " hours");

                // Calculate gross weekly salary
                double grossWeeklySalary = calculateGrossWeeklySalary(totalHoursWorked, Double.parseDouble(hourlyRate));
                System.out.println("Gross Weekly Salary: " + grossWeeklySalary);

                // Calculate deductions
                double sssDeduction = calculateSSSDeduction(grossWeeklySalary);
                double philhealthDeduction = calculatePhilhealthDeduction(grossWeeklySalary);
                double pagibigDeduction = calculatePagibigDeduction(grossWeeklySalary);
                double withholdingTax = calculateWithholdingTax(grossWeeklySalary);

                // Display deductions
                System.out.println("SSS Deduction: " + sssDeduction);
                System.out.println("Philhealth Deduction: " + philhealthDeduction);
                System.out.println("Pagibig Deduction: " + pagibigDeduction);
                System.out.println("Withholding Tax: " + withholdingTax);

                // Calculate net weekly salary
                double netWeeklySalary = calculateNetWeeklySalary(grossWeeklySalary, sssDeduction, philhealthDeduction, pagibigDeduction, withholdingTax);
                System.out.println("Net Weekly Salary: " + netWeeklySalary);
            } else {
                System.out.println("Employee not found.");
            }

            scanner.close();
            userInputScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // Method to calculate total hours worked given employee records
    private static double calculateTotalHours(List<String[]> employeeRecords) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm");
        double totalHours = 0;
        try {
            for (String[] record : employeeRecords) {
                Date startDate = dateFormat.parse(record[3] + " " + record[4]);
                Date endDate = dateFormat.parse(record[3] + " " + record[5]);
                long durationMillis = endDate.getTime() - startDate.getTime();
                totalHours += durationMillis / (1000.0 * 60 * 60);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalHours;
    }

    // Method to calculate gross weekly salary
    private static double calculateGrossWeeklySalary(double totalHoursWorked, double hourlyRate) {
        // Assuming employee should work exactly 40 hours per week
        double standardHoursPerWeek = 40;
        double regularHours = Math.min(totalHoursWorked, standardHoursPerWeek);
        double grossSalary = regularHours * hourlyRate;
        return grossSalary;
    }

    // Method to calculate SSS deduction
    private static double calculateSSSDeduction(double grossSalary) {
        double[] sssRanges = { 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250, 9750,
                10250, 10750, 11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750,
                17250, 17750, 18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750,
                24250, 24750, 25250, 25750, 26250, 26750, 27250, 27750, 28250, 28750, 29250, 29750, 30250, 30750,
                31250, 31750, 32250, 32750, 33250, 33750, 34250, 34750, 35250, 35750, 36250, 36750, 37250, 37750,
                38250, 38750, 39250, 39750, 40250, 40750, 41250, 41750, 42250, 42750, 43250, 43750, 44250, 44750,
                45250, 45750, 46250, 46750, 47250, 47750, 48250, 48750, 49250, 49750, 50250, 50750, 51250, 51750,
                52250, 52750, 53250, 53750, 54250, 54750, 55250, 55750, 56250, 56750, 57250, 57750, 58250, 58750,
                59250, 59750, 60250, 60750, 61250, 61750, 62250, 62750, 63250, 63750, 64250, 64750, 65250, 65750,
                66250, 66750, 67250, 67750, 68250, 68750, 69250, 69750, 70250, 70750, 71250, 71750, 72250, 72750,
                73250, 73750, 74250, 74750, 75250, 75750, 76250, 76750, 77250, 77750, 78250, 78750, 79250, 79750,
                80250, 80750, 81250, 81750, 82250, 82750, 83250, 83750, 84250, 84750, 85250, 85750, 86250, 86750,
                87250, 87750, 88250, 88750, 89250, 89750, 90250, 90750, 91250, 91750, 92250, 92750, 93250, 93750,
                94250, 94750, 95250, 95750, 96250, 96750, 97250, 97750, 98250, 98750, 99250, 99750, 100250 };
        double[] sssDeductions = { 135, 157.50, 180, 202.50, 225, 247.50, 270, 292.50, 315, 337.50, 360, 382.50,
                405, 427.50, 450, 472.50, 495, 517.50, 540, 562.50, 585, 607.50, 630, 652.50, 675, 697.50, 720,
                742.50, 765, 787.50, 810, 832.50, 855, 877.50, 900, 922.50, 945, 967.50, 990, 1012.50, 1035, 1057.50,
                1080, 1102.50, 1125 };
        double sssContribution = 0;
        for (int i = 0; i < sssRanges.length; i++) {
            if (grossSalary <= sssRanges[i]) {
                sssContribution = sssDeductions[i];
                break;
            }
        }
        return sssContribution;
    }

    // Method to calculate Philhealth deduction
    private static double calculatePhilhealthDeduction(double grossSalary) {
        double philhealthContribution = 300;
        if (grossSalary > 10000 && grossSalary <= 59999.99) {
            philhealthContribution = 300 + (grossSalary - 10000) * 0.03;
            if (philhealthContribution > 1800) {
                philhealthContribution = 1800;
            }
        } else if (grossSalary >= 60000) {
            philhealthContribution = 1800;
        }
        return philhealthContribution;
    }

    // Method to calculate Pagibig deduction
    private static double calculatePagibigDeduction(double grossSalary) {
        double pagibigContribution = 0;
        if (grossSalary >= 1000 && grossSalary <= 1500) {
            pagibigContribution = grossSalary * 0.01;
        } else if (grossSalary > 1500) {
            pagibigContribution = grossSalary * 0.02;
        }
        return pagibigContribution;
    }

    // Method to calculate withholding tax
    private static double calculateWithholdingTax(double grossSalary) {
        double withholdingTax = 0;
        if (grossSalary > 20832 && grossSalary <= 33333) {
            withholdingTax = (grossSalary - 20832) * 0.20;
        } else if (grossSalary > 33333 && grossSalary <= 66667) {
            withholdingTax = 2500 + (grossSalary - 33333) * 0.25;
        } else if (grossSalary > 66667 && grossSalary <= 166667) {
            withholdingTax = 10833.33 + (grossSalary - 66667) * 0.30;
        } else if (grossSalary > 166667 && grossSalary <= 666667) {
            withholdingTax = 40833.33 + (grossSalary - 166667) * 0.32;
        } else if (grossSalary > 666667) {
            withholdingTax = 200833.33 + (grossSalary - 666667) * 0.35;
        }
        return withholdingTax;
    }

    // Method to calculate net weekly salary after applying deductions
    private static double calculateNetWeeklySalary(double grossWeeklySalary, double sssDeduction,
            double philhealthDeduction, double pagibigDeduction, double withholdingTax) {
        double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
        return grossWeeklySalary - totalDeductions;
    }
}
