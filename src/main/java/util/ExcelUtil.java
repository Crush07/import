package util;

import annotate.Note;
import com.alibaba.fastjson.JSONObject;
import entity.Course;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hysea
 */
public class ExcelUtil {

    public static <T> Integer read(Workbook workbook, Class<T> clazz, Function<List<T>,Integer> function) {

        HashMap<String, String> map;

        T temp = null;
        try {
            temp = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return 0;
        }

        String[] title;
        Field[] fields;
        String[] methods;
        try{
            fields = temp.getClass().getFields();
            title = new String[fields.length];
            methods = new String[fields.length];

            map = new HashMap<>(fields.length);

            for(int i = 0;i < fields.length;i++){
                Field field = fields[i];
                methods[i] = "set"+ CodingStandardUtils.camelCase(field.getName(),true);
                title[i] = field.getAnnotation(Note.class).value().replaceAll(" .*","");
            }
            for(int i = 0;i < title.length;i++){
                map.put(title[i],methods[i]);
            }
        }catch (Exception t){
            t.printStackTrace();
            return 0;
        }

        List<JSONObject> res = new ArrayList<>();

        //2.得到表
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        Cell cell;
        List<String> head = new ArrayList<>();

        JSONObject line;

        row = rows.next();
        Iterator<Cell> cells = row.cellIterator();
        while (cells.hasNext()) {
            cell = cells.next();
            String cellValue = cell.getStringCellValue();
            head.add(cellValue);
        }

        while (rows.hasNext()) {
            line = new JSONObject();
            row = rows.next();
            // 获取单元格
            for(int i = 0;i < head.size();i++){
                cell = row.getCell(i);
                if(cell != null){
                    cell.setCellType(CellType.STRING);
                    String cellValue = cell.getStringCellValue();
                    line.put(map.get(head.get(i)), cellValue == null?"":cellValue);
                }else{
                    line.put(map.get(head.get(i)), "");
                }
            }
            res.add(line);
            System.out.println(line);
        }

        List<T> list = new ArrayList<>();
        for (JSONObject element : res) {
            T object = null;
            try {
                object = clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return 0;
            }
            Map<String, Method> entityMethods = Arrays.stream(clazz.getMethods()).filter(s -> s.getName().contains("set")).collect(Collectors.toMap(Method::getName, Function.identity()));

            for (String functionName : element.keySet()) {
                try {
                    if (entityMethods.containsKey(functionName)) {
                        entityMethods.get(functionName).invoke(object, element.getString(functionName));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            list.add(object);
        }

        if (list.size() > 0) {
            return function.apply(list);
        }

        return 0;

    }

    public static XSSFWorkbook write(String sheetName, String []title, String [][]values, XSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new XSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        XSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();

        //声明列对象
        XSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

}
