package ysu.edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ysu.edu.pojo.Sign;
import ysu.edu.pojo.Student;
import ysu.edu.service.ISignService;
import ysu.edu.service.IStudentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/download")
public class DownLoadController {
    @Resource
    ISignService signService;
    @Resource
    IStudentService studentService;
    @GetMapping("/person")
    void personDownload(HttpServletResponse response,Sign signx) throws IOException {
        int index = 0;
        List<Sign> list =(List<Sign>) signService.getList(signx);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "个人赛队伍报名汇总表";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet");
        HSSFRow head = sheet.createRow(index++);
        head.createCell(0).setCellValue("编号");
        head.createCell(1).setCellValue("队伍名称");
        head.createCell(2).setCellValue("学生姓名");
        head.createCell(3).setCellValue("学生学号");
        head.createCell(4).setCellValue("是否过审");

        for(Sign sign : list) {
            HSSFRow row = sheet.createRow(index++);
            row.createCell(0).setCellValue(sign.getId());
            row.createCell(1).setCellValue(sign.getTeamName());
            row.createCell(2).setCellValue(sign.getStudentName());
            row.createCell(3).setCellValue(sign.getStudentId());
            row.createCell(4).setCellValue(sign.getVerifyName());
            //row.createCell(5).setCellValue(sign.getLastlogin()==null ? "" : admin.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        workbook.write(response.getOutputStream());
    }
    @GetMapping("/team")
    void teamDownload(HttpServletResponse response,Sign signx) throws IOException {
        int index = 0;
        List<Sign> list = (List<Sign>)signService.teamList(signx);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "团队赛队伍报名汇总表";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet");
        HSSFRow head = sheet.createRow(index++);
        head.createCell(0).setCellValue("编号");
        head.createCell(1).setCellValue("队伍名称");
        head.createCell(2).setCellValue("队长");
        head.createCell(3).setCellValue("是否过审");


        for(Sign sign : list) {
            HSSFRow row = sheet.createRow(index++);
            row.createCell(0).setCellValue(sign.getId());
            row.createCell(1).setCellValue(sign.getTeamName());
            row.createCell(2).setCellValue(sign.getCaptainName());
            row.createCell(3).setCellValue(sign.getVerifyName());
            //拿到小队成员学号，切分
            String[] stuId = sign.getStudentId().split(",");
            int k = 0;
            for (int i = 0; i < stuId.length; i++) {
                head.createCell(4+k).setCellValue("成员" + (i+1) + "学号");
                head.createCell(5+k).setCellValue("成员" + (i+1) + "姓名");
                k += 2;
            }
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.in("num_id",stuId);
            List<Student> studentList = studentService.list(wrapper);
            int m = 0;
            for (Student student : studentList) {
                row.createCell(4+m).setCellValue(student.getNumId());
                row.createCell(5+m).setCellValue(student.getName());
                m += 2;
            }
        }
        workbook.write(response.getOutputStream());
    }
}
