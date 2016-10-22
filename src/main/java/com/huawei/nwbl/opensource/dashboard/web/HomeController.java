package com.huawei.nwbl.opensource.dashboard.web;

import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index", "projects", projectRepository.getAllByIsVisibleOrderByName());
    }

    @GetMapping("download/{startDate}/{endDate}/{projectsId}")
    public void download(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId,
            HttpServletResponse response) {

        HSSFWorkbook workbook = createExcel(gerritChangeRepository.getAllByUpdatedOnBetween(startDate, endDate,
                projectsId));

        try {
            response.setHeader("Content-Disposition", "attachment; filename=gerrit-changes.xlsx");
            response.setContentType("text/html");
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HSSFWorkbook createExcel(List<Object[]> gerritChanges) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Gerrit Changes");

        Integer rowId = 0;
        HSSFRow rowhead = sheet.createRow(rowId);
        rowhead.createCell(0).setCellValue("Gerrit Id");
        rowhead.createCell(1).setCellValue("Project");
        rowhead.createCell(2).setCellValue("Owner");
        rowhead.createCell(3).setCellValue("Code Size");
        rowhead.createCell(4).setCellValue("Status");
        rowhead.createCell(5).setCellValue("Updated On");

        for (Object[] gerritChange : gerritChanges) {
            rowId++;
            Long id = (Long) gerritChange[0];
            String link = (String) gerritChange[1];
            String project = (String) gerritChange[2];
            String member = (String) gerritChange[3];
            Integer codeSize = (Integer) gerritChange[4];
            String status = (String) gerritChange[5];
            java.sql.Date updatedOn = new java.sql.Date(((Date) gerritChange[6]).getTime());

            HSSFRow row = sheet.createRow(rowId);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(id);

            HSSFHyperlink url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
            url_link.setAddress(link);
            cell0.setHyperlink(url_link);
            row.createCell(1).setCellValue(project);
            row.createCell(2).setCellValue(member);
            row.createCell(3).setCellValue(codeSize);
            row.createCell(4).setCellValue(status);
            row.createCell(5).setCellValue(updatedOn.toString());
        }
        return workbook;
    }
}