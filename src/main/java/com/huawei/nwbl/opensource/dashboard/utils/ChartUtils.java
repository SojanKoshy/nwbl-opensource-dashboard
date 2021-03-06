package com.huawei.nwbl.opensource.dashboard.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Create Google chart data in json
 */
@SuppressWarnings("unchecked")
public class ChartUtils {
    private JSONArray cols = new JSONArray();
    private JSONArray rows = new JSONArray();

//    {
//        "cols": [
//        {"label":"Topping","type":"string"},
//        {"label":"Slices","type":"number"}
//      ],
//        "rows": [
//        {"c":[{"v":"Mushrooms"},{"v":3}]},
//        {"c":[{"v":"Onions"},{"v":1}]},
//        {"c":[{"v":"Olives"},{"v":1}]}
//      ]
//    }

    public String createJson() {
        JSONObject json = new JSONObject();
        json.put("cols", cols);
        json.put("rows", rows);
        return json.toString();
    }

    public void addColumnHeading(String name, String type) {
        JSONObject colHeading = new JSONObject();

        colHeading.put("label", name);
        colHeading.put("type", type);
        cols.add(colHeading);
    }

    public void addColumnAnnotation() {
        JSONObject colHeading = new JSONObject();

        colHeading.put("label", "");
        colHeading.put("type", "string");
        JSONObject p = new JSONObject();
        p.put("role", "annotation");
        colHeading.put("p", p);
        cols.add(colHeading);
    }

    public void addRow(Object... elements) {
        JSONObject row = new JSONObject();
        JSONArray cells = new JSONArray();
        for (Object element : elements) {
            JSONObject cell = new JSONObject();
            cell.put("v", element);
            cells.add(cell);
        }
        row.put("c", cells);
        rows.add(row);
    }

    public String stringToGoogleDate(java.sql.Date date) {
        String[] d = date.toString().split("-");
        return String.format("Date(%s, %s, %s)", d[0], Integer.valueOf(d[1]) - 1, Integer.valueOf(d[2]) - 1);
    }
}
