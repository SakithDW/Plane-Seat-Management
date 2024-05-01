import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static int [] A = new int[14];
    static int [] B = new int[12];
    static int [] C = new int[12];
    static int [] D = new int[14];
    static boolean firstAvailableSeatFound = false;

    static ArrayList<Ticket> buyers =new ArrayList<>();
    static boolean programOn =true;

    static Scanner input  = new Scanner(System.in);

    public static void main(String[] args) {
        while (programOn){
            displayMenu();
            int menuNumber = integerValidator("Please select an option :",0,7);
            switchMenu(menuNumber);
        }
    }

    public static void displayMenu(){
        System.out.println("""
                ************************************
                *           MENU OPTIONS           *
                ************************************
                *   1. Buy a seat                  *
                *   2. Cancel a seat               *
                *   3. Find first available seat   *
                *   4. show seating plan           *
                *   5. Print Ticket Information    *
                *      and Total ticket sales      *
                *   6. Search Ticket               *
                *   0. Quit                        *
                ************************************
                       """);
    }

    public static void switchMenu(int menuNumber){
        //This method is to select the option that user need to do.
        switch (menuNumber) {
            case 1 -> buyTicket();
            case 2 -> cancelTicket();
            case 3 -> findFirstAvailableSeat();
            case 4 -> printSeatingPlan();
            case 5 -> {printInfo();calculateTotalPrice();}
            case 6 -> searchTickets();
            case 0 -> programOn = false;
        }
    }

    public static int integerValidator(String prompt,int min , int max){
        //This method is to validate integer inputs we get in this code.
        int data;
        while (true){
            try {
                System.out.print(prompt);
                data =input.nextInt();
                if (min<=data && data<=max){
                    return data;
                }
                else {
                    System.out.println("Please enter a number between "+min+" and "+max);
                }
            }catch (InputMismatchException e){
                input.nextLine();
                System.out.println("Enter a valid Integer");
            }
        }
    }

    public static void cancelTicket(){
        //If user needs to cancel the ticket it happens with help of this method.
        int rowNumber = integerValidator("Please Enter a row number : ",1,4);
        int seatNumber;
        if(rowNumber ==1||rowNumber==4){
            seatNumber=integerValidator("Please Enter a seat number : ",1,14);
            removeFromList(rowNumber, seatNumber);

        }else if(rowNumber==2||rowNumber==3){
            seatNumber=integerValidator("Please Enter a seat number : ",1,12);
            removeFromList(rowNumber, seatNumber);

        }

    }

    private static void removeFromList(int rowNumber, int seatNumber) {
        int count=0;
        try {
            for (Ticket ticket : buyers) {
                if (ticket.seat == seatNumber && ticket.row == rowNumber) {
                    seatReset(rowNumber, seatNumber);
                    System.out.println("Seat has successfully cancelled.");
                    break;
                }
                count++;
            }
            buyers.remove(count);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("This seat is already vacant.");
        }
    }

    public static boolean isNotOccupied(int rowNum, int seatNum){
        //This method is to check if a seat is booked or not.
        if (rowNum==1){
            return A[seatNum-1]==0;
        }
        else if(rowNum==2){
            return B[seatNum-1]==0;
        }
        else if(rowNum==3){
            return C[seatNum-1]==0;
        }

        return D[seatNum - 1] == 0;
    }

    public static void markSeatAsBooked(int rowNum, int seatNum){
    /*When user buys a ticket,that particular seat
     being marked with the help of this method*/
        if (rowNum==1){
            A[seatNum-1]=1;
        }
        else if (rowNum==2){
            B[seatNum-1]=1;
        }
        else if (rowNum==3){
            C[seatNum-1]=1;
        }
        else{
            D[seatNum-1]=1;
        }
    }

    public static void seatReset(int rowNum, int seatNum){
        //if the user needs to cancel a ticket,with the help of this method I have marked seat as 0 if it was occupied before.
        try{
            if (rowNum==1){
                if (A[seatNum-1]==1){
                    A[seatNum-1]=0;
                }
            }
            else if(rowNum==2){
                if (B[seatNum-1]==1){
                    B[seatNum-1]=0;
                }
            }
            else if(rowNum==3) {
                if (C[seatNum-1]==1){
                    C[seatNum-1]=0;
                }
            }
            else {
                if (D[seatNum-1]==1){
                    D[seatNum-1]=0;
                }
            }

        }catch (IndexOutOfBoundsException e){
            System.out.println("This seat is already vacant.");
        }
    }

    public static String userDetailsInput(String prompt){
        //this is for validating string inputs we get.
        String userDetails;
        while(true) {
            try {
                System.out.print(prompt);
                userDetails = input.next();
                return userDetails;
            }catch (InputMismatchException e){
                System.out.println("Enter a valid input.");
            }
        }
    }

    public static void buyTicket(){
        //With the help of this method I completed the process of buying a ticket.
        int rowNumber,seatNumber;
        boolean run=true;
        while (run) {
            rowNumber = integerValidator("Please Enter a row number : \n" +
                    "1 for A, 2 for B, 3 for C, 4 for D", 1, 4);
            if (rowNumber == 1||rowNumber==4) {
                seatNumber = integerValidator("Please Enter a seat number : ", 1, 14);
                occupySeat(rowNumber, seatNumber);
            } else if (rowNumber == 2||rowNumber==3) {
                seatNumber = integerValidator("Please Enter a seat number : ", 1, 12);
                occupySeat(rowNumber, seatNumber);
            }


            while (true){
                String wantToContinue=userDetailsInput("Do you want to book another ticket?(Yes/No) :");
                if (wantToContinue.equalsIgnoreCase("No")){
                    run=false;
                    break;
                }
                else if (wantToContinue.equalsIgnoreCase("Yes")) {
                    break;
                }
                else{
                    System.out.println("Invalid input.");
                }
            }
        }

    }

    private static void occupySeat(int rowNumber, int seatNumber) {
        double price = priceDetector(seatNumber);
        if (!isNotOccupied(rowNumber, seatNumber)) {
            System.out.println("This Seat has been already occupied.");
        } else {
            String firstName = userDetailsInput("what is your first name: ");
            String lastName = userDetailsInput("what is your last name: ");
            String email = userDetailsInput("Enter your E-Mail here: ");
            markSeatAsBooked(rowNumber, seatNumber);
            Person person = new Person(firstName, lastName, email);
            Ticket ticket = new Ticket(rowNumber, seatNumber, price, person);
            buyers.add(ticket);
        }
    }

    public static double priceDetector(int seatNumber){
        if(seatNumber<6 && seatNumber>=1){
            return 200;
        } else if (seatNumber>=6 && seatNumber<10) {
            return  150;
        }
        else{
            return 180;
        }
    }

    public static void formattedSeatingPlan(int[] row){
        int count = 1;
        for(int elem: row){
            if (elem == 1 ){
                System.out.print("X");
            }
            else {
                System.out.print("O");
            }
            if(count==7){
                System.out.print("  ");
            }
            count++;
        }
    }

    public static void printSeatingPlan(){
        formattedSeatingPlan(A);
        System.out.println("\n");
        formattedSeatingPlan(B);
        System.out.println("\n");
        formattedSeatingPlan(C);
        System.out.println("\n");
    }

    public static void printInfo(){
        for (Ticket ticket: buyers){
            ticket.print();
        }
    }

    public static void calculateTotalPrice(){
        double totalPrice=0;
        for (Ticket ticket: buyers){
            totalPrice+=ticket.price;
        }
        System.out.println("Total price is ₤"+totalPrice);
    }

    public static int findFirstAvailableInRow(int[] row){
        int count =1;
        for (int elem: row){
            if(elem!=1){
                firstAvailableSeatFound =true;
                break;
            }
            count++;
        }
        return count;
    }

    public static void findFirstAvailableSeat(){
        for(int i = 1;i<5;i++){
            if(i==1 && !firstAvailableSeatFound){
                System.out.println("A"+findFirstAvailableInRow(A));
            }
            else if(i==2 && !firstAvailableSeatFound){
                System.out.println("B"+findFirstAvailableInRow(B));
            }
            else if(i==3 && !firstAvailableSeatFound){
                System.out.println("C"+findFirstAvailableInRow(C));
            }
            else if(i==4 && !firstAvailableSeatFound){
                System.out.println("D"+findFirstAvailableInRow(D));
            }

        }
        firstAvailableSeatFound=false;

    }

    public static void searchTickets(){
        String row = userDetailsInput("Enter the row(A,B,C,D): ");
        if(row.equalsIgnoreCase("A")){
            printTicket(1,14);
        }
        else if(row.equalsIgnoreCase("B")){
            printTicket(2,12);
        }
        else if(row.equalsIgnoreCase("C")){
            printTicket(3,12);
        }
        else if(row.equalsIgnoreCase("D")){
            printTicket(2,14);
        }
        else {
            System.out.println("Invalid input");
        }
    }

    public static void printTicket(int row, int seat){
        int seatNo = integerValidator(
                "Enter the seat number", 1, seat);
        for(Ticket ticket : buyers){
            if((ticket.row==row)&&(ticket.seat==seatNo)){
                ticket.print();
            }
            else {
                if(!buyers.contains(ticket)){
                    System.out.println("This seat is available.");
                }
            }

        }
    }
}


