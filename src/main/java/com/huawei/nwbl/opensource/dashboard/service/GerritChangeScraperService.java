package com.huawei.nwbl.opensource.dashboard.service;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gerrit commit page scraper
 */
@Service
public class GerritChangeScraperService extends GerritScraperService {
    private static final String CHANGE_PAGE_PATH = "/#/c/";

    private static final String CHANGE_TABLE_XPATH = "//*[@id=\"gerrit_body\"]/div/div/div/div/div/div[3]/table";
    private static final String CHANGE_TABLE_CLASS = "changeTable";

    private static final String PATCH_SETS_XPATH = "//*[@id=\"gerrit_body\"]/div/div/div/div/div/div[1]/div[3]/div[1]/button[2]/div";

    private static final int FIRST_FILE_PATH = 3;
    private static final int SIZE = 5;

    private static final int EXPECT_CELL_SIZE_IN_ROW = 9;
    private static final int EXPECT_CELL_SIZE_IN_LAST_ROW = 7;
    private String branchId;

    private boolean hasNextPage = false;
    private String nextPageUrl;

    public void scrape(String branchId) {
        this.branchId = branchId;
        this.url = BASE_URL + CHANGE_PAGE_PATH + branchId;
        do {
            if (hasNextPage) {
                this.url = nextPageUrl;
                hasNextPage = false;
            }
            loadPage();
            scrapeChangeTable();
            scrapePatchSets();
        } while (hasNextPage);
    }

    public String scrapeAndGetFirstFilePath(String branchId) {
        this.branchId = branchId;
        this.url = BASE_URL + CHANGE_PAGE_PATH + branchId;
        loadPage();
        return scrapeFirstFilePath();
    }

    private String scrapeFirstFilePath() {
        HtmlTable changeTable = page.getFirstByXPath(CHANGE_TABLE_XPATH);

        if (changeTable == null || !changeTable.getAttribute("class").contains(CHANGE_TABLE_CLASS)) {
            log.error("Change table is not found in page. Please check the url '{}'", url);
            return null;
        }

        HtmlTableBody tableBody = changeTable.getBodies().get(0);

        int rowSize = tableBody.getRows().size();
        log.info("Number of rows in change table are {}", rowSize);

        //*[@id="gerrit_body"]/div/div/div/div/div/div[3]/table/tbody/tr[3]/td[4]/a

        List<HtmlTableRow> rowList = tableBody.getRows();

        for (int rowId = 2; rowId < rowSize - 2; rowId++) {
            List<HtmlTableCell> cellList = rowList.get(rowId).getCells();
            int cellsInRow = cellList.size();
            if (cellsInRow != EXPECT_CELL_SIZE_IN_ROW) {
                log.error("Number of cells in row are {} while expected is {}. Please check the url '{}'",
                        cellsInRow, EXPECT_CELL_SIZE_IN_ROW, url);
                return null;
            }
            String filePath = getFirstFilePath(cellList.get(FIRST_FILE_PATH));
            if(filePath.trim().endsWith(".java")) {
                return filePath;
            }
        }

        List<HtmlTableCell> cellList = rowList.get(2).getCells();
        return getFirstFilePath(cellList.get(FIRST_FILE_PATH));
    }

    private String getFirstFilePath(HtmlTableCell cell) {
        String firstFilePath = cell.asText();
        log.info("firstFilePath is '{}'", firstFilePath);
        return firstFilePath;
    }


    private void scrapeChangeTable() {
        HtmlTable changeTable = page.getFirstByXPath(CHANGE_TABLE_XPATH);

        if (changeTable == null || !changeTable.getAttribute("class").contains(CHANGE_TABLE_CLASS)) {
            log.error("Change table is not found in page. Please check the url '{}'", url);
            return;
        }

        HtmlTableBody tableBody = changeTable.getBodies().get(0);

        int rowSize = tableBody.getRows().size();
        log.info("Number of rows in change table are {}", rowSize);

        //*[@id="gerrit_body"]/div/div/div/div/div/div[3]/table/tbody/tr[7]/th[4]

        List<HtmlTableRow> rowList = tableBody.getRows();
        List<HtmlTableCell> cellList = rowList.get(rowSize - 1).getCells();
        int cellsInRow = cellList.size();

        if (cellsInRow != EXPECT_CELL_SIZE_IN_LAST_ROW) {
            log.error("Number of cells in row are {} while expected is {}. Please check the url '{}'",
                    cellsInRow, EXPECT_CELL_SIZE_IN_LAST_ROW, url);
            return;
        }
        storeSize(cellList.get(SIZE));
    }

    private void scrapePatchSets() {
        HtmlDivision patchSetsDivision = page.getFirstByXPath(PATCH_SETS_XPATH);

        if (patchSetsDivision == null) {
            log.error("Unable to find Patch Sets in page. Page might not be loaded properly");
            return;
        }

        String patchSets = patchSetsDivision.asText();
        log.info("Patch sets is '{}'", patchSets);

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(patchSets);
        if (matcher.find()) {
            Integer currentPatch = Integer.valueOf(matcher.group(0));

            if (currentPatch != 1) {
                Integer nextPatch = currentPatch - 1;
                hasNextPage = true;
                nextPageUrl = BASE_URL + CHANGE_PAGE_PATH + branchId + '/' + nextPatch.toString();
            }
        } else {
            log.error("Unable to parse the next patch number from '{}'", patchSets);
        }
    }

    private void storeSize(HtmlTableCell cell) {
        String size = cell.asText();
        log.info("Size is '{}'", size);
    }


}
