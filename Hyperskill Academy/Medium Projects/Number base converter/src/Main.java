
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.random.RandomGenerator;

public class Main {
    static LinkedHashMap<Character,Integer> values = new LinkedHashMap<>();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        initValues(values);
        menu();
    }

    private static void menu(){
        boolean exitRequested = false;
        while(!exitRequested) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String sourceBase,targetBase = new String();
            sourceBase = scanner.next();
            if (!sourceBase.equals("/exit")) {
                targetBase = scanner.next();
                scanner.nextLine();
                fromSourceToTarget(Integer.valueOf(sourceBase),Integer.valueOf(targetBase));
            }
            else exitRequested = true;
        }
    }
    private static void fromSourceToTarget(int sourceBase,int targetBase){
        boolean exitRequested = false;
        while(!exitRequested){
            System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)\n",sourceBase,targetBase);
            String command = scanner.nextLine();
            if(command.equals("/back"))
                exitRequested = true;
            else
                System.out.println("Conversion result: "+fractionToTargetBase(command,sourceBase,targetBase));
        }
    }
    private static String fractionToTargetBase(String number,int sourceBase,int targetBase){
        try {
            BigInteger decimalNumber = new BigInteger(toDecimal(number, sourceBase));
            decimalNumber = new BigInteger(toTargetBase(decimalNumber, targetBase).toString());
            return decimalNumber.toString();
        } catch (Exception e) {
            try {
                //System.out.println("real");
                BigDecimal decimalNumber = new BigDecimal(fractionToDecimal(number,sourceBase));
                //decimalNumber = decimalNumber.setScale(5, RoundingMode.HALF_DOWN);
                String res = (fractionToTargetBase(decimalNumber,targetBase));
                if(res.split("\\.").length > 1) {
                    while (res.split("\\.")[1].length() > 5) {
                        res = res.substring(0, res.length() - 1);
                    }
                    while (res.split("\\.")[1].length() < 5)
                        res += "0";
                }
                return res;

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return "";
    }

    private static String fractionToDecimal(String number,int sourceBase) throws Exception{
        String decimalNumber = new String("0");
        int power = number.split("\\.")[0].length()-1;
        boolean integerPart = true;
        for(int i=0;i<number.length();i++){
            if(number.charAt(i) == '.') {
                integerPart = false;
                decimalNumber = decimalNumber +".0";
                power = 1;
                continue;
            }
            if(integerPart) {
                decimalNumber = String.valueOf(new BigDecimal(decimalNumber).add(new BigDecimal(values.get(number.charAt(i))*Math.pow(sourceBase,power))));
                power--;
            }
            else{
                decimalNumber = String.valueOf(new BigDecimal(decimalNumber).add(new BigDecimal(values.get(number.charAt(i))/Math.pow(sourceBase,power))));
                power++;
            }
        }
        //System.out.println(decimalNumber);
        return decimalNumber;
    }
    private static String fractionToTargetBase(BigDecimal number,int targetBase)throws Exception{
        //System.out.println(number);
        String result = new String();
        BigInteger res1 = new BigInteger(number.toString().split("\\.")[0]);
        String res = toTargetBase(res1,targetBase);
        if(!number.toString().contains("."))
            return res;
        BigDecimal fractionPart = new BigDecimal("0."+number.toString().split("\\.")[1]);
        BigDecimal multiplier = new BigDecimal(targetBase);
        //System.out.println(targetBase);
        String res2 = new String();
        int length = 0;
        while(fractionPart.compareTo(BigDecimal.ZERO) != 0 && length < 5){
            BigDecimal value = fractionPart.multiply(multiplier);
            String v = new String();
            for(Map.Entry entry : values.entrySet()){
                if(entry.getValue().toString().equals(value.toString().split("\\.")[0])){
                    v = entry.getKey().toString();
                    break;
                }
            }
            res2 = res2 + v;
            //System.out.println(res2);
            //Thread.sleep(1000);
            fractionPart = new BigDecimal("0."+value.toString().split("\\.")[1]);
            length++;
        }
        if(res2.isEmpty() && !number.toString().contains("."))
            return res;
        while(res2.length() < 5)
            res2+="0";
        return res+"."+res2;
    }

    private static String toDecimal(String number,int sourceBase) throws Exception{
        int power = 0;
        BigInteger base = new BigInteger(String.valueOf(sourceBase));
        BigInteger value;
        BigInteger result = new BigInteger("0");
        for(int i=number.length()-1;i>=0;i--,power++) {
            //result = String.valueOf(((values.get(number.charAt(i)) * base.pow(power).toString()) + Integer.valueOf(result)));
            result = result.add(base.pow(power).multiply(new BigInteger(values.get(number.charAt(i)).toString())));
        }
        return result.toString();
    }

    private static String toTargetBase(BigInteger number,int targetBase){
        //System.out.println(number);
        if(number.equals(BigInteger.ZERO))
            return "0";
        String result = new String();
        String value = new String();
        BigInteger tgb = new BigInteger(String.valueOf(targetBase));
        BigInteger power = BigInteger.ONE;
        while(number.compareTo(BigInteger.ZERO) > 0){
            String remainder = number.remainder(tgb).toString();
            for(Map.Entry entry : values.entrySet()){
                if(entry.getValue().toString().equals(remainder)){
                    value = entry.getKey().toString();
                    break;
                }
            }
            number = number.divide(tgb);
            result = value+result;
            //power = power.multiply(BigInteger.TEN);
        }
        return result;
    }

    private static int binaryToDecimal(int binaryNumber){
        int decimalNumber = 0;
        double power = 0;
        while(binaryNumber > 0){
            decimalNumber = decimalNumber + ((binaryNumber%10) * (int)Math.pow(2.0,power));
            power++;
            binaryNumber/=10;
        }
        return decimalNumber;
    }

    private static int octalToDecimal(int octalNumber){
        int decimalNumber = 0;
        double power = 0;
        while(octalNumber > 0){
            decimalNumber = decimalNumber + ((octalNumber%10) * (int)Math.pow(8.0,power));
            power++;
            octalNumber/=10;
        }
        return decimalNumber;
    }

    private static int hexToDecimal(String hexNumber){
        int decimalNumber = 0;
        double power = 0;
        for(int i = hexNumber.length()-1;i>=0;i--,power++){
            if(hexNumber.charAt(i) > '9'){
                if(hexNumber.charAt(i) == 'A' || hexNumber.charAt(i) == 'a')
                    decimalNumber = decimalNumber + (int)(10 * Math.pow(16.0,power));
                else if(hexNumber.charAt(i) == 'B' || hexNumber.charAt(i) == 'b')
                    decimalNumber = decimalNumber + (int)(11 * Math.pow(16.0,power));
                else if(hexNumber.charAt(i) == 'C' || hexNumber.charAt(i) == 'c')
                    decimalNumber = decimalNumber + (int)(12 * Math.pow(16.0,power));
                else if(hexNumber.charAt(i) == 'D' || hexNumber.charAt(i) == 'd')
                    decimalNumber = decimalNumber + (int)(13 * Math.pow(16.0,power));
                else if(hexNumber.charAt(i) == 'E' || hexNumber.charAt(i) == 'e')
                    decimalNumber = decimalNumber + (int)(14 * Math.pow(16.0,power));
                else
                    decimalNumber = decimalNumber + (int)(15 * Math.pow(16.0,power));
            }
            else{
                decimalNumber = decimalNumber + (int)(Integer.valueOf(hexNumber.charAt(i)-'0') * Math.pow(16.0,power));
            }
        }
        return decimalNumber;
    }

    private static BigInteger decimalToBinary(BigInteger decimalNumber){
        BigInteger binaryNumber = BigInteger.ZERO;
        BigInteger power = BigInteger.ONE;
        while(decimalNumber.compareTo(BigInteger.ZERO) > 0){
            BigInteger remainder = decimalNumber.remainder(BigInteger.TWO);
            decimalNumber = decimalNumber.divide(BigInteger.TWO);
            //binaryNumber = remainder*power+binaryNumber;
            remainder = remainder.multiply(power);
            binaryNumber = binaryNumber.add(remainder);
            power =  power.multiply(BigInteger.TEN);
        }
        return binaryNumber;
    }

    private static int decimalToOctal(int decimalNumber){
        int octalNumber = 0;
        int power = 1;
        while(decimalNumber > 0){
            int remainder = decimalNumber%8;
            decimalNumber /= 8;
            octalNumber = remainder*power + octalNumber;
            power*=10;
        }
        return octalNumber;
    }

    private static String decimalToHEX(int decimalNumber){
        String hexNumber = new String();
        String hexValues[] = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        int power = 1;
        while(decimalNumber > 0){
            int remainder = decimalNumber % 16;
            decimalNumber/=16;
            hexNumber = hexValues[remainder]+hexNumber;
        }
        return hexNumber;
    }

    private static void initValues(LinkedHashMap<Character,Integer> map){
        for(int i='0';i<='9';i++)
            map.put((char)i,(int)(i-'0'));
        for(int i='a';i<='z';i++)
            map.put((char)i,(int)(i-'a'+10));
        //System.out.println(map.toString());
    }
}
