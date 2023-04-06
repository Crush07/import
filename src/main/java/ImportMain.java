import dao.CourseMapper;
import entity.Course;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.ExcelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author hysea
 */
public class ImportMain {

    public static void main(String[] args) {

        CourseMapper courseMapper = new CourseMapper();

        File file = new File("course.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //1.创建工作簿,使用excel能操作的这边都看看操作
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExcelUtil.read(workbook, Course.class, courseMapper::batchInsertAeCourse);
    }

}
