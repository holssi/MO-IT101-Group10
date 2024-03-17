package initial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class main {
	 public static String[] employee;
	   public static void main(String[] args) {
	       boolean conts = true;
	       while (conts) {
	           System.out.println("start");
	           define_data_type();
	           if (employee != null) {
	               displayEmployeeDetails();
	               getWeeklyHoursAndSalary(employee[0]);
	           } else {
	               System.out.println("No data Found. Please try again");
	           }
	           System.out.println("End");
	           System.out.println("***************************");
	       }
	   }
	   public static void reset_data() {
	       employee = null;
	   }
	   public static void define_data_type() {
	       reset_data();
	       System.out.println("Enter an Employee number:");
	       BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	       try {
	           String userInput = inputReader.readLine();
	           String employee_detail = get_employee_details(userInput);
	           if (!employee_detail.equals("")) {
	               String[] row = employee_detail.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	               employee = row;
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	   }
	   public static String get_employee_details(String employee_id) {
	       String file = "src\\employee_details.csv"; // add your own file location
	       BufferedReader reader = null;
	       String line;
	       String employee_found = "";
	       try {
	           reader = new BufferedReader(new FileReader(file));
	           while ((line = reader.readLine()) != null) {
	               String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	               if (row[0].equals(employee_id)) {
	                   employee_found = line;
	                   break;
	               }
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	       } finally {
	           try {
	               if (reader != null)
	                   reader.close();
	           } catch (IOException e) {
	               e.printStackTrace();
	           }
	       }
	       return employee_found;
	   }
	   public static void displayEmployeeDetails() {
	       if (employee.length >= 4) {
	           System.out.println("Employee ID: " + employee[0]);
	           System.out.println("Name: " + employee[1] + " " + employee[2]);
	           System.out.println("Birthdate: " + employee[3]);
	           System.out.println("Address: " + employee[4]);
	           System.out.println("Phone Number: " + employee[5]);
	           System.out.println("SSS #: " + employee[6]);
	           System.out.println("Philhealth #: " + employee[7]);
	           System.out.println("TIN #: " + employee[8]);
	           System.out.println("Pag-ibig #: " + employee[9]);
	           System.out.println("Status: " + employee[10]);
	           System.out.println("Position: " + employee[11]);
	           System.out.println("Immediate Supervisor: " + employee[12]);
	           System.out.println("Basic Salary: " + employee[13]);
	           System.out.println("Rice Subsidy: " + employee[14]);
	           System.out.println("Phone Allowance: " + employee[15]);
	           System.out.println("Clothing Allowance: " + employee[16]);
	           System.out.println("Gross Semi-monthly Rate: " + employee[17]);
	           System.out.println("Hourly Rate: " + employee[18]);
	       } else {
	           System.out.println("Insufficient data for employee details.");
	       }
	   }
	   public static void getWeeklyHoursAndSalary(String employeeId) {
	       // Get date range
	       BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	       String startDate = "";
	       String endDate = "";
	       try {
	           System.out.println("Enter start date (MM/DD/YYYY):");
	           startDate = inputReader.readLine();
	           System.out.println("Enter end date (MM/DD/YYYY):");
	           endDate = inputReader.readLine();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       // Read attendance CSV file
	       String attendanceFile = "src\\attendance.csv"; //add your own file location
	       BufferedReader reader = null;
	       String line;
	       List<Double> weeklyHoursList = new ArrayList<>();
	       try {
	           reader = new BufferedReader(new FileReader(attendanceFile));
	           System.out.println("Employee ID\tDATE\tTimeIn\tTimeOut\tHoursRendered");
	           while ((line = reader.readLine()) != null) {
	               String[] record = line.split(",");
	               if (record[0].equals(employeeId) && isWithinDateRange(record[3], startDate, endDate)) {
	                   double hoursWorked = calculateHoursWorked(record[4], record[5]);
	                   System.out.println(record[0] + "\t" + record[3] + "\t" + record[4] + "\t" + record[5] + "\t" + hoursWorked);
	                   weeklyHoursList.add(hoursWorked);
	               }
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	       } finally {
	           try {
	               if (reader != null)
	                   reader.close();
	           } catch (IOException e) {
	               e.printStackTrace();
	           }
	       }
	       // Calculate total weekly hours
	       double totalWeeklyHours = 0;
	       for (double hours : weeklyHoursList) {
	           totalWeeklyHours += hours;
	       }
	       // Display total weekly hours
	       System.out.println("Total weekly hours: " + totalWeeklyHours);
	       // Display hourly rate
	       System.out.println("Hourly Rate (based on employee selected): " + employee[18]);
	       // Calculate weekly salary
	       double hourlyRate = Double.parseDouble(employee[18]);
	       double weeklySalary = totalWeeklyHours * hourlyRate;
	       // Display total weekly salary
	       System.out.println("------------------------------------------------------------------------");
	       System.out.println("Total Weekly Salary: " + weeklySalary);
	   }
	   public static double calculateHoursWorked(String startTime, String endTime) {
	       if (startTime.equals("0:00") || endTime.equals("0:00")) {
	           return 0;
	       }
	       String[] startParts = startTime.trim().split(":");
	       String[] endParts = endTime.trim().split(":");
	       int startHour = Integer.parseInt(startParts[0]);
	       int startMinute = Integer.parseInt(startParts[1]);
	       int endHour = Integer.parseInt(endParts[0]);
	       int endMinute = Integer.parseInt(endParts[1]);
	       int graceStartHour = 8;
	       int graceStartMinute = 11;
	       if (startHour == graceStartHour && startMinute >= graceStartMinute) {
	           startHour = graceStartHour;
	           startMinute = graceStartMinute;
	       }
	       int totalMinutesWorked = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
	       double hoursWorked = totalMinutesWorked / 60.0;
	       return hoursWorked;
	   }
	   public static boolean isWithinDateRange(String dateStr, String startDateStr, String endDateStr) {
	       SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	       try {
	           Date date = dateFormat.parse(dateStr);
	           Date startDate = dateFormat.parse(startDateStr);
	           Date endDate = dateFormat.parse(endDateStr);
	           // Adjusted to include start date in the range
	           return !date.before(startDate) && !date.after(endDate);
	       } catch (ParseException e) {
	           e.printStackTrace();
	           return false;
	       }
	   }
	}
