package util;

public class CodingStandardUtils {

    public static String camelCase(String columnName,boolean capital){

        if(!columnName.contains("_")){
            if(capital){
                return columnName.substring(0,1).toUpperCase() + columnName.substring(1);
            }else{
                return columnName.substring(0,1).toLowerCase() + columnName.substring(1);
            }
        }

        String[] s = columnName.split("_");

        StringBuilder sb = new StringBuilder();

        for(int i = 0;i < s.length;i++){

            if(i == 0 && !capital){
                sb.append(s[i]);
            }else{
                String firstChar = s[i].substring(0,1);

                firstChar = firstChar.toUpperCase();

                sb.append(firstChar).append(s[i].substring(1));
            }
        }

        return sb.toString();

    }

    public static String underLine(String javaName){
        StringBuilder res = new StringBuilder();
        if(javaName.charAt(0) >= 'A' && javaName.charAt(0) <= 'Z'){
            res.append((javaName.charAt(0) + 32));
        }
        for(int i = 1;i < javaName.length();i++){
            if(javaName.charAt(i) >= 'A' && javaName.charAt(i) <= 'Z'){
                res.append("_").append(javaName.charAt(i) + 32);
            }else if(javaName.charAt(i) >= '0' && javaName.charAt(i) <= '9'){
                res.append("_").append(javaName.charAt(i));
            }else{
                res.append(javaName.charAt(i));
            }
        }
        return res.toString();
    }

    public static String toJavaType(String columnType){
        if(columnType.contains("varchar")){
            return "String";
        }else if(columnType.contains("text")){
            return "String";
        }else if(columnType.contains("int")) {
            return "Integer";
        }else if(columnType.contains("float")) {
            return "Float";
        }else if(columnType.contains("double")) {
            return "Double";
        }else if(columnType.contains("date")){
            return "Date";
        }
        return "";
    }

}
